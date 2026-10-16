package com.vlaptev.testorderrepairman;

import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction;
import com.intellij.codeInspection.util.IntentionFamilyName;
import com.intellij.codeInspection.util.IntentionName;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiAnnotationMemberValue;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiConstantEvaluationHelper;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiErrorElement;
import com.intellij.psi.PsiExpression;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiModifierList;
import com.intellij.psi.PsiNameValuePair;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TestOrderRepairman extends PsiElementBaseIntentionAction {
    private static final String ORDER_ANNOTATION = "org.junit.jupiter.api.Order";
    private static final Set<String> TEST_ANNOTATIONS = Set.of(
            "org.junit.jupiter.api.Test",
            "org.junit.jupiter.api.TestFactory",
            "org.junit.jupiter.api.TestTemplate",
            "org.junit.jupiter.api.RepeatedTest",
            "org.junit.jupiter.params.ParameterizedTest"
    );
    private static final String ACTION_TEXT = "Renumber JUnit @Order values in this class";

    @Override
    public void invoke(@NotNull Project project, Editor editor, @NotNull PsiElement element) {
        PsiClass testClass = findTargetClass(element);
        if (testClass == null) {
            return;
        }

        // Validate the entire class before changing any values.
        List<OrderEntry> entries = collectOrderEntries(testClass);
        if (!needsRenumbering(entries)) {
            return;
        }

        for (int i = 0; i < entries.size(); i++) {
            OrderEntry entry = entries.get(i);
            int expectedOrder = i + 1;
            if (entry.order != expectedOrder) {
                PsiExpression replacement = JavaPsiFacade.getElementFactory(project)
                        .createExpressionFromText(Integer.toString(expectedOrder), entry.value);
                entry.value.replace(replacement);
            }
        }
    }

    @Override
    public boolean isAvailable(@NotNull Project project, Editor editor, @NotNull PsiElement element) {
        PsiClass testClass = findTargetClass(element);
        return testClass != null && needsRenumbering(collectOrderEntries(testClass));
    }

    @Override
    public @NotNull @IntentionFamilyName String getFamilyName() {
        return ACTION_TEXT;
    }

    @Override
    public @NotNull @IntentionName String getText() {
        return ACTION_TEXT;
    }

    @Nullable
    private static PsiClass findTargetClass(PsiElement element) {
        PsiAnnotation annotation = PsiTreeUtil.getParentOfType(element, PsiAnnotation.class, false);
        if (annotation == null || !ORDER_ANNOTATION.equals(annotation.getQualifiedName())) {
            return null;
        }

        PsiElement parent = annotation.getParent();
        if (!(parent instanceof PsiModifierList) || !(parent.getParent() instanceof PsiMethod)) {
            return null;
        }

        PsiMethod method = (PsiMethod) parent.getParent();
        if (!method.getContainingFile().isWritable() || !isTestMethod(method)) {
            return null;
        }
        return method.getContainingClass();
    }

    @NotNull
    private static List<OrderEntry> collectOrderEntries(PsiClass testClass) {
        List<OrderEntry> entries = new ArrayList<>();
        PsiConstantEvaluationHelper evaluator = JavaPsiFacade.getInstance(testClass.getProject())
                .getConstantEvaluationHelper();

        // getMethods() contains only methods declared directly in this class.
        for (PsiMethod method : testClass.getMethods()) {
            if (!isTestMethod(method)) {
                continue;
            }

            PsiAnnotation annotation = null;
            for (PsiAnnotation candidate : method.getModifierList().getAnnotations()) {
                if (ORDER_ANNOTATION.equals(candidate.getQualifiedName())) {
                    if (annotation != null) {
                        return Collections.emptyList();
                    }
                    annotation = candidate;
                }
            }
            if (annotation == null) {
                continue;
            }

            PsiNameValuePair[] attributes = annotation.getParameterList().getAttributes();
            if (attributes.length != 1
                    || (attributes[0].getName() != null && !"value".equals(attributes[0].getName()))
                    || PsiTreeUtil.findChildOfType(annotation, PsiErrorElement.class) != null) {
                return Collections.emptyList();
            }

            PsiAnnotationMemberValue value = attributes[0].getValue();
            if (!(value instanceof PsiExpression)) {
                return Collections.emptyList();
            }

            Integer order = evaluateOrder(evaluator, (PsiExpression) value);
            if (order == null) {
                return Collections.emptyList();
            }
            entries.add(new OrderEntry((PsiExpression) value, order));
        }

        entries.sort(Comparator.comparingInt(entry -> entry.value.getTextOffset()));
        return entries;
    }

    @Nullable
    private static Integer evaluateOrder(PsiConstantEvaluationHelper evaluator, PsiExpression value) {
        Object constant = evaluator.computeConstantExpression(value);
        if (constant instanceof Integer || constant instanceof Byte || constant instanceof Short) {
            return ((Number) constant).intValue();
        }
        if (constant instanceof Character) {
            return (int) ((Character) constant).charValue();
        }
        return null;
    }

    private static boolean needsRenumbering(List<OrderEntry> entries) {
        for (int i = 0; i < entries.size(); i++) {
            if (entries.get(i).order != i + 1) {
                return true;
            }
        }
        return false;
    }

    private static boolean isTestMethod(PsiMethod method) {
        Set<PsiClass> visited = new HashSet<>();
        for (PsiAnnotation annotation : method.getModifierList().getAnnotations()) {
            if (isTestAnnotation(annotation, visited)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isTestAnnotation(PsiAnnotation annotation, Set<PsiClass> visited) {
        String name = annotation.getQualifiedName();
        if (name == null) {
            return false;
        }
        if (TEST_ANNOTATIONS.contains(name)) {
            return true;
        }
        if (name.startsWith("java.lang.annotation.")) {
            return false;
        }

        PsiClass annotationClass = annotation.resolveAnnotationType();
        if (annotationClass == null || !visited.add(annotationClass)) {
            return false;
        }
        PsiModifierList modifiers = annotationClass.getModifierList();
        if (modifiers != null) {
            for (PsiAnnotation metaAnnotation : modifiers.getAnnotations()) {
                if (isTestAnnotation(metaAnnotation, visited)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static final class OrderEntry {
        private final PsiExpression value;
        private final int order;

        private OrderEntry(PsiExpression value, int order) {
            this.value = value;
            this.order = order;
        }
    }
}

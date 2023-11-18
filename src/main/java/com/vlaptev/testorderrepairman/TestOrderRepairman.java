package com.vlaptev.testorderrepairman;

import com.intellij.codeInsight.intention.IntentionAction;
import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction;
import com.intellij.codeInspection.util.IntentionFamilyName;
import com.intellij.codeInspection.util.IntentionName;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.JavaTokenType;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiAnnotationMemberValue;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaToken;
import com.intellij.psi.PsiLiteralExpression;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

public class TestOrderRepairman extends PsiElementBaseIntentionAction implements IntentionAction {
    @Override
    public void invoke(
            @NotNull Project project, Editor editor, @NotNull PsiElement element
    ) throws IncorrectOperationException {

        PsiFile file = element.getContainingFile();
        List<Integer> orderValues = findOrderList(file);
        if (orderValues.size() < 2) {
            return;
        }
        Collections.sort(orderValues);
        boolean hasIncorrectOrder = IntStream.range(1, orderValues.size()).anyMatch(
                i -> orderValues.get(i) - orderValues.get(i - 1) != 1
        );

        if (hasIncorrectOrder) {
            int currentOrder = 1;
            var factory = JavaPsiFacade.getElementFactory(project);
            for (PsiAnnotation annotation : PsiTreeUtil.findChildrenOfType(file, PsiAnnotation.class)) {
                if ("org.junit.jupiter.api.Order".equals(annotation.getQualifiedName())) {
                    var newAnnotation = factory.createAnnotationFromText("@Order(" + currentOrder + ")", null);
                    annotation.replace(newAnnotation);
                    currentOrder++;
                }
            }
        }
    }

    @Override
    public boolean isAvailable(@NotNull Project project, Editor editor, @NotNull PsiElement element) {
        if (element instanceof PsiJavaToken token) {
            if (token.getTokenType() != JavaTokenType.INTEGER_LITERAL) {
                return false;
            }
            PsiElement parent = token.getParent().getParent().getParent().getParent();
            if (parent instanceof PsiAnnotation annotation) {
                String qualifiedName = annotation.getQualifiedName();
                return "org.junit.jupiter.api.Order".equals(qualifiedName);
            }
            return false;
        }
        return false;
    }

    @Override
    public @NotNull @IntentionFamilyName String getFamilyName() {
        return "Repair JUnit @Order annotation numerics";
    }

    @Override
    public @IntentionName @NotNull String getText() {
        return "Repair JUnit @Order annotation numerics";
    }

    @NotNull
    private static List<Integer> findOrderList(PsiFile file) {
        List<Integer> orderValues = new ArrayList<>();
        for (PsiAnnotation annotation : PsiTreeUtil.findChildrenOfType(file, PsiAnnotation.class)) {
            if ("org.junit.jupiter.api.Order".equals(annotation.getQualifiedName())) {
                PsiAnnotationMemberValue value = annotation.findAttributeValue(null);
                if (value instanceof PsiLiteralExpression) {
                    Object literalValue = ((PsiLiteralExpression) value).getValue();
                    if (literalValue instanceof Integer) {
                        orderValues.add((Integer) literalValue);
                    }
                }
            }
        }
        return orderValues;
    }
}

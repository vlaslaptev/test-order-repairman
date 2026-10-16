package com.vlaptev.testorderrepairman;

import com.intellij.codeInsight.intention.IntentionAction;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.testFramework.LightProjectDescriptor;
import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase;

public class TestOrderRepairmanTest extends LightJavaCodeInsightFixtureTestCase {
    private static final String HINT = "Renumber JUnit @Order values in this class";

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        myFixture.addClass("package org.junit.jupiter.api; public @interface Order { int value(); }");
        myFixture.addClass("package org.junit.jupiter.api; public @interface Test {}");
        myFixture.addClass("package org.junit.jupiter.api; public @interface RepeatedTest { int value(); }");
        myFixture.addClass("package org.junit.jupiter.api; public @interface TestFactory {}");
        myFixture.addClass("package org.junit.jupiter.api; public @interface TestTemplate {}");
        myFixture.addClass("package org.junit.jupiter.api; public @interface Nested {}");
        myFixture.addClass("package org.junit.jupiter.params; public @interface ParameterizedTest {}");
        myFixture.addClass("package other; public @interface Order { int value(); }");
    }

    private void doTest(String testName) {
        myFixture.configureByFile(testName + ".java");
        PsiAnnotation annotation = PsiTreeUtil.getParentOfType(
                myFixture.getFile().findElementAt(myFixture.getCaretOffset()), PsiAnnotation.class, false);
        assertNotNull(annotation);
        IntentionAction action = myFixture.findSingleIntention(HINT);
        myFixture.launchAction(action);
        myFixture.checkResultByFile(testName + ".after.java");
        myFixture.checkHighlighting(false, false, false);

        // Applying the same operation again must leave the file unchanged.
        String result = myFixture.getFile().getText();
        TestOrderRepairman intention = new TestOrderRepairman();
        assertFalse(intention.isAvailable(getProject(), myFixture.getEditor(), annotation));
        WriteCommandAction.runWriteCommandAction(getProject(), () ->
                intention.invoke(getProject(), myFixture.getEditor(), annotation));
        myFixture.checkResult(result);
    }

    private void assertUnavailable(String testName) {
        myFixture.configureByFile(testName + ".java");
        String original = myFixture.getFile().getText();
        int offset = myFixture.getCaretOffset();
        assertTrue(myFixture.filterAvailableIntentions(HINT).isEmpty());
        PsiElement element = myFixture.getFile().findElementAt(offset);
        assertNotNull(element);
        WriteCommandAction.runWriteCommandAction(getProject(), () ->
                new TestOrderRepairman().invoke(getProject(), myFixture.getEditor(), element));
        myFixture.checkResult(original);
    }

    public void testFullyQualifiedAnnotations() { doTest("before.template"); }
    public void testClassScope() { doTest("classScope"); }
    public void testNestedClassScope() { doTest("nestedClassScope"); }
    public void testPermutedValues() { doTest("permuted"); }
    public void testShiftedValues() { doTest("shifted"); }
    public void testSingleValue() { doTest("single"); }
    public void testConstantsAndExpressions() { doTest("expressions"); }
    public void testCaretOnConstantName() { doTest("constantCaret"); }
    public void testConflictingImport() { doTest("conflictingImport"); }
    public void testComposedAndOtherTestKinds() { doTest("testKinds"); }
    public void testAlreadyNumbered() { assertUnavailable("alreadyNumbered"); }
    public void testUnresolvedValue() { assertUnavailable("unresolvedValue"); }
    public void testInvalidValue() { assertUnavailable("invalidValue"); }
    public void testIncompleteValue() { assertUnavailable("incompleteValue"); }
    public void testDuplicateOrder() { assertUnavailable("duplicateOrder"); }
    public void testFieldIsNotATarget() { assertUnavailable("fieldTarget"); }
    public void testClassIsNotATarget() { assertUnavailable("classTarget"); }
    public void testHelperIsNotATarget() { assertUnavailable("helperTarget"); }

    @Override
    protected LightProjectDescriptor getProjectDescriptor() {
        return JAVA_11;
    }

    @Override
    protected String getTestDataPath() {
        return "src/test/testData";
    }
}

package com.vlaptev.testorderrepairman;

import com.intellij.codeInsight.intention.IntentionAction;
import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase;
import org.junit.Assert;

public class TestOrderRepairmanTest extends LightJavaCodeInsightFixtureTestCase {
    protected void doTest(String testName, String hint) {
        myFixture.configureByFile(testName + ".java");
        final IntentionAction action = myFixture.findSingleIntention(hint);
        Assert.assertNotNull(action);
        myFixture.launchAction(action);
        myFixture.checkResultByFile(testName + ".after.java");
    }

    public void testIntention() {
        doTest("before.template", "Repair JUnit @Order annotation numerics");
    }

    @Override
    protected String getTestDataPath() {
        return "src/test/testData";
    }
}
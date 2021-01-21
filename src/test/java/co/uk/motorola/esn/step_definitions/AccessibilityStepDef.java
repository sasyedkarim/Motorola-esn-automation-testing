package co.uk.motorola.esn.step_definitions;

import co.uk.motorola.esn.context.ScenarioContext;
import co.uk.motorola.esn.utils.common.AccessibilityUtil;
import io.cucumber.java.en.Then;


public class AccessibilityStepDef {

    private AccessibilityUtil accessibilityUtil = new AccessibilityUtil();
    private ScenarioContext scenarioContext;

    public AccessibilityStepDef(ScenarioContext scenarioContext) {
        this.scenarioContext = scenarioContext;
    }

    @Then("^I run accessibility test$")
    public void iAmAbleToValidateTheAccessibilityTest() throws Throwable {
        accessibilityUtil.validateAccessibilityTest(accessibilityUtil.runAudit(scenarioContext), scenarioContext);
    }

    @Then("^I run accessibility audit$")
    public void iRunAccessibilityAudit() throws Throwable {
        accessibilityUtil.runAccessibilityAudit(accessibilityUtil.runAudit(scenarioContext), scenarioContext);
    }
}

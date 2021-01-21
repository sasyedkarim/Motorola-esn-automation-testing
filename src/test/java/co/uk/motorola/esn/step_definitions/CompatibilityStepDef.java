package co.uk.motorola.esn.step_definitions;

import co.uk.motorola.esn.context.ScenarioContext;
import co.uk.motorola.esn.utils.common.CompatibilityUtil;
import io.cucumber.java.en.Then;

public class CompatibilityStepDef {
    private final CompatibilityUtil compatibilityUtil = new CompatibilityUtil();
    private final ScenarioContext scenarioContext;

    public CompatibilityStepDef(ScenarioContext scenarioContext) {
        this.scenarioContext = scenarioContext;
    }


    @Then("^User validate the screen Layout$")
    public void userValidateTheScreenLayout() {
        compatibilityUtil.validateLayoutBug(scenarioContext);
    }


}

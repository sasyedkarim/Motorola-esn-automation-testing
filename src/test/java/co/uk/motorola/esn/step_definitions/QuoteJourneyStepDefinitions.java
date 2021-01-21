package co.uk.motorola.esn.step_definitions;

import co.uk.motorola.esn.context.ScenarioContext;
import co.uk.motorola.esn.utils.FunctionalUtil;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.datatable.DataTable;



public class QuoteJourneyStepDefinitions {

    private final FunctionalUtil functionalUtil = new FunctionalUtil();
    private final ScenarioContext scenarioContext;

    public QuoteJourneyStepDefinitions(ScenarioContext scenarioContext) {
        this.scenarioContext = scenarioContext;
    }

    @Given("^user launches Amazon webapp$")
    public void userLaunchesAmazonWebapp() {
         System.out.println("Launching Amazon Application");
        scenarioContext.getDriver().get(System.getProperty("UX"));
        System.out.println("Given statement executed successfully");
    }

    @And("^User validates the following (labels|messages) in the screen$")
    public void validateLabels(String labelOrMessage, DataTable labels) {
        scenarioContext.getLOG().info(labelOrMessage);
        functionalUtil.validatesLabels(labels, scenarioContext);
    }

    @And("^User validates the error messages for mandatory fields from '(.+)' page$")
    public void validateErrorMessages(String screen, DataTable fields) {
        functionalUtil.validateErrorMessages(screen, fields, scenarioContext);
    }

    @When("^User fills '(.+)' screens and '(.+)' layout with '(.+)' values$")
    public void userValidateLayoutScreen(String screens, String operation, String value) throws Throwable {
        functionalUtil.fillScreensAndValidateLayout(screens, operation, value, scenarioContext);
    }

    @And("^User '(.+)' The home page '(.+)' values$")
    public void userFillsTheHomePage(String operation, String values) {
        functionalUtil.fillLandingScreen(operation, values, scenarioContext);
    }

    @And("^User '(.+)' The signin page '(.+)' values")
    public void userFillsTheSignInPage(String operation, String values) {
        functionalUtil.fillSingInScreen(operation, values, scenarioContext);
    }

    @And("^validate the Page Title as '(.+)'$")
    public void validateThePageTitleAs(String pageTitle) {
        functionalUtil.validateTitleOfThePage(pageTitle,scenarioContext);
    }

    @And("^User '(.+)' the Account Creation Page with '(.+)' values$")
    public void userFillsTheAccountCreationPageWithValues(String operation, String values) {
        functionalUtil.fillRegisterPage(operation, values, scenarioContext);
    }


    @And("^User validates the following field and Values in the '(.+)' screen$")
    public void userValidatesTheFollowingFieldAndValuesInTheScreen(String screen,DataTable values) {
        scenarioContext.getLOG().info(screen);
        functionalUtil.validateValues(screen,values,scenarioContext);
        }

}
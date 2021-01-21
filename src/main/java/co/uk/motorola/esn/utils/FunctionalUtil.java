package co.uk.motorola.esn.utils;

import co.uk.motorola.esn.context.ScenarioContext;
import co.uk.motorola.esn.pages.*;
import co.uk.motorola.esn.steps.AbstractSteps;
import co.uk.motorola.esn.utils.common.AccessibilityUtil;
import co.uk.motorola.esn.utils.common.CompatibilityUtil;
import io.cucumber.datatable.DataTable;
import org.assertj.core.api.SoftAssertions;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.Select;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class FunctionalUtil {

    private String[] allFieldAndValuesFromFeatureFile;
    private final HomePage homePage = new HomePage();
    private final SignInPage signInPage = new SignInPage();
    private final RegisterPage registerPage = new RegisterPage();

    private final AccessibilityUtil accessibilityUtil = new AccessibilityUtil();

    private final SoftAssertions softAssertions = new SoftAssertions();
    private String[] values;
    private final CompatibilityUtil compatibilityUtil = new CompatibilityUtil();
    private final AbstractSteps abstractSteps = new AbstractSteps();

    private static final String UPDATES = "updates";
    private static final String CLICK = "click";
    private static final String DEFAULT = "default";
    private static final String LOADER = "loader";
    private static final String DRIVING_LICENSE_YEARS_DROPDOWN = "yearsDrivingLicenceDropDown";
    private static final String CONTINUE_BUTTON = "continueButton";
    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm";
    private static final String TIME_ZONE = "Europe/London";
    private static final String CLICKING = "Clicking {}";
    private static final String SCROLL_INTO_VIEW = "arguments[0].scrollIntoView(true);";
    private static final String NULL_POINTER_EXCEPTION_MSG = "Null pointer exception on clicking field ";
    private static final String FAILED_CLICKING_EXCEPTION_MSG = "Failed in clicking element ";
    private static final String VALUE = "value";
    private static final String DROPDOWN = "dropdown";
    private static final String HYPERLINK = "hyperlink";
    private static final String CHECKING_IF_MSG = "Checking if ";
    private static final String ACTUAL_VALUE_MSG = ", actual value is ";
    private static final String CLICKED = "clicked";
    private static final String DISPLAYED = "displayed";
    private static final String PRESENT = "present";
    private static final String CHECKING_MSG = "Checking ";
    private static final String NOT_DISPLAYED_MSG = " is not displayed";
    private static final String DISABLED = "disabled";
    private static final String CLASS = "class";
    private static final String NOT_ENABLED_MSG = " is not enabled";
    private static final String NOTPRESENT = "notpresent";
    private static final String EMPTY_MSG = " is empty";
    private static final String EQUAL_TO_MSG = " is equal to ";
    private static final String FIELD_CONTAINS_MSG = " field contains ";
    private static final String LABEL = "label";
    private static final String VALIDATING_MSG = "Validating {} is {}";
    private static final String VALIDATING_CONTAINS_MSG = "Validating {} contains {}";
    private static final String NOTDISPLAYED = "notdisplayed";
    private static final String BLANK = "blank";
    private static final String ACTUAL_MSG = "Actual is ";
    private static final String VALIDATE = "validate";
    private static final String AUDIT = "audit";
    private static final String BILLING_ADDRESS_TEXTAREA = "billingAddressTextArea";
    private static final String TEXT_CONTENT = "textContent";
    private static final String EMAIL_TEXTFIELD = "emailTextField";
    private static final String HOME_POSTCODE_TEXTFIELD = "homePostCodeTextField";
    private static final String RANDOM = "Random";
    private static final String NOTHING = "nothing";
    private static final String FIELD_KEY_INFO = "Field key is {}";
    private static final String DEFAULT_PASSWORD = "Password1";
    private static final String RESPONSE = "Response";
    private static final String NOT_FOUND_MSG = " not found in ";
    private static final String NEXT_BUTTON_CLICK = "nextButton:click";
    private static final String CONTINUE_BUTTON_CLICK = "continueButton:click";
    private static final String FILLS = "fills";
    private static final String TEST_CASE_ID = "Test case id ";
    private static final String VALIDATION_FAILED_MSG = "Validation failed.";
    private static final String VALIDATION_PASSED_MSG = "Validation passed.";
    private static final String CONFIRM_EMAIL_TEXTFIELD = "confirmYourEmailTextField";
    private static final String EMAIL_UX_RESPONSE = "emailAddressUXResponse";


    /**
     * get the page object dynamically
     *
     * @param screenName which is a string that contains the screen for which the page object is needed
     * @return page object for a particular screen.
     */
    private AbstractSteps getPageObject(String screenName) {
        switch (screenName.toLowerCase()) {
            case "amazonhomepage":
                return homePage;
            case "amazonsigninpage":
                return signInPage;
            case "amazonregisterpage":
                return registerPage;
            default:
                return null;
        }
    }

    /**
     * Validating the page to ensure whether we are in correct page or not
     *
     * @param pageTitle which has the title name of particular page
     */
    public void validateTitleOfThePage(String pageTitle, ScenarioContext scenarioContext) {
        scenarioContext.getLOG().info("Validating Page title is {}", pageTitle);
        switch (pageTitle.toLowerCase()) {
            case "amazon sign in":
                System.out.println("PageTitle1:" + scenarioContext.getDriver().getTitle());
                softAssertions.assertThat(scenarioContext.getDriver().getTitle().equals(pageTitle)).describedAs(ACTUAL_MSG + scenarioContext.getDriver().getTitle() + ", Expected is " + pageTitle).isTrue();
                break;
            case "amazon registration":
                System.out.println("PageTitle2:" + scenarioContext.getDriver().getTitle());
                softAssertions.assertThat(scenarioContext.getDriver().getTitle().equals(pageTitle)).describedAs(ACTUAL_MSG + scenarioContext.getDriver().getTitle() + ", Expected is " + pageTitle).isTrue();
                break;
                default:
                softAssertions.fail("Unable to understand the page title value - " + pageTitle);
                break;
        }
        softAssertions.assertAll();
    }


    /**
     * Entering the values in textbox using page and their field
     *
     * @param field which contains fields and its values as key/value pairs
     * @param page  which can access particular page objects
     * @param value which contains the value of particular element
     */
    private void enterTextInTextField(AbstractSteps page, String field, String value, ScenarioContext scenarioContext) {
        scenarioContext.getLOG().info("Entering text {} in {}", value, field);

        for (int i = 0; i < 5; i++) {
            try {
                page.findElementClickable(field, scenarioContext).sendKeys(value);
                break;
            } catch (NullPointerException e) {
                softAssertions.fail("Null Pointer Exception on entering text in field " + field);
            } catch (WebDriverException e) {
                scenarioContext.getLOG().info(e.getMessage());
            }
        }
        softAssertions.assertAll();
    }

    /**
     * Fills the selfserve screen based on the inputs
     * Create an HashMap which contains fields and its values as key/value pairs
     * and sent it fillMyScreen method
     *
     * @param operation string that can be either "updates" or "fill"
     * @param values    string that contains '##' separated fields and values from feature file
     */
    public void fillRegisterPage(String operation, String values, ScenarioContext scenarioContext) {
        LinkedHashMap<String, String> fillRegistrationPageFieldsAndValues = new LinkedHashMap<>();
        allFieldAndValuesFromFeatureFile = values.split("##");
        if (operation.equalsIgnoreCase(UPDATES)) {
//            if operation is UPDATES, have default values in HashMap and update only the values sent from feature file
//            Include default values here if any fields are added to this page
            fillRegistrationPageFieldsAndValues.put("yourNameTextField","SyedTest");
            fillRegistrationPageFieldsAndValues.put("emailAddressTextField","test@test.com");
            fillRegistrationPageFieldsAndValues.put("passwordTextField","S3curity");
            fillRegistrationPageFieldsAndValues.put("confirmPasswordTextField","S3curity");
            fillRegistrationPageFieldsAndValues.put(CONTINUE_BUTTON,CLICK);
            }
        //In the HashMap with default values, replace the default values with values sent from feature file
        //if operation is "fills", HashMap will not have default values. So only fields sent from feature file will be filled
        if (!values.equalsIgnoreCase(DEFAULT)) {
            for (String fieldAndValueFromFeatureFile : allFieldAndValuesFromFeatureFile) {
                String[] fieldAndValue = fieldAndValueFromFeatureFile.split(":");
                //calling this method to fill values that has ':'
                fieldAndValue[1] = valueToBeFilled(fieldAndValue);
                fillRegistrationPageFieldsAndValues.put(fieldAndValue[0], fieldAndValue[1]);
            }
        }
        scenarioContext.getLOG().info("Filling - Password Reset Page");
        fillMyScreen(fillRegistrationPageFieldsAndValues, registerPage, scenarioContext);
    }


    /**
     * Handled the elements by generic way for all dropdown,textfield,button and checkbox
     *
     * @param fieldAndValues which contains fields and its values as key/value pairs
     * @param page           which can access particular page objects
     */

    private void fillMyScreen(LinkedHashMap<String, String> fieldAndValues, AbstractSteps page, ScenarioContext scenarioContext) {
        List<String> keys = new ArrayList<>(fieldAndValues.keySet());
        for (String key : keys) {

            if (!fieldAndValues.get(key).equalsIgnoreCase("skip")) {

                if (key.toLowerCase().contains("textfield")) {
                    enterTextInTextField(page, key, fieldAndValues.get(key), scenarioContext);
                } else if (key.toLowerCase().contains("checkbox")) {
                    click(page, key, scenarioContext);
                } else if (key.toLowerCase().contains("button")) {
                    click(page, key, scenarioContext);
                } else if (key.toLowerCase().contains("shortwait")) {
                    page.waitForElementNotPresent(LOADER, scenarioContext);
                    this.shortWait(Integer.parseInt(fieldAndValues.get(key)), scenarioContext);
                } else if (key.toLowerCase().contains(HYPERLINK)) {
                    click(page, key, scenarioContext);
                } else if (key.toLowerCase().contains("gotourl")) {
                    abstractSteps.switchToLastOpenWindow(scenarioContext);
                    softAssertions.assertThat(scenarioContext.getDriver().getTitle().contains("404")).isFalse();
                } else if (key.toLowerCase().contains("closewindow")) {
                    //anyPage.waitForElementNotPresent(LOADER, scenarioContext);
                    scenarioContext.getLOG().info("closing the window");
                    scenarioContext.getDriver().close();
                    abstractSteps.switchToLastOpenWindow(scenarioContext);
                    softAssertions.assertThat(scenarioContext.getDriver().getTitle().contains("404")).isFalse();
                }
            } else {
                scenarioContext.getLOG().info("Skipping {}", key);
            }
        }
        scenarioContext.getInputDataMap().putAll(fieldAndValues);
    }


    /**
     * Clicking the particular element using page and their field
     *
     * @param page  which can access particular page objects
     * @param field which contains fields and its values as key/value pairs
     */
    private void click(AbstractSteps page, String field, ScenarioContext scenarioContext) {
        //anyPage.waitForElementNotPresent(LOADER, scenarioContext);
        scenarioContext.getLOG().info(CLICKING, field);
        boolean clicked = false;

        for (int i = 0; i < 5; i++) {
            try
            {
                    scenarioContext.getDriver().executeScript(SCROLL_INTO_VIEW, page.findElementClickable(field, scenarioContext));
                    page.findElementClickable(field, scenarioContext).click();
                    clicked = true;
                    scenarioContext.getLOG().info("{} clicked by element.click", field);
            } catch (StaleElementReferenceException | ElementNotVisibleException e) {
                scenarioContext.getLOG().info("StaleElementReferenceException or ElementNotVisibleException thrown on webelement.click ====== {}", e.getMessage());
            } catch (ElementNotInteractableException e) {
                scenarioContext.getLOG().info("ElementNotInteractableException thrown ====== {}", e.getMessage());
                try {
                    scenarioContext.getDriver().executeScript("arguments[0].click()", page.findElementPresent(field, scenarioContext));
                    clicked = true;
                    scenarioContext.getLOG().info("{} clicked by java script click", field);
                } catch (StaleElementReferenceException e2) {
                    scenarioContext.getLOG().info("StaleElementReferenceException thrown on javascript click ====== {}", e.getMessage());
                }
            } catch (WebDriverException e) {
                scenarioContext.getLOG().info("WebDriverException thrown on webelement.click ====== {}", e.getMessage());
                try {
                    if (e.getMessage().contains("is not clickable at point") || e.getMessage().contains("Element is not currently visible and may not be manipulated") || e.getMessage().contains("safari.SafariDriver") || e.getMessage().contains("Cannot read property")) {
                        scenarioContext.getDriver().executeScript("arguments[0].click()", page.findElementPresent(field, scenarioContext));
                        clicked = true;
                        scenarioContext.getLOG().info("{} clicked by javascript click inside webdriver exception", field);
                    }
                } catch (StaleElementReferenceException e2) {
                    scenarioContext.getLOG().info("StaleElementReferenceException thrown on javascript click ====== {}", e2.getMessage());
                }
            } catch (NullPointerException e) {
                softAssertions.fail(NULL_POINTER_EXCEPTION_MSG + field);
            }
            if (clicked) {
                break;
            }
        }
        if (!clicked) softAssertions.fail(FAILED_CLICKING_EXCEPTION_MSG + field);
        softAssertions.assertAll();
    }

    /**
     * Clicking the particular element
     *
     * @param element which should be clicked
     */
    private void click(WebElement element, String value, ScenarioContext scenarioContext) {
        scenarioContext.getLOG().info(CLICKING, value);
        boolean clicked = false;
        for (int i = 0; i < 5; i++) {
            try {
                scenarioContext.getDriver().executeScript(SCROLL_INTO_VIEW, element);
                element.click();
                clicked = true;
                break;
            } catch (StaleElementReferenceException e) {
                scenarioContext.getLOG().info(e.toString());
            } catch (WebDriverException e) {
                scenarioContext.getLOG().info(e.getMessage());
            } catch (NullPointerException e) {
                softAssertions.fail(NULL_POINTER_EXCEPTION_MSG + element.getText());
            }
        }
        if (!clicked) softAssertions.fail(FAILED_CLICKING_EXCEPTION_MSG + element.getText());
        softAssertions.assertAll();
    }


    /**
     * Validating the labels for the particular screens
     *
     * @param labels which has number of labels and corresponding object name to validate labels
     */
    public void validatesLabels(DataTable labels, ScenarioContext scenarioContext) {
        //anyPage.waitForElementNotPresent(LOADER, scenarioContext);
        for (List<String> message : labels.asLists()) {
            for (String mess : message) {
                mess = mess.replace("$WHITESPACE", " ");
                scenarioContext.getLOG().info("Validating presence of {}", mess);
                boolean messFound = false;
                for (int i = 0; i < 10; i++) {
                    messFound = homePage.isMessagePresent(mess, scenarioContext);
                    if (messFound)
                        break;
                    else
                        shortWait(2, scenarioContext);
                }
                softAssertions.assertThat(messFound).describedAs(mess).isTrue();
            }
        }
        softAssertions.assertAll();
    }




    /**
     * validating whether particular field error messages are throws or not
     *
     * @param fields which has fields to validate the error messages
     * @param screen which has particular page name
     */
    public void validateErrorMessages(String screen, DataTable fields, ScenarioContext scenarioContext) {
        //anyPage.waitForElementNotPresent(LOADER, scenarioContext);
        AbstractSteps page = getPageObject(screen);
        for (List<String> message : fields.asLists()) {
            for (String mess : message) {
                scenarioContext.getLOG().info("Validating error message {}", mess);
                page.findElementPresent(mess, scenarioContext).getAttribute(TEXT_CONTENT);
                softAssertions.assertThat(page.findElementPresent(mess, scenarioContext).getAttribute(TEXT_CONTENT).equals("Mandatory")).describedAs(CHECKING_IF_MSG + mess + " is Mandatory , actual value is " + page.findElementPresent(mess, scenarioContext).getAttribute(TEXT_CONTENT)).isTrue();
            }
        }
        softAssertions.assertAll();
    }


    private void shortWait(int seconds, ScenarioContext scenarioContext) {
        try {
            // commenting out to see regression impact
            scenarioContext.getLOG().info("Waiting for {} seconds", seconds);
            Thread.sleep(seconds * 1000L);
        } catch (Exception e) {
            scenarioContext.getLOG().info(e.toString());
        }
    }



    /**
     * validating the Layout of the particular screen
     *
     * @param screens   which has particular page name
     * @param operation which has whether to perform accessibility and compatibility
     * @param value     which has fields to be validated
     */
    public void fillScreensAndValidateLayout(String screens, String operation, String value, ScenarioContext scenarioContext) throws IOException {
        List<String> screensToFill = new ArrayList<>();
        for (String screenToFill : screens.split(",")) {
            screensToFill.add(screenToFill);
            scenarioContext.getLOG().info("to fill ==================================================={}", screenToFill);
        }
        if (screensToFill.contains("Let’s get started")) {
            if (operation.equalsIgnoreCase(VALIDATE)) {
                compatibilityUtil.validateLayoutBug(scenarioContext);
            } else if (operation.equalsIgnoreCase(AUDIT)) {
                accessibilityUtil.validateAccessibilityTest(accessibilityUtil.runAudit(scenarioContext), scenarioContext);
            }
        }

    }

    /**
     * validating whether particular fields are present or not
     *
     * @param fieldAndValue which has field and value
     * @return value to be filled in the field.
     */
    private String valueToBeFilled(String[] fieldAndValue) {
        if (fieldAndValue.length > 2) {
            for (int i = 2; i < fieldAndValue.length; i++) {
                fieldAndValue[1] = fieldAndValue[1] + ":" + fieldAndValue[i];
            }
        }
        return fieldAndValue[1];
    }


    public String gettingSystemDate(String[] dateAndFormat) {
        String dateFormatted;
        Date dateValue = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(dateValue);
        // SystemDate+ === Future date (No of days)
        // SystemDate+- === Back date (No of days)
        if (dateAndFormat[0].contains("+")) {
            int daysToAdd = Integer.parseInt(dateAndFormat[0].split("\\+")[1]);
            c.add(Calendar.DATE, daysToAdd);
            dateValue = c.getTime();
        }
        // SystemDate-10--ddMMyyyy === Back date (No of Years)
        if (dateAndFormat[0].contains("SystemDate-")) {
            int yearsToSubtract = Integer.parseInt(dateAndFormat[0].split("-")[1]);
            c.add(Calendar.YEAR, -yearsToSubtract);
            dateValue = c.getTime();
        }
        // SystemDate*10-5--ddMMyyyy === Back date (No of Years - No of days)
        if (dateAndFormat[0].contains("*") && dateAndFormat[0].contains("-")) {
            int yearsToSubtract = Integer.parseInt(dateAndFormat[0].split("\\*")[1].split("-")[0]);
            int daysToSubtract = Integer.parseInt(dateAndFormat[0].split("-")[1]);
            c.add(Calendar.YEAR, -yearsToSubtract);
            c.add(Calendar.DATE, -daysToSubtract);
            dateValue = c.getTime();
        }
        // SystemDate*10*5--ddMMyyyy === Back date (No of Years + No of days)
        if (dateAndFormat[0].contains("*") && !dateAndFormat[0].contains("-")) {
            int yearsToSubtract = Integer.parseInt(dateAndFormat[0].split("\\*")[1]);
            int daysToSubtract = Integer.parseInt(dateAndFormat[0].split("\\*")[2]);
            c.add(Calendar.YEAR, -yearsToSubtract);
            c.add(Calendar.DATE, daysToSubtract);
            dateValue = c.getTime();
        }
        // SystemDate~5--ddMMyyyy === Subtracting number of days
        if (dateAndFormat[0].contains("~")) {
            int daysToSubtract = Integer.parseInt(dateAndFormat[0].split("~")[1]);
            c.add(Calendar.DATE, -daysToSubtract);
            dateValue = c.getTime();
        }
        SimpleDateFormat sf = new SimpleDateFormat(dateAndFormat[1]);
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        sdf.setTimeZone(TimeZone.getTimeZone(TIME_ZONE));
        dateFormatted = sf.format(dateValue);

        return dateFormatted;
    }


       /**
     * Fills the Landing screen based on the inputs
     * and sent it fillMyScreen method
     * Create an HashMap which contains fields and its values as key/value pairs
     *
     * @param operation string that can be either "updates" or "fill"
     * @param values    string that contains '##' separated fields and values from feature file
     */

    public void fillLandingScreen(String operation, String values, ScenarioContext scenarioContext) {
        LinkedHashMap<String, String> landingFieldsAndValues = new LinkedHashMap<>();
        allFieldAndValuesFromFeatureFile = values.split("##");
        if (operation.equalsIgnoreCase(UPDATES)) {
            landingFieldsAndValues.put("acceptCookiesButton", CLICK);
            landingFieldsAndValues.put("accountListHyperlink", CLICK);
        }
        if (!values.equalsIgnoreCase(DEFAULT)) {
            for (String fieldAndValueFromFeatureFile : allFieldAndValuesFromFeatureFile) {
                String[] fieldAndValue = fieldAndValueFromFeatureFile.split(":");
                //calling this method to fill values that has ':'
                fieldAndValue[1] = valueToBeFilled(fieldAndValue);
                landingFieldsAndValues.put(fieldAndValue[0], fieldAndValue[1]);
            }

        }
        scenarioContext.getLOG().info("Landing - Page");
        fillMyScreen(landingFieldsAndValues, homePage, scenarioContext);

        try (InputStream input = new FileInputStream("src/test/resources/DefaultValues/UIDefaultValues.properties")) {
            scenarioContext.setDefaultValues(new Properties());
            scenarioContext.getDefaultValues().load(input);

        } catch (IOException e) {
            scenarioContext.getLOG().info(e.toString());
            softAssertions.fail("Exception in reading UI Defaults properties file");
        }
        softAssertions.assertAll();
    }

    /**
     * Fills the YourSignInScreen screen based on the inputs
     * and sent it fillMyScreen method
     * Create an HashMap which contains fields and its values as key/value pairs
     *
     * @param operation string that can be either "updates" or "fill"
     * @param values    string that contains '##' separated fields and values from feature file
     */

    public void fillSingInScreen(String operation, String values, ScenarioContext scenarioContext) {
        LinkedHashMap<String, String> signInPageFieldsAndValues = new LinkedHashMap<>();
        allFieldAndValuesFromFeatureFile = values.split("##");
        if (operation.equalsIgnoreCase(UPDATES)) {
            signInPageFieldsAndValues.put("createAccountButton", CLICK);
        }
        if (!values.equalsIgnoreCase(DEFAULT)) {
            for (String fieldAndValueFromFeatureFile : allFieldAndValuesFromFeatureFile) {
                String[] fieldAndValue = fieldAndValueFromFeatureFile.split(":");
                //calling this method to fill values that has ':'
                fieldAndValue[1] = valueToBeFilled(fieldAndValue);
                signInPageFieldsAndValues.put(fieldAndValue[0], fieldAndValue[1]);
            }

        }
        scenarioContext.getLOG().info("Filling - Security Page");
        fillMyScreen(signInPageFieldsAndValues, signInPage, scenarioContext);
        softAssertions.assertAll();
    }



        /**
         * Validating the values for the particular fields in the screen
         *
         * @param fieldValues which has number of field values to validate whether entering value is correct or not
         */
    public void validateValues(String screen, DataTable fieldValues, ScenarioContext scenarioContext) {
        //exceptionPage.waitForElementNotPresent(LOADER, scenarioContext);
        scenarioContext.getLOG().info("Screen Name", screen);
        AbstractSteps page = getPageObject(screen);
        for (List<String> message : fieldValues.asLists()) {
            for (String mess : message) {
                String[] fieldElementsAndValues = mess.split("##");
                for (String elementAndValue : fieldElementsAndValues) {
                    values = elementAndValue.split(":", 2);
                    if (values.length > 2) {
                        for (int i = 2; i < values.length; i++) {
                            values[1] = values[1] + ":" + values[i];
                        }
                    }
                    if (values[1].contains("SystemDate")) {
                        String[] dateAndFormat = values[1].split("--");
                        values[1] = gettingSystemDate(dateAndFormat);
                    }
                    if (values[0].toLowerCase().contains("button")) {
                        validateButtons(scenarioContext, page, values);
                    } else if ((values[0].toLowerCase().contains(DROPDOWN))) {
                        validateDropDowns(scenarioContext, page, values);
                    } else if (values[0].toLowerCase().contains("textfield")) {
                        validateTextFields(scenarioContext, page, values);
                    } else if (values[0].toLowerCase().contains("textarea")) {
                        validateTexArea(scenarioContext, page, values);
                    } else if (values[0].toLowerCase().contains(LABEL) && values[1].toLowerCase().contains(LABEL)) {
                        softAssertions.assertThat(page.findElementPresent(values[0], scenarioContext).getText().replace("£-", "").equals(page.findElementPresent(values[1], scenarioContext).getText().replace("£", ""))).describedAs(CHECKING_IF_MSG + values[0] + FIELD_CONTAINS_MSG + values[1] + ACTUAL_VALUE_MSG + page.findElementPresent(values[1], scenarioContext).getText().replace("£", "")).isTrue();
                    } else if (values[0].toLowerCase().contains("checkbox")) {
                        validateCheckBox(scenarioContext, page, values);
                    } else if (values[0].toLowerCase().contains(HYPERLINK)) {
                        validateHyperLink(scenarioContext, page, values);
                    } else if (values[0].toLowerCase().contains("tabname")) {
                        scenarioContext.getLOG().info("Validating {} is present", values[0]);
                        softAssertions.assertThat(scenarioContext.getDriver().getTitle().equals(values[1])).describedAs(CHECKING_IF_MSG + values[0] + FIELD_CONTAINS_MSG + values[1] + ACTUAL_VALUE_MSG + scenarioContext.getDriver().getTitle()).isTrue();
                    } else if (values[0].toLowerCase().contains(LABEL) && !(values[1].toLowerCase().contains(LABEL))) {
                        validateLabels(scenarioContext, page, values);
                    } else {
                        scenarioContext.getLOG().info("Field name does not correspond to any field type - {} -- {}", values[0], values[1]);
                        softAssertions.fail("Field name does not correspond to any field type - " + values[0] + " -- " + values[1]);
                    }
                }
            }
        }
        softAssertions.assertAll();
    }

    /**
     * Validating the values for the buttons
     *
     * @param scenarioContext which has number of all data
     * @param page            which is the page object
     * @param values          the value to be verified
     */
    private void validateButtons(ScenarioContext scenarioContext, AbstractSteps page, String[] values) {
        scenarioContext.getLOG().info(VALIDATING_MSG, values[0], values[1]);
        if (values[0].toLowerCase().contains("text") && !values[1].toLowerCase().contains(CLICKED) && !values[1].toLowerCase().contains(DISPLAYED) && !values[1].toLowerCase().contains("abled") && !values[1].toLowerCase().contains(PRESENT)) {
            softAssertions.assertThat(page.findElementPresent(values[0], scenarioContext).getText().equals(values[1])).describedAs("checking if " + values[0] + " button class contains " + values[1] + " actual value is " + page.findElementPresent(values[0], scenarioContext).getText()).isTrue();
        } else {
            switch (values[1].toLowerCase()) {
                case CLICKED:
                    softAssertions.assertThat(page.findElementPresent(values[0], scenarioContext).isSelected()).describedAs(CHECKING_MSG + values[0] + " button is selected").isTrue();
                    break;
                case PRESENT:
                    softAssertions.assertThat(!page.findElementsPresent(values[0], scenarioContext).isEmpty()).describedAs(CHECKING_MSG + values[0] + " button is displayed").isTrue();
                    break;
                case DISPLAYED:
                    softAssertions.assertThat(page.findElementPresent(values[0], scenarioContext).isDisplayed()).describedAs(CHECKING_MSG + values[0] + " button is displayed").isTrue();
                    break;
                case NOTDISPLAYED:
                    softAssertions.assertThat(!page.findElementPresent(values[0], scenarioContext).isDisplayed()).describedAs(CHECKING_MSG + values[0] + NOT_DISPLAYED_MSG).isTrue();
                    break;
                case "enabled":
                    softAssertions.assertThat(page.findElementPresent(values[0], scenarioContext).isEnabled()).describedAs(CHECKING_MSG + values[0] + "  is enabled").isTrue();
                    break;
                case DISABLED:
                    boolean disabled1 = !page.findElementPresent(values[0], scenarioContext).isEnabled();
                    boolean disabled = false;
                    try {
                        page.findElementPresent(values[0], scenarioContext).getAttribute(DISABLED);
                        disabled = true;
                    } catch (Exception e) {
                        scenarioContext.getLOG().info(e.toString());
                    }
                    softAssertions.assertThat((disabled || disabled1)).describedAs(CHECKING_MSG + values[1] + " button is not enabled").isTrue();
                    break;
                case "disable":
                    softAssertions.assertThat(page.findElementPresent(values[0], scenarioContext).getAttribute(CLASS).contains(values[1])).describedAs(CHECKING_MSG + values[0] + NOT_ENABLED_MSG).isTrue();
                    break;
                case "notclicked":
                    softAssertions.assertThat(!(page.findElementPresent(values[0], scenarioContext).isSelected())).describedAs(CHECKING_MSG + values[0] + " button is not selected").isTrue();
                    break;
                case NOTPRESENT:
                    softAssertions.assertThat(page.findElementsPresent(values[0], scenarioContext).isEmpty()).describedAs(CHECKING_MSG + values[0] + " button is not present in the screen").isTrue();
                    break;
                default:
                    softAssertions.fail(values[1] + " does not correspond to any valid values");
            }
        }
    }

    /**
     * Validating the values for the dropdowns
     *
     * @param scenarioContext which has number of all data
     * @param page            which is the page object
     * @param values          the value to be verified
     */
    private void validateDropDowns(ScenarioContext scenarioContext, AbstractSteps page, String[] values) {
        scenarioContext.getLOG().info(VALIDATING_CONTAINS_MSG, values[0], values[1]);
        //DDL wrapping function is removed and all the drop down values will be stored in select tag
        if (values[0].contains(DRIVING_LICENSE_YEARS_DROPDOWN)) {
            softAssertions.assertThat(new Select(page.findElementPresent(values[0], scenarioContext)).getFirstSelectedOption().getText().replace(" ", "").replaceAll("\n", "").equals(values[1])).describedAs("Checking if selected option in " + values[0] + " is " + values[1] + ACTUAL_VALUE_MSG + new Select(page.findElementPresent(values[0], scenarioContext)).getFirstSelectedOption().getText()).isTrue();
        } else if (values[1].equalsIgnoreCase(DISPLAYED)) {
            softAssertions.assertThat(page.findElementPresent(values[0], scenarioContext).isDisplayed()).describedAs(CHECKING_MSG + values[0] + "  is displayed").isTrue();
        } else if (values[1].equalsIgnoreCase(NOTDISPLAYED)) {
            softAssertions.assertThat(!page.findElementPresent(values[0], scenarioContext).isDisplayed()).describedAs(CHECKING_MSG + values[0] + NOT_DISPLAYED_MSG).isTrue();
        } else if (values[1].equalsIgnoreCase(NOTPRESENT)) {
            softAssertions.assertThat(page.findElementsPresent(values[0], scenarioContext).isEmpty()).describedAs(CHECKING_IF_MSG + values[0] + " is not present in the screen").isTrue();
        } else if (values[1].equalsIgnoreCase(DISABLED)) {
            softAssertions.assertThat(!page.findElementPresent(values[0], scenarioContext).isEnabled()).describedAs(CHECKING_MSG + values[0] + NOT_ENABLED_MSG).isTrue();
        } else {
            softAssertions.assertThat(new Select(page.findElementPresent(values[0], scenarioContext)).getFirstSelectedOption().getText().trim().equals(values[1])).describedAs("Checking if selected option in " + values[0] + " is " + values[1] + ACTUAL_VALUE_MSG + new Select(page.findElementPresent(values[0], scenarioContext)).getFirstSelectedOption().getText().trim()).isTrue();
        }
    }

    /**
     * Validating the values for the textfields
     *
     * @param scenarioContext which has number of all data
     * @param page            which is the page object
     * @param values          the value to be verified
     */
    private void validateTextFields(ScenarioContext scenarioContext, AbstractSteps page, String[] values) {
        scenarioContext.getLOG().info(VALIDATING_CONTAINS_MSG, values[0], values[1]);
        if (values[1].equalsIgnoreCase(BLANK)) {
            softAssertions.assertThat(page.findElementPresent(values[0], scenarioContext).getAttribute(VALUE).isEmpty()).describedAs(CHECKING_IF_MSG + values[0] + EMPTY_MSG).isTrue();
        } else if (values[1].equalsIgnoreCase(DISPLAYED)) {
            softAssertions.assertThat(page.findElementPresent(values[0], scenarioContext).isDisplayed()).describedAs(CHECKING_MSG + values[0] + "  is displayed").isTrue();
        } else if (values[1].equalsIgnoreCase(NOTDISPLAYED)) {
            softAssertions.assertThat(page.findElementsPresent(values[0], scenarioContext).isEmpty()).describedAs(CHECKING_MSG + values[0] + NOT_DISPLAYED_MSG).isTrue();
        } else if (values[1].equalsIgnoreCase(DISABLED)) {
            softAssertions.assertThat(!page.findElementPresent(values[0], scenarioContext).isEnabled()).describedAs(CHECKING_MSG + values[0] + NOT_ENABLED_MSG).isTrue();
        } else if (values[0].toLowerCase().contains("defaulttextfield")) {
            softAssertions.assertThat(page.findElementPresent(values[0], scenarioContext).getAttribute("placeholder").trim().equals(values[1])).describedAs(CHECKING_IF_MSG + values[0] + EQUAL_TO_MSG + values[1] + ACTUAL_VALUE_MSG + page.findElementPresent(values[0], scenarioContext).getAttribute("placeholder").trim()).isTrue();
        } else if (values[1].toLowerCase().contains("readonly")) {
            Boolean isReadOnly = ((page.findElementPresent(values[0], scenarioContext).getAttribute(CLASS).trim().equals("form-control")) || (page.findElementPresent(values[0], scenarioContext).getAttribute("name").contains("READONLY")));
            softAssertions.assertThat(isReadOnly).describedAs(CHECKING_IF_MSG + values[0] + " is read only").isTrue();
        } else {
            softAssertions.assertThat(page.findElementPresent(values[0], scenarioContext).getAttribute(VALUE).trim().equals(values[1])).describedAs(CHECKING_IF_MSG + values[0] + EQUAL_TO_MSG + values[1] + ACTUAL_VALUE_MSG + page.findElementPresent(values[0], scenarioContext).getAttribute(VALUE)).isTrue();
        }
    }

    /**
     * Validating the values for the text area
     *
     * @param scenarioContext which has number of all data
     * @param page            which is the page object
     * @param values          the value to be verified
     */
    private void validateTexArea(ScenarioContext scenarioContext, AbstractSteps page, String[] values) {
        scenarioContext.getLOG().info(VALIDATING_CONTAINS_MSG, values[0], values[1]);
        if (values[1].equalsIgnoreCase(NOTDISPLAYED) && values[0].toLowerCase().contains("errormessage")) {
            softAssertions.assertThat(!page.findElementPresent(values[0], scenarioContext).isDisplayed()).describedAs(CHECKING_MSG + values[0] + NOT_DISPLAYED_MSG).isTrue();
        } else if (values[1].equalsIgnoreCase(NOTDISPLAYED)) {
            softAssertions.assertThat(!page.findElementPresent(values[0], scenarioContext).isDisplayed()).describedAs(CHECKING_MSG + values[0] + NOT_DISPLAYED_MSG).isTrue();
        } else if (values[1].equalsIgnoreCase(NOTPRESENT)) {
            softAssertions.assertThat(page.findElementsPresent(values[0], scenarioContext).isEmpty()).describedAs(CHECKING_IF_MSG + values[0] + " is not present in the screen").isTrue();
        } else if ((((values[0].toLowerCase().contains("address")) || (values[0].toLowerCase().contains("textareavalue"))) && (!values[0].equalsIgnoreCase(BILLING_ADDRESS_TEXTAREA)))) {
            scenarioContext.getLOG().info("============================================{}", page.findElementPresent(values[0], scenarioContext).getAttribute(VALUE));
            softAssertions.assertThat(page.findElementPresent(values[0], scenarioContext).getAttribute(VALUE).trim().equals(values[1])).describedAs(CHECKING_IF_MSG + values[0] + EQUAL_TO_MSG + values[1] + ACTUAL_VALUE_MSG + page.findElementPresent(values[0], scenarioContext).getAttribute(VALUE).trim()).isTrue();
        } else if (values[1].equalsIgnoreCase(BLANK)) {
            softAssertions.assertThat(page.findElementPresent(values[0], scenarioContext).getText().isEmpty()).describedAs(CHECKING_IF_MSG + values[0] + EMPTY_MSG).isTrue();
        } else if (values[1].toLowerCase().contains("!")) {
            String[] dataValues = values[1].split("!");
            values[0] = page.findElementPresent(values[0], scenarioContext).getText();
            scenarioContext.getLOG().info("Validating {} is not equal to {}", values[0], dataValues[1]);
            softAssertions.assertThat((values[0].compareTo(dataValues[1]) != 0)).describedAs(CHECKING_IF_MSG + values[0] + EQUAL_TO_MSG + values[1] + ACTUAL_VALUE_MSG + dataValues[1]).isTrue();
        } else {
            scenarioContext.getLOG().info("============================================{}", page.findElementPresent(values[0], scenarioContext).getText());
            softAssertions.assertThat(page.findElementPresent(values[0], scenarioContext).getText().equals(values[1])).describedAs(CHECKING_IF_MSG + values[0] + EQUAL_TO_MSG + values[1] + ACTUAL_VALUE_MSG + page.findElementPresent(values[0], scenarioContext).getText()).isTrue();
        }
    }

    /**
     * Validating the values for the labels
     *
     * @param scenarioContext which has number of all data
     * @param page            which is the page object
     * @param values          the value to be verified
     */
    private void validateLabels(ScenarioContext scenarioContext, AbstractSteps page, String[] values) {
        scenarioContext.getLOG().info(VALIDATING_MSG, values[0], values[1]);
        if (values[1].equalsIgnoreCase(DISPLAYED)) {
            softAssertions.assertThat(page.findElementPresent(values[0], scenarioContext).isDisplayed()).describedAs(CHECKING_MSG + values[0] + " label is displayed").isTrue();
        } else if (values[1].equalsIgnoreCase(NOTDISPLAYED)) {
            softAssertions.assertThat(!page.findElementPresent(values[0], scenarioContext).isDisplayed()).describedAs(CHECKING_MSG + values[0] + " label is not displayed").isTrue();
        } else if (values[1].equalsIgnoreCase("readonly") || values[1].equalsIgnoreCase("active") || values[1].equalsIgnoreCase("inactive") || values[1].equalsIgnoreCase(DISABLED) || values[1].equalsIgnoreCase("complete")) {
            softAssertions.assertThat(page.findElementPresent(values[0], scenarioContext).getAttribute(CLASS).trim().contains(values[1])).describedAs(CHECKING_IF_MSG + values[0] + " field class contains " + values[1] + ACTUAL_VALUE_MSG + page.findElementPresent(values[0], scenarioContext).getAttribute(CLASS).trim()).isTrue();
        }
        else if (values[1].equalsIgnoreCase(NOTPRESENT)) {
            softAssertions.assertThat(page.findElementsPresent(values[0], scenarioContext).isEmpty()).describedAs(CHECKING_IF_MSG + values[0] + " label is not present in the screen").isTrue();
        } else if (values[1].equalsIgnoreCase(PRESENT)) {
            softAssertions.assertThat(!page.findElementsPresent(values[0], scenarioContext).isEmpty()).describedAs(CHECKING_IF_MSG + values[0] + " label is not present in the screen").isTrue();
        } else if (values[1].equalsIgnoreCase(BLANK)) {
            softAssertions.assertThat(page.findElementPresent(values[0], scenarioContext).getText().equals("")).describedAs(CHECKING_IF_MSG + values[0] + EMPTY_MSG).isTrue();
        } else {
            scenarioContext.getLOG().info("value to check is {}", values[1]);
            scenarioContext.getLOG().info("value to present with trim check is {}", page.findElementPresent(values[0], scenarioContext).getText());
            scenarioContext.getLOG().info("value to present without trim heck is {}", page.findElementPresent(values[0], scenarioContext).getText().trim());
            //softAssertions.assertThat(page.findElementPresent(values[0], scenarioContext).getText().trim().equals(values[1])).describedAs(CHECKING_IF_MSG + values[0] + FIELD_CONTAINS_MSG + values[1] + ACTUAL_VALUE_MSG + page.findElementPresent(values[0], scenarioContext).getText().trim()).isTrue();
        }
    }

    /**
     * Validating the values for the checkboxes
     *
     * @param scenarioContext which has number of all data
     * @param page            which is the page object
     * @param values          the value to be verified
     */
    private void validateCheckBox(ScenarioContext scenarioContext, AbstractSteps page, String[] values) {
        if (values[1].equalsIgnoreCase("checked")) {
            scenarioContext.getLOG().info("Validating {} is checked", values[0]);
            softAssertions.assertThat(page.findElementPresent(values[0], scenarioContext).isSelected()).describedAs(CHECKING_IF_MSG + values[0] + " is checked").isTrue();
        } else if (values[1].equalsIgnoreCase("unchecked")) {
            scenarioContext.getLOG().info("Validating {} is unchecked", values[0]);
            if (!(page.findElementPresent(values[0], scenarioContext).isSelected())) {
                softAssertions.assertThat(!page.findElementPresent(values[0], scenarioContext).isSelected()).describedAs(CHECKING_IF_MSG + values[0] + " field is " + values[1] + ", actual value is checked").isTrue();
            }
        } else if (values[1].equalsIgnoreCase(NOTDISPLAYED)) {
            softAssertions.assertThat(!page.findElementPresent(values[0], scenarioContext).isDisplayed()).describedAs(CHECKING_MSG + values[0] + NOT_DISPLAYED_MSG).isTrue();
        } else if (values[1].equalsIgnoreCase(NOTPRESENT)) {
            softAssertions.assertThat(page.findElementsPresent(values[0], scenarioContext).isEmpty()).describedAs(CHECKING_MSG + values[0] + " checkbox is not present in the screen").isTrue();
        } else {
            softAssertions.fail(values[1] + " does not correspond to any valid values");
        }
    }

    /**
     * Validating the values for the hyperlinks
     *
     * @param scenarioContext which has number of all data
     * @param page            which is the page object
     * @param values          the value to be verified
     */
    private void validateHyperLink(ScenarioContext scenarioContext, AbstractSteps page, String[] values) {
        scenarioContext.getLOG().info("Validating {} is valid", values[0]);
        if (values[1].equalsIgnoreCase(CLICKED)) {
            abstractSteps.switchToLastOpenWindow(scenarioContext);
            softAssertions.assertThat(scenarioContext.getDriver().getTitle().contains("404")).isFalse();
            scenarioContext.getDriver().close();
            abstractSteps.switchToLastOpenWindow(scenarioContext);
        } else if (values[1].toLowerCase().contains(DISABLED)) {
            Boolean isHrefLinkNotClickable = (page.findElementPresent(values[0], scenarioContext).getAttribute("href")) == null;
            scenarioContext.getLOG().info("href link value {}", isHrefLinkNotClickable);
            softAssertions.assertThat(isHrefLinkNotClickable).describedAs(CHECKING_IF_MSG + values[0] + " is disabled").isTrue();
        } else if (values[1].startsWith("www") || values[1].startsWith("https")) {
            abstractSteps.switchToLastOpenWindow(scenarioContext);
            scenarioContext.getLOG().info("Switching to URL\t{}", values[1]);
            softAssertions.assertThat(scenarioContext.getDriver().getCurrentUrl().contains(values[1])).isTrue();
            scenarioContext.getDriver().close();
            abstractSteps.switchToLastOpenWindow(scenarioContext);
        } else if (values[1].contains("ENV-")) {
            String[] env = values[1].split("-")[1].split("/");
            abstractSteps.switchToLastOpenWindow(scenarioContext);
            values[1] = System.getProperty(env[0]).concat("/").concat(env[1]);
            scenarioContext.getLOG().info("Switching to URL\t{}", values[1]);
            softAssertions.assertThat(scenarioContext.getDriver().getCurrentUrl().contains(System.getProperty(env[0]).concat("/").concat(env[1]))).isTrue();
            scenarioContext.getDriver().close();
            abstractSteps.switchToLastOpenWindow(scenarioContext);
        } else {
            scenarioContext.getLOG().info(VALIDATING_MSG, values[0], values[1]);
            if (values[1].equalsIgnoreCase(DISPLAYED)) {
                softAssertions.assertThat(page.findElementPresent(values[0], scenarioContext).isDisplayed()).describedAs(CHECKING_MSG + values[0] + " is displayed").isTrue();
            } else {
                softAssertions.assertThat(page.findElementPresent(values[0], scenarioContext).getText().equals(values[1])).describedAs(CHECKING_IF_MSG + values[0] + " contains " + values[1] + ACTUAL_VALUE_MSG + page.findElementPresent(values[0], scenarioContext).getText()).isTrue();
            }
        }
    }


}
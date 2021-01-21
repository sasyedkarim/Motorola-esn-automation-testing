package co.uk.motorola.esn.pages;

import co.uk.motorola.esn.steps.AbstractSteps;
import org.openqa.selenium.By;

public class RegisterPage extends AbstractSteps {

    public RegisterPage() {

        webElementPath.put("createAccountLabel", By.xpath("//*[@id=\"ap_register_form\"]/div/div/h1"));
        webElementPath.put("yourNameTextField", By.xpath("//*[@id=\"ap_customer_name\"]"));
        webElementPath.put("emailAddressTextField", By.xpath("//*[@id=\"ap_email\"]"));
        webElementPath.put("passwordTextField", By.xpath("//*[@id=\"ap_password\"]"));
        webElementPath.put("confirmPasswordTextField", By.xpath("//*[@id=\"ap_password_check\"]"));
        webElementPath.put("continueButton", By.xpath("//*[@id=\"continue\"]"));
        webElementPath.put("passwordHintLabel", By.className("a-alert-content"));
        webElementPath.put("continueButtonText", By.xpath("//*[@id=\"continue\"]"));
    }
}

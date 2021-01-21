package co.uk.motorola.esn.pages;

import co.uk.motorola.esn.steps.AbstractSteps;
import org.openqa.selenium.By;

public class SignInPage extends AbstractSteps {

    public SignInPage() {

        webElementPath.put("createAccountButton", By.xpath("//*[@id=\"createAccountSubmit\"]"));
        webElementPath.put("signInLabel", By.xpath("//*[@id=\"authportal-main-section\"]/div[2]/div/div[1]/form/div/div/div/h1"));
        webElementPath.put("emailOrPhoneNumberLabel", By.xpath("//*[@id=\"authportal-main-section\"]/div[2]/div/div[1]/form/div/div/div/div[1]/label"));
        webElementPath.put("newToAmazonLabel", By.xpath("//*[@id=\"authportal-main-section\"]/div[2]/div/div[2]/h5"));
        }
}

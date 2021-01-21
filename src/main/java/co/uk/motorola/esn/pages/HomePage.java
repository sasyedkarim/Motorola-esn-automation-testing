package co.uk.motorola.esn.pages;

import co.uk.motorola.esn.steps.AbstractSteps;
import org.openqa.selenium.By;

public class HomePage extends AbstractSteps {

    public HomePage() {

        webElementPath.put("acceptCookiesButton", By.xpath("//*[@id=\"sp-cc-accept\"]"));
        webElementPath.put("accountListHyperlink", By.xpath("//*[@id=\"nav-link-accountList\"]"));
        webElementPath.put("homePageTitleLabel", By.xpath("/html/head/title"));
    }
}

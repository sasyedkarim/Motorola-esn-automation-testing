package co.uk.motorola.esn.pages;

import co.uk.motorola.esn.steps.AbstractSteps;
import org.openqa.selenium.By;

public class ExceptionPage extends AbstractSteps {

    public ExceptionPage() {

        webElementPath.put("cookiesHyperlink", By.xpath("//a[contains(text(),'Cookies')]"));
    }
}

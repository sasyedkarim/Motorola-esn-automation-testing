package co.uk.motorola.esn.steps;

import co.uk.motorola.esn.context.ScenarioContext;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;

public class AbstractSteps extends AbstractPage {

    protected final HashMap<String, By> webElementPath = new HashMap<>();
    String executeScript = "window.onbeforeunload = function(e){};";

    public WebElement findElementPresent(String elementName, ScenarioContext scenarioContext) {
        scenarioContext.getDriver().executeScript(executeScript);
        return waitForElementPresent(webElementPath.get(elementName), scenarioContext);
    }

    public List<WebElement> findElementsPresent(String elementName, ScenarioContext scenarioContext) {
        scenarioContext.getDriver().executeScript(executeScript);
        return waitForElementsPresent(webElementPath.get(elementName), scenarioContext);
    }

    public WebElement findElementClickable(String elementName, ScenarioContext scenarioContext) {
        scenarioContext.getDriver().executeScript(executeScript);
        return waitForUnstableElement(webElementPath.get(elementName), scenarioContext);
    }

    public WebElement findElementVisible(String elementName, ScenarioContext scenarioContext) {
        scenarioContext.getDriver().executeScript(executeScript);
        return waitForElementVisible(webElementPath.get(elementName), scenarioContext);
    }

    public boolean isMessagePresent(String message, ScenarioContext scenarioContext) {
        scenarioContext.getDriver().executeScript(executeScript);
        String xpath;
        StringBuilder msg = new StringBuilder();
        String apos = "random char";
        if (message.contains("'") || message.contains("’")) {
            apos = "'";
        }
        if (message.contains(apos)) {
            String[] split = message.split(apos);
            for (int i = 0; i < split.length; i++) {
                if (i == split.length - 1) {
                    msg.append(apos).append(split[i]).append(apos).append(",");
                } else {
                    msg.append(apos).append(split[i]).append(apos).append(",\"'\",");
                }
            }
            xpath = "//*[contains(.,concat(" + msg + "\"\"))]";
        } else {
            xpath = "//*[contains(.,\"" + message + "\")]";
        }
        return elementDisplayed(By.xpath(xpath), scenarioContext);
    }

    public void waitForElementNotPresent(String elementName, ScenarioContext scenarioContext) {
        scenarioContext.getDriver().executeScript(executeScript);
        new WebDriverWait(scenarioContext.getDriver(), Duration.ofSeconds(60)).until(ExpectedConditions.not(ExpectedConditions.presenceOfAllElementsLocatedBy(webElementPath.get(elementName))));
    }

    public void waitForAttributeToBe(String elementName, ScenarioContext scenarioContext) {
        new WebDriverWait(scenarioContext.getDriver(), Duration.ofSeconds(10)).until(ExpectedConditions.attributeContains(webElementPath.get(elementName), "aria-expanded", "true"));
    }
}
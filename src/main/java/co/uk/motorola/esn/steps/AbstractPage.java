package co.uk.motorola.esn.steps;

import co.uk.motorola.esn.context.ScenarioContext;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;
import java.util.List;
import java.util.Set;

public class AbstractPage {
    private int driverWaitTime = 10;
    String context = "context";

    public AbstractPage() {
        if (System.getProperty("driverWaitTime") != null) {
            this.driverWaitTime = Integer.parseInt(System.getProperty("driverWaitTime"));
        }
    }

    public WebElement findElement(By by, ScenarioContext scenarioContext) {
        return scenarioContext.getDriver().findElement(by);
    }

    public List<WebElement> findElements(By by, ScenarioContext scenarioContext) {
        return scenarioContext.getDriver().findElements(by);
    }

    public WebElement waitForUnstableElement(By by, ScenarioContext scenarioContext) {
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException var3) {
            scenarioContext.getLOG().error(var3.getMessage());
            Thread.currentThread().interrupt();
        }
        return this.waitForElementPresent(by, scenarioContext);
    }

    public WebElement waitForElementPresent(By by, ScenarioContext scenarioContext) {
        WebDriverWait wait = new WebDriverWait(scenarioContext.getDriver(), Duration.ofSeconds(this.driverWaitTime));
        try {
            return wait.until(ExpectedConditions.presenceOfElementLocated(by));
        } catch (NoAlertPresentException | UnhandledAlertException var4) {
            scenarioContext.getLOG().info(context, var4);
            scenarioContext.getDriver().switchTo().alert().dismiss();
            return wait.until(ExpectedConditions.presenceOfElementLocated(by));
        }
    }

    public List<WebElement> waitForElementsPresent(By by, ScenarioContext scenarioContext) {
        try {
            Wait<WebDriver> wait = new WebDriverWait(scenarioContext.getDriver(), Duration.ofSeconds(this.driverWaitTime), Duration.ofMillis(100L));
            wait.until(ExpectedConditions.elementToBeClickable(by));
            return this.findElements(by, scenarioContext);
        } catch (TimeoutException var3) {
            scenarioContext.getLOG().info(context, var3);
            return this.findElements(by, scenarioContext);
        }
    }

    public WebElement waitForElementVisible(By by, ScenarioContext scenarioContext) {
        Wait<WebDriver> wait = new WebDriverWait(scenarioContext.getDriver(), Duration.ofSeconds(this.driverWaitTime));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    public Object executeScript(String string, WebElement element, ScenarioContext scenarioContext) throws InterruptedException {
        try {
            return scenarioContext.getDriver().executeScript(string, element);
        } catch (StaleElementReferenceException var5) {
            scenarioContext.getLOG().warn(context, var5);
            this.waitForPageLoad();
            return scenarioContext.getDriver().executeScript(string, element);
        }
    }

    public Object executeScript(String script, ScenarioContext scenarioContext) {
        return scenarioContext.getDriver().executeScript(script);
    }

    public boolean elementDisplayed(By by, ScenarioContext scenarioContext) {
        try {
            Wait<WebDriver> wait = new WebDriverWait(scenarioContext.getDriver(), Duration.ofMillis(5L));
            wait.until(ExpectedConditions.presenceOfElementLocated(by));
            this.findElement(by, scenarioContext);
            return true;
        } catch (Exception var3) {
            scenarioContext.getLOG().info(context, var3);
            return false;
        }
    }

    public void waitForPageLoad() throws InterruptedException {
        Thread.sleep(2000L);
    }

    public void switchToFrameById(WebElement wbFrame, ScenarioContext scenarioContext) {
        scenarioContext.getDriver().switchTo().frame(wbFrame);
    }

    public void switchToDefault(ScenarioContext scenarioContext) {
        scenarioContext.getDriver().switchTo().defaultContent();
    }

    public void switchToLastOpenWindow(ScenarioContext scenarioContext) {
        Set<String> handles = scenarioContext.getDriver().getWindowHandles();
        if (!handles.isEmpty()) {
            String handle = handles.toArray()[handles.size() - 1].toString();
            scenarioContext.getDriver().switchTo().window(handle);
        }
    }

}

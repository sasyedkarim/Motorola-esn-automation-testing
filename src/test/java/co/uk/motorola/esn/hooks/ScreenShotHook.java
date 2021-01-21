package co.uk.motorola.esn.hooks;

import co.uk.motorola.esn.context.ScenarioContext;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.*;

import java.io.IOException;

public class ScreenShotHook {

    private final ScenarioContext scenarioContext;

    public ScreenShotHook(ScenarioContext scenarioContext) {
        this.scenarioContext = scenarioContext;
    }

    /**
     * <p>
     * Takes screen-shot if the scenario fails
     * Generate HAR file on test failure
     * </p>
     *
     * @param scenario will be the individual scenario's within the Feature files
     * @throws InterruptedException Exception thrown if there is an interruption within the JVM
     */
    @After()
    public void afterTest(Scenario scenario) throws InterruptedException, IOException {
        try {
              scenarioContext.getDriver().executeScript("window.scrollBy(0,-250);");
                for (int i = 0; i < 4; i++) {
                    byte[] screenShot = ((TakesScreenshot) scenarioContext.getDriver()).getScreenshotAs(OutputType.BYTES);
                    scenario.attach(screenShot, "image/png", "Screenshot " + (i + 1));
                    scenarioContext.getDriver().executeScript("window.scrollBy(0,500);");
                }
                if (!System.getProperty("driverType").equals("chrome") && (Integer.parseInt(System.getProperty("surefire.thread.count")) > 1 || Integer.parseInt(System.getProperty("surefire.fork.count")) > 1)) {
                    scenarioContext.getDriver().close();
                    scenarioContext.getDriver().quit();
                }
        } catch (WebDriverException e) {
            scenarioContext.getLOG().error(e.getMessage());
        }
    }


    /**
     * <p>
     * Maximise browser window before test begins to keep environment consistent
     * </p>
     */
    @Before()
    public void beforeScenario() {
        scenarioContext.setExecutionCount(scenarioContext.getExecutionCount() + 1);
    }
}
package co.uk.motorola.esn.steps.accessibility;

import co.uk.motorola.esn.context.ScenarioContext;
import org.openqa.selenium.*;
import java.io.IOException;
import java.util.*;

public class AccessibilityScanner {

    private final JsFactory jsFactory;

    public AccessibilityScanner(ScenarioContext scenarioContext) throws IOException {
        jsFactory = JsFactory.getInstance(scenarioContext);
    }

    public Map<String, Object> runAccessibilityAudit(ScenarioContext scenarioContext) {
        Map<String, Object> auditReport = new HashMap<>();
        scenarioContext.getDriver().executeScript(jsFactory.getAccessibilityContent());
        String accessibilityTests = "var auditConfig = new axs.AuditConfiguration(); "
                + "var results = axs.Audit.run(auditConfig);"
                + "var auditScenarioContexts = axs.Audit.auditScenarioContexts(results);"
                + "var report = axs.Audit.createReport(results);return report";
        String report = (String) scenarioContext.getDriver().executeScript(accessibilityTests);

        scenarioContext.getLOG().info("Accessibility Report === {}", report);

        try {
            scenarioContext.getDriver().executeScript("$.active;");
        } catch (WebDriverException e) {
            scenarioContext.getLOG().info("++++++++Injecting jQuery+++++++++++++");
            scenarioContext.getDriver().executeScript(jsFactory.getJqueryContent());
        }

        List<ScenarioContext> errors = parseReport(report, "Error:");
        List<ScenarioContext> warnings = parseReport(report, "Warning:");

        decorateElements(errors, "red", scenarioContext);
        decorateElements(warnings, "yellow", scenarioContext);
        final byte[] screenshot = ((TakesScreenshot) scenarioContext.getDriver()).getScreenshotAs(OutputType.BYTES);
        auditReport.put("error", errors);
        auditReport.put("warning", warnings);
        auditReport.put("screenshot", screenshot);
        auditReport.put("plain_report", report);
        return auditReport;
    }

    private void decorateElements(List<ScenarioContext> results, String color, ScenarioContext scenarioContext) {
        for (ScenarioContext result : results) {
            List<String> locators = result.getElements();
            addBorder(locators, result.getRule(), color, scenarioContext);
        }
    }

    public List<ScenarioContext> parseReport(String report, String filterOn) {
        if (filterOn.toLowerCase().contains("error"))
            filterOn = "Error:";
        else if (filterOn.toLowerCase().contains("warning"))
            filterOn = "Warning:";
        else
            throw new IllegalArgumentException("Currently only support filtering on Error: and Warning:");
        if (report == null)
            throw new NullPointerException("Report to parse cannot be null");
        List<ScenarioContext> parsedScenarioContext = new ArrayList<>();
        int startError = report.indexOf(filterOn);
        while (startError > 0) {
            ScenarioContext result = new ScenarioContext();
            int end = report.indexOf("\n\n", startError);
            String error = report.substring(startError + filterOn.length(), end).trim();
            result.setRule(error.substring(0, error.indexOf("\n")));
            String link = null;
            String[] locators;
            int elementStart = error.indexOf("\n") + 1;
            String element;
            if (error.contains("See")) {
                element = error.substring(elementStart, error.indexOf("See"));
                link = error.substring(error.indexOf("See"));
            } else {
                element = error.substring(elementStart);
            }
            locators = element.split("\n");
            result.setElements(Arrays.asList(locators));
            result.setUrl(link);
            parsedScenarioContext.add(result);
            startError = report.indexOf(filterOn, end);
        }
        return parsedScenarioContext;
    }

    private void addBorder(List<String> locators, String rule, String color, ScenarioContext scenarioContext) {
        for (String locator : locators) {
            rule = new StringBuilder().append("<p>").append(rule).append("</p>").toString();
            String script = "$(\"" + locator + "\").css(\"border\",\"5px solid " + color + "\")";
            scenarioContext.getDriver().executeScript(script);
        }
    }
}

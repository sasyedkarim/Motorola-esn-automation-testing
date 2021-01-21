package co.uk.motorola.esn.utils.common;

import co.uk.motorola.esn.context.ScenarioContext;
import co.uk.motorola.esn.steps.accessibility.AccessibilityScanner;
import org.assertj.core.api.SoftAssertions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class AccessibilityUtil {

    private final SoftAssertions softAssertions = new SoftAssertions();

    public Map<String, Object> runAudit(ScenarioContext scenarioContext) throws IOException {
        Map<String, Object> auditReport;
        AccessibilityScanner scanner;
        scanner = new AccessibilityScanner(scenarioContext);
        auditReport = scanner.runAccessibilityAudit(scenarioContext);
        return auditReport;

    }

    public void validateAccessibilityTest(Map<String, Object> audit, ScenarioContext scenarioContext) {
        ArrayList<String> al = new ArrayList<>();
        List<ScenarioContext> errors = (List<ScenarioContext>) audit.get("warning");
        for (ScenarioContext error : errors) {
            al.add("Rule *************** " + error.getRule());
            List<String> ignoredAccessibilityFailures = Arrays.asList("AX_FOCUS_01", "AX_TEXT_02", "AX_COLOR_01", "AX_IMAGE_01 ");
            boolean ignoreFailures = ignoredAccessibilityFailures.stream().anyMatch((error.getRule())::contains);
            if (!ignoreFailures) {
                for (String element : error.getElements()) {
                    al.add("Violated Element ----- " + (element));
                    scenarioContext.getLOG().info("Violated Elements : ------ {}", element);
                    softAssertions.fail("Rule : " + error.getRule() + " Violated Elements : " + element);
                }
            } else {
                scenarioContext.getLOG().info("No accessibility issues observed");
            }
        }
        softAssertions.assertAll();
        scenarioContext.getLOG().info("Summary report .... {}", al);
    }

    public void runAccessibilityAudit(Map<String, Object> audit, ScenarioContext scenarioContext) {
        ArrayList<String> al = new ArrayList<>();
        List<ScenarioContext> errors = (List<ScenarioContext>) audit.get("warning");
        for (ScenarioContext error : errors) {
            al.add(error.getRule());
            scenarioContext.getLOG().info("Error: ----------------- {}", error.getRule());
            al.add(error.getUrl());
            scenarioContext.getLOG().info("Audit Rules URL : ------ {}", error.getUrl());
            for (String element : error.getElements()) {
                scenarioContext.getLOG().info("Violated Elements : ------ {}", element);
            }
        }
    }
}

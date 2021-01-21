package co.uk.motorola.esn.utils.common;

import co.uk.motorola.esn.context.ScenarioContext;
import com.googlecode.fightinglayoutbugs.FightingLayoutBugs;
import com.googlecode.fightinglayoutbugs.LayoutBug;
import com.googlecode.fightinglayoutbugs.SimpleTextDetector;
import com.googlecode.fightinglayoutbugs.WebPage;
import org.assertj.core.api.SoftAssertions;

import java.io.File;
import java.util.*;

public class CompatibilityUtil {

    private int noOfLayoutIssue = 0;
    private String path = System.getProperty("user.dir");
    private String layoutBugScreenshot = path + "/target/LayoutBugsScreenshot";

    private Map<LayoutBug, Integer> validateLayout(ScenarioContext scenarioContext) {
        Map<LayoutBug, Integer> layoutIssue = new LinkedHashMap<>();
        try {
            WebPage webPage = new WebPage(scenarioContext.getDriver());
            FightingLayoutBugs flb = new FightingLayoutBugs();
            flb.enableDebugMode();
            flb.setScreenshotDir(new File(layoutBugScreenshot));
            flb.setTextDetector(new SimpleTextDetector());
            Collection<LayoutBug> layoutBugs = flb.findLayoutBugsIn(webPage);
            for (LayoutBug bug : layoutBugs) {
                if (!(bug.toString().contains("Detected invalid image URL")) &&
                        !(bug.toString().contains("text with too low contrast"))) {
                    layoutIssue.put(bug, noOfLayoutIssue++);
                }
            }
        } catch (Exception e) {
            scenarioContext.getLOG().info(e.toString());
        }
        return layoutIssue;
    }

    public void validateLayoutBug(ScenarioContext scenarioContext) {
        SoftAssertions softAssertions = new SoftAssertions();
        List<Set<LayoutBug>> bug = new ArrayList<>();
        List<Collection<Integer>> noOfLayoutBug = new ArrayList<>();
        bug.add(validateLayout(scenarioContext).keySet());
        noOfLayoutBug.add(validateLayout(scenarioContext).values());
        scenarioContext.getLOG().info("---------------Compatibility Issues---------------");
        for (Set<LayoutBug> bugs : bug) {
            scenarioContext.getLOG().info("Bug - > {}", bugs);
            for (LayoutBug str : bugs) {
                scenarioContext.getLOG().info(str.getDescription());
                softAssertions.fail("Failed in verifying compatibility" + str.getDescription());
            }
        }
        scenarioContext.getLOG().info("Found -> {}", noOfLayoutBug);
        scenarioContext.getLOG().info("-----------------Validation Completed--------------");
        softAssertions.assertAll();
    }


}

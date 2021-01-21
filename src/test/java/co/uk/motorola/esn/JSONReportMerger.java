package co.uk.motorola.esn;

import org.apache.commons.io.FileUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.File;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.UUID;

/**
 * A Cucumber JSON report merger
 */
public class JSONReportMerger {
    public static void main(String[] args) throws Throwable {
        File reportDirectory = new File(args[0]);
        if (reportDirectory.exists()) {
            JSONReportMerger jsonReportMerger = new JSONReportMerger();
            jsonReportMerger.mergeReports(reportDirectory);
        }
    }

    /**
     * Merge all reports together into master report in given reportDirectory
     */
    public void mergeReports(File reportDirectory) throws Throwable {
        String reportFileName = "cucumber.json";
        Path targetReportPath = Paths.get(reportDirectory.toString() + File.separator + reportFileName);
        if (Files.exists(targetReportPath, LinkOption.NOFOLLOW_LINKS)) {
            FileUtils.forceDelete(targetReportPath.toFile());
        }
        File mergedReport = null;
        Collection<File> existingReports = FileUtils.listFiles(reportDirectory, new String[]{"json"}, true);
        for (File report : existingReports) {
            if (!report.getName().equals(reportFileName)) {
                renameEmbeddedImages(report);
                renameFeatureIDsAndNames(report);
                if (mergedReport == null) {
                    mergedReport = new File(reportDirectory, reportFileName);
                    mergedReport.createNewFile();
                    FileUtils.copyFile(report, mergedReport);
                } else {
                    mergeFiles(mergedReport, report);
                }
            }
        }
    }

    /**
     * merge source file into target
     */
    public void mergeFiles(File target, File source) throws Throwable {
        String targetReport = FileUtils.readFileToString(target, "UTF-8");
        String sourceReport = FileUtils.readFileToString(source, "UTF-8");
        JSONParser jp = new JSONParser();
        try {
            JSONArray parsedTargetJSON = (JSONArray) jp.parse(targetReport);
            JSONArray parsedSourceJSON = (JSONArray) jp.parse(sourceReport);
            parsedTargetJSON.addAll(parsedSourceJSON);
            Writer writer = new JSONWriter();
            parsedTargetJSON.writeJSONString(writer);
            FileUtils.writeStringToFile(target, writer.toString(), "UTF-8");
        } catch (Exception pe) {
            pe.printStackTrace();
        }
    }

    /**
     * Prepend parent directory name to all feature names for easier report analysis
     */
    public void renameFeatureIDsAndNames(File reportFile) throws Throwable {
        String reportDirName = reportFile.getParentFile().getName();
        String fileAsString = FileUtils.readFileToString(reportFile, "UTF-8");
        JSONParser jp = new JSONParser();
        try {
            JSONArray parsedJSON = (JSONArray) jp.parse(fileAsString);
            for (Object o : parsedJSON) {
                JSONObject jo = (JSONObject) o;
                String curFeatureID = jo.get("id").toString();
                String curFeatureName = jo.get("name").toString();
                String newFeatureID = String.format("%s - %s", reportDirName, curFeatureID);
                String newFeatureName = String.format("%s - %s", reportDirName, curFeatureName);
                jo.put("id", newFeatureID);
                jo.put("name", newFeatureName);
            }
            Writer writer = new JSONWriter();
            parsedJSON.writeJSONString(writer);
            FileUtils.writeStringToFile(reportFile, writer.toString(), "UTF-8");
        } catch (Exception pe) {
            pe.printStackTrace();
        }
    }

    /**
     * Give unique names to embedded images to ensure they aren't lost during merge
     * Update report file to reflect new image names
     */
    public void renameEmbeddedImages(File reportFile) throws Throwable {
        File reportDirectory = reportFile.getParentFile();
        String reportImageExtension = "png";
        Collection<File> embeddedImages = FileUtils.listFiles(reportDirectory, new String[]{reportImageExtension}, true);
        String fileAsString = FileUtils.readFileToString(reportFile, "UTF-8");
        for (File image : embeddedImages) {
            String curImageName = image.getName();
            String uniqueImageName = reportDirectory.getName() + "-" + UUID.randomUUID().toString() + "." + reportImageExtension;
            image.renameTo(new File(reportDirectory, uniqueImageName));
            fileAsString = fileAsString.replace(curImageName, uniqueImageName);
        }
        FileUtils.writeStringToFile(reportFile, fileAsString, "UTF-8");
    }
}
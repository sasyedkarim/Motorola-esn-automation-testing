package co.uk.motorola.esn;

import de.sstoehr.harreader.HarReader;
import de.sstoehr.harreader.model.Har;
import de.sstoehr.harreader.model.HarEntry;
import org.apache.commons.io.FileUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class AutoSmart {
    private static String errorLogsFile = "Exceptions.xlsx";

    public static void main(String[] args) throws Throwable {

        File myFile = new File("target/Exceptions.xlsx");
        if (myFile.exists())
            myFile.delete();

        try {
            XSSFWorkbook myWorkBook = new XSSFWorkbook();
            XSSFSheet sheet = myWorkBook.createSheet();
            sheet.createRow(0);
            XSSFRow row = sheet.getRow(0);
            row.createCell(0).setCellValue("Feature");
            row.createCell(1).setCellValue("Scenario");
            row.createCell(2).setCellValue("Cucumber Exception");
            row.createCell(3).setCellValue("SureFire Exception");
            row.createCell(4).setCellValue("Har Json String");
            row.createCell(5).setCellValue("Probable Cause of failure");
            row.createCell(6).setCellValue("DOM");
            String[] fileType = new String[]{"xml"};
            File cukeFilesDir = new File("target/cucumber_reports/regression_results");
            File sureFireFilesDir = new File("target/surefire-reports");
            File harFilesDir = new File("target/cucumber_reports");
            File jsonFilesDir = new File("target/cucumber_reports/regression_results");
            if (cukeFilesDir.exists() && sureFireFilesDir.exists() && harFilesDir.exists() && jsonFilesDir.exists()) {
//                System.out.println("=================11111111111111===================");
                List<File> cukeFiles = (List<File>) FileUtils.listFiles(cukeFilesDir, fileType, false);
                List<File> sureFireFiles = (List<File>) FileUtils.listFiles(sureFireFilesDir, fileType, false);
                List<File> harFiles = (List<File>) FileUtils.listFiles(harFilesDir, new String[]{"har"}, false);
                List<File> jsonFiles = (List<File>) FileUtils.listFiles(jsonFilesDir, new String[]{"cucumber.json"}, false);
                ArrayList<ArrayList<String>> cucumberErrorString = getScenarioDetailsWithErrorMessage("cucumber", cukeFiles, "testcase");
                ArrayList<ArrayList<String>> sureErrorString = getScenarioDetailsWithErrorMessage("sureFire", sureFireFiles, "testcase");
                ArrayList<ArrayList<String>> harDetailsList = getHarDetails(harFiles);
                ArrayList<ArrayList<String>> failureCauseList = getErrorAnalysis(harFiles);
                ArrayList<ArrayList<String>> DOMString = getDOM(jsonFiles, sureErrorString);
                printArrayLists(cucumberErrorString);
                printArrayLists(sureErrorString);
                printArrayLists(harDetailsList);
                printArrayLists(failureCauseList);
                printArrayLists(DOMString);
                updateCucumberReport(failureCauseList);
                for (int i = 0; i < sureErrorString.size(); i++) {
                    if (sureErrorString.get(i).get(0).contains(cucumberErrorString.get(i).get(1))) {
                        sheet.createRow(i + 1);
                        row = sheet.getRow(i + 1);
                        row.createCell(0).setCellValue(cucumberErrorString.get(i).get(0));
                        row.createCell(1).setCellValue(cucumberErrorString.get(i).get(1));
                        row.createCell(2).setCellValue(cucumberErrorString.get(i).get(2));
                        row.createCell(3).setCellValue(sureErrorString.get(i).get(1));
                    } else {
                        for (ArrayList<String> cukeError : cucumberErrorString) {
                            if (sureErrorString.get(i).get(0).contains(cukeError.get(1))) {
                                sheet.createRow(i + 1);
                                row = sheet.getRow(i + 1);
                                row.createCell(0).setCellValue(cucumberErrorString.get(i).get(0));
                                row.createCell(1).setCellValue(cucumberErrorString.get(i).get(1));
                                row.createCell(2).setCellValue(cucumberErrorString.get(i).get(2));
                                row.createCell(3).setCellValue(sureErrorString.get(i).get(1));
                                break;
                            }
                        }
                    }
//                    System.out.println("22222222222222222" + sureErrorString.get(i).get(0));
//                    System.out.println("33333333333333333" + harDetailsList.get(i).get(0));
                    if (sureErrorString.get(i).get(0).toLowerCase().replaceAll("\\s+", "").contains(harDetailsList.get(i).get(0).toLowerCase())) {
                        row.createCell(4).setCellValue(harDetailsList.get(i).get(1));
                    } else {
                        for (ArrayList<String> harDetails : harDetailsList) {
                            if (sureErrorString.get(i).get(0).toLowerCase().replaceAll("\\s+", "").contains(harDetails.get(0).toLowerCase())) {
                                row.createCell(4).setCellValue(harDetailsList.get(i).get(1));
                            }
                        }
                    }
                    if (sureErrorString.get(i).get(0).toLowerCase().replaceAll("\\s+", "").contains(failureCauseList.get(i).get(0).toLowerCase())) {
                        row.createCell(5).setCellValue(failureCauseList.get(i).get(1));
                    } else {
                        for (ArrayList<String> failureDetails : failureCauseList) {
                            if (sureErrorString.get(i).get(0).toLowerCase().replaceAll("\\s+", "").contains(failureDetails.get(0).toLowerCase())) {
                                row.createCell(5).setCellValue(failureCauseList.get(i).get(1));
                            }
                        }
                    }

                    for (ArrayList<String> dom : DOMString) {
                        if (sureErrorString.get(i).get(0).toLowerCase().replaceAll("\\s+", "").contains(dom.get(0).toLowerCase().replaceAll("\\s+", ""))) {
                            row.createCell(6).setCellValue(dom.get(1));
                            break;
                        }
                    }


                }
                FileOutputStream os = new FileOutputStream(new File("target/" + errorLogsFile));
                myWorkBook.write(os);
                os.close();
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static ArrayList<ArrayList<String>> getScenarioDetailsWithErrorMessage(String plugin, List<File> xmlFiles, String node) throws Throwable {
        NodeList nodeList = null;
        ArrayList<ArrayList<String>> returnNodeList = new ArrayList<ArrayList<String>>();
        for (File xmlFile : xmlFiles) {
            String xml = new String(Files.readAllBytes(Paths.get(xmlFile.getPath())));
            Document doc = null;
            try {
                DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                InputSource src = new InputSource();
                src.setCharacterStream(new StringReader(xml));
                doc = builder.parse(src);
            } catch (SAXException e) {
                e.printStackTrace();
                return returnNodeList;
            } catch (IOException e) {
                e.printStackTrace();
                return returnNodeList;
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
                return returnNodeList;
            }

            nodeList = doc.getElementsByTagName(node);

            switch (plugin) {
                case "cucumber":
                    for (int i = 0; i < nodeList.getLength(); i++) {
                        if (!nodeList.item(i).getTextContent().trim().isEmpty()) {
                            ArrayList<String> nodeDetails = new ArrayList<String>();
                            nodeDetails.add(nodeList.item(i).getAttributes().getNamedItem("classname").getNodeValue().trim());
                            nodeDetails.add(nodeList.item(i).getAttributes().getNamedItem("name").getNodeValue().trim());
                            nodeDetails.add(nodeList.item(i).getTextContent().trim());
                            returnNodeList.add(nodeDetails);
                        }
                    }
                    break;
                case "sureFire":
                    for (int i = 0; i < nodeList.getLength(); i++) {
//Habnero 1.1.4
                        if (!nodeList.item(i).getTextContent().trim().isEmpty()) {
//Habanero 1.0.7
//                            if (!nodeList.item(i).getTextContent().trim().isEmpty()
//                                    && nodeList.item(i).getAttributes().getNamedItem("name").getNodeValue().trim().equalsIgnoreCase(nodeList.item(i).getAttributes().getNamedItem("classname").getNodeValue().trim())){
                            ArrayList<String> nodeDetails = new ArrayList<String>();
//Habanero 1.0.7
//                            nodeDetails.add(nodeList.item(i).getAttributes().getNamedItem("classname").getNodeValue().trim());
//Habnero 1.1.4
                            nodeDetails.add(nodeList.item(i).getAttributes().getNamedItem("name").getNodeValue().trim());
                            nodeDetails.add(nodeList.item(i).getTextContent().trim());
                            returnNodeList.add(nodeDetails);
                        }
                    }
                    break;
            }
        }
        return returnNodeList;
    }

    public static ArrayList<ArrayList<String>> getHarDetails(List<File> harFiles) throws Throwable {
        ArrayList<ArrayList<String>> returnString = new ArrayList<ArrayList<String>>();
        for (File harFile : harFiles) {
            ArrayList<String> result = new ArrayList<String>();
            String json = new String(Files.readAllBytes(Paths.get(harFile.getPath())));
            result.add(harFile.getName().split(".har")[0]);
            result.add(json);
            returnString.add(result);
        }
        return returnString;
    }

    public static ArrayList<ArrayList<String>> getDOM(List<File> jsonFiles, ArrayList<ArrayList<String>> sureErrorString) throws Throwable {
        ArrayList<ArrayList<String>> failedScenarioAndItsDOM = new ArrayList<ArrayList<String>>();
//        File cucumberJson = new File("target/cucumber_reports/regression_results/cucumber.json");
        String cucumberJson = FileUtils.readFileToString(new File("target/cucumber_reports/regression_results/cucumber.json"), "UTF-8");
        JSONParser jp = new JSONParser();
        JSONArray parsedTargetJSON = (JSONArray) jp.parse(cucumberJson);
        JSONObject file = (JSONObject) parsedTargetJSON.get(0);
        JSONArray elements = (JSONArray) file.get("elements");
        for (ArrayList<String> errors : sureErrorString) {

            for (int i = 0; i < elements.size(); i++) {
                JSONObject scenario = (JSONObject) elements.get(i);
                String scenarioName = scenario.get("name").toString();
                if (errors.get(0).equalsIgnoreCase(scenarioName)) {
                    ArrayList<String> scenarioAndDOM = new ArrayList<>();
                    JSONArray after = (JSONArray) scenario.get("after");
                    JSONObject afterElement = (JSONObject) after.get(0);
                    JSONArray output = (JSONArray) afterElement.get("output");
                    scenarioAndDOM.add(scenarioName);
                    scenarioAndDOM.add((String) output.get(1));
                    failedScenarioAndItsDOM.add(scenarioAndDOM);
                }
            }
        }

        return failedScenarioAndItsDOM;

    }

    public static ArrayList<ArrayList<String>> getErrorAnalysis(List<File> harFiles) throws Throwable {
        ArrayList<ArrayList<String>> returnString = new ArrayList<ArrayList<String>>();
        for (File harFile : harFiles) {
            ArrayList<String> result = new ArrayList<String>();
            HarReader harReader = new HarReader();
            Har har = harReader.readFromFile(harFile);
            List<HarEntry> hEntry = har.getLog().getEntries();
            int hEntrySize = hEntry.size();
//            System.out.println("222222222222222222" + hEntry);
            if (hEntry.get(hEntrySize - 1).getResponse().getStatus() == 0 && hEntry.get(hEntrySize - 1).getRequest().getMethod().toString().equalsIgnoreCase("CONNECT")) {
                result.add(harFile.getName().split(".har")[0]);
                result.add("Network Connectivity issue: Connect method - Response code 0");
            } else {
                try (FileWriter fw = new FileWriter("target/response.txt", true);
                     BufferedWriter bw = new BufferedWriter(fw);
                     PrintWriter out = new PrintWriter(bw)) {
                    out.println(" ");
                    for (HarEntry entry : hEntry) {
                        if (entry.getResponse().getStatus() != 200) {
                            out.println("Response code " + entry.getResponse().getStatus() + "  : " + entry.getRequest().getUrl() + " ; ");
                        }
                    }
                } catch (Exception e) {
                }
                String content = new String(Files.readAllBytes(Paths.get("target/response.txt")));
                File myfile = new File("target/response.txt");
                if (myfile.exists())
                    myfile.delete();
                result.add(harFile.getName().split(".har")[0]);
                result.add(content);
            }
            returnString.add(result);
        }
        return returnString;
    }

    public static void updateCucumberReport(ArrayList<ArrayList<String>> failureList) {

        File jsonFile = new File("target/cucumber_reports/regression_results/cucumber.json");
        try {
//            File jsonFile = new File("D:\\lambda\\Polo\\src\\test\\resources\\cucumber.json");
            String cucumberJson = FileUtils.readFileToString(jsonFile, "UFT-8");
            JSONParser jp = new JSONParser();
            JSONArray parsedTargetJSON = (JSONArray) jp.parse(cucumberJson);
            JSONObject file = (JSONObject) parsedTargetJSON.get(0);
            JSONArray elements = (JSONArray) file.get("elements");
            for (ArrayList<String> errors : failureList) {

                for (int i = 0; i < elements.size(); i++) {
                    JSONObject scenario = (JSONObject) elements.get(i);
                    String scenarioName = scenario.get("name").toString().replace(" ", "");
                    if (errors.get(0).equalsIgnoreCase(scenarioName)) {
                        //System.out.println("==================================================" + scenarioName);
                        JSONArray steps = (JSONArray) scenario.get("steps");
                        JSONObject step = (JSONObject) steps.get(steps.size() - 1);
                        for (int j = 0; j < steps.size(); j++) {
                            step = (JSONObject) steps.get(j);
                            JSONObject result = (JSONObject) step.get("result");
                            String status = (String) result.get("status");
                            if (status.equals("failed")) {
                                step = (JSONObject) steps.get(j);
                                break;
                            }
                        }

                        JSONObject result = (JSONObject) step.get("result");
                        String errorMessage = result.get("error_message").toString();
                        result.put("error_message", errorMessage + "\n" + "possible cause of failure is " + errors.get(1));
                        errorMessage = result.get("error_message").toString();
                        break;
                    }
                }
            }
            //JSONArray element = (JSONArray) parsedTargetJSON.get("elements");

            FileWriter fileToWrite = new FileWriter("target/cucumber_reports/regression_results/cucumber.json");

            fileToWrite.write(parsedTargetJSON.toJSONString());
            fileToWrite.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static void printArrayLists(ArrayList<ArrayList<String>> listToPrint) {
        for (ArrayList<String> list : listToPrint) {
            for (String string : list) {
                //System.out.println("-------------------------------" + string);
            }
        }
    }

}

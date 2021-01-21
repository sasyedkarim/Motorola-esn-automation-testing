package co.uk.motorola.esn.context;

import co.uk.motorola.esn.steps.WebDriverDiscovery;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.assertj.core.api.SoftAssertions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.security.SecureRandom;
import java.util.*;

public class ScenarioContext {

    private final WebDriverDiscovery webDriverDiscovery = new WebDriverDiscovery();

    private Logger log = LoggerFactory.getLogger(getClass());
    private String dataBackBoneXmlName = "";
    private Map<String, String> xmlResponseMap = new LinkedHashMap<>();
    private Map<String, String> propertyTransfer = new LinkedHashMap<>();
    private Map<String, String> outputMap = new LinkedHashMap<>();
    private List<Map<String, Map<String, String>>> expectedValues = new ArrayList<>();
    private Properties defaultValues;
    private int pdfFileCount = 1;
    private int emailFileCount = 1;
    private int xmlFileCount = 1;
    private int iqhwdCount = 1;
    private int randomAddressNo;
    private int printRules = 0;
    private int textBlocks = 0;
    private int totalPagesInDocument = 0;
    private String documentErrors = "";
    private String registerUser = "";
    private String corporateName = "";
    private int executionCount = 0;
    private int count = 0;
    private int scCount = 0;
    private int rowIndex = 0;
    private int numberOfDriversToBeAdded = new SecureRandom().nextInt(1) + 1;
    private SoftAssertions softAssertions = new SoftAssertions();
    private Map<String, String> inputDataMap = new LinkedHashMap<>();
    private RemoteWebDriver driver = null;
    private Map<String, String> workingData = new LinkedHashMap<>();
    private String rule;
    private List<String> elements;
    private String url;
    String driverType = "driverType";


    public RemoteWebDriver getDriver() {
        if (driver == null) {
            WebDriverManager.chromedriver().setup();
            driver = webDriverDiscovery.makeDriver();
            getLOG().info("Browser Version: {}", getDriver().getCapabilities().getVersion());
            if (!(System.getProperty(driverType).equalsIgnoreCase("appium") || System.getProperty(driverType).equalsIgnoreCase("chrome-docker") || System.getProperty(driverType).equalsIgnoreCase("docker-grid"))) {
                driver.manage().window().maximize();
            }
        }
        return driver;
    }

    public void setDriver(RemoteWebDriver driver) {
        this.driver = driver;
    }

    public void close() {
        if (driver != null) {
            driver.quit();
        }
    }

    public Logger getLOG() {
        return log;
    }

    public void setLOG(Logger log) {
        this.log = log;
    }

    public String getDataBackBoneXmlName() {
        return dataBackBoneXmlName;
    }

    public void setDataBackBoneXmlName(String dataBackBoneXmlName) {
        this.dataBackBoneXmlName = dataBackBoneXmlName;
    }

    public Map<String, String> getXmlResponseMap() {
        return xmlResponseMap;
    }

    public void setXmlResponseMap(Map<String, String> xmlResponseMap) {
        this.xmlResponseMap = xmlResponseMap;
    }

    public Map<String, String> getPropertyTransfer() {
        return propertyTransfer;
    }

    public void setPropertyTransfer(Map<String, String> propertyTransfer) {
        this.propertyTransfer = propertyTransfer;
    }

    public Map<String, String> getOutputMap() {
        return outputMap;
    }

    public void setOutputMap(Map<String, String> outputMap) {
        this.outputMap = outputMap;
    }

    public List<Map<String, Map<String, String>>> getExpectedValues() {
        return expectedValues;
    }

    public void setExpectedValues(List<Map<String, Map<String, String>>> expectedValues) {
        this.expectedValues = expectedValues;
    }

    public Properties getDefaultValues() {
        return defaultValues;
    }

    public void setDefaultValues(Properties defaultValues) {
        this.defaultValues = defaultValues;
    }

    public int getPdfFileCount() {
        return pdfFileCount;
    }

    public void setPdfFileCount(int pdfFileCount) {
        this.pdfFileCount = pdfFileCount;
    }

    public int getEmailFileCount() {
        return emailFileCount;
    }

    public void setEmailFileCount(int emailFileCount) {
        this.emailFileCount = emailFileCount;
    }

    public int getXmlFileCount() {
        return xmlFileCount;
    }

    public void setXmlFileCount(int xmlFileCount) {
        this.xmlFileCount = xmlFileCount;
    }

    public int getIqhwdCount() {
        return iqhwdCount;
    }

    public void setIqhwdcount(int iqhwdCount) {
        this.iqhwdCount = iqhwdCount;
    }

    public int getRandomAddressNo() {
        return randomAddressNo;
    }

    public void setRandomAddressNo(int randomAddressNo) {
        this.randomAddressNo = randomAddressNo;
    }

    public int getPrintRules() {
        return printRules;
    }

    public void setPrintRules(int printRules) {
        this.printRules = printRules;
    }

    public int getTextBlocks() {
        return textBlocks;
    }

    public void setTextBlocks(int textBlocks) {
        this.textBlocks = textBlocks;
    }

    public int getTotalPagesInDocument() {
        return totalPagesInDocument;
    }

    public void setTotalPagesInDocument(int totalPagesInDocument) {
        this.totalPagesInDocument = totalPagesInDocument;
    }

    public String getDocumentErrors() {
        return documentErrors;
    }

    public void setDocumentErrors(String documentErrors) {
        this.documentErrors = documentErrors;
    }

    public String getRegisterUser() {
        return registerUser;
    }

    public void setRegisterUser(String registerUser) {
        this.registerUser = registerUser;
    }

    public String getCorporateName() {
        return corporateName;
    }

    public void setCorporateName(String corporateName) {
        this.corporateName = corporateName;
    }

    public int getExecutionCount() {
        return executionCount;
    }

    public void setExecutionCount(int executionCount) {
        this.executionCount = executionCount;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getScCount() {
        return scCount;
    }

    public void setScCount(int scCount) {
        this.scCount = scCount;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }

    public int getNumberOfDriversToBeAdded() {
        return numberOfDriversToBeAdded;
    }

    public void setNumberOfDriversToBeAdded(int numberOfDriversToBeAdded) {
        this.numberOfDriversToBeAdded = numberOfDriversToBeAdded;
    }

    public SoftAssertions getSoftAssertions() {
        return softAssertions;
    }

    public void setSoftAssertions(SoftAssertions softAssertions) {
        this.softAssertions = softAssertions;
    }

    public Map<String, String> getInputDataMap() {
        return inputDataMap;
    }

    public void setInputDataMap(Map<String, String> inputDataMap) {
        this.inputDataMap = inputDataMap;
    }

    public Map<String, String> getWorkingData() {
        return workingData;
    }

    public void setWorkingData(Map<String, String> workingData) {
        this.workingData = workingData;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public List<String> getElements() {
        return elements;
    }

    public void setElements(List<String> elements) {
        this.elements = elements;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

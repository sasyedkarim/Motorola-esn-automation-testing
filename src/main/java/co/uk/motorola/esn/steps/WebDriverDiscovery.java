package co.uk.motorola.esn.steps;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class WebDriverDiscovery {

    protected static final Logger log = LoggerFactory.getLogger(WebDriverDiscovery.class);
    private static final Map<String, String> mobileEmulation = new HashMap<>();

    public RemoteWebDriver makeDriver() {
        switch (System.getProperty("driverType")) {
            case "firefox":
                return new FirefoxDriver();
            case "safari":
                return new SafariDriver();
            case "ie":
                InternetExplorerOptions options = new InternetExplorerOptions();
                options.introduceFlakinessByIgnoringSecurityDomains();
                return new InternetExplorerDriver(options);
            case "chrome":
                return new ChromeDriver();
            case "mobile":
                ChromeOptions deviceName = new ChromeOptions();
                String[] device = getDeviceName().split(",");
                mobileEmulation.put("deviceName", mobileEmulation(device));
                deviceName.setExperimentalOption("mobileEmulation", mobileEmulation);
                return new ChromeDriver(deviceName);
            case "chrome-docker":
                ChromeOptions chromeOption = new ChromeOptions();
                String[] chromeOptions = System.getProperty("chromeOptions").split(",");
                for (String option : chromeOptions) {
                    chromeOption.addArguments(option);
                }
                return new ChromeDriver(chromeOption);
            case "appium":
                try {
                    String url = "http://127.0.0.1:4723/wd/hub";
                    DesiredCapabilities appiumCapabilities = new DesiredCapabilities();
                    String[] appiumCaps = System.getProperty("capabilities").split(",");
                    for (String cap : appiumCaps) {
                        appiumCapabilities.setCapability(cap.split(":")[0], cap.split(":")[1]);
                    }
                    if (appiumCapabilities.asMap().containsKey("browserName")) {
                        return new RemoteWebDriver(new URL(url), appiumCapabilities);
                    } else if (appiumCapabilities.getCapability("platformName").toString().equalsIgnoreCase("android")) {
                        return new AndroidDriver(new URL(url), appiumCapabilities);
                    } else {
                        return new IOSDriver(new URL(url), appiumCapabilities);
                    }
                } catch (MalformedURLException e) {
                    log.info("MalformedURLException {}", e.getMessage());
                    return null;
                }
            case "docker-grid":
                try {
                    DesiredCapabilities dockerGridCapabilities = new DesiredCapabilities();
                    String driverURL = System.getProperty("driverURL");
                    String[] caps = System.getProperty("capabilities").split(",");
                    for (String cap : caps) {
                        dockerGridCapabilities.setCapability(cap.split(":")[0], cap.split(":")[1]);
                    }
                    return new RemoteWebDriver(new URL(driverURL), dockerGridCapabilities);
                } catch (MalformedURLException e) {
                    log.info("MalformedURLException {}", e.getMessage());
                    return null;
                }
            default:
                ArrayList<String> cliArgsCap = new ArrayList<>();
                cliArgsCap.add("--webdriver-loglevel=NONE");
                cliArgsCap.add("--web-security=false");
                cliArgsCap.add("--ssl-protocol=any");
                cliArgsCap.add("--ignore-ssl-errors=true");
                return new PhantomJSDriver();
        }
    }

    public static String getPlatform() {
        return System.getProperty("platform");
    }

    public static String getBrowserVersion() {
        return System.getProperty("browserVersion");
    }

    public static String getBrowserName() {
        return System.getProperty("browserName");
    }

    public static String getAppiumVersion() {
        return System.getProperty("appiumVersion");
    }

    public static String getDeviceName() {
        return System.getProperty("deviceName");
    }

    public static String getDeviceOrientation() {
        return System.getProperty("deviceOrientation");
    }

    public static String getPlatformVersion() {
        return System.getProperty("platformVersion");
    }

    private static String mobileEmulation(String[] deviceOptions) {
        String deviceModel;
        if (deviceOptions.length > 1) {
            deviceModel = deviceOptions[0] + " " + deviceOptions[1];
        } else {
            deviceModel = deviceOptions[0];
        }
        return deviceModel;
    }

}
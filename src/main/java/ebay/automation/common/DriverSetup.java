package ebay.automation.common;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class DriverSetup {

    public AppiumDriver<MobileElement> driver;
    public static HashMap<Thread, AppiumDriver<MobileElement>> drivers = new HashMap<Thread, AppiumDriver<MobileElement>>();

    private ITestContext ctx;
    public String deviceName;
    public String platformName;
    public String platformVersion;
    public String deviceType;

    public static Logger logger = Logger.getLogger(DriverSetup.class.getName());
    public static String methodname;

    public static Set<ITestResult> testResultsSet = new HashSet<ITestResult>();

    /**
     * Logger Initialisation to be used for whole execution.
     *
     * @throws Exception
     */
    @BeforeSuite(alwaysRun = true)
    public void initialise() throws Exception {
        DOMConfigurator.configureAndWatch("./src/test/resources/testconfig/log4j.xml");
    }

    /**
     * Launch Application based on the details present in Environments.properties
     *
     * @param devicename
     * @param platformname
     * @param platformversion
     * @param ctx
     * @throws Exception
     */
    @BeforeTest()
    @Parameters({"devicename", "platformname", "platformversion"})
    public void oneTimeSetUp(String devicename, String platformname, String platformversion, ITestContext ctx) throws Exception {

        this.ctx = ctx;
        deviceName = devicename;
        platformName = platformname;
        platformVersion = platformversion;

        launchAppSession(ctx, platformname, platformversion);
    }

    /**
     * App Clean-up after completing the execution.
     *
     * @throws Exception
     */
    @AfterTest(alwaysRun = true)
    public void oneTimeTearDown() throws Exception {
        AndroidDriver<MobileElement> androidDriver = getcurrentAndroidThreadDriver();
        androidDriver.removeApp(getSystemParameter("APP_PACKAGE"));
        logMessage("Quitting Android Driver instance: " + androidDriver);
        androidDriver.quit();
    }

    /**
     * Gets current Thread driver to assign to the test while running in parllel.
     *
     * @return
     * @throws Exception
     */
    public static AndroidDriver<MobileElement> getcurrentAndroidThreadDriver() throws Exception {
        logMessage("Current driver value: " + drivers.get(Thread.currentThread()));
        return (AndroidDriver<MobileElement>) drivers.get(Thread.currentThread());
    }

    /**
     * To log message with Date and time
     *
     * @param message
     */
    public static void logMessage(String message) {
        Reporter.log("[" + new SimpleDateFormat("HH:mm:ss:SSS").format((new Date())) + "] [Thread ID : " + Thread.currentThread().getId() + "] " + message, true);
    }

    /**
     * Launch App session
     *
     * @param ctx
     * @param platformname
     * @param platformversion
     * @throws Exception
     */
    public void launchAppSession(ITestContext ctx, String platformname, String platformversion) throws Exception {
        driver = launchAndroidApp(ctx);
        /** Assigning drivers to Thread to support parallel run execution**/
        drivers.put(Thread.currentThread(), driver);
        logMessage("CurrentThread:" + Thread.currentThread() + "AppiumDriver<MobileElement> :" + driver + " created for " + platformname + " " + platformversion);
    }

    /*
     * Gets the Parameter from Environment.properties file.
     */
    public static String getSystemParameter(String parameter) {
        String value = System.getenv(parameter);
        if (value == null || value.trim().length() == 0) {
            value = ConfigReader.config.getProperty(parameter);
        }
        DriverSetup.logMessage("Using " + parameter + " " + value);
        return value;
    }

    /**
     * Gets the Parameter from Environment.properties file.
     *
     * @param ctx
     * @return
     * @throws Exception
     */
    public static AndroidDriver<MobileElement> launchAndroidApp(ITestContext ctx) throws Exception {
        AndroidDriver<MobileElement> driver;
        String deviceName = ctx.getCurrentXmlTest().getParameter("devicename");
        String platformName = ctx.getCurrentXmlTest().getParameter("platformname");
        String platformVersion = ctx.getCurrentXmlTest().getParameter("platformversion");
        String testApp = getSystemParameter("ANDROID_TEST_APP");
        String appPackage = getSystemParameter("APP_PACKAGE");
        String appActivity = getSystemParameter("APP_ACTIVITY");
        String appWaitActivity = getSystemParameter("APP_WAIT_ACTIVITY");

        DriverSetup.logMessage("Trying to instantiate driver with capabilities: devicename: " + deviceName + " platformname: " + platformName + " platformversion: " + platformVersion);
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("deviceName", deviceName);
        capabilities.setCapability("platformName", platformName);
        capabilities.setCapability("platformVersion", platformVersion);

        capabilities.setCapability("app", testApp);
        capabilities.setCapability("appPackage", appPackage);
        capabilities.setCapability("appActivity", appActivity);
        capabilities.setCapability("appWaitActivity", appWaitActivity);
        capabilities.setCapability(AndroidMobileCapabilityType.AUTO_GRANT_PERMISSIONS, true);

        capabilities.setCapability("fullReset", "true");

        if (!platformVersion.startsWith("4.") && !platformVersion.startsWith("5.") && !platformVersion.startsWith("6.")) {
            capabilities.setCapability("automationName", "uiautomator2");
        }

        DriverSetup.logMessage("Initiating Driver session on " +
                "http://localhost:4723/wd/hub with following capabilities: " + capabilities);

        driver = new AndroidDriver<MobileElement>(new URL("http://localhost:4723/wd/hub"), capabilities);
        driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
        driver.rotate(ScreenOrientation.PORTRAIT);
        return driver;
    }
}
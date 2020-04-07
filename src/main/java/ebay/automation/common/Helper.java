package ebay.automation.common;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Helper {

    /**
     * fetchElement helps to find the UIElements element based on the locator passed in as argument
     * method overloaded with no implicit wait time - uses default wait time of 30 seconds
     *
     * @param driver Test Driver session
     * @param object Locator name as in UIElement Properties file
     * @return Returns UIElements element
     * @throws Exception
     */
    public static MobileElement fetchElement(AppiumDriver<MobileElement> driver, String object) throws Exception {
        try {
            WebDriverWait wait = new WebDriverWait(driver, 30, 100);
            wait.ignoring(org.openqa.selenium.NoSuchElementException.class);
            wait.until(ExpectedConditions.presenceOfElementLocated(GetUIElements.getProperties(object)));

            MobileElement element = driver.findElement(GetUIElements.getProperties(object));
            return element;
        } catch (Exception e) {
            DriverSetup.logMessage("MobileElement " + object + " not found");
            throw new Exception("Unable to locate element: " + object + e);
        }
    }

    /**
     * fetchElements helps to find the UIElements elements based on the locator passed in as argument
     *
     * @param driver Test Driver Session
     * @param object Locator name as in UIElement Properties file
     * @return Returns list of UIElements elements
     * @throws Exception
     */
    public static List<MobileElement> fetchElements(AppiumDriver<MobileElement> driver, String object)
            throws Exception {
        try {
            WebDriverWait wait = new WebDriverWait(driver, 30, 100);
            wait.ignoring(org.openqa.selenium.NoSuchElementException.class);
            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(GetUIElements.getProperties(object)));

            List<MobileElement> elements = driver.findElements(GetUIElements.getProperties(object));
            return elements;
        } catch (Exception e) {
            DriverSetup.logMessage("MobileElement List " + object + " not found");
            throw new Exception("Unable to locate elements: " + object + e);
        }
    }

    /**
     * waitTillVisible waits for the element to be visible until the time - used by wrapper method. ** Not to be used by tests**
     *
     * @param driver    Test Driver Session
     * @param thisBy    Selenium By Class which is returned by GetUIElements.getProperties
     * @param timevalue Time to wait for the element to be visible
     * @return Returns boolean true if element is visible and returns false if element is not being visible within the time passed.
     */
    public static Boolean waitTillVisible(AppiumDriver<MobileElement> driver, By thisBy, long timevalue) {
        try {
            new WebDriverWait(driver, timevalue, 100).until(ExpectedConditions.visibilityOfElementLocated(thisBy));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * waitTillInvisible waits for the element to be invisible within the time - used by wrapper method. ** Not to be used by tests**
     *
     * @param driver    Test Driver Session
     * @param thisBy    Selenium By Class which is returned by GetUIElements.getProperties
     * @param timevalue Time to wait for the element to be invisible
     * @return Returns boolean true if element becomes invisible and returns false if element is still visible.
     */
    public static Boolean waitTillInvisible(AppiumDriver<MobileElement> driver, By thisBy, long timevalue) {
        try {
            new WebDriverWait(driver, timevalue, 100).until(ExpectedConditions.invisibilityOfElementLocated(thisBy));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * assertElementExists asserts whether element exists. Assert fails if the element doesnt exist.
     *
     * @param driver    Test Driver Session
     * @param locator   Locator name as in UIElement Properties file
     * @param timevalue Time to wait for the element to be located
     * @param failMsg   Failure message - to throw if the element is not present
     * @throws Exception
     */
    public static void assertElementExists(AppiumDriver<MobileElement> driver, String locator, long timevalue, String failMsg) throws Exception {
        boolean elementExists = doesElementExist(driver, locator, timevalue);
        if (elementExists) {
            DriverSetup.logMessage("Assert element: " + locator + " exists");
        } else {
            Assert.fail(failMsg);
        }
    }

    /**
     * doesWebElementExist checks whether the element is present or not
     * It tries to find the element based on the locator and value passed in as argument
     *
     * @param driver   Test Driver Session
     * @param selector Locator name as in UIElement Properties file
     * @param time     Time to wait for the element to be located
     * @return Returns true if element is present and false if element is not present
     */
    public static boolean doesElementExist(AppiumDriver<MobileElement> driver, String selector, long time) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, time, 100);
            wait.ignoring(org.openqa.selenium.NoSuchElementException.class);
            By locator;
            locator = GetUIElements.getProperties(selector);
            wait.until(ExpectedConditions.presenceOfElementLocated(locator));
            return true;
        } catch (Exception e) {
            DriverSetup.logMessage("MobileElement " + selector + " doesn't exist");
            return false;
        }
    }

    /**
     * Hides the keyboard if present
     *
     * @param driver Test Driver Session
     * @throws Exception
     */
    public static void hideKeyboard(AppiumDriver<MobileElement> driver) throws Exception {
        try {
            driver.hideKeyboard();
        } catch (Exception e) {
            DriverSetup.logMessage("Soft keyboard not appearing to hide!");
        }
    }

    /**
     * Takes Screen Shot of the current screen and copies the file to output folder
     *
     * @param flag      Flag value - true or false taken from env properties file or CI parameter
     * @param driver    Test Driver Session
     * @param foldename Output folder name - where file to be copied
     * @throws Exception
     */
    public static void takeScreenshot(boolean flag, AppiumDriver driver, String foldename) throws Exception {
        if (flag == true) {
            try {
                File Folderpath = new File("file-screenshots" + File.separator + foldename);
                Folderpath.mkdirs();
                String fileName = DriverSetup.methodname + "-" + Helper.getDate() + ".png";
                //    WebDriver driverInstance = new Augmenter().augment(driver);
                File srcfile = driver.getScreenshotAs(OutputType.FILE);
                FileUtils.copyFile(srcfile, new File(Folderpath + File.separator + fileName));
            } catch (Exception e) {
                DriverSetup.logMessage(e.getMessage());
            }
        }
    }

    /**
     * Get DateTime in a format to use for filenames like screenshots
     *
     * @return
     */
    public static String getDate() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd_hhmmss");
        return df.format(new Date());
    }

    /**
     * Assertions with Logs enabled and report failure.
     *
     * @param actvalue
     * @param expvalue
     */
    public static void assertText(String actvalue, String expvalue) {
        if (actvalue.equalsIgnoreCase(expvalue)) {
            DriverSetup.logMessage("Assert Text passed! Expected value is: " + expvalue);
        } else if (actvalue.contains(expvalue)) {
            DriverSetup.logMessage("Assert Text passed! Expected value is: " + expvalue);
        } else {
            Assert.fail("Actual value not equals expected value. Expected: " + expvalue + ", Actual: " + actvalue);
        }
    }
}

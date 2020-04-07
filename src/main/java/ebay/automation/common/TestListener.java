package ebay.automation.common;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.UnreachableBrowserException;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.TestListenerAdapter;

import java.io.File;
import java.io.IOException;

public class TestListener extends TestListenerAdapter {

    /**
     * TO Capture ScreenShot
     *
     * @param result
     */
    public void captureScreenShot(ITestResult result) {
        try {
            if (result != null) {
                DriverSetup ti = (DriverSetup) result.getInstance();
                if (ti != null) {
                    String myDevice = ti.deviceName;
                    String myPlatform = ti.platformName;
                    String myVersion = ti.platformVersion;
                    String myTestNum = result.getName();
                    String fileName = (myPlatform + "_" + myDevice + "_" + myVersion + "_" + myTestNum + "_" + Helper.getDate()) + ".png";
                    fileName = fileName.replaceAll("[^a-zA-Z0-9\\\\s\\\\.]", "");
                    result.setAttribute("screenshotLoc", fileName);

                    WebDriver driverInstance = new Augmenter().augment(ti.driver);
                    if (driverInstance != null) {
                        File srcfile = null;
                        try {
                            srcfile = ((TakesScreenshot) driverInstance).getScreenshotAs(OutputType.FILE);
                            DriverSetup.logger.info("Taking screenshot for the failure");
                        } catch (UnreachableBrowserException ex) {
                            ex.printStackTrace();
                        }

                        try {
                            if (srcfile != null) {
                                FileUtils.copyFile(srcfile, new File("file-screenshots" + File.separator + fileName));
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Reporter.setCurrentTestResult(result);
                        if (result.getThrowable() != null) {
                            Reporter.log(result.getThrowable().getMessage());
                        }
                        Reporter.log("<a href=" + fileName + ">Click to open failure screenshot</a>");
                    } else {
                        DriverSetup.logMessage("Driver not exist. Screenshot cannot be taken !!");
                    }
                } else {
                    DriverSetup.logMessage("Driver not exist. Screenshot cannot be taken !!");
                }
            } else {
                DriverSetup.logMessage("Driver not exist. Screenshot cannot be taken !!");
            }
        } catch (Exception e) {
            DriverSetup.logMessage("Unable to capture screenshot due to exception: " + e);
        }
    }

    /**
     * Capture screenshot on a test failure.
     *
     * @param result
     */
    @Override
    public void onTestFailure(ITestResult result) {
        captureScreenShot(result);
    }

    /**
     * Capture screenshot for a failure during app configuration.
     *
     * @param result
     */
    @Override
    public void onConfigurationFailure(ITestResult result) {
        System.out.println("In Configuration failure");
        captureScreenShot(result);
    }

    /**
     * @param result
     */
    @Override
    public void onConfigurationSkip(ITestResult result) {
        System.out.println("In Configuration failure");
        captureScreenShot(result);
    }
}

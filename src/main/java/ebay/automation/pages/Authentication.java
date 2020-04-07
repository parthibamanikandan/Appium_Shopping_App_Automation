package ebay.automation.pages;

import ebay.automation.common.AndroidGestures;
import ebay.automation.common.DriverSetup;
import ebay.automation.common.Helper;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;

public class Authentication {

    private AndroidDriver driver;

    /**
     * Preset driver to the current instance.
     *
     * @param driver
     */
    public Authentication(AndroidDriver<MobileElement> driver) {
        this.driver = driver;
    }

    /**
     * Sign in to EBay Application with username and password.
     *
     * @param userName
     * @param password
     * @throws Exception
     */
    public void signIn(String userName, String password) throws Exception {
        DriverSetup.logMessage("Signing in with: " + userName + " " + password);
        Helper.waitTillInvisible(driver, By.id("translucent_progress_bar"), 60);
        AndroidGestures.tap(driver, "signIn");
        AndroidGestures.setEditValue(driver, "username", userName);
        AndroidGestures.setEditValue(driver, "password", password);
        Helper.hideKeyboard(driver);
        AndroidGestures.tap(driver, "signIn");
        AndroidGestures.tap(driver, "mayBeLater");
        Helper.waitTillInvisible(driver, By.id("translucent_progress_bar"), 60);
        DriverSetup.logMessage("Signed in successfully");
    }

    /**
     * Sign out from the EBay App.
     *
     * @throws Exception
     */
    public void signOut() throws Exception {
        AndroidGestures.tap(driver, "menu");
        AndroidGestures.tap(driver, "userinfo");
        AndroidGestures.tap(driver, "signout");
        AndroidGestures.tap(driver, "SignoutConfirmation");
    }
}



package ebay.automation.pages;

import ebay.automation.common.AndroidGestures;
import ebay.automation.common.Constants;
import ebay.automation.common.DriverSetup;
import ebay.automation.common.Helper;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class SearchProduct {

    private AndroidDriver driver;

    /**
     * Preset driver to the current instance.
     *
     * @param driver
     */
    public SearchProduct(AndroidDriver<MobileElement> driver) {
        this.driver = driver;
    }

    /**
     * Search for an product and wait for all progress bars to finish.
     *
     * @param productName
     * @throws Exception
     */
    public void searchSelectProduct(String productName) throws Exception {
        AndroidGestures.tap(driver, "searchButton");
        AndroidGestures.setEditValue(driver, "searchBox", productName);
        AndroidGestures.EnterEvent(driver);
        Helper.waitTillInvisible(driver, By.id("translucent_progress_bar"), 60);
        MobileElement productItem = getRandomElement();
        AndroidGestures.tap(driver, productItem);
        Helper.waitTillInvisible(driver, By.id("translucent_progress_bar"), 60);
    }

    /**
     * Add Product to cart and CheckOut.
     *
     * @throws Exception
     */
    public void addCheckOutProduct() throws Exception {
        AndroidGestures.scrollFindToTap(driver, "addToCart", "");
        AndroidGestures.tap(driver, "viewCart");
        AndroidGestures.tap(driver, "checkoutButton");
        Helper.waitTillInvisible(driver, By.id("translucent_progress_bar"), 60);
    }

    /**
     * Remove Item from the Cart after the validation.
     *
     * @throws Exception
     */
    public void removeItem() throws Exception {
        AndroidGestures.tap(driver, "closeButton");
        AndroidGestures.tap(driver, "reomoveButton");
        AndroidGestures.tap(driver, "removeconfirmation");
        Helper.waitTillInvisible(driver, By.id("translucent_progress_bar"), 60);
        AndroidGestures.tap(driver, "closeButton");
    }

    /**
     * Generate Random Element from list of Elements present.
     *
     * @return
     * @throws Exception
     */
    public MobileElement getRandomElement() throws Exception {
        AndroidGestures.windowSwipe(driver, Constants.swipeDirection.UP);
        AndroidGestures.windowSwipe(driver, Constants.swipeDirection.UP);
        List<MobileElement> results = Helper.fetchElements(driver, "searchResults");
        int size = results.size();
        DriverSetup.logMessage("Results size: " + size);
        MobileElement randomElement = results.get(ThreadLocalRandom.current().nextInt(0, size));
        return randomElement;
    }
}



package ebay.automation;

import ebay.automation.common.DriverSetup;
import ebay.automation.common.Helper;
import ebay.automation.pages.Authentication;
import ebay.automation.pages.SearchProduct;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import org.testng.annotations.*;

public class Ebay extends DriverSetup {
    AndroidDriver<MobileElement> testdriver;
    Authentication authentication;
    SearchProduct searchProduct;

    /**
     * Set current instance Driver to the test.
     *
     * @param uname
     * @param password
     * @throws Exception
     */
    @BeforeClass
    @Parameters({"uname", "password"})
    public void setTestdriver(String uname, String password) throws Exception {
        this.testdriver = getcurrentAndroidThreadDriver();
        authentication = new Authentication(testdriver);
        searchProduct = new SearchProduct(testdriver);
        logMessage("Sign in to the App");
        authentication.signIn(uname, password);
    }

    /**
     * Search and Add the products with possible assertions.
     *
     * @param product
     * @throws Exception
     */
    @Test()
    @Parameters({"product"})
    public void SearchAddProduct(String product) throws Exception {
        logMessage("Search for the product");
        searchProduct.searchSelectProduct(product);
        String itemName = Helper.fetchElement(driver, "itemName").getText();
        String itemPrice = Helper.fetchElement(driver, "itemPrice").getText().substring(3).trim();
        searchProduct.addCheckOutProduct();
        String checkoutTitle = Helper.fetchElement(driver, "checkoutTitle").getText();
        String checkoutPrice = Helper.fetchElement(driver, "checkoutPrice").getText().substring(3).trim();
        logMessage("Checkout actual price :" + checkoutPrice);
        logMessage("Expected price from search :" + itemPrice);
        Helper.assertText(checkoutTitle, itemName);
        Helper.assertText(checkoutPrice, itemPrice);
    }

    /**
     * After the validations remove the item from the cart.
     *
     * @throws Exception
     */
    @AfterMethod
    public void removeItems() throws Exception {
        logMessage("Remove Item from the Cart");
        searchProduct.removeItem();
        logMessage("Removed Item from the Cart");
    }

    /**
     * SignOut from the App before APP cleanup.
     *
     * @throws Exception
     */
    @AfterClass
    public void tearDown() throws Exception {
        logMessage("Sign out from App");
        authentication.signOut();
        logMessage("Signed out from App");
    }
}


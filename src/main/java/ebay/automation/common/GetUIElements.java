package ebay.automation.common;

import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;

import java.lang.reflect.Method;

public class GetUIElements {

    /*
     * getProperties method implements reflection concept --> to identify
     * element @ Runtime.
     */
    public static By getProperties(String property) throws Exception {
        try {
            String element = ConfigReader.config.getProperty(property);
            if (element != null) {
                String[] params = ConfigReader.config.getProperty(property).split(",");
                String newIdentifier = params[0] + ",";
                String newValue = element.replace(newIdentifier, "");
                if (params[0] != null || newValue != null) {
                    if (params[0].equalsIgnoreCase("accessibilityid")) {
                        Class<MobileBy> cl = MobileBy.class;
                        Method m1 = cl.getMethod(params[0], String.class);
                        MobileBy.ByAccessibilityId s1 = (MobileBy.ByAccessibilityId) m1.invoke(null, newValue);
                        return s1;
                    } else {
                        Class<By> cl = By.class;
                        Method m1 = cl.getMethod(params[0], String.class);

                        By s1 = (By) m1.invoke(null, newValue);
                        return s1;
                    }
                } else {
                    DriverSetup.logMessage("MobileElement: " + property + " format is not proper in the property file");
                    throw new Exception("MobileElement: " + property + " format is not proper in the property file");
                }
            } else {
                DriverSetup.logMessage("MobileElement: " + property + " not found in the property file");
                throw new Exception("MobileElement: " + property + " not found in the property file");
            }
        } catch (Exception e) {
            throw e;
        }
    }

    /*
     * Below getProperties takes extra value parameter to replace the $value (in
     * the properties file) with actual value passed.
     */
    public static By getProperties(String property, String value) throws Exception {
        try {
            String element = ConfigReader.config.getProperty(property);
            if (element != null) {
                String[] params = ConfigReader.config.getProperty(property).split(",");
                String newIdentifier = params[0] + ",";
                String newValue = element.replace(newIdentifier, "");

                if (newValue.contains("$value")) {
                    newValue = newValue.replace("$value", value);
                } else {
                    DriverSetup.logMessage("MobileElement: " + property + " does not contain $value");
                    throw new Exception("MobileElement: " + property + " does not contain $value");
                }
                if (params[0] != null || newValue != null) {
                    if (params[0].equalsIgnoreCase("accessibilityid")) {
                        Class<MobileBy> cl = MobileBy.class;
                        Method m1 = cl.getMethod(params[0], String.class);
                        MobileBy.ByAccessibilityId s1 = (MobileBy.ByAccessibilityId) m1.invoke(null, newValue);
                        return s1;
                    } else {
                        Class<By> cl = By.class;
                        Method m1 = cl.getMethod(params[0], String.class);
                        By s1 = (By) m1.invoke(null, newValue);
                        return s1;
                    }
                } else {
                    DriverSetup.logMessage("MobileElement: " + property + " format is not proper in the property file");
                    throw new Exception("MobileElement: " + property + " format is not proper in the property file");
                }
            } else {
                DriverSetup.logMessage("MobileElement: " + property + " not found in the property file");
                throw new Exception("MobileElement: " + property + " not found in the property file");
            }
        } catch (Exception e) {
            throw e;
        }
    }
}

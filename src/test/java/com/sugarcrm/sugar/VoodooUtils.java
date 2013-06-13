package com.sugarcrm.sugar;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;
//import java.util.logging.Level;

import com.sugarcrm.voodoo.automation.VInterface;
import com.sugarcrm.voodoo.automation.Voodoo;
import com.sugarcrm.voodoo.configuration.Configuration;

/**
 * Provides general utility functionality that is not part of the application
 * @author David Safar <dsafar@sugarcrm.com>
 */
public class VoodooUtils {
    public final Voodoo voodoo;
    public final VInterface iface;
    private static VoodooUtils utils;
    private static final String currentWorkingPath = System.getProperty("user.dir");
    private static final String relativeResourcesPath = File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator;
    private Configuration grimoireConfig;

    public static VoodooUtils getInstance() throws Exception {
        if (utils == null) utils = new VoodooUtils();
        return utils;
    }

    private VoodooUtils() throws Exception {
        // Allow users to add configuration file through CLI using -Dvoodoo.conf=[path]
        String CLIvoodooPropsPath = System.getProperty("voodoo.conf");
        String voodooPropsPath = currentWorkingPath + relativeResourcesPath + "voodoo.properties";
        if (CLIvoodooPropsPath != null) voodooPropsPath = CLIvoodooPropsPath;
        //System.out.println("voodoo.properties path: " + voodooPropsPath);
        Configuration voodooConfig = new Configuration();
        voodooConfig.load(voodooPropsPath);
        voodoo = Voodoo.getInstance(voodooConfig);
        iface = voodoo.getInterface();
    }

    /**
     * Gets the Grimoire Specific Properties
     * @return
     */
    public Properties getGrimoireConfig(){
        return grimoireConfig;
    }

    /**
     * Launches the application on the currently defined platform
     * @throws Exception
     */
    public void launchApp() throws Exception {
        voodoo.log.info("Launching app...");
        String grimoirePropsPath = currentWorkingPath + relativeResourcesPath + "grimoire.properties";
        grimoireConfig = new Configuration();
        grimoireConfig.load(new FileInputStream(grimoirePropsPath));
        iface.start();
        iface.go(grimoireConfig.getProperty("env.base_url", "http://localhost/sugar"));
        iface.maximize();
        voodoo.log.info("App launched.");
    } // end launchApp()

    /**
     * Closes the application (web browser, mobile app, etc.)
     * @throws Exception
     */
    public void closeApp() throws Exception {
        iface.stop();
    } // end closeApp()

    /**
     * Gets a set of unique window identifiers and return
     */
    public List <String> getWindowHandles(){
        List <String> windowHandles = null;
        //TODO: Implement getWindowHandles
        return windowHandles;
    }//end getWindowHandles()

    /**
     * Switches to the passed in window by its unique handle
     */
    public void switchToWindow(String windowHandle){
        //TODO: Implement switchToWindow
    }//end switchToWindow()

    /**
     * Closes a browser window where needed
     */
    public void closeWindow(String windowHandle){
        //TODO: Implement closeWindow
    }//end closeWindow()

    /**
     * Takes a lower-case prefix and appends an existing camel-cased string,
     * preserving camel-case for the resulting string (i.e. capitalizes the
     * first letter of the existing string).
     * @param prefix	the lower-case string for the beginning of the result
     * @param body	the existing camel-cased string to capitalize and append
     * @return	the resulting camel-cased string
     */
    public String prependCamelCase(String prefix, String body)
    {
        return prefix + capitalize(body);
    }

    /**
     * Transforms a C-style string (lower-case, words separated by _s) into a
     * Java-style (camel-cased, no _s) string.
     * @param toCase	the string to convert to camel case
     * @return	the camel-cased string
     */
    public String camelCase(String toCase)
    {
        String[] parts = toCase.split("_");
        String camelCaseString = "";
        for (String part : parts){
            camelCaseString = camelCaseString + capitalize(part);
        }
        return camelCaseString;
    }

    /**
     * Capitalizes the first letter of the passed string.
     * @param s	the string to capitalize
     * @return	the capitalized string
     */
    static String capitalize(String s) {
        return s.substring(0, 1).toUpperCase() +
                s.substring(1).toLowerCase();
    }
}

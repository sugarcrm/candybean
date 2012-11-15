package com.sugarcrm.voodoo.automation.framework;

import java.awt.Toolkit;
import java.io.File;
import java.util.HashMap;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.openqa.selenium.JavascriptExecutor;

import com.google.common.base.Function;
import com.sugarcrm.voodoo.automation.IAutomation;
import com.sugarcrm.voodoo.automation.Utils;
import com.sugarcrm.voodoo.automation.Voodoo;
import com.sugarcrm.voodoo.automation.control.VControl;
import com.sugarcrm.voodoo.automation.control.VSelect;


public class Selenium implements IAutomation {

  private final Voodoo voodoo;
  private final Properties props;
  private final WebDriver browser;
  private HashMap<Integer, String> windowHandles = new HashMap<Integer, String>();
  private int windowIndex = 0;


  /**
   * @param voodoo
   * @param props
   * @param browserType
   * @throws Exception
   */
  public Selenium(Voodoo voodoo, Properties props, InterfaceType browserType) throws Exception {
    this.voodoo = voodoo;
    this.props = props;
    this.browser = this.getBrowser(browserType);
  }


  @Override
    public void start() throws Exception {
      voodoo.log.info("Selenium: starting automation.");
    }


  @Override
    public void stop() throws Exception {
      voodoo.log.info("Selenium: stopping automation.");
      browser.quit();
    }


  public void closeWindow() throws Exception {
    voodoo.log.info("Selenium: closing window.");
    this.browser.close();
  }


  @Override
    public void go(String url) throws Exception {
      voodoo.log.info("Selenium: going to URL and switching to window: " + url);
      browser.get(url);
      browser.switchTo().window(browser.getWindowHandle());
    }


  @Override
    public void acceptDialog() throws Exception {
      voodoo.log.info("Selenium: accepting dialog.");
      Alert alert = browser.switchTo().alert();
      alert.accept();
    }


  @Override
    public void focusByIndex(int index) throws Exception {
      voodoo.log.info("Selenium: focusing window by index: " + index);
      Set<String> Handles = browser.getWindowHandles();
      while (Handles.iterator().hasNext()){
        String windowHandle = Handles.iterator().next();
        if (!windowHandles.containsValue(windowHandle)){
          windowHandles.put(windowIndex, windowHandle);
          windowIndex++;
        }
        Handles.remove(windowHandle);
      }
      browser.switchTo().window(windowHandles.get(index));
    }


  @Override
    public void focusByTitle(String title) throws Exception {
      voodoo.log.info("Selenium: focusing window by title: " + title);
      Set<String> handles = browser.getWindowHandles();
      while (handles.iterator().hasNext()){
        String windowHandle = handles.iterator().next();
        WebDriver window = browser.switchTo().window(windowHandle);
        if (window.getTitle().equals(title)){
          break;
        }
        handles.remove(windowHandle);
      }
    }


  @Override
    public void focusByUrl(String url) throws Exception {
      voodoo.log.info("Selenium: focusing window by url: " + url);
      Set<String> handles = browser.getWindowHandles();
      while (handles.iterator().hasNext()){
        String windowHandle = handles.iterator().next();
        WebDriver window = browser.switchTo().window(windowHandle);
        if (window.getCurrentUrl().equals(url)){
          break;
        }
        handles.remove(windowHandle);
      }
    }


  public void maximize() {
    voodoo.log.info("Selenium: maximizing window");
    java.awt.Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    browser.manage().window().setSize(new Dimension(screenSize.width, screenSize.height));
  }


  @Override
    public String getAttribute(VControl control, String attribute) throws Exception {
      voodoo.log.info("Selenium: getting attribute: " + attribute + " for control: " + control.toString());
      By by = Selenium.getBy(control.getHook().hookStrategy, control.getHook().hookString);
      WebElement we =  browser.findElement(by);
      String value = we.getAttribute(attribute);
      if (value == null) throw new Exception("Selenium: attribute does not exist.");
      else return value;
    }


  @Override
    public String getText(VControl control) throws Exception {
      By by = Selenium.getBy(control.getHook().hookStrategy, control.getHook().hookString);
      return browser.findElement(by).getText();
    }


  @Override
    public void click(VControl control) throws Exception {
      By by = Selenium.getBy(control.getHook().hookStrategy, control.getHook().hookString);
      browser.findElement(by).click();
    }

  @Override
    public void doubleClick(VControl control) throws Exception {
      By by = Selenium.getBy(control.getHook().hookStrategy, control.getHook().hookString);
      WebElement we = browser.findElement(by);
      Actions action = new Actions(browser);
      action.doubleClick(we).perform();
    }

  @Override
    public void dragNDrop(VControl control1, VControl control2) throws Exception {
      By by1 = Selenium.getBy(control1.getHook().hookStrategy, control1.getHook().hookString);
      By by2 = Selenium.getBy(control2.getHook().hookStrategy, control2.getHook().hookString);
      WebElement draggable = browser.findElement(by1);
      WebElement target = browser.findElement(by2);
      Actions action = new Actions(browser);
      action.dragAndDrop(draggable, target).build().perform();
    }


  @Override
    public void hover(VControl control) throws Exception {
      By by = Selenium.getBy(control.getHook().hookStrategy, control.getHook().hookString);
      WebElement we = browser.findElement(by);
      Actions action = new Actions(browser);
      action.moveToElement(we).perform();
    }


  @Override
    public void rightClick(VControl control) throws Exception {
      By by = Selenium.getBy(control.getHook().hookStrategy, control.getHook().hookString);
      WebElement we = browser.findElement(by);
      Actions action = new Actions(browser);
      action.contextClick(we).perform();
    }


  @Override
    public void sendString(VControl control, String input) throws Exception {
      By by = Selenium.getBy(control.getHook().hookStrategy, control.getHook().hookString);
      WebElement we = browser.findElement(by);
      we.clear();
      we.sendKeys(input);
    }


  @Override
    public void scroll(VControl control) throws Exception {
      By by = Selenium.getBy(control.getHook().hookStrategy, control.getHook().hookString);
      WebElement we = browser.findElement(by);
      int y = we.getLocation().y;
      ((JavascriptExecutor) browser).executeScript("window.scrollBy(0," + y +");");
    }


  @Override	
    public void waitOn(VControl control) throws Exception{
      By by = Selenium.getBy(control.getHook().hookStrategy, control.getHook().hookString);
      final WebElement we = browser.findElement(by);
      long explicitWait = Long.parseLong(Utils.getCascadingPropertyValue(props, "12000", "perf.explicit_wait"));
      WebDriverWait wait = new WebDriverWait(this.browser, explicitWait);
      wait.until(new Function<WebDriver, Boolean>() {
        public Boolean apply(WebDriver driver) {
          return we.isDisplayed();
        }
      });
    }


  @Override
    public void wait(VControl control, String attribute, String value) throws Exception {
      By by = Selenium.getBy(control.getHook().hookStrategy, control.getHook().hookString);
      final WebElement we = browser.findElement(by);
      final String vAttribute = attribute;
      final String vValue = value;
      long explicitWait = Long.parseLong(Utils.getCascadingPropertyValue(props, "12000", "perf.explicit_wait"));
      WebDriverWait wait = new WebDriverWait(this.browser, explicitWait);
      wait.until(new Function<WebDriver, Boolean>() {
        public Boolean apply(WebDriver driver) {
          return we.getAttribute(vAttribute).contains(vValue);
        }
      });
    }


  @Override
    public String getSelected(VSelect select) throws Exception {
      voodoo.log.info("Selenium: getting selected value from control: " + select.toString());
      By by = Selenium.getBy(select.getHook().hookStrategy, select.getHook().hookString);
      Select dropDownList = new Select(browser.findElement(by));
      WebElement selectedOption  = dropDownList.getFirstSelectedOption();
      return selectedOption.getText();
    }


  @Override
    public void select(VSelect select, boolean isSelected) throws Exception {
      By by = Selenium.getBy(select.getHook().hookStrategy, select.getHook().hookString);
      WebElement we = browser.findElement(by);
      if (!we.getAttribute("type").equals("checkbox")) throw new Exception("Selenium: web element is not a checkbox.");
      if (we.isSelected() != isSelected) we.click();
    }


  @Override
    public void select(VSelect select, String value) throws Exception {
      voodoo.log.info("Selenium: selecting value from control: " + select.toString());
      By by = Selenium.getBy(select.getHook().hookStrategy, select.getHook().hookString);
      Select dropDownList = new Select(browser.findElement(by));
      dropDownList.selectByVisibleText(value);
    }


  private static By getBy(Strategy strategy, String hook) throws Exception {
    switch (strategy) {
      case CSS:
        return By.cssSelector(hook);
      case XPATH:
        return By.xpath(hook);
      case ID:
        return By.id(hook);
      case NAME:
        return By.name(hook);
      case LINK:
        return By.linkText(hook);
      case PLINK:
        return By.partialLinkText(hook);
      default:
        throw new Exception("Selenium: strategy type not recognized.");
    }
  }

  /**
   * @param browserType
   * @return
   * @throws Exception
   */
  private WebDriver getBrowser(InterfaceType browserType) throws Exception {
    WebDriver webDriver = null;
    switch (browserType) {
      case FIREFOX:		
        String profileName = Utils.getCascadingPropertyValue(this.props, "default", "browser.firefox_profile");
        String ffBinaryPath = Utils.getCascadingPropertyValue(this.props, "//home//conrad//Applications//firefox-10//firefox", "browser.firefox_binary");
        FirefoxProfile ffProfile = (new ProfilesIni()).getProfile(profileName);
        FirefoxBinary ffBinary = new FirefoxBinary(new File(ffBinaryPath));
        //			if (System.getProperty("headless") != null) {
        //				FirefoxBinary ffBinary = new FirefoxBinary();//new File("//home//conrad//Applications//firefox-10//firefox"));
        //				ffBinary.setEnvironmentProperty("DISPLAY", ":1"); 
        //				webDriver = new FirefoxDriver(ffBinary, ffProfile);
        //			}
        voodoo.log.info("Instantiating Firefox with profile name: " + profileName + " and binary path: " + ffBinaryPath);
        webDriver = new FirefoxDriver(ffBinary, ffProfile);
        break;
      case CHROME:
        String workingDir = System.getProperty("user.dir");
        ChromeOptions chromeOptions = new ChromeOptions();
        String chromeDriverLogPath = Utils.getCascadingPropertyValue(props, workingDir + "/log/chromedriver.log", "browser.chrome_driver_log_path");
        chromeOptions.addArguments("--log-path=" + chromeDriverLogPath);
        String chromeDriverPath = Utils.getCascadingPropertyValue(props, workingDir + "/etc/chromedriver-mac", "browser.chrome_driver_path");
        //			chromeOptions.setBinary(new File(chromeDriverPath));
        System.setProperty("webdriver.chrome.driver", chromeDriverPath);
        voodoo.log.info("Instantiating Chrome with:\n    log path:" + chromeDriverLogPath + "\n    driver path: " + chromeDriverPath);
        webDriver = new ChromeDriver(chromeOptions);
        break;
      case IE:
        throw new Exception("Selenium: ie browser not yet supported.");
      case SAFARI:
        throw new Exception("Selenium: safari browser not yet supported.");
      default:
        throw new Exception("Selenium: browser type not recognized.");
    }
    long implicitWait = Long.parseLong(props.getProperty("perf.implicit_wait"));
    if (System.getProperty("headless") == null) {
      java.awt.Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
      webDriver.manage().window().setSize(new Dimension(screenSize.width, screenSize.height));
    }
    webDriver.manage().timeouts().implicitlyWait(implicitWait, TimeUnit.SECONDS);
    return webDriver;
  }


  //	/**
  //	 * @param selectElement
  //	 * @param actionElement
  //	 */
  //	public static void allOptionsAction(Select selectElement, WebElement actionElement) {
  //		List<WebElement> options = selectElement.getOptions();
  //		for (WebElement option : options) {
  //			selectElement.selectByVisibleText(option.getText());
  //			actionElement.click();
  //		}
  //	}
  //	
  //	
  //	/**
  //	 * @param selectElement
  //	 * @param actionOptionValues
  //	 * @param actionElement
  //	 * @throws Exception
  //	 */
  //	public static void optionAction(Select selectElement, Set<String> actionOptionValues, WebElement actionElement) throws Exception {
  //		List<WebElement> allOptions = selectElement.getOptions();
  //		HashSet<String> optionValues = new HashSet<String>();
  //		for(WebElement option : allOptions) {
  //			optionValues.add(option.getText());
  ////			System.out.println("Adding to options set:" + option.getText());
  //		}
  //		if(optionValues.containsAll(actionOptionValues)) {
  //			for(String option : actionOptionValues) {
  //				selectElement.selectByVisibleText(option);
  //				actionElement.click();
  //			}
  //		} else throw new Exception("Specified select option unavailable...");
  //	}
  //	
  //	
  //	/**
  //	 * @param tableElement
  //	 * @param rowRelativeXPathTextKey
  //	 * @param value
  //	 * @return
  //	 */
  //	public static boolean tableContainsValue(WebElement tableElement, String rowRelativeXPathTextKey, String value) {
  //		List<WebElement> rows = tableElement.findElements(By.tagName("tr"));
  //		for (WebElement row : rows) {
  //			if (row.findElement(By.xpath(rowRelativeXPathTextKey)).getText().equalsIgnoreCase(value)) return true;
  //		}
  //		return false;
  //	}
  //	
  //	
  //	/**
  //	 * @param table
  //	 * @param rowRelativeXPathTextKey
  //	 * @param rowRelativeXPathElementValue
  //	 * @return
  //	 * @throws Exception
  //	 */
  //	public static Map<String, WebElement> loadMapFromTable(WebElement table, String rowRelativeXPathTextKey, String rowRelativeXPathElementValue) throws Exception {
  //		Map<String, WebElement>	rowMap = new HashMap<String, WebElement>();
  //		List<WebElement> rows = table.findElements(By.tagName("tr"));
  ////		System.out.println("table # rows:" + rows.size());
  //		for (WebElement row : rows) {
  ////			List<WebElement> childTDs = row.findElements(By.tagName("td"));
  ////			for (WebElement childTD : childTDs) System.out.println("td text:" + childTD.getText());
  //			String k = row.findElement(By.xpath(rowRelativeXPathTextKey)).getText();
  //			WebElement v = row.findElement(By.xpath(rowRelativeXPathElementValue));
  ////			System.out.println("key text:" + k + ", value we:" + v.getTagName() + "/" + v.getText());
  //			rowMap.put(k, v);
  //		}
  //		return rowMap;
  //	}
  //	
  //
  //	/**
  //	 * @param element
  //	 * @return
  //	 */
  //	public static String webElementToString(WebElement element) {
  //		List<WebElement> childElements = element.findElements(By.xpath("*"));
  //		String s = element.getTagName() + ":" + element.getText() + " ";
  //		for(WebElement we : childElements) {
  //			s += we.getTagName() + ":" + we.getText() + " ";
  //		}
  //		return s;
  //	}
  //	
  //	
  //	/**
  //	 * @param nativeOptions
  //	 * @param queryOptionNames
  //	 * @return
  //	 */
  //	public static boolean optionValuesEqual(List<WebElement> nativeOptions, Set<String> queryOptionNames) {
  //		Set<String> nativeOptionNames = new HashSet<String>();
  //		for (WebElement option : nativeOptions) {
  //			nativeOptionNames.add(option.getText());
  //		}
  //		if (nativeOptionNames.containsAll(queryOptionNames) && queryOptionNames.containsAll(nativeOptionNames)) return true;
  //		else return false;
  //	}
}

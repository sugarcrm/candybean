package com.sugarcrm.voodoo;

import java.awt.Toolkit;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JOptionPane;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Utils {
	
	public static String webElementToString(WebElement element) {
		List<WebElement> childElements = element.findElements(By.xpath("*"));
		String s = element.getTagName() + ":" + element.getText() + " ";
		for(WebElement we : childElements) {
			s += we.getTagName() + ":" + we.getText() + " ";
		}
		return s;
	}
	
	public static void maximizeBrowserWindow(WebDriver browser) {
		java.awt.Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		browser.manage().window().setSize(new Dimension(screenSize.width, screenSize.height));
	}
	
	public static boolean optionValuesEqual(List<WebElement> nativeOptions, Set<String> queryOptionNames) {
		Set<String> nativeOptionNames = new HashSet<String>();
		for (WebElement option : nativeOptions) {
			nativeOptionNames.add(option.getText());
		}
		if (nativeOptionNames.containsAll(queryOptionNames) && queryOptionNames.containsAll(nativeOptionNames)) return true;
		else return false;
		
	}
	
	public static void allOptionsAction(Select selectElement, WebElement actionElement) {
		List<WebElement> options = selectElement.getOptions(); 
		for(WebElement option : options) {
			selectElement.selectByVisibleText(option.getText());
			actionElement.click();
		}
	}
	
	public static void optionAction(Select selectElement, Set<String> actionOptionValues, WebElement actionElement) throws Exception {
		List<WebElement> allOptions = selectElement.getOptions();
		HashSet<String> optionValues = new HashSet<String>();
		for(WebElement option : allOptions) {
			optionValues.add(option.getText());
//			System.out.println("Adding to options set:" + option.getText());
		}
		if(optionValues.containsAll(actionOptionValues)) {
			for(String option : actionOptionValues) {
				selectElement.selectByVisibleText(option);
				actionElement.click();
			}
		} else throw new Exception("Specified select option unavailable...");
	}
	
	public static boolean tableContainsValue(WebElement tableElement, String rowRelativeXPathTextKey, String value) {
		List<WebElement> rows = tableElement.findElements(By.tagName("tr"));
		for (WebElement row : rows) {
			if (row.findElement(By.xpath(rowRelativeXPathTextKey)).getText().equalsIgnoreCase(value)) return true;
		}
		return false;
	}
	
	public static Map<String, WebElement> loadMapFromTable(WebElement table, String rowRelativeXPathTextKey, String rowRelativeXPathElementValue) throws Exception {
		Map<String, WebElement>	rowMap = new HashMap<String, WebElement>();
		List<WebElement> rows = table.findElements(By.tagName("tr"));
//		System.out.println("table # rows:" + rows.size());
		for (WebElement row : rows) {
//			List<WebElement> childTDs = row.findElements(By.tagName("td"));
//			for (WebElement childTD : childTDs) System.out.println("td text:" + childTD.getText());
			String k = row.findElement(By.xpath(rowRelativeXPathTextKey)).getText();
			WebElement v = row.findElement(By.xpath(rowRelativeXPathElementValue));
//			System.out.println("key text:" + k + ", value we:" + v.getTagName() + "/" + v.getText());
			rowMap.put(k, v);
		}
		return rowMap;
	}
	
	public static void explicitWait(WebDriver browser, long timeout, final By by) throws Exception {
		(new WebDriverWait(browser, timeout)).until(new ExpectedCondition<WebElement>(){
			public WebElement apply(WebDriver wd) {
				return wd.findElement(by);
			}});
	}
	
	public static void interact(String s) {
		JOptionPane.showInputDialog(s);
	}
	
	public static String trimString(String s, int length) {
		if (s.length() <= length) return s;
		return s.substring(s.length() - length); 
	}

	public static class Pair<X, Y> { 
		  public final X x; 
		  public final Y y; 
		  public Pair(X x, Y y) { 
			  this.x = x; 
			  this.y = y; 
		  }
		  @Override public String toString() {
			  return "x:" + x.toString() + ",y:" + y.toString();
		  }
	}
	
	public static class Triplet<X, Y, Z> { 
		  public final X x; 
		  public final Y y; 
		  public final Z z; 
		  public Triplet(X x, Y y, Z z) { 
			  this.x = x; 
			  this.y = y;
			  this.z = z;
		  } 
		  @Override public String toString() {
			  return "x:" + x.toString() + ",y:" + y.toString() + ",z:" + z.toString();
		  }
	}
}

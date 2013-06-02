package com.sugarcrm.voodoo.automation.control;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.sugarcrm.voodoo.automation.VInterface;
import com.sugarcrm.voodoo.automation.Voodoo;
import com.sugarcrm.voodoo.automation.control.VHook.Strategy;

/**
 * @author cwarmbold
 *
 */
public class VTable extends VControl {
	
	public VTable(Voodoo voodoo, VInterface iface, Strategy tableStrategy, String tableHook) throws Exception {
		super(voodoo, iface, tableStrategy, tableHook);
	}
	
	public VTable(Voodoo voodoo, VInterface iface, VHook tableHook) throws Exception {
		super(voodoo, iface, tableHook);
	}
	
	/**
  	 * @param tableElement
  	 * @param rowRelativeXPathTextKey
  	 * @param value
  	 * @return
  	 */
  	public boolean containsText(String value) {
  		List<WebElement> webElements = super.iface.wd.findElements(By.xpath("*"));
  		for (WebElement we : webElements) {
  			if (we.getText().trim().equals(value)) return true;
  		}
  		return false;
  	}
  	
//  	/**
//  	 * @param table
//  	 * @param rowRelativeXPathTextKey
//  	 * @param rowRelativeXPathElementValue
//  	 * @return
//  	 * @throws Exception
//  	 */
//  	public static Map<String, WebElement> loadMapFromTable(WebElement table, String rowRelativeXPathTextKey, String rowRelativeXPathElementValue) throws Exception {
//  		Map<String, WebElement>	rowMap = new HashMap<String, WebElement>();
//  		List<WebElement> rows = table.findElements(By.tagName("tr"));
//  //		System.out.println("table # rows:" + rows.size());
//  		for (WebElement row : rows) {
//  //			List<WebElement> childTDs = row.findElements(By.tagName("td"));
//  //			for (WebElement childTD : childTDs) System.out.println("td text:" + childTD.getText());
//  			String k = row.findElement(By.xpath(rowRelativeXPathTextKey)).getText();
//  			WebElement v = row.findElement(By.xpath(rowRelativeXPathElementValue));
//  //			System.out.println("key text:" + k + ", value we:" + v.getTagName() + "/" + v.getText());
//  			rowMap.put(k, v);
//  		}
//  		return rowMap;
//  	}
	
	@Override
	public String toString() {
		return "VTable(" + super.toString() + ")";
	}
}

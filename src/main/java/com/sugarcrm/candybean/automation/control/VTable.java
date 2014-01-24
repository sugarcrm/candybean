/**
 * Candybean is a next generation automation and testing framework suite.
 * It is a collection of components that foster test automation, execution
 * configuration, data abstraction, results illustration, tag-based execution,
 * top-down and bottom-up batches, mobile variants, test translation across
 * languages, plain-language testing, and web service testing.
 * Copyright (C) 2013 SugarCRM, Inc. <candybean@sugarcrm.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.sugarcrm.candybean.automation.control;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.sugarcrm.candybean.automation.VInterface;
import com.sugarcrm.candybean.automation.Candybean;
import com.sugarcrm.candybean.automation.control.VHook.Strategy;
import com.sugarcrm.candybean.utilities.exception.CandybeanException;

/**
 * VControl is a control that represents and allows for interaction with the TABLE element.
 * Any HTML table tag on the page can be represented by a VTable.
 * @author cwarmbold
 */
public class VTable extends VControl {
	
	public VTable(Candybean voodoo, VInterface iface, Strategy tableStrategy, String tableHook) throws CandybeanException {
		super(voodoo, iface, tableStrategy, tableHook);
	}
	
	public VTable(Candybean voodoo, VInterface iface, VHook tableHook) throws CandybeanException {
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
  			if (we.getText().trim().equals(value)) {
  				return true;
  			}
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

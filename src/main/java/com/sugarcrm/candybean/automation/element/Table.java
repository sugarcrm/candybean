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
package com.sugarcrm.candybean.automation.element;

/**
 * Table is an element that represents and allows for enhanced interaction with
 * a table element.
 * 
 * @author Conrad Warmbold
 */
public interface Table {
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
}

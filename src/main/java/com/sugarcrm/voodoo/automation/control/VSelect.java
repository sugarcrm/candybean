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
package com.sugarcrm.voodoo.automation.control;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import com.sugarcrm.voodoo.automation.VInterface;
import com.sugarcrm.voodoo.automation.Voodoo;
import com.sugarcrm.voodoo.automation.control.VHook.Strategy;

/**
 * VSelect is a control that allows for interaction with the SELECT element.
 *
 * @author cwarmbold
 */

public class VSelect extends VControl {
	
	/**
	 * Instantiate a VSelect object.
	 *
	 * @param voodoo	  {@link Voodoo} object for this run
	 * @param iface	  {@link VInterface} for this run
	 * @param strategy  {@link Strategy} used to search for element
	 * @param hook		  value of strategy to search for
	 * @throws Exception	 <i>not thrown</i>
	 */
	public VSelect(Voodoo voodoo, VInterface iface,
						Strategy strategy, String hook) throws Exception {
		super(voodoo, iface, strategy, hook);
	}
	
	/**
	 * Instantiate a VSelect object.
	 *
	 * @param voodoo	{@link Voodoo} object for this run
	 * @param iface	{@link VInterface} for this run
	 * @param hook		{@link VHook} used to search for this element
	 * @throws Exception	 <i>not thrown</i>
	 */
	public VSelect(Voodoo voodoo, VInterface iface, VHook hook)
		throws Exception {
		super(voodoo, iface, hook);
	}
	
	/**
	 * Get the text of the currently selected option.
	 *
	 * <p>If this is a multiple select, only the text of the first
	 * selected option is returned.</p>
	 *
	 * @return text of the currently selected option
	 * @throws Exception (NullPointerException) if no option is
	 *			  selected or (UnexpectedTagNameException) if the element
	 *			  found is not a SELECT.
	 */
	public String getSelected() throws Exception {
		super.voodoo.log.info("Selenium: getting selected value from control: " + this.toString());
		Select dropDownList = new Select(super.we);
		WebElement selectedOption = dropDownList.getFirstSelectedOption();
		return selectedOption.getText();
	}

	/**
	 * Returns the boolean value of the control's selection state.
	 * 
	 * @return	boolean true if the control is selected
	 * @throws Exception
	 */
	public boolean isSelected() throws Exception {
		super.voodoo.log.info("Selenium: returns true if control: " + this.toString() + " is selected");
		return super.we.isSelected();
	}

	/**
	 * Toggle the state of a CHECKBOX or RADIO element.
	 *
	 * <p>N.b. This method operates on CHECKBOX or RADIO elements rather than
	 * SELECT elements.</p>
	 *
	 * @param isSelected	desired state of checkbox or radio
	 * @throws Exception 	if element is not found or if element is not a checkbox or radio
	 */
	public void select(boolean isSelected) throws Exception {
		voodoo.log.info("Selenium: setting select: " + this.toString() + " to value: " + isSelected);
		String type = super.we.getAttribute("type");
		if (!type.equals("checkbox") && !type.equals("radio"))
			throw new Exception("Selenium: web element is not a checkbox or radio.");
		if (super.we.isSelected() != isSelected)
			super.we.click();
	}

	/**
	 * Select an option by its visible text.
	 *
	 * @param value  text of the option to be selected
	 * @throws Exception	 if the element is not found
	 */
	public void select(String value) throws Exception {
		voodoo.log.info("Selenium: selecting value '" + value  +  "' from control: " + this.toString());
		Select dropDownList = new Select(super.we);
		dropDownList.selectByVisibleText(value);
	}
	
	@Override
	public String toString() {
		return "VSelect(" + super.toString() + ")";
	}
}

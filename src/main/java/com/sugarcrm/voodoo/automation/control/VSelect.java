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
	 * Toggle the state of a CHECKBOX element.
	 *
	 * <p>N.b. This method operates on CHECKBOX elements rather than
	 * SELECT elements.</p>
	 *
	 * @param isSelected	 desired state of checkbox
	 * @throws Exception if element is not found or if element is not a checkbox
	 */
	public void select(boolean isSelected) throws Exception {
		voodoo.log.info("Selenium: setting select: " + this.toString() + " to value: " + isSelected);
		if (!super.we.getAttribute("type").equals("checkbox"))
			throw new Exception("Selenium: web element is not a checkbox.");
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

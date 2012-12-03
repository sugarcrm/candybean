package com.sugarcrm.voodoo.automation.control;


import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import com.sugarcrm.voodoo.automation.IInterface;
import com.sugarcrm.voodoo.automation.VInterface;
import com.sugarcrm.voodoo.automation.Voodoo;
import com.sugarcrm.voodoo.automation.control.VHook.Strategy;


/**
 * @author cwarmbold
 *
 */
public class VSelect extends VControl {
	
	public VSelect(Voodoo voodoo, VInterface iface, Strategy strategy, String hook) throws Exception {
		super(voodoo, iface, strategy, hook);
	}
	
	public VSelect(Voodoo voodoo, VInterface iface, VHook hook) throws Exception {
		super(voodoo, iface, hook);
	}
	
	public String getSelected() throws Exception {
		super.voodoo.log.info("Selenium: getting selected value from control: " + this.toString());
		WebElement we = super.iface.wd.findElement(super.getBy(this.hook));
		Select dropDownList = new Select(we);
		WebElement selectedOption = dropDownList.getFirstSelectedOption();
		return selectedOption.getText();
	}

	public void select(boolean isSelected) throws Exception {
		voodoo.log.info("Selenium: setting select: " + this.toString() + " to value: " + isSelected);
		WebElement we = super.iface.wd.findElement(super.getBy(this.hook));
		if (!we.getAttribute("type").equals("checkbox"))
			throw new Exception("Selenium: web element is not a checkbox.");
		if (we.isSelected() != isSelected)
			we.click();
	}

	public void select(String value) throws Exception {
		voodoo.log.info("Selenium: selecting value from control: " + this.toString());
		WebElement we = super.iface.wd.findElement(super.getBy(this.hook));
		Select dropDownList = new Select(we);
		dropDownList.selectByVisibleText(value);
	}
	
	@Override
	public String toString() {
		return "VSelect(" + super.toString() + ")";
	}
}

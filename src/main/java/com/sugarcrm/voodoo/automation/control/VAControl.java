package com.sugarcrm.voodoo.automation.control;

import com.sugarcrm.voodoo.automation.VInterface;
import com.sugarcrm.voodoo.automation.Voodoo;

public class VAControl implements IAControl {
	
	protected final Voodoo voodoo;
	protected final VInterface iface;

	public VAControl(Voodoo voodoo, VInterface iface) throws Exception {
		this.voodoo = voodoo;
		this.iface = iface;
	}
	
	@Override
	public String getCurrentActivity() throws Exception {
		return this.iface.vac.solo.getCurrentActivity();
	}
	
	@Override
	public String getButton(int index) throws Exception {
		return this.iface.vac.solo.getButton(index);
	}
	
	@Override
	public String getButton(String text) throws Exception {
		return this.iface.vac.solo.getButton(text);
	}
	
	@Override
	public void clickOnText(String text) throws Exception {
		this.iface.vac.solo.clickOnText(text);
	}
	
	@Override
	public void enterText(int index, String text) throws Exception {
		this.iface.vac.solo.enterText(index, text);
	}
	
	@Override
	public void clickOnButton(String text) throws Exception {
		this.iface.vac.solo.clickOnButton(text);
	}
	
	@Override
	public boolean searchText(String text) throws Exception {
		return this.iface.vac.solo.searchText(text);
	}
	
}

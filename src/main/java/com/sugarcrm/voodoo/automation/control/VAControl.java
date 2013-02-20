package com.sugarcrm.voodoo.automation.control;

import com.sugarcrm.voodoo.automation.VInterface;
import com.sugarcrm.voodoo.automation.Voodoo;

public class VAControl {//implements IAControl {
	
	protected final Voodoo voodoo;
	protected final VInterface iface;

	public VAControl(Voodoo voodoo, VInterface iface) throws Exception {
		this.voodoo = voodoo;
		this.iface = iface;
	}
	
//	@Override
//	public String getCurrentActivity() throws Exception {
//		msg("Performing getCurrentyActivity");
//		return this.iface.vac.solo.getCurrentActivity();
//	}
//	
//	@Override
//	public String getButton(int index) throws Exception {
//		msg("Performing getButton by Index: " + index);
//		return this.iface.vac.solo.getButton(index);
//	}
//	
//	@Override
//	public String getButton(String text) throws Exception {
//		msg("Performing getButton by test: " + text);
//		return this.iface.vac.solo.getButton(text);
//	}
//	
//	@Override
//	public void clickOnText(String text) throws Exception {
//		msg("Performing clickOnText: " + text);
//		this.iface.vac.solo.clickOnText(text);
//	}
//	
//	@Override
//	public void enterText(int index, String text) throws Exception {
//		msg("Performing enterText for index: " + index + ", with text: " + text);
//		this.iface.vac.solo.enterText(index, text);
//	}
//	
//	@Override
//	public void clickOnButton(String text) throws Exception {
//		msg("Performing clickOnButton with text: " + text);
//		this.iface.vac.solo.clickOnButton(text);
//	}
//	
//	@Override
//	public boolean searchText(String text) throws Exception {
//		msg("Performing searchText: " + text);
//		return this.iface.vac.solo.searchText(text);
//	}
	
	private void msg(String msg) { 
		System.out.println("[VoodooAndroid Test]: " + msg);
	}
	
}

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

//import com.sugarcrm.voodoo.automation.VInterface;
//import com.sugarcrm.voodoo.automation.Voodoo;
//
public class VAControl {//implements IAControl {
//	
//	protected final Voodoo voodoo;
//	protected final VInterface iface;
//
//	public VAControl(Voodoo voodoo, VInterface iface) throws Exception {
//		this.voodoo = voodoo;
//		this.iface = iface;
//	}
//	
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

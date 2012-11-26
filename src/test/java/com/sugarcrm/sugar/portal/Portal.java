package com.sugarcrm.sugar.portal;


import com.sugarcrm.voodoo.automation.Utils;
import com.sugarcrm.voodoo.automation.Voodoo;
import com.sugarcrm.voodoo.automation.control.VControl;
import com.sugarcrm.sugar.Sugar;
import com.sugarcrm.sugar.SugarTest;
import com.sugarcrm.sugar.cases.Case;


public class Portal {
	
	public static void login(Voodoo voodoo, Sugar sugar, String username, String password) throws Exception {
		String sugarURL = Utils.getCascadingPropertyValue(SugarTest.sugarProps, "http://localhost/sugar/", "env.base_url");
		String relPortalURL = Utils.getCascadingPropertyValue(SugarTest.sugarProps, "portal/", "env.portal_rel_url");
		voodoo.auto.go(sugarURL + relPortalURL);
		(new VControl(sugar.getHook("portallogin_textfield_username"), voodoo.auto)).sendString(username);
		(new VControl(sugar.getHook("portallogin_textfield_password"), voodoo.auto)).sendString(password);
		(new VControl(sugar.getHook("portallogin_button_login"), voodoo.auto)).click();
		(new VControl(sugar.getHook("portal_navbar_menu_user"), voodoo.auto)).waitOn();
	}
	
	public static void logout(Voodoo voodoo, Sugar sugar) throws Exception {
		(new VControl(sugar.getHook("portal_navbar_menu_user"), voodoo.auto)).click();
		(new VControl(sugar.getHook("portal_navbar_menuitem_logout"), voodoo.auto)).click();
		(new VControl(sugar.getHook("portallogin_button_login"), voodoo.auto)).waitOn();
	}
	
	public static class Cases {
		
		public static void create(Voodoo voodoo, Sugar sugar, Case portalCase) throws Exception {
			Portal.Cases.navigate(voodoo, sugar);
			(new VControl(sugar.getHook("portal_cases_create"), voodoo.auto)).waitOn();
			(new VControl(sugar.getHook("portal_cases_create"), voodoo.auto)).click();
			(new VControl(sugar.getHook("portal_cases_textfield_subject"), voodoo.auto)).waitOn();
			(new VControl(sugar.getHook("portal_cases_textfield_subject"), voodoo.auto)).sendString(portalCase.subject());
			(new VControl(sugar.getHook("portal_cases_button_save"), voodoo.auto)).click();
			(new VControl(sugar.getHook("portal_cases_header"), voodoo.auto)).waitOn();
		}
		
		public static String readFirstCaseSubjectText(Voodoo voodoo, Sugar sugar) throws Exception {
			Portal.Cases.navigate(voodoo, sugar);
			return (new VControl(sugar.getHook("portal_cases_firstcasesubject_link"), voodoo.auto)).getText();
		}
		
		public static void navigate(Voodoo voodoo, Sugar sugar) throws Exception {
			(new VControl(sugar.getHook("portal_navbar_menu_cases"), voodoo.auto)).click();
		}
		
		public static void delete(Voodoo voodoo, Sugar sugar, Case portalCase) throws Exception {
			throw new Exception("deleteCase not yet supported");
		}
	}
}

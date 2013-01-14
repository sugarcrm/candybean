package com.sugarcrm.sugar.portal;


import com.sugarcrm.voodoo.automation.IInterface;
import com.sugarcrm.voodoo.utilities.Utils;
import com.sugarcrm.sugar.Sugar;
import com.sugarcrm.sugar.SugarTest;
import com.sugarcrm.sugar.cases.Case;


public class Portal {
	
	public static void login(Sugar sugar, IInterface iface, String username, String password) throws Exception {
		String sugarURL = Utils.getCascadingPropertyValue(SugarTest.sugarProps, "http://localhost/sugar/", "env.base_url");
		String relPortalURL = Utils.getCascadingPropertyValue(SugarTest.sugarProps, "portal/", "env.portal_rel_url");
		iface.go(sugarURL + relPortalURL);
		iface.getControl(sugar.getHook("portallogin_textfield_username")).sendString(username);
		iface.getControl(sugar.getHook("portallogin_textfield_password")).sendString(password);
		iface.getControl(sugar.getHook("portallogin_button_login")).click();
		iface.getControl(sugar.getHook("portal_navbar_menu_user")).halt(4);
	}
	
	public static void logout(Sugar sugar, IInterface iface) throws Exception {
		iface.getControl(sugar.getHook("portal_navbar_menu_user")).click();
		iface.getControl(sugar.getHook("portal_navbar_menuitem_logout")).click();
		iface.getControl(sugar.getHook("portallogin_button_login")).halt(4);
	}
	
	public static class Cases {
		
		public static void create(Sugar sugar, IInterface iface, Case portalCase) throws Exception {
			Portal.Cases.navigate(sugar, iface);
			iface.getControl(sugar.getHook("portal_cases_create")).halt(4);
			iface.getControl(sugar.getHook("portal_cases_create")).click();
			iface.getControl(sugar.getHook("portal_cases_textfield_subject")).halt(4);
			iface.getControl(sugar.getHook("portal_cases_textfield_subject")).sendString(portalCase.subject());
			iface.getControl(sugar.getHook("portal_cases_button_save")).click();
			iface.getControl(sugar.getHook("portal_cases_header")).halt(4);
		}
		
		public static String readFirstCaseSubjectText(Sugar sugar, IInterface iface) throws Exception {
			Portal.Cases.navigate(sugar, iface);
			return iface.getControl(sugar.getHook("portal_cases_firstcasesubject_link")).getText();
		}
		
		public static void navigate(Sugar sugar, IInterface iface) throws Exception {
			iface.getControl(sugar.getHook("portal_navbar_menu_cases")).click();
		}
		
		public static void delete(Sugar sugar, IInterface iface, Case portalCase) throws Exception {
			throw new Exception("deleteCase not yet supported");
		}
	}
}

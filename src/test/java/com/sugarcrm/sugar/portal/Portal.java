package com.sugarcrm.sugar.portal;

import javax.swing.JOptionPane;

import com.sugarcrm.voodoo.Utils;
import com.sugarcrm.voodoo.Voodoo;
import com.sugarcrm.sugar.Sugar;
import com.sugarcrm.sugar.SugarTest;
import com.sugarcrm.sugar.cases.Case;


public class Portal {
	
	public static void login(Voodoo voodoo, Sugar sugar, String username, String password) throws Exception {
		String sugarURL = Utils.getCascadingPropertyValue(SugarTest.sugarProps, "http://localhost/sugar/", "env.base_url");
		String relPortalURL = Utils.getCascadingPropertyValue(SugarTest.sugarProps, "portal/", "env.portal_rel_url");
		voodoo.go(sugarURL + relPortalURL);
		voodoo.input(sugar.getHook("portallogin_textfield_username"), username);
		voodoo.input(sugar.getHook("portallogin_textfield_password"), password);
		voodoo.click(sugar.getHook("portallogin_button_login"));
		voodoo.pause(400);
	}
	
	public static void logout(Voodoo voodoo, Sugar sugar) throws Exception {
		voodoo.click(sugar.getHook("portal_navbar_menu_user"));
		voodoo.click(sugar.getHook("portal_navbar_menuitem_logout"));
		voodoo.pause(400);
	}
	
	public static class Cases {
		
		public static void create(Voodoo voodoo, Sugar sugar, Case portalCase) throws Exception {
			voodoo.click(sugar.getHook("portal_navbar_menu_cases"));
			voodoo.pause(400);
			voodoo.click(sugar.getHook("portal_cases_create"));
			voodoo.pause(400);
			voodoo.input(sugar.getHook("portal_cases_textfield_subject"), portalCase.subject());
			voodoo.click(sugar.getHook("portal_cases_button_save"));		
			voodoo.pause(400);
		}
		
		public static void read(Voodoo voodoo, Sugar sugar) throws Exception {
			voodoo.click(sugar.getHook("portal_navbar_menu_cases"));
			voodoo.pause(400);
		}
		
		public static void delete(Voodoo voodoo, Sugar sugar, Case portalCase) throws Exception {
			throw new Exception("deleteCase not yet supported");
		}
	}
}

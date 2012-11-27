package com.sugarcrm.sugar;

import java.util.HashMap;
import java.util.Properties;

import com.sugarcrm.voodoo.automation.Utils;
import com.sugarcrm.voodoo.automation.Voodoo;
import com.sugarcrm.voodoo.automation.control.VControl;
import com.sugarcrm.voodoo.automation.control.VHook;


public class Sugar {
	
	private static Sugar instance = null;
	private static HashMap<String, VHook> hooksMap;
	
	private Sugar(Properties hooks) throws Exception {
		Sugar.hooksMap = Utils.getHooks(hooks);
	}

	public static Sugar getInstance(Properties hooks) throws Exception {
		if (Sugar.instance == null) Sugar.instance = new Sugar(hooks); 
		return Sugar.instance;
	}
	
	public VHook getHook(String name) {
		return Sugar.hooksMap.get(name);
	}
	
	public static void login(Voodoo voodoo, Sugar sugar, String username, String password) throws Exception {
		String sugarURL = Utils.getCascadingPropertyValue(SugarTest.sugarProps, "http://localhost/sugar/", "env.base_url");
		voodoo.auto.go(sugarURL);
		(new VControl(sugar.getHook("login_textfield_username"), voodoo.auto)).sendString(username);
		(new VControl(sugar.getHook("login_textfield_password"), voodoo.auto)).sendString(password);
		(new VControl(sugar.getHook("login_button_login"), voodoo.auto)).click();
		(new VControl(sugar.getHook("navbar_menu_user"), voodoo.auto)).waitOn();
	}
	
	public static void logout(Voodoo voodoo, Sugar sugar) throws Exception {
		(new VControl(sugar.getHook("navbar_menu_user"), voodoo.auto)).click();
		(new VControl(sugar.getHook("navbar_menuitem_logout"), voodoo.auto)).click();
		(new VControl(sugar.getHook("login_button_login"), voodoo.auto)).waitOn();
	}
}

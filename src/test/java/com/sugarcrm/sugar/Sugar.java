package com.sugarcrm.sugar;

import java.util.HashMap;
import java.util.Properties;

import com.sugarcrm.voodoo.automation.IInterface;
import com.sugarcrm.voodoo.automation.control.VHook;
import com.sugarcrm.voodoo.utilities.Utils;


public class Sugar {
	
	private static Sugar instance = null;
	private static HashMap<String, VHook> hooksMap;
	
	private Sugar(Properties hooks) throws Exception {
		Sugar.hooksMap = VHook.getHooks(hooks);
	}

	public static Sugar getInstance(Properties hooks) throws Exception {
		if (Sugar.instance == null) Sugar.instance = new Sugar(hooks); 
		return Sugar.instance;
	}
	
	public VHook getHook(String name) {
		return Sugar.hooksMap.get(name);
	}
	
	public static void login(Sugar sugar, IInterface iface, String username, String password) throws Exception {
		String sugarURL = Utils.getCascadingPropertyValue(SugarTest.sugarProps, "http://localhost/sugar/", "env.base_url");
		iface.go(sugarURL);
		iface.getControl(sugar.getHook("login_textfield_username")).sendString(username);
		iface.getControl(sugar.getHook("login_textfield_password")).sendString(password);
		iface.getControl(sugar.getHook("login_button_login")).click();
		iface.getControl(sugar.getHook("navbar_menu_user")).halt(4);
	}
	
	public static void logout(Sugar sugar, IInterface iface) throws Exception {
		iface.getControl(sugar.getHook("navbar_menu_user")).click();
		iface.getControl(sugar.getHook("navbar_menuitem_logout")).click();
		iface.getControl(sugar.getHook("login_button_login")).halt(4);
	}
}

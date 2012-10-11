package com.sugarcrm.sugar;

import java.util.HashMap;
import java.util.Properties;

import com.sugarcrm.voodoo.Utils;
import com.sugarcrm.voodoo.Voodoo;
import com.sugarcrm.voodoo.automation.VHook;


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
		voodoo.input(sugar.getHook("login_textfield_username"), username);
		voodoo.input(sugar.getHook("login_textfield_password"), password);
		voodoo.click(sugar.getHook("login_button_login"));
		voodoo.pause(400);
	}
	
	public static void logout(Voodoo voodoo, Sugar sugar) throws Exception {
		voodoo.click(sugar.getHook("navbar_menu_user"));
		voodoo.click(sugar.getHook("navbar_menuitem_logout"));
		voodoo.pause(400);
	}
}

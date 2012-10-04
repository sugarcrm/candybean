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
	
	public void login(Voodoo voodoo, String username, String password) throws Exception {
		voodoo.input(this.getHook("login_textfield_username"), "admin");
		voodoo.input(this.getHook("login_textfield_password"), "asdf");
		voodoo.click(this.getHook("login_button_login"));
		voodoo.pause(400);
	}
	
	public void logout(Voodoo voodoo) throws Exception {
		voodoo.click(this.getHook("navbar_menu_user"));
		voodoo.click(this.getHook("navbar_menuitem_logout"));
		voodoo.pause(400);
	}
}

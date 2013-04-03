package com.sugarcrm.sugar;

import java.io.File;
import java.util.HashMap;

import com.sugarcrm.sugar.modules.UsersModule.UserRecord;
import com.sugarcrm.voodoo.automation.VInterface;
import com.sugarcrm.voodoo.automation.Voodoo;
import com.sugarcrm.voodoo.automation.control.VHook;
import com.sugarcrm.voodoo.automation.control.VHook.Strategy;
import com.sugarcrm.voodoo.configuration.Configuration;

public class Sugar {
	
	public Voodoo v;
	public VInterface i;
	public UserRecord admin;	
	public Configuration config;
	public Modules modules;
	
	private HashMap<String, VHook> hooksMap;
	
	private static final String curWorkDir = System.getProperty("user.dir");
	private static final String relPropsPath = curWorkDir + File.separator + "src" + File.separator + "test" + File.separator + "resources";
	private static final String sugarPropsPath = relPropsPath + File.separator + "sugar.properties";
	private static final String sugarHooksPath = relPropsPath + File.separator + "sugar.hooks";
	private static String voodooPropsPath;
	
	public Sugar() throws Exception {
		Configuration voodooConfig = new Configuration();
		String voodooPropsFilename = System.getProperty("voodoo_prop_filename");
		if (voodooPropsFilename == null) voodooPropsFilename = "voodoo-mac.properties";
		voodooPropsPath = relPropsPath + File.separator + voodooPropsFilename;
		voodooConfig.load(voodooPropsPath);
    	config = new Configuration();
    	config.load(sugarPropsPath);
		Configuration sugarHooksConfig = new Configuration();
		sugarHooksConfig.load(sugarHooksPath);
		v = Voodoo.getInstance(voodooConfig);
		i = v.getInterface();
		hooksMap = VHook.getHooks(sugarHooksConfig);
		modules = new Modules(this);

		String adminUsername = config.getProperty("sugar.username", "admin");
		String adminPassword1 = config.getProperty("sugar.password1", "asdf");
		String adminPassword2 = config.getProperty("sugar.password2", "asdf");
		String adminName = config.getProperty("sugar.name", "Administrator");
		admin = new UserRecord(adminUsername, adminPassword1, adminPassword2, adminName);
	}

	public VHook getHook(String name) {
		return hooksMap.get(name);
	}
	
	public void login(String username, String password) throws Exception {
		String sugarURL = config.getProperty("env.base_url", "http://localhost/ent670/");
		i.go(sugarURL);
		i.getControl(Strategy.ID, "user_name").sendString(username);
		i.getControl(Strategy.ID, "user_password").sendString(password);
		i.getControl(Strategy.ID, "login_button").click();
	}
	
	public void logout() throws Exception {
		String sugarURL = config.getProperty("env.base_url", "http://localhost/ent670/");
		i.go(sugarURL + "/index.php?module=Users&action=Logout");
	}
}

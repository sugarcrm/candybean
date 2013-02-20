package com.sugarcrm.sugar;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Properties;

import com.sugarcrm.sugar.modules.UsersModule.UserRecord;
import com.sugarcrm.voodoo.automation.VInterface;
import com.sugarcrm.voodoo.automation.Voodoo;
import com.sugarcrm.voodoo.automation.control.VHook;
import com.sugarcrm.voodoo.automation.control.VHook.Strategy;
import com.sugarcrm.voodoo.utilities.Utils;

public class Sugar {
	
	public Voodoo v;
	public VInterface i;
	public UserRecord admin;	
	public Properties props;
	public Modules modules;
	
	private HashMap<String, VHook> hooksMap;
	
	private static final String curWorkDir = System.getProperty("user.dir");
	private static final String relPropsPath = curWorkDir + File.separator + "src" + File.separator + "test" + File.separator + "resources";
	private static final String voodooPropsPath = relPropsPath + File.separator + "voodoo.properties";
	private static final String sugarPropsPath = relPropsPath + File.separator + "sugar.properties";
	private static final String sugarHooksPath = relPropsPath + File.separator + "sugar.hooks";

	public Sugar() throws Exception {
		Properties voodooProps = new Properties();
		voodooProps.load(new FileInputStream(voodooPropsPath));
    	props = new Properties();
    	props.load(new FileInputStream(new File(sugarPropsPath)));
		Properties sugarHooksProps = new Properties();
		sugarHooksProps.load(new FileInputStream(new File(sugarHooksPath)));
		v = Voodoo.getInstance(voodooProps);
		i = v.getInterface();
		hooksMap = VHook.getHooks(sugarHooksProps);
		modules = new Modules(this);

		String adminUsername = Utils.getCascadingPropertyValue(props, "admin", "sugar.username");
		String adminPassword1 = Utils.getCascadingPropertyValue(props, "asdf", "sugar.password1");
		String adminPassword2 = Utils.getCascadingPropertyValue(props, "asdf", "sugar.password2");
		String adminName = Utils.getCascadingPropertyValue(props, "Administrator", "sugar.name");
		admin = new UserRecord(adminUsername, adminPassword1, adminPassword2, adminName);
	}

	public VHook getHook(String name) {
		return hooksMap.get(name);
	}
	
	public void login(String username, String password) throws Exception {
		String sugarURL = Utils.getCascadingPropertyValue(props, "http://localhost/ent670/", "env.base_url");
		i.go(sugarURL);
		i.getControl(Strategy.ID, "user_name").sendString(username);
		i.getControl(Strategy.ID, "user_password").sendString(password);
		i.getControl(Strategy.ID, "login_button").click();
	}
	
	public void logout() throws Exception {
		String sugarURL = Utils.getCascadingPropertyValue(props, "http://localhost/ent670/", "env.base_url");
		i.go(sugarURL + "/index.php?module=Users&action=Logout");
	}
}

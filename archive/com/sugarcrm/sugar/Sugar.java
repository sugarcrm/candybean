/**
 * ====
 *     Candybean is a next generation automation and testing framework suite.
 *     It is a collection of components that foster test automation, execution
 *     configuration, data abstraction, results illustration, tag-based execution,
 *     top-down and bottom-up batches, mobile variants, test translation across
 *     languages, plain-language testing, and web service testing.
 *     Copyright (C) 2013 <candybean@sugarcrm.com>
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * ====
 *
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

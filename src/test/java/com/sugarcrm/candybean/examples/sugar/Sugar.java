/**
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
package com.sugarcrm.candybean.examples.sugar;

import java.util.Map;
import java.util.Properties;

import com.sugarcrm.candybean.automation.Candybean;
import com.sugarcrm.candybean.automation.element.Hook;
import com.sugarcrm.candybean.automation.webdriver.WebDriverInterface;
import com.sugarcrm.candybean.configuration.Configuration;

public class Sugar {
	
	protected Candybean candybean;
	protected WebDriverInterface iface;
	protected Configuration config;
	protected Map<String, Hook> hooks;
	protected SugarUser adminUser;
	
	public Sugar(Candybean candybean, Configuration sugarConfig, Properties sugarHooks, SugarUser adminUser) throws Exception {
		this.candybean = candybean;
		this.iface = candybean.getWebDriverInterface();
		this.config = sugarConfig;
		this.hooks = Hook.getHooks(sugarHooks);
		this.adminUser = adminUser;
//		model = buildModel();
	}
	
	public void login() throws Exception {
		String urlBase = config.getValue("url.base", "http://localhost/ent700/");
		iface.go(urlBase);
		iface.getWebDriverElement(hooks.get("login.textfield.username")).sendString(adminUser.getBuilder().getRequiredAttributes().get("username"));
		iface.getWebDriverElement(hooks.get("login.textfield.password")).sendString(adminUser.getBuilder().getRequiredAttributes().get("password"));
		iface.getWebDriverElement(hooks.get("login.button.login")).click();
	}
	
	public void logout() throws Exception {
		iface.getWebDriverElement(hooks.get("main.menu.user")).click();
		iface.getWebDriverElement(hooks.get("main.link.logout")).click();
	}
	
	public void stop() throws Exception {
		iface.stop();		
	}
}	

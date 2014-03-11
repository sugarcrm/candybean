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
package com.sugarcrm.candybean.examples.yelp;

import com.sugarcrm.candybean.automation.webdriver.WebDriverInterface;
import com.sugarcrm.candybean.automation.element.Hook.Strategy;
import com.sugarcrm.candybean.model.Model;

public class YelpHooks {
	
	private WebDriverInterface i;
	private YelpUser user;
	private Model model;
	
	public YelpHooks(WebDriverInterface i, YelpUser user) {
		this.i = i;
		this.user = user;
	}
	
	public void start() throws Exception {
		String urlBase = "http://www.yelp.com/";
		this.i.go(urlBase);
	}
	
	public void login() throws Exception {
		this.i.getWebDriverElement(Strategy.LINK, "Log In").click();
		String loginUrl = "https://www.yelp.com/login";
		this.i.getWebDriverElement(Strategy.NAME, "email").sendString(user.email());
		this.i.getWebDriverElement(Strategy.NAME, "password").sendString(user.password());
		this.i.getWebDriverElement(Strategy.NAME, "action_submit").click();
	}
	
	public void logout() throws Exception {
		String mainUrlBase = "http://www.yelp.com/";
		this.i.getWebDriverElement(Strategy.ID, "topbar-account-link").click();
		this.i.getWebDriverElement(Strategy.LINK, "Log Out").click();
	}
	
	public void stop() throws Exception {
		String finalUrl = "http://www.yelp.com/";
	}
}	

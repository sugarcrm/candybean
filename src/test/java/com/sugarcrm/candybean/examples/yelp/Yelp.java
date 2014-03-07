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

import java.util.Map;
import java.util.Properties;
import java.util.Set;

import com.sugarcrm.candybean.automation.webdriver.WebDriverInterface;
import com.sugarcrm.candybean.automation.element.Hook;
import com.sugarcrm.candybean.automation.element.Hook.Strategy;
import com.sugarcrm.candybean.exceptions.CandybeanException;
import com.sugarcrm.candybean.model.IModel;
import com.sugarcrm.candybean.model.Model;
import com.sugarcrm.candybean.model.Page;

public class Yelp implements IModel {
	
	public Model model;

	private WebDriverInterface i;
	private Map<String, Hook> hooks;
	private YelpUser defaultUser;
	
	public Yelp(WebDriverInterface i, Properties yelpHooks, YelpUser defaultUser) throws CandybeanException {
		this.i = i;
		hooks = Hook.getHooks(yelpHooks);
		this.defaultUser = defaultUser;
//		model = buildModel();
	}
	
	public void start() throws CandybeanException {
		String urlBase = "http://www.yelp.com/";
		i.go(urlBase);
	}
	
	public void login() throws CandybeanException {
		i.getWebDriverElement(Strategy.LINK, "Log In").click();
		String loginUrl = "https://www.yelp.com/login";
		i.getWebDriverElement(hooks.get("login.textfield.email")).sendString(defaultUser.email());
		i.getWebDriverElement(hooks.get("login.textfield.password")).sendString(defaultUser.password());
		i.getWebDriverElement(hooks.get("login.button.login")).click();
	}
	
	public void logout() throws CandybeanException {
		String mainUrlBase = "http://www.yelp.com/";
		i.getWebDriverElement(hooks.get("main.link.account")).click();
		i.getWebDriverElement(hooks.get("main.link.logout")).click();
	}
	
	public void stop() throws CandybeanException {
		String finalUrl = "http://www.yelp.com/";
	}
	
	public void run(int timeout_in_minutes) {
		long startTime = System.currentTimeMillis();
		long lap = System.currentTimeMillis();
	}

	@Override
	public Set<Page> getStartPages() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void executeRandomStartPage() {
		// TODO Auto-generated method stub
	}
	
//	private Model buildModel() {
//		Page startPage = new Page();
//		Page loginPage = new Page();
//		Page mainPage = new Page();
//		
//		startPage.addLink(loginPage);
//		Model yelpModel = new Model("http://www.yelp.com/");
//		
//		return yelpModel;
//	}
}	

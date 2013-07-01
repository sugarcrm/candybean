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
package com.sugarcrm.sugar.modules;

import com.sugarcrm.sugar.Sugar;
import com.sugarcrm.voodoo.automation.control.VHook.Strategy;

/**
 * @author Conrad Warmbold
 *
 */
public class AccountsModule {
	
	private final Sugar sugar;
	
	public AccountsModule(Sugar sugar) { this.sugar = sugar; }
	
	public void createAccount(AccountRecord account) throws Exception {
		sugar.i.getControl(Strategy.ID, "moduleTab_AllAccounts").hover();
		sugar.i.getControl(Strategy.PLINK, "Create Account").click();
		sugar.i.getControl(Strategy.ID, "phone_office_label").hover();
		sugar.i.getControl(Strategy.ID, "name").sendString(account.name);
		sugar.i.getControl(Strategy.ID, "SAVE_HEADER").click();
//		sugar.i.getControl(Strategy.ID, "edit_button").halt(4);
//		sugar.i.getControl(Strategy.ID, "moduleTab_AllHome").click();
	}
	
	public void deleteAllAccounts() throws Exception {
		sugar.i.getControl(Strategy.ID, "moduleTab_AllAccounts").hover();
		sugar.i.getControl(Strategy.ID, "massall_top").click();
		sugar.i.getControl(Strategy.ID, "delete_listview_top").click();
		sugar.i.acceptDialog();
		sugar.i.getControl(Strategy.ID, "moduleTab_AllHome").click();
	}
	
	public void editAccount(AccountRecord oldAccount, AccountRecord newAccount) throws Exception {
		this.searchAccounts(oldAccount.name);
		sugar.i.getControl(Strategy.PLINK, oldAccount.name).click();
		sugar.i.pause(800); // click doesn't always fully depress; adding pause to hopefully add consistency
		sugar.i.getControl(Strategy.ID, "edit_button").click();
		sugar.i.getControl(Strategy.CSS, "html.yui3-js-enabled body.yui-skin-sam div#main div#content table#contentTable tbody tr td form#EditView div#EditView_tabs div div#detailpanel_1.edit table#LBL_ACCOUNT_INFORMATION.edit tbody tr td input#name").sendString(newAccount.name);
//		sugar.i.getControl(Strategy.XPATH, "/html/body/div[5]/div/table/tbody/tr/td/form/div/div/div/table/tbody/tr/td[2]/input").sendString(newAccount.name);
		sugar.i.getControl(Strategy.ID, "SAVE_HEADER").click();
//		sugar.i.getControl(Strategy.ID, "edit_button").halt(4);
//		sugar.i.getControl(Strategy.ID, "moduleTab_AllHome").click();
	}
	
	public void searchAccounts(String search) throws Exception {
//		sugar.i.interact("About to click all accounts...");
		sugar.i.getControl(Strategy.ID, "moduleTab_AllAccounts").click();
//		sugar.i.interact("About to send search string...");
		sugar.i.getControl(Strategy.ID, "name_basic").sendString(search);
//		sugar.i.interact("About to click search...");
		sugar.i.getControl(Strategy.ID, "search_form_submit").click();
	}
	
	public static class AccountRecord {
		
		public String name = null;

		public AccountRecord(String name) {
			this.name = name;
		}
	}
}
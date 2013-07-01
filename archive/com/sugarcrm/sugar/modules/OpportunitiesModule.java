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
import com.sugarcrm.sugar.modules.AccountsModule.AccountRecord;
import com.sugarcrm.voodoo.automation.control.VHook.Strategy;

/**
 * @author Conrad Warmbold
 *
 */
public class OpportunitiesModule {
	
	private final Sugar sugar;
	
	public OpportunitiesModule(Sugar sugar) { this.sugar = sugar; }
	
	public void createOpportunity(OpportunityRecord opportunity) throws Exception {
		sugar.i.getControl(Strategy.ID, "moduleTab_AllOpportunities").hover();
		sugar.i.getControl(Strategy.PLINK, "Create Opportunity").click();
		sugar.i.getControl(Strategy.ID, "account_name_label").hover();
		sugar.i.getControl(Strategy.ID, "name").sendString(opportunity.name);
		sugar.i.getControl(Strategy.ID, "btn_account_name").click();
		sugar.i.focusWindow(1);
		sugar.i.getControl(Strategy.PLINK, opportunity.account.name).click();
		sugar.i.focusWindow(0);
		sugar.i.getControl(Strategy.ID, "date_closed").sendString(opportunity.closeDate);
		sugar.i.getControl(Strategy.ID, "amount").sendString(opportunity.amount);
		sugar.i.getControl(Strategy.ID, "SAVE_HEADER").click();
//		sugar.i.getControl(Strategy.ID, "edit_button").halt(4);
//		sugar.i.getControl(Strategy.ID, "moduleTab_AllHome").click();
	}
	
	public void deleteAllOpportunities() throws Exception {
		sugar.i.getControl(Strategy.ID, "moduleTab_AllOpportunities").click();
		sugar.i.getControl(Strategy.ID, "massall_top").click();
		sugar.i.getControl(Strategy.ID, "delete_listview_top").click();
		sugar.i.acceptDialog();
		sugar.i.getControl(Strategy.ID, "moduleTab_AllHome").click();
	}
	
	public void editOpportunity(OpportunityRecord oldOpportunity, OpportunityRecord newOpportunity) throws Exception {
		this.searchOpportunities(oldOpportunity.name);
		sugar.i.getControl(Strategy.PLINK, oldOpportunity.name).click();
		sugar.i.getControl(Strategy.ID, "edit_button").click();
		sugar.i.getControl(Strategy.ID, "name").sendString(newOpportunity.name);
		sugar.i.getControl(Strategy.ID, "btn_account_name").click();
		sugar.i.focusWindow(1);
		sugar.i.getControl(Strategy.PLINK, newOpportunity.account.name).click();
		sugar.i.focusWindow(0);
		sugar.i.getControl(Strategy.ID, "date_closed").sendString(newOpportunity.closeDate);
		sugar.i.getControl(Strategy.ID, "amount").sendString(newOpportunity.amount);
		sugar.i.getControl(Strategy.ID, "SAVE_HEADER").click();
//		sugar.i.getControl(Strategy.ID, "edit_button").halt(4);
//		sugar.i.getControl(Strategy.ID, "moduleTab_AllHome").click();
	}
	
	public void searchOpportunities(String search) throws Exception {
		sugar.i.getControl(Strategy.ID, "moduleTab_AllOpportunities").click();
		sugar.i.getControl(Strategy.ID, "name_basic").sendString(search);
		sugar.i.getControl(Strategy.ID, "search_form_submit").click();
	}
	
	public static class OpportunityRecord {
		
		public String name = null;
		public AccountRecord account = null;
		public String closeDate = null;
		public String amount = null;

		public OpportunityRecord(String name, AccountRecord account, String closeDate, String amount) {
			this.name = name;
			this.account = account;
			this.closeDate = closeDate;
			this.amount = amount;
		}
	}
}
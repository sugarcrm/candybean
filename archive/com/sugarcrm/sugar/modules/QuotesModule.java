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
public class QuotesModule {
	
	private final Sugar sugar;
	
	public QuotesModule(Sugar sugar) { this.sugar = sugar; }
	
	public void createQuote(QuoteRecord quote) throws Exception {
		sugar.i.getControl(Strategy.ID, "moduleTabExtraMenuAll").click();
		sugar.i.getControl(Strategy.PLINK, "Quotes").hover();
		sugar.i.getControl(Strategy.PLINK, "Create Quote").click();
		sugar.i.getControl(Strategy.ID, "opportunity_name_label").hover();
		sugar.i.getControl(Strategy.ID, "name").sendString(quote.subject);
		sugar.i.getControl(Strategy.ID, "date_quote_expected_closed").sendString(quote.closeDate);
		sugar.i.getControl(Strategy.ID, "btn_billing_account_name").click();
		sugar.i.focusWindow(1);
		sugar.i.getControl(Strategy.PLINK, quote.billingAccount.name).click();
		sugar.i.focusWindow(0);
		sugar.i.getControl(Strategy.ID, "SAVE_HEADER").click();
//		sugar.i.getControl(Strategy.ID, "edit_button").halt(4);
//		sugar.i.getControl(Strategy.ID, "moduleTab_AllHome").click();
	}
	
	public void deleteAllQuotes() throws Exception {
		sugar.i.getControl(Strategy.ID, "moduleTabExtraMenuAll").click();
		sugar.i.getControl(Strategy.PLINK, "Quotes").click();
		sugar.i.getControl(Strategy.ID, "massall_top").click();
		sugar.i.getControl(Strategy.ID, "delete_listview_top").click();
		sugar.i.acceptDialog();
		sugar.i.getControl(Strategy.ID, "moduleTab_AllHome").click();
	}
	
	public void editQuote(QuoteRecord oldQuote, QuoteRecord newQuote) throws Exception {
		this.searchQuotes(oldQuote.subject);
		sugar.i.getControl(Strategy.PLINK, oldQuote.subject).click();
		sugar.i.getControl(Strategy.ID, "edit_button").click();
		sugar.i.getControl(Strategy.ID, "name").sendString(newQuote.subject);
		sugar.i.getControl(Strategy.ID, "date_quote_expected_closed").sendString(newQuote.closeDate);
		sugar.i.getControl(Strategy.ID, "btn_billing_account_name").click();
		sugar.i.focusWindow(1);
		sugar.i.getControl(Strategy.PLINK, newQuote.billingAccount.name).click();
		sugar.i.focusWindow(0);
		sugar.i.getControl(Strategy.ID, "SAVE_HEADER").click();
//		sugar.i.getControl(Strategy.ID, "edit_button").halt(4);
//		sugar.i.getControl(Strategy.ID, "moduleTab_AllHome").click();
	}
	
	public void searchQuotes(String search) throws Exception {
		sugar.i.getControl(Strategy.ID, "moduleTabExtraMenuAll").click();
		sugar.i.getControl(Strategy.PLINK, "Quotes").click();
		sugar.i.getControl(Strategy.ID, "name_basic").sendString(search);
		sugar.i.getControl(Strategy.ID, "search_form_submit").click();
	}
	
	public static class QuoteRecord {
		
		public String subject = null;
		public String closeDate = null;
		public AccountRecord billingAccount = null;

		public QuoteRecord(String subject, String closeDate, AccountRecord billingAccount) {
			this.subject = subject;
			this.closeDate = closeDate;
			this.billingAccount = billingAccount;
		}
	}
}
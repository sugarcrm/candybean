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
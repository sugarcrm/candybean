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
		sugar.i.focusByIndex(1);
		sugar.i.getControl(Strategy.PLINK, opportunity.account.name).click();
		sugar.i.focusByIndex(0);
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
		sugar.i.focusByIndex(1);
		sugar.i.getControl(Strategy.PLINK, newOpportunity.account.name).click();
		sugar.i.focusByIndex(0);
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
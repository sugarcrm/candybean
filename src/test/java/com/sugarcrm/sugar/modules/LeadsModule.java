package com.sugarcrm.sugar.modules;

import com.sugarcrm.sugar.Sugar;
import com.sugarcrm.voodoo.automation.control.VHook.Strategy;

/**
 * @author Conrad Warmbold
 *
 */
public class LeadsModule {
	
	private final Sugar sugar;
	
	public LeadsModule(Sugar sugar) { this.sugar = sugar; }
	
	public void createLead(LeadRecord lead) throws Exception {
		sugar.i.getControl(Strategy.ID, "moduleTab_AllLeads").hover();
		sugar.i.getControl(Strategy.PLINK, "Create Lead").click();
		sugar.i.getControl(Strategy.ID, "phone_work_label").hover();
		sugar.i.getControl(Strategy.ID, "last_name").sendString(lead.lastName);
		sugar.i.getControl(Strategy.ID, "SAVE_HEADER").click();
//		sugar.i.getControl(Strategy.ID, "edit_button").halt(4);
//		sugar.i.getControl(Strategy.ID, "moduleTab_AllHome").click();
	}
	
	public void deleteAllLeads() throws Exception {
		sugar.i.getControl(Strategy.ID, "moduleTab_AllLeads").click();
		sugar.i.getControl(Strategy.ID, "massall_top").click();
		sugar.i.getControl(Strategy.ID, "delete_listview_top").click();
		sugar.i.acceptDialog();
		sugar.i.getControl(Strategy.ID, "moduleTab_AllHome").click();
	}
	
	public void editLead(LeadRecord oldLead, LeadRecord newLead) throws Exception {
		this.searchLeads(oldLead.lastName);
		sugar.i.getControl(Strategy.PLINK, oldLead.lastName).click();
		sugar.i.getControl(Strategy.ID, "edit_button").click();
		sugar.i.getControl(Strategy.ID, "last_name").sendString(newLead.lastName);
		sugar.i.getControl(Strategy.ID, "SAVE_HEADER").click();
//		sugar.i.getControl(Strategy.ID, "edit_button").halt(4);
//		sugar.i.getControl(Strategy.ID, "moduleTab_AllHome").click();
	}
	
	public void searchLeads(String search) throws Exception {
		sugar.i.getControl(Strategy.ID, "moduleTab_AllLeads").click();
		sugar.i.getControl(Strategy.ID, "name_basic").sendString(search);
		sugar.i.getControl(Strategy.ID, "search_form_submit").click();
	}
	
	public static class LeadRecord {
		
		public String lastName = null;

		public LeadRecord(String lastName) {
			this.lastName = lastName;
		}
	}
}
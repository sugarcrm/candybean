package com.sugarcrm.sugar.modules;

import com.sugarcrm.sugar.Sugar;
import com.sugarcrm.voodoo.automation.control.VHook.Strategy;

/**
 * @author Conrad Warmbold
 *
 */
public class CampaignsModule {
	
	private final Sugar sugar;
	
	public CampaignsModule(Sugar sugar) { this.sugar = sugar; }
	
	public void createCampaign(CampaignRecord campaign) throws Exception {
		sugar.i.getControl(Strategy.ID, "moduleTabExtraMenuAll").click();
		sugar.i.getControl(Strategy.PLINK, "Campaigns").hover();
		sugar.i.getControl(Strategy.PLINK, "Classic").click();
		sugar.i.getControl(Strategy.ID, "status_label").hover();
		sugar.i.getControl(Strategy.ID, "name").sendString(campaign.name);
		sugar.i.getSelect(Strategy.ID, "status").select(campaign.status);
		sugar.i.getSelect(Strategy.ID, "campaign_type").select(campaign.type);
		sugar.i.getControl(Strategy.ID, "end_date").sendString(campaign.endDate);
		sugar.i.getControl(Strategy.ID, "SAVE_HEADER").click();
//		sugar.i.getControl(Strategy.ID, "moduleTab_AllHome").click();
	}
	
	public void deleteAllCampaigns() throws Exception {
		sugar.i.getControl(Strategy.ID, "moduleTabExtraMenuAll").click();
		sugar.i.getControl(Strategy.PLINK, "Campaigns").click();
		sugar.i.getControl(Strategy.ID, "massall_top").click();
		sugar.i.getControl(Strategy.ID, "delete_listview_top").click();
		sugar.i.acceptDialog();
		sugar.i.getControl(Strategy.ID, "moduleTab_AllHome").click();
	}
	
	public void editCampaign(CampaignRecord oldCampaign, CampaignRecord newCampaign) throws Exception {
		this.searchCampaigns(oldCampaign.name);
		sugar.i.getControl(Strategy.PLINK, oldCampaign.name).click();
		sugar.i.getControl(Strategy.ID, "edit_button").click();
		sugar.i.getControl(Strategy.ID, "name").sendString(newCampaign.name);
		sugar.i.getControl(Strategy.ID, "SAVE_HEADER").click();
//		sugar.i.getControl(Strategy.ID, "edit_button").halt(4);
//		sugar.i.getControl(Strategy.ID, "moduleTab_AllHome").click();
	}
	
	public void searchCampaigns(String search) throws Exception {
		sugar.i.getControl(Strategy.ID, "moduleTabExtraMenuAll").click();
		sugar.i.getControl(Strategy.PLINK, "Campaigns").click();
		sugar.i.getControl(Strategy.ID, "name_basic").sendString(search);
		sugar.i.getControl(Strategy.ID, "search_form_submit").click();
	}
	
	public static class CampaignRecord {
		
		public String name = null;
		public String status = null;
		public String type = null;
		public String endDate = null;

		public CampaignRecord(String name, String status, String type, String endDate) {
			this.name = name;
			this.status = status;
			this.type = type;
			this.endDate = endDate;
		}
	}
}
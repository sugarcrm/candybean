package com.sugarcrm.sugar.modules;

import com.sugarcrm.sugar.Sugar;
import com.sugarcrm.voodoo.automation.control.VHook.Strategy;

/**
 * @author Conrad Warmbold
 *
 */
public class EmailsModule {
	
	private final Sugar sugar;
	
	public EmailsModule(Sugar sugar) { this.sugar = sugar; }
	
	public void createEmailTemplate(EmailTemplateRecord emailTemplate) throws Exception {
		sugar.i.getControl(Strategy.ID, "moduleTabExtraMenuAll").click();
		sugar.i.getControl(Strategy.PLINK, "Emails").hover();
		sugar.i.getControl(Strategy.PLINK, "Create Email Template").click();
		sugar.i.getControl(Strategy.ID, "lineLabel_team_name_primary").hover();
		sugar.i.getControl(Strategy.ID, "name").sendString(emailTemplate.name);
		sugar.i.getControl(Strategy.ID, "SAVE").click();
//		sugar.i.getControl(Strategy.ID, "editEmailTemplatesButton").halt(4);
//		sugar.i.getControl(Strategy.ID, "moduleTab_AllHome").click();
	}
	
	public void deleteAllEmailTemplates() throws Exception {
		sugar.i.getControl(Strategy.ID, "moduleTabExtraMenuAll").click();
		sugar.i.getControl(Strategy.PLINK, "Emails").click();
		sugar.i.getControl(Strategy.ID, "massall_top").click();
		sugar.i.getControl(Strategy.ID, "delete_listview_top").click();
		sugar.i.acceptDialog();
		sugar.i.getControl(Strategy.ID, "moduleTab_AllHome").click();
	}
	
	public void editEmailTemplate(EmailTemplateRecord oldEmailTemplate, EmailTemplateRecord newEmailTemplate) throws Exception {
		this.searchEmailTemplates(oldEmailTemplate.name);
		sugar.i.getControl(Strategy.PLINK, oldEmailTemplate.name).click();
		sugar.i.getControl(Strategy.ID, "edit_button").click();
		sugar.i.getControl(Strategy.ID, "name").sendString(newEmailTemplate.name);
		sugar.i.getControl(Strategy.ID, "SAVE").click();
//		sugar.i.getControl(Strategy.ID, "editEmailTemplatesButton").halt(4);
//		sugar.i.getControl(Strategy.ID, "moduleTab_AllHome").click();
	}
	
	public void searchEmailTemplates(String search) throws Exception {
		sugar.i.getControl(Strategy.ID, "moduleTabExtraMenuAll").click();
		sugar.i.getControl(Strategy.PLINK, "Emails").click();
		sugar.i.getControl(Strategy.ID, "name_basic").sendString(search);
		sugar.i.getControl(Strategy.ID, "search_form_submit").click();
	}
	
	public static class EmailTemplateRecord {
		
		public String name = null;

		public EmailTemplateRecord(String name) {
			this.name = name;
		}
	}
}
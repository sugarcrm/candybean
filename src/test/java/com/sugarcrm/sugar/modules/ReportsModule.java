package com.sugarcrm.sugar.modules;

import com.sugarcrm.sugar.Sugar;
import com.sugarcrm.voodoo.automation.control.VHook.Strategy;

/**
 * @author Conrad Warmbold
 *
 */
public class ReportsModule {
	
	private final Sugar sugar;
	
	public ReportsModule(Sugar sugar) { this.sugar = sugar; }
	
	public void createReport(ReportRecord report) throws Exception {
		sugar.i.getControl(Strategy.ID, "moduleTab_AllReports").hover();
		sugar.i.getControl(Strategy.PLINK, "Create Report").click();
		sugar.i.getControl(Strategy.XPATH, "/html/body/div[5]/div/table/tbody/tr/td/form/div[2]/table/tbody/tr[2]/td[3]/table/tbody/tr/td[3]/h3").hover();
		throw new Exception("createReport not yet implemented.");
//		sugar.i.getControl(Strategy.ID, "name").sendString(caseRecord.subject);
//		sugar.i.getControl(Strategy.ID, "btn_account_name").click();
//		sugar.i.focusByIndex(1);
//		sugar.i.getControl(Strategy.PLINK, caseRecord.account.name).click();
//		sugar.i.focusByIndex(0);
//		sugar.i.getControl(Strategy.ID, "SAVE_HEADER").click();
//		sugar.i.getControl(Strategy.ID, "edit_button").halt(4);
//		sugar.i.getControl(Strategy.ID, "moduleTab_AllHome").click();
	}
	
	public void deleteAllReports() throws Exception {
		sugar.i.getControl(Strategy.ID, "moduleTab_AllReports").click();
		sugar.i.getControl(Strategy.ID, "basic_search_link").click();
		sugar.i.getControl(Strategy.ID, "massall_top").click();
		sugar.i.getControl(Strategy.ID, "delete_listview_top").click();
		sugar.i.acceptDialog();
		sugar.i.getControl(Strategy.ID, "moduleTab_AllHome").click();
	}
	
	public void editReport(ReportRecord oldReport, ReportRecord newReport) throws Exception {
		this.searchReports(oldReport.title);
		sugar.i.getControl(Strategy.PLINK, oldReport.title).click();
		sugar.i.getControl(Strategy.ID, "edit_button").click();
		throw new Exception("editReport not yet implemented.");
//		sugar.i.getControl(Strategy.ID, "name").sendString(newReport.name);
//		sugar.i.getControl(Strategy.ID, "SAVE_HEADER").click();
//		sugar.i.getControl(Strategy.ID, "edit_button").halt(4);
//		sugar.i.getControl(Strategy.ID, "moduleTab_AllHome").click();
	}
	
	public void searchReports(String titleSearch) throws Exception {
		sugar.i.getControl(Strategy.ID, "moduleTab_AllReports").click();
		sugar.i.getControl(Strategy.NAME, "name").sendString(titleSearch);
		sugar.i.getControl(Strategy.ID, "search_form_submit_advanced").click();
	}
	
	public static class ReportRecord {
		
		public String title = null;
		
		public ReportRecord(String title) {
			this.title = title;
		}
	}
}
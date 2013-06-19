package com.sugarcrm.sugar.modules;

import com.sugarcrm.sugar.Sugar;
import com.sugarcrm.voodoo.automation.control.VHook.Strategy;

/**
 * @author Conrad Warmbold
 *
 */
public class CallsModule {
	
	private final Sugar sugar;
	
	public CallsModule(Sugar sugar) { this.sugar = sugar; }
	
	public void editCall(CallRecord oldCall, CallRecord newCall) throws Exception {
		this.searchCalls(oldCall.subject);
		sugar.i.getControl(Strategy.PLINK, oldCall.subject).click();
		sugar.i.getControl(Strategy.ID, "edit_button").click();
		sugar.i.getControl(Strategy.ID, "name").sendString(newCall.subject);
		sugar.i.getControl(Strategy.ID, "SAVE_HEADER").click();
//		sugar.i.getControl(Strategy.ID, "edit_button").halt(4);
//		sugar.i.getControl(Strategy.ID, "moduleTab_AllHome").click();
	}
	
	public void deleteAllCalls() throws Exception {
		sugar.i.getControl(Strategy.ID, "moduleTab_AllCalendar").hover();
		sugar.i.getControl(Strategy.PLINK, "Schedule Call").click();
		sugar.i.getControl(Strategy.ID, "moduleTab_AllCalls").click();
		sugar.i.getControl(Strategy.ID, "massall_top").click();
		sugar.i.getControl(Strategy.ID, "delete_listview_top").click();
		sugar.i.acceptDialog();
		sugar.i.getControl(Strategy.ID, "moduleTab_AllHome").click();
	}
	
	public void scheduleCall(CallRecord call) throws Exception {
		sugar.i.getControl(Strategy.ID, "moduleTab_AllCalendar").hover();
		sugar.i.getControl(Strategy.PLINK, "Schedule Call").click();
		sugar.i.getControl(Strategy.ID, "status_label").hover(); // to reset hover
		sugar.i.getControl(Strategy.ID, "name").sendString(call.subject);
		sugar.i.getControl(Strategy.ID, "SAVE_HEADER").click();
//		sugar.i.getControl(Strategy.ID, "moduleTab_AllHome").click();
	}
	
	public void searchCalls(String search) throws Exception {
		String sugarURL = sugar.config.getValue("env.base_url", "http://localhost/ent670/");
		sugar.i.go(sugarURL + "/index.php?module=Calls&action=ListView");
		sugar.i.getControl(Strategy.ID, "name_basic").sendString(search);
		sugar.i.getControl(Strategy.ID, "search_form_submit").click();
	}
	
	public static class CallRecord {
		
		public String subject = null;
//		public String startDate = null;

		public CallRecord(String subject) {
			this.subject = subject;
//			this.startDate = startDate;
		}
	}
}
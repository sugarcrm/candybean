package com.sugarcrm.sugar.modules;

import com.sugarcrm.sugar.Sugar;
import com.sugarcrm.voodoo.automation.control.VHook.Strategy;

/**
 * @author Conrad Warmbold
 *
 */
public class MeetingsModule {
	
	private final Sugar sugar;
	
	public MeetingsModule(Sugar sugar) { this.sugar = sugar; }
	
	public void deleteAllMeetings() throws Exception {
		sugar.i.getControl(Strategy.ID, "moduleTab_AllAccounts").hover();
		sugar.i.getControl(Strategy.ID, "massall_top").click();
		sugar.i.getControl(Strategy.ID, "delete_listview_top").click();
		sugar.i.acceptDialog();
		sugar.i.getControl(Strategy.ID, "moduleTab_AllHome").click();
	}
	
	public void editMeeting(MeetingRecord oldMeeting, MeetingRecord newMeeting) throws Exception {
		this.searchMeetings(oldMeeting.subject);
		sugar.i.getControl(Strategy.PLINK, oldMeeting.subject).click();
		sugar.i.getControl(Strategy.ID, "edit_button").click();
		sugar.i.getControl(Strategy.ID, "name").sendString(newMeeting.subject);
		sugar.i.getControl(Strategy.ID, "SAVE_HEADER").click();
//		sugar.i.getControl(Strategy.ID, "moduleTab_AllHome").click();
	}
	
	public void scheduleMeeting(MeetingRecord meeting) throws Exception {
		sugar.i.getControl(Strategy.ID, "moduleTab_AllCalendar").hover();
		sugar.i.getControl(Strategy.PLINK, "Schedule Meeting").click();
		sugar.i.getControl(Strategy.ID, "status_label").hover(); // to reset hover
		sugar.i.getControl(Strategy.ID, "name").sendString(meeting.subject);
//		sugar.i.getControl(Strategy.ID, "date_start_date").sendString(meeting.startDate);
//		sugar.i.getControl(Strategy.ID, "date_end_date").sendString(meeting.endDate);
		sugar.i.getControl(Strategy.ID, "SAVE_HEADER").click();
//		sugar.i.getControl(Strategy.ID, "moduleTab_AllHome").click();
	}
	
	public void searchMeetings(String search) throws Exception {
		String sugarURL = sugar.config.getProperty("env.base_url", "http://localhost/ent670/");
		sugar.i.go(sugarURL + "/index.php?module=Meetings&action=ListView");
		sugar.i.getControl(Strategy.ID, "name_basic").sendString(search);
		sugar.i.getControl(Strategy.ID, "search_form_submit").click();
	}
	
	public static class MeetingRecord {
		
		public String subject = null;
//		public String startDate = null;
//		public String endDate = null;

		public MeetingRecord(String subject) {
			this.subject = subject;
//			this.startDate = startDate;
//			this.endDate = endDate;
		}
	}
}
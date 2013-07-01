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
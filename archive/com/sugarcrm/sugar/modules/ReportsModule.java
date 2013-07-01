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
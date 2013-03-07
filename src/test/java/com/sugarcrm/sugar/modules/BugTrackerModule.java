package com.sugarcrm.sugar.modules;

import com.sugarcrm.sugar.Sugar;
import com.sugarcrm.voodoo.automation.control.VHook.Strategy;
import com.sugarcrm.voodoo.utilities.Utils;

/**
 * @author Conrad Warmbold
 *
 */
public class BugTrackerModule {
	
	private final Sugar sugar;
	
	public BugTrackerModule(Sugar sugar) { this.sugar = sugar; }
	
	public void reportBug(BugRecord bug) throws Exception {
		String sugarURL = Utils.getCascadingPropertyValue(sugar.props, "http://localhost/ent670/", "env.base_url");
		sugar.i.go(sugarURL + "/index.php?module=Bugs&action=EditView");
		sugar.i.getControl(Strategy.ID, "name").sendString(bug.subject);
		sugar.i.getControl(Strategy.ID, "SAVE_HEADER").click();
//		sugar.i.getControl(Strategy.ID, "moduleTab_AllHome").click();
	}
	
	public void deleteAllBugs() throws Exception {
		String sugarURL = Utils.getCascadingPropertyValue(sugar.props, "http://localhost/ent670/", "env.base_url");
		sugar.i.go(sugarURL + "/index.php?module=Bugs&action=ListView");
		sugar.i.getControl(Strategy.ID, "massall_top").click();
		sugar.i.getControl(Strategy.ID, "delete_listview_top").click();
		sugar.i.acceptDialog();
		sugar.i.getControl(Strategy.ID, "moduleTab_AllHome").click();
	}
	
	public void editBug(BugRecord oldBug, BugRecord newBug) throws Exception {
		this.searchBugTracker(oldBug.subject);
		sugar.i.getControl(Strategy.PLINK, oldBug.subject).click();
		sugar.i.getControl(Strategy.ID, "edit_button").click();
		sugar.i.getControl(Strategy.ID, "name").sendString(newBug.subject);
		sugar.i.getControl(Strategy.ID, "SAVE_HEADER").click();
//		sugar.i.getControl(Strategy.ID, "edit_button").halt(4);
//		sugar.i.getControl(Strategy.ID, "moduleTab_AllHome").click();
	}
	
	public void searchBugTracker(String search) throws Exception {
		String sugarURL = Utils.getCascadingPropertyValue(sugar.props, "http://localhost/ent670/", "env.base_url");
		sugar.i.go(sugarURL + "/index.php?module=Bugs&action=ListView");
		sugar.i.getControl(Strategy.ID, "name_basic").sendString(search);
		sugar.i.getControl(Strategy.ID, "search_form_submit").click();
	}
	
	public static class BugRecord {
		
		public String subject = null;

		public BugRecord(String subject) {
			this.subject = subject;
		}
	}
}
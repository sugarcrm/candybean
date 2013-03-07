package com.sugarcrm.sugar.modules;

import com.sugarcrm.sugar.Sugar;
import com.sugarcrm.voodoo.automation.control.VHook.Strategy;
import com.sugarcrm.voodoo.utilities.Utils;

/**
 * @author Conrad Warmbold
 *
 */
public class TasksModule {
	
	private final Sugar sugar;
	
	public TasksModule(Sugar sugar) { this.sugar = sugar; }
	
	public void createTask(TaskRecord task) throws Exception {
		sugar.i.getControl(Strategy.ID, "moduleTab_AllCalendar").hover();
		sugar.i.getControl(Strategy.PLINK, "Create Task").click();
		sugar.i.getControl(Strategy.ID, "status_label").hover(); // to reset hover
		sugar.i.getControl(Strategy.ID, "name").sendString(task.subject);
		sugar.i.getControl(Strategy.ID, "SAVE_HEADER").click();
//		sugar.i.getControl(Strategy.ID, "edit_button").halt(4);
//		sugar.i.getControl(Strategy.ID, "moduleTab_AllHome").click();
	}
	
	public void deleteAllTasks() throws Exception {
		String sugarURL = Utils.getCascadingPropertyValue(sugar.props, "http://localhost/ent670/", "env.base_url");
		sugar.i.go(sugarURL + "/index.php?module=Tasks&action=ListView");
		sugar.i.getControl(Strategy.ID, "massall_top").click();
		sugar.i.getControl(Strategy.ID, "delete_listview_top").click();
		sugar.i.acceptDialog();
		sugar.i.getControl(Strategy.ID, "moduleTab_AllHome").click();
	}
	
	public void editTask(TaskRecord oldTask, TaskRecord newTask) throws Exception {
		this.searchTasks(oldTask.subject);
		sugar.i.getControl(Strategy.PLINK, oldTask.subject).click();
		sugar.i.getControl(Strategy.ID, "edit_button").click();
		sugar.i.getControl(Strategy.ID, "name").sendString(newTask.subject);
		sugar.i.getControl(Strategy.ID, "SAVE_HEADER").click();
//		sugar.i.getControl(Strategy.ID, "edit_button").halt(4);
//		sugar.i.getControl(Strategy.ID, "moduleTab_AllHome").click();
	}
	
	public void searchTasks(String search) throws Exception {
		String sugarURL = Utils.getCascadingPropertyValue(sugar.props, "http://localhost/ent670/", "env.base_url");
		sugar.i.go(sugarURL + "/index.php?module=Tasks&action=ListView");
		sugar.i.getControl(Strategy.ID, "name_basic").sendString(search);
		sugar.i.getControl(Strategy.ID, "search_form_submit").click();
	}
	
	public static class TaskRecord {
		
		public String subject = null;
//		public String startDate = null;
//		public String endDate = null;

		public TaskRecord(String subject) {
			this.subject = subject;
//			this.startDate = startDate;
//			this.endDate = endDate;
		}
	}
}
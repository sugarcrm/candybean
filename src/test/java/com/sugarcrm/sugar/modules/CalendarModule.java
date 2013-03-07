package com.sugarcrm.sugar.modules;

import com.sugarcrm.sugar.Sugar;
import com.sugarcrm.sugar.modules.CallsModule.CallRecord;
import com.sugarcrm.sugar.modules.MeetingsModule.MeetingRecord;
import com.sugarcrm.sugar.modules.TasksModule.TaskRecord;

/**
 * @author Conrad Warmbold
 *
 */
public class CalendarModule {
	
	private final Sugar sugar;
	
	public CalendarModule(Sugar sugar) { this.sugar = sugar; }
	
	public void scheduleMeeting(MeetingRecord meeting) throws Exception {
		sugar.modules.meetings.scheduleMeeting(meeting);
	}
	
	public void scheduleCall(CallRecord call) throws Exception {
		sugar.modules.calls.scheduleCall(call);
	}
	
	public void createTask(Sugar sugar, TaskRecord task) throws Exception {
		sugar.modules.tasks.createTask(task);
	}
}
package com.sugarcrm.sugar.admin;


import com.sugarcrm.voodoo.automation.Voodoo;
import com.sugarcrm.voodoo.automation.IAutomation.Strategy;
import com.sugarcrm.voodoo.automation.control.VControl;
import com.sugarcrm.voodoo.automation.control.VHook;
import com.sugarcrm.voodoo.automation.control.VSelect;
import com.sugarcrm.sugar.Sugar;


public class TimePeriods {
	
	public static void create(Voodoo voodoo, Sugar sugar, TimePeriod timePeriod) throws Exception {
		(new VControl(sugar.getHook("navbar_menu_user"), voodoo.auto)).click();
		(new VControl(sugar.getHook("navbar_menuitem_admin"), voodoo.auto)).waitOn();
		(new VControl(sugar.getHook("navbar_menuitem_admin"), voodoo.auto)).click();
		(new VControl(sugar.getHook("admin_header"), voodoo.auto)).waitOn();
		(new VControl(new VHook(Strategy.XPATH, "/html/body/div[5]/div/table/tbody/tr/td/div[2]/div/div/table[15]/tbody/tr/td/h3"), voodoo.auto)).scroll();
		(new VControl(new VHook(Strategy.ID, "timeperiod_management"), voodoo.auto)).click();
		(new VControl(sugar.getHook("navbar_menu_timeperiods"), voodoo.auto)).hover();
		(new VControl(sugar.getHook("navbar_menuitem_createtimeperiod"), voodoo.auto)).waitOn();
		(new VControl(sugar.getHook("navbar_menuitem_createtimeperiod"), voodoo.auto)).click();
		(new VControl(new VHook(Strategy.NAME, "name"), voodoo.auto)).waitOn();
		(new VControl(new VHook(Strategy.NAME, "name"), voodoo.auto)).hover();
		(new VControl(new VHook(Strategy.NAME, "name"), voodoo.auto)).sendString(timePeriod.name);
		(new VSelect(new VHook(Strategy.NAME, "is_fiscal_year"), voodoo.auto)).select(timePeriod.isFiscalYear);
		(new VControl(new VHook(Strategy.ID, "start_date"), voodoo.auto)).sendString(timePeriod.startDate);
		(new VControl(new VHook(Strategy.ID, "end_date"), voodoo.auto)).sendString(timePeriod.endDate);
		(new VControl(new VHook(Strategy.ID, "btn_save"), voodoo.auto)).click();
	}
}

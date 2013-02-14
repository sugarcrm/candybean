package com.sugarcrm.sugar.admin;


import com.sugarcrm.voodoo.automation.IInterface;
import com.sugarcrm.voodoo.automation.control.VHook.Strategy;
import com.sugarcrm.voodoo.automation.control.VHook;
import com.sugarcrm.sugar.Sugar;


public class TimePeriods {
	
	public static void create(Sugar sugar, IInterface iface, TimePeriod timePeriod) throws Exception {
		iface.getControl(sugar.getHook("navbar_menu_user")).click();
		iface.getControl(sugar.getHook("navbar_menuitem_admin")).halt(4);
		iface.getControl(sugar.getHook("navbar_menuitem_admin")).click();
		iface.getControl(sugar.getHook("admin_header")).halt(4);
		iface.getControl(new VHook(Strategy.XPATH, "/html/body/div[5]/div/table/tbody/tr/td/div[2]/div/div/table[15]/tbody/tr/td/h3")).scroll();
		iface.getControl(new VHook(Strategy.ID, "timeperiod_management")).click();
		iface.getControl(sugar.getHook("navbar_menu_timeperiods")).hover();
		iface.getControl(sugar.getHook("navbar_menuitem_createtimeperiod")).halt(4);
		iface.getControl(sugar.getHook("navbar_menuitem_createtimeperiod")).click();
		iface.getControl(new VHook(Strategy.NAME, "name")).halt(4);
		iface.getControl(new VHook(Strategy.NAME, "name")).hover();
		iface.getControl(new VHook(Strategy.NAME, "name")).sendString(timePeriod.name);
		iface.getSelect(new VHook(Strategy.NAME, "is_fiscal_year")).select(timePeriod.isFiscalYear);
		iface.getControl(new VHook(Strategy.ID, "start_date")).sendString(timePeriod.startDate);
		iface.getControl(new VHook(Strategy.ID, "end_date")).sendString(timePeriod.endDate);
		iface.getControl(new VHook(Strategy.ID, "btn_save")).click();
	}
}

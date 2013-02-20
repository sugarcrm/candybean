package com.sugarcrm.sugar;

import com.sugarcrm.voodoo.automation.control.VHook.Strategy;
import com.sugarcrm.sugar.Sugar;

public class Employees {
	
	public static void navTo(Sugar sugar) throws Exception {
		sugar.i.getControl(sugar.getHook("navbar_menu_user")).click();
		sugar.i.getControl(Strategy.ID, "employees_link").click();
		sugar.i.getControl(Strategy.XPATH, "/html/body/div[5]/div/table/tbody/tr/td/div/h2[2]").halt(4);
	}
}

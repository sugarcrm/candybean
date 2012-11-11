package com.sugarcrm.sugar.cases;

import javax.swing.JOptionPane;

import com.sugarcrm.voodoo.automation.Voodoo;
import com.sugarcrm.voodoo.automation.control.VControl;
import com.sugarcrm.sugar.Sugar;


public class Cases {
	
	public static void create(Voodoo voodoo, Sugar sugar, Case sugarCase) throws Exception {
		throw new Exception("Cases.create not yet implemented.");
	}
	
	public static void read(Voodoo voodoo, Sugar sugar, Case sugarCase) throws Exception {
		(new VControl(sugar.getHook("navbar_menu_more"), voodoo.auto)).click();
		voodoo.pause(200);
		(new VControl(sugar.getHook("navbar_menuitem_showmore"), voodoo.auto)).click();
		voodoo.pause(200);
//		voodoo.scroll(sugar.getHook("navbar_menuitem_cases"), voodoo.auto);
		(new VControl(sugar.getHook("navbar_menuitem_cases"), voodoo.auto)).click();
		voodoo.pause(400);
		(new VControl(sugar.getHook("cases_textfield_subjectsearch"), voodoo.auto)).sendString(sugarCase.subject());
		(new VControl(sugar.getHook("cases_button_search"), voodoo.auto)).click();
		voodoo.pause(400);
	}
	
	public static void delete(Voodoo voodoo, Sugar sugar, Case sugarCase) throws Exception {
		throw new Exception("Cases.delete not yet implemented.");
	}
}

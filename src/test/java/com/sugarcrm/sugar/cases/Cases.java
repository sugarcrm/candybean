package com.sugarcrm.sugar.cases;

import javax.swing.JOptionPane;

import com.sugarcrm.voodoo.automation.Voodoo;
import com.sugarcrm.sugar.Sugar;


public class Cases {
	
	public static void create(Voodoo voodoo, Sugar sugar, Case sugarCase) throws Exception {
		throw new Exception("Cases.create not yet implemented.");
	}
	
	public static void read(Voodoo voodoo, Sugar sugar, Case sugarCase) throws Exception {
		voodoo.click(sugar.getHook("navbar_menu_more"));
		voodoo.pause(200);
		voodoo.click(sugar.getHook("navbar_menuitem_showmore"));
		voodoo.pause(200);
//		voodoo.scroll(sugar.getHook("navbar_menuitem_cases"));
		voodoo.click(sugar.getHook("navbar_menuitem_cases"));
		voodoo.pause(400);
		voodoo.input(sugar.getHook("cases_textfield_subjectsearch"), sugarCase.subject());
		voodoo.click(sugar.getHook("cases_button_search"));
		voodoo.pause(400);
	}
	
	public static void delete(Voodoo voodoo, Sugar sugar, Case sugarCase) throws Exception {
		throw new Exception("Cases.delete not yet implemented.");
	}
}

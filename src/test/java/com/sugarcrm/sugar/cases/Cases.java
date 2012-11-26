package com.sugarcrm.sugar.cases;


import com.sugarcrm.voodoo.automation.Voodoo;
import com.sugarcrm.voodoo.automation.IAutomation.Strategy;
import com.sugarcrm.voodoo.automation.control.VControl;
import com.sugarcrm.voodoo.automation.control.VHook;
import com.sugarcrm.sugar.Sugar;


public class Cases {
	
	public static void create(Voodoo voodoo, Sugar sugar, Case sugarCase) throws Exception {
		throw new Exception("Cases.create not yet implemented.");
	}
	
	public static void modify(Voodoo voodoo, Sugar sugar, Case oldCase, Case newCase) throws Exception {
		Cases.search(voodoo, sugar, oldCase);
		(new VControl(new VHook(Strategy.PLINK, oldCase.subject()), voodoo.auto)).waitOn();
		(new VControl(new VHook(Strategy.PLINK, oldCase.subject()), voodoo.auto)).click();
		(new VControl(sugar.getHook("cases_button_edit"), voodoo.auto)).waitOn();
		(new VControl(sugar.getHook("cases_button_edit"), voodoo.auto)).click();
		(new VControl(sugar.getHook("cases_textfield_subject"), voodoo.auto)).sendString(newCase.subject());
		(new VControl(sugar.getHook("cases_button_team"), voodoo.auto)).click();
		voodoo.auto.focusByIndex(1);
		(new VControl(new VHook(Strategy.PLINK, oldCase.team().name()), voodoo.auto)).click();
		voodoo.auto.focusByIndex(0);
		(new VControl(sugar.getHook("cases_button_saveheader"), voodoo.auto)).click();
	}
	
	public static void search(Voodoo voodoo, Sugar sugar, Case sugarCase) throws Exception {
		(new VControl(sugar.getHook("navbar_menu_more"), voodoo.auto)).click();
		(new VControl(sugar.getHook("navbar_menuitem_showmore"), voodoo.auto)).waitOn();
		(new VControl(sugar.getHook("navbar_menuitem_showmore"), voodoo.auto)).click();
		(new VControl(sugar.getHook("navbar_menuitem_cases"), voodoo.auto)).waitOn();
		(new VControl(sugar.getHook("navbar_menuitem_cases"), voodoo.auto)).click();
		(new VControl(sugar.getHook("cases_textfield_subjectsearch"), voodoo.auto)).waitOn();
		(new VControl(sugar.getHook("cases_textfield_subjectsearch"), voodoo.auto)).sendString(sugarCase.subject());
		(new VControl(sugar.getHook("cases_button_search"), voodoo.auto)).click();
	}
	
	public static void delete(Voodoo voodoo, Sugar sugar, Case sugarCase) throws Exception {
		throw new Exception("Cases.delete not yet implemented.");
	}
}

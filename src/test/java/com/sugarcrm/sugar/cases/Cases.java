package com.sugarcrm.sugar.cases;


import com.sugarcrm.voodoo.automation.IInterface;
import com.sugarcrm.voodoo.automation.control.VHook.Strategy;
import com.sugarcrm.voodoo.automation.control.VHook;
import com.sugarcrm.sugar.Sugar;


public class Cases {
	
	public static void create(Sugar sugar, IInterface iface, Case sugarCase) throws Exception {
		throw new Exception("Cases.create not yet implemented.");
	}
	
	public static void modify(Sugar sugar, IInterface iface, Case oldCase, Case newCase) throws Exception {
		Cases.search(sugar, iface, oldCase);
		iface.getControl(new VHook(Strategy.PLINK, oldCase.subject())).waitOn();
		iface.getControl(new VHook(Strategy.PLINK, oldCase.subject())).click();
		iface.getControl(sugar.getHook("cases_button_edit")).waitOn();
		iface.getControl(sugar.getHook("cases_button_edit")).click();
		iface.getControl(sugar.getHook("cases_textfield_subject")).sendString(newCase.subject());
		iface.getControl(sugar.getHook("cases_button_team")).click();
		iface.focusByIndex(1);
		iface.getControl(new VHook(Strategy.PLINK, oldCase.team().name())).click();
		iface.focusByIndex(0);
		iface.interact("Pausing for save button watch...");
		iface.getControl(sugar.getHook("cases_button_saveheader")).click();
	}
	
	public static void search(Sugar sugar, IInterface iface, Case sugarCase) throws Exception {
		iface.getControl(sugar.getHook("navbar_menu_more")).click();
		iface.getControl(sugar.getHook("navbar_menuitem_showmore")).waitOn();
		iface.getControl(sugar.getHook("navbar_menuitem_showmore")).click();
		iface.getControl(sugar.getHook("navbar_menuitem_cases")).waitOn();
		iface.getControl(sugar.getHook("navbar_menuitem_cases")).click();
		iface.getControl(sugar.getHook("cases_textfield_subjectsearch")).waitOn();
		iface.getControl(sugar.getHook("cases_textfield_subjectsearch")).sendString(sugarCase.subject());
		iface.getControl(sugar.getHook("cases_button_search")).click();
	}
	
	public static void delete(Sugar sugar, IInterface iface, Case sugarCase) throws Exception {
		throw new Exception("Cases.delete not yet implemented.");
	}
}

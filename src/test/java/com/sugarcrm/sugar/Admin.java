package com.sugarcrm.sugar;

import com.sugarcrm.voodoo.automation.control.VHook.Strategy;
import com.sugarcrm.sugar.Sugar;

public class Admin {
	
	public static void setModuleVisibility(Sugar sugar) throws Exception {
		Admin.navTo(sugar);
		sugar.i.getControl(Strategy.ID, "product_catalog").scroll();
		sugar.i.getControl(Strategy.ID, "configure_tabs").click();
		throw new Exception("setModuleVisibility not yet implemented");
//		sugar.i.getControl(sugar.getHook("portal_link_configureportal")).click();
//		sugar.i.getSelect(sugar.getHook("portal_checkbox_portalenable")).select(isEnabled);
//		sugar.i.getControl(sugar.getHook("portal_button_save")).click();
//		voodoo.interact("Wait...");
	}
	
	public static void setPortalEnabled(Sugar sugar, boolean isEnabled) throws Exception {
		Admin.navTo(sugar);
		sugar.i.getControl(sugar.getHook("admin_table_devtools")).scroll();
		sugar.i.getControl(sugar.getHook("admin_link_sugarportal")).click();
		sugar.i.getControl(sugar.getHook("portal_link_configureportal")).click();
		sugar.i.getSelect(sugar.getHook("portal_checkbox_portalenable")).select(isEnabled);
		sugar.i.getControl(sugar.getHook("portal_button_save")).click();
//		voodoo.interact("Wait...");
	}
	
	public static void navTo(Sugar sugar) throws Exception {
		sugar.i.getControl(sugar.getHook("navbar_menu_user")).click();
		sugar.i.getControl(sugar.getHook("navbar_menuitem_admin")).click();
//		sugar.i.getControl(sugar.getHook("admin_header")).halt(4);
	}
}

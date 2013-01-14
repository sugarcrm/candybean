package com.sugarcrm.sugar.admin;


import com.sugarcrm.voodoo.automation.IInterface;
import com.sugarcrm.sugar.Sugar;


public class Administration {
	
	public static void setPortalEnabled(Sugar sugar, IInterface iface, boolean isEnabled) throws Exception {
		iface.getControl(sugar.getHook("navbar_menu_user")).click();
		iface.getControl(sugar.getHook("navbar_menuitem_admin")).halt(4);
		iface.getControl(sugar.getHook("navbar_menuitem_admin")).click();
		iface.getControl(sugar.getHook("admin_header")).halt(4);
		iface.getControl(sugar.getHook("admin_table_devtools")).scroll();
		iface.getControl(sugar.getHook("admin_link_sugarportal")).halt(4);
		iface.getControl(sugar.getHook("admin_link_sugarportal")).click();
		iface.getControl(sugar.getHook("portal_link_configureportal")).halt(4);
		iface.getControl(sugar.getHook("portal_link_configureportal")).click();
		iface.getControl(sugar.getHook("portal_checkbox_portalenable")).halt(4);
		iface.getSelect(sugar.getHook("portal_checkbox_portalenable")).select(isEnabled);
		iface.getControl(sugar.getHook("portal_button_save")).click();
//		voodoo.interact("Wait...");
	}
}

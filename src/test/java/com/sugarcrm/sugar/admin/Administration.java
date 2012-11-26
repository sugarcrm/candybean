package com.sugarcrm.sugar.admin;


import com.sugarcrm.voodoo.automation.Voodoo;
import com.sugarcrm.voodoo.automation.control.VControl;
import com.sugarcrm.voodoo.automation.control.VSelect;
import com.sugarcrm.sugar.Sugar;


public class Administration {
	
	public static void setPortalEnabled(Voodoo voodoo, Sugar sugar, boolean isEnabled) throws Exception {
		(new VControl(sugar.getHook("navbar_menu_user"), voodoo.auto)).click();
		(new VControl(sugar.getHook("navbar_menuitem_admin"), voodoo.auto)).waitOn();
		(new VControl(sugar.getHook("navbar_menuitem_admin"), voodoo.auto)).click();
		(new VControl(sugar.getHook("admin_header"), voodoo.auto)).waitOn();
		(new VControl(sugar.getHook("admin_table_devtools"), voodoo.auto)).scroll();
		(new VControl(sugar.getHook("admin_link_sugarportal"), voodoo.auto)).waitOn();
		(new VControl(sugar.getHook("admin_link_sugarportal"), voodoo.auto)).click();
		(new VControl(sugar.getHook("portal_link_configureportal"), voodoo.auto)).waitOn();
		(new VControl(sugar.getHook("portal_link_configureportal"), voodoo.auto)).click();
		(new VControl(sugar.getHook("portal_checkbox_portalenable"), voodoo.auto)).waitOn();
		(new VSelect(sugar.getHook("portal_checkbox_portalenable"), voodoo.auto)).select(isEnabled);
		(new VControl(sugar.getHook("portal_button_save"), voodoo.auto)).click();
//		voodoo.interact("Wait...");
	}
}

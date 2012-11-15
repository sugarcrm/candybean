package com.sugarcrm.sugar.admin;


import com.sugarcrm.voodoo.automation.Voodoo;
import com.sugarcrm.voodoo.automation.control.VControl;
import com.sugarcrm.sugar.Sugar;


public class Administration {
	
	public static void selectPortalEnable(Voodoo voodoo, Sugar sugar) throws Exception {
		(new VControl(sugar.getHook("navbar_menu_user"), voodoo.auto)).click();
		voodoo.pause(400);
		(new VControl(sugar.getHook("navbar_menuitem_admin"), voodoo.auto)).click();
		voodoo.pause(800);
//		JOptionPane.showInputDialog("pause");
		(new VControl(sugar.getHook("admin_table_devtools"), voodoo.auto)).scroll();
		voodoo.pause(800);
//		JOptionPane.showInputDialog("pause");
		(new VControl(sugar.getHook("admin_link_sugarportal"), voodoo.auto)).click();
		voodoo.pause(800);
		(new VControl(sugar.getHook("portal_link_configureportal"), voodoo.auto)).click();
		voodoo.pause(800);
//		JOptionPane.showInputDialog("pause");
		(new VControl(sugar.getHook("portal_checkbox_portalenable"), voodoo.auto)).click();
		voodoo.pause(200);
		(new VControl(sugar.getHook("portal_button_save"), voodoo.auto)).click();
		voodoo.pause(800);
//		JOptionPane.showInputDialog("pause");
	}
}

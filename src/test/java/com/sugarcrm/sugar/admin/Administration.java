package com.sugarcrm.sugar.admin;

import javax.swing.JOptionPane;

import com.sugarcrm.voodoo.Voodoo;
import com.sugarcrm.sugar.Sugar;


public class Administration {
	
	public static void selectPortalEnable(Voodoo voodoo, Sugar sugar) throws Exception {
		voodoo.click(sugar.getHook("navbar_menu_user"));
		voodoo.pause(400);
		voodoo.click(sugar.getHook("navbar_menuitem_admin"));
		voodoo.pause(1000);
		JOptionPane.showInputDialog("pause");
		// TODO: Scroll down?
		voodoo.click(sugar.getHook("admin_link_sugarportal"));
		voodoo.pause(800);
		voodoo.click(sugar.getHook("portal_link_configureportal"));
		voodoo.pause(800);
		JOptionPane.showInputDialog("pause");
		voodoo.click(sugar.getHook("portal_checkbox_portalenable"));
		voodoo.pause(200);
		voodoo.click(sugar.getHook("portal_button_save"));
		voodoo.pause(800);
		JOptionPane.showInputDialog("pause");
	}
}

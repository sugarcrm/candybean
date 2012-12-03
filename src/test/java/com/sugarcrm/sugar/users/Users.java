package com.sugarcrm.sugar.users;

import javax.swing.JOptionPane;

import com.sugarcrm.voodoo.automation.IInterface;
import com.sugarcrm.sugar.Sugar;
import com.sugarcrm.sugar.users.User.PortalUser;


public class Users {
	
	public static void create(Sugar sugar, IInterface iface, User user) throws Exception {
		if (user instanceof PortalUser) {
			iface.getControl(sugar.getHook("navbar_menu_user")).click();
			iface.pause(400);
			iface.getControl(sugar.getHook("navbar_menuitem_admin")).click();
			iface.pause(800);
			iface.getControl(sugar.getHook("admin_link_usermanagement")).click();
			iface.pause(800);
			iface.getControl(sugar.getHook("navbar_menu_users")).hover();
			iface.pause(800);
			iface.getControl(sugar.getHook("navbar_menuitem_createportaluser")).click();
			iface.pause(800);
			iface.getControl(sugar.getHook("users_textfield_username")).sendString(user.username());
			iface.getControl(sugar.getHook("users_textfield_name")).sendString(user.name());
			iface.getControl(sugar.getHook("users_tab_password")).click();
			iface.pause(400);
			iface.getControl(sugar.getHook("users_textfield_password")).sendString(user.password1());
			iface.getControl(sugar.getHook("users_textfield_passwordconfirm")).sendString(user.password2());
			iface.getControl(sugar.getHook("users_button_save")).click();
//			JOptionPane.showInputDialog("pause");
		} else throw new Exception("Only portal users are supported for creation at this time.");
	}
	
	public static void delete(Sugar sugar, IInterface iface, User user) throws Exception {
		iface.getControl(sugar.getHook("navbar_menu_user")).click();
		iface.pause(400);
		iface.getControl(sugar.getHook("navbar_menuitem_admin")).click();
		iface.pause(800);
		iface.getControl(sugar.getHook("admin_link_usermanagement")).click();
		iface.pause(800);
		iface.getControl(sugar.getHook("navbar_menu_users")).click();
		iface.pause(800);
		iface.getControl(sugar.getHook("users_textfield_namesearch")).sendString(user.username());
		iface.getControl(sugar.getHook("search_form_submit")).click();
		iface.pause(800);
		JOptionPane.showInputDialog("pause");
		throw new Exception("deleteUser not yet supported");
//		iface.getControl(sugar.getHook("users_button_save"));
//		JOptionPane.showInputDialog("pause");
	}
}

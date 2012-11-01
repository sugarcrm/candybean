package com.sugarcrm.sugar.users;

import javax.swing.JOptionPane;

import com.sugarcrm.voodoo.automation.Voodoo;
import com.sugarcrm.sugar.Sugar;
import com.sugarcrm.sugar.users.User.PortalUser;


public class Users {
	
	public static void create(Voodoo voodoo, Sugar sugar, User user) throws Exception {
		if (user instanceof PortalUser) {
			voodoo.click(sugar.getHook("navbar_menu_user"));
			voodoo.pause(400);
			voodoo.click(sugar.getHook("navbar_menuitem_admin"));
			voodoo.pause(800);
			voodoo.click(sugar.getHook("admin_link_usermanagement"));
			voodoo.pause(800);
			voodoo.hover(sugar.getHook("navbar_menu_users"));
			voodoo.pause(800);
			voodoo.click(sugar.getHook("navbar_menuitem_createportaluser"));
			voodoo.pause(800);
			voodoo.input(sugar.getHook("users_textfield_username"), user.username());
			voodoo.input(sugar.getHook("users_textfield_name"), user.name());
			voodoo.click(sugar.getHook("users_tab_password"));
			voodoo.pause(400);
			voodoo.input(sugar.getHook("users_textfield_password"), user.password1());
			voodoo.input(sugar.getHook("users_textfield_passwordconfirm"), user.password2());
			voodoo.click(sugar.getHook("users_button_save"));
//			JOptionPane.showInputDialog("pause");
		} else throw new Exception("Only portal users are supported for creation at this time.");
	}
	
	public static void delete(Voodoo voodoo, Sugar sugar, User user) throws Exception {
		voodoo.click(sugar.getHook("navbar_menu_user"));
		voodoo.pause(400);
		voodoo.click(sugar.getHook("navbar_menuitem_admin"));
		voodoo.pause(800);
		voodoo.click(sugar.getHook("admin_link_usermanagement"));
		voodoo.pause(800);
		voodoo.click(sugar.getHook("navbar_menu_users"));
		voodoo.pause(800);
		voodoo.input(sugar.getHook("users_textfield_namesearch"), user.username());
		voodoo.click(sugar.getHook("search_form_submit"));
		voodoo.pause(800);
		JOptionPane.showInputDialog("pause");
		throw new Exception("deleteUser not yet supported");
//		voodoo.click(sugar.getHook("users_button_save"));
//		JOptionPane.showInputDialog("pause");
	}
}

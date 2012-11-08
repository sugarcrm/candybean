package com.sugarcrm.sugar.users;

import javax.swing.JOptionPane;

import com.sugarcrm.voodoo.automation.Voodoo;
import com.sugarcrm.voodoo.automation.control.VControl;
import com.sugarcrm.sugar.Sugar;
import com.sugarcrm.sugar.users.User.PortalUser;


public class Users {
	
	public static void create(Voodoo voodoo, Sugar sugar, User user) throws Exception {
		if (user instanceof PortalUser) {
			(new VControl(sugar.getHook("navbar_menu_user"), voodoo.auto)).click();
			voodoo.pause(400);
			(new VControl(sugar.getHook("navbar_menuitem_admin"), voodoo.auto)).click();
			voodoo.pause(800);
			(new VControl(sugar.getHook("admin_link_usermanagement"), voodoo.auto)).click();
			voodoo.pause(800);
			(new VControl(sugar.getHook("navbar_menu_users"), voodoo.auto)).hover();
			voodoo.pause(800);
			(new VControl(sugar.getHook("navbar_menuitem_createportaluser"), voodoo.auto)).click();
			voodoo.pause(800);
			(new VControl(sugar.getHook("users_textfield_username"), voodoo.auto)).sendString(user.username());
			(new VControl(sugar.getHook("users_textfield_name"), voodoo.auto)).sendString(user.name());
			(new VControl(sugar.getHook("users_tab_password"), voodoo.auto)).click();
			voodoo.pause(400);
			(new VControl(sugar.getHook("users_textfield_password"), voodoo.auto)).sendString(user.password1());
			(new VControl(sugar.getHook("users_textfield_passwordconfirm"), voodoo.auto)).sendString(user.password2());
			(new VControl(sugar.getHook("users_button_save"), voodoo.auto)).click();
//			JOptionPane.showInputDialog("pause");
		} else throw new Exception("Only portal users are supported for creation at this time.");
	}
	
	public static void delete(Voodoo voodoo, Sugar sugar, User user) throws Exception {
		(new VControl(sugar.getHook("navbar_menu_user"), voodoo.auto)).click();
		voodoo.pause(400);
		(new VControl(sugar.getHook("navbar_menuitem_admin"), voodoo.auto)).click();
		voodoo.pause(800);
		(new VControl(sugar.getHook("admin_link_usermanagement"), voodoo.auto)).click();
		voodoo.pause(800);
		(new VControl(sugar.getHook("navbar_menu_users"), voodoo.auto)).click();
		voodoo.pause(800);
		(new VControl(sugar.getHook("users_textfield_namesearch"), voodoo.auto)).sendString(user.username());
		(new VControl(sugar.getHook("search_form_submit"), voodoo.auto)).click();
		voodoo.pause(800);
		JOptionPane.showInputDialog("pause");
		throw new Exception("deleteUser not yet supported");
//		(new VControl(sugar.getHook("users_button_save"), voodoo.auto));
//		JOptionPane.showInputDialog("pause");
	}
}

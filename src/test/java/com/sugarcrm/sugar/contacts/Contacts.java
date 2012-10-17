package com.sugarcrm.sugar.contacts;

import javax.swing.JOptionPane;

import com.sugarcrm.voodoo.Voodoo;
import com.sugarcrm.sugar.Sugar;


public class Contacts {
	
	public static void create(Voodoo voodoo, Sugar sugar, Contact contact) throws Exception {
		voodoo.hover(sugar.getHook("navbar_menu_contacts"));
		voodoo.pause(400);
		voodoo.click(sugar.getHook("navbar_menuitem_createcontact"));
		voodoo.pause(800);
		voodoo.hover(sugar.getHook("contacts_textfield_lastname"));
		voodoo.input(sugar.getHook("contacts_textfield_lastname"), contact.lastName());
		voodoo.click(sugar.getHook("contacts_button_accountselect"));
//		voodoo.focus(1);
		voodoo.input(sugar.getHook("accountsearch_textfield_name"), contact.account().name());
		voodoo.click(sugar.getHook("accountsearch_button_search"));
		voodoo.pause(400);
		voodoo.click(sugar.getHook("accountsearch_link_firstitem"));
//		voodoo.focus(0);
		voodoo.scroll(sugar.getHook("contacts_panel_portalinfo"));
		voodoo.pause(400);
		voodoo.input(sugar.getHook("contacts_textfield_portalname"), contact.portalName());
		if (contact.portalActive())	voodoo.click(sugar.getHook("contacts_checkbox_portalactive"));
		voodoo.input(sugar.getHook("contacts_password_portalpassword1"), contact.portalPassword());
		voodoo.input(sugar.getHook("contacts_password_portalpassword2"), contact.portalPassword());
		voodoo.scroll(sugar.getHook("contacts_div_bottomlinks"));
		voodoo.click(sugar.getHook("contacts_button_savefooter"));
		voodoo.pause(400);
		JOptionPane.showInputDialog("pause");
	}
	
	public static void delete(Voodoo voodoo, Sugar sugar, Contact contact) throws Exception {
//		voodoo.click(sugar.getHook("navbar_menu_user"));
//		voodoo.pause(400);
//		voodoo.click(sugar.getHook("navbar_menuitem_admin"));
//		voodoo.pause(800);
//		voodoo.click(sugar.getHook("admin_link_usermanagement"));
//		voodoo.pause(800);
//		voodoo.click(sugar.getHook("navbar_menu_users"));
//		voodoo.pause(800);
//		voodoo.input(sugar.getHook("users_textfield_namesearch"), contact.username());
//		voodoo.click(sugar.getHook("search_form_submit"));
//		voodoo.pause(800);
//		JOptionPane.showInputDialog("pause");
		throw new Exception("deleteContact not yet supported");
//		voodoo.click(sugar.getHook("users_button_save"));
//		JOptionPane.showInputDialog("pause");
	}
}

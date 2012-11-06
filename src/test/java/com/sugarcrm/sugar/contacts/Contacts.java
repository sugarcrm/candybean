package com.sugarcrm.sugar.contacts;

import javax.swing.JOptionPane;

import com.sugarcrm.voodoo.automation.Voodoo;
import com.sugarcrm.voodoo.automation.control.VControl;
import com.sugarcrm.sugar.Sugar;


public class Contacts {
	
	public static void create(Voodoo voodoo, Sugar sugar, Contact contact) throws Exception {
		(new VControl(sugar.getHook("navbar_menu_contacts"), voodoo.auto)).hover();
		voodoo.pause(400);
		(new VControl(sugar.getHook("navbar_menuitem_createcontact"), voodoo.auto)).click();
		voodoo.pause(800);
		(new VControl(sugar.getHook("contacts_textfield_lastname"), voodoo.auto)).hover();
		(new VControl(sugar.getHook("contacts_textfield_lastname"), voodoo.auto)).sendString(contact.lastName());
		(new VControl(sugar.getHook("contacts_button_accountselect"), voodoo.auto)).click();
		voodoo.auto.focusByIndex(1);
		(new VControl(sugar.getHook("accountsearch_textfield_name"), voodoo.auto)).sendString(contact.account().name());
		(new VControl(sugar.getHook("accountsearch_button_search"), voodoo.auto)).click();
		voodoo.pause(400);
		(new VControl(sugar.getHook("accountsearch_link_firstitem"), voodoo.auto)).click();
		voodoo.auto.focusByIndex(0);
		(new VControl(sugar.getHook("contacts_panel_portalinfo"), voodoo.auto)).scroll();
		voodoo.pause(400);
		(new VControl(sugar.getHook("contacts_textfield_portalname"), voodoo.auto)).sendString(contact.portalName());
		if (contact.portalActive())	(new VControl(sugar.getHook("contacts_checkbox_portalactive"), voodoo.auto)).click();
		(new VControl(sugar.getHook("contacts_password_portalpassword1"), voodoo.auto)).sendString(contact.portalPassword());
		(new VControl(sugar.getHook("contacts_password_portalpassword2"), voodoo.auto)).sendString(contact.portalPassword());
		(new VControl(sugar.getHook("contacts_div_bottomlinks"), voodoo.auto)).scroll();
		(new VControl(sugar.getHook("contacts_button_savefooter"), voodoo.auto)).click();
		voodoo.pause(400);
		JOptionPane.showInputDialog("pause");
	}
	
	public static void delete(Voodoo voodoo, Sugar sugar, Contact contact) throws Exception {
//		voodoo.click(sugar.getHook("navbar_menu_user"), voodoo.auto));
//		voodoo.pause(400);
//		voodoo.click(sugar.getHook("navbar_menuitem_admin"), voodoo.auto));
//		voodoo.pause(800);
//		voodoo.click(sugar.getHook("admin_link_usermanagement"), voodoo.auto));
//		voodoo.pause(800);
//		voodoo.click(sugar.getHook("navbar_menu_users"), voodoo.auto));
//		voodoo.pause(800);
//		voodoo.input(sugar.getHook("users_textfield_namesearch"), contact.username(), voodoo.auto));
//		voodoo.click(sugar.getHook("search_form_submit"), voodoo.auto));
//		voodoo.pause(800);
//		JOptionPane.showInputDialog("pause");
		throw new Exception("deleteContact not yet supported");
//		voodoo.click(sugar.getHook("users_button_save"), voodoo.auto));
//		JOptionPane.showInputDialog("pause");
	}
}

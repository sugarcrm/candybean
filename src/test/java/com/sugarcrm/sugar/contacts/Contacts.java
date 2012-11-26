package com.sugarcrm.sugar.contacts;

import com.sugarcrm.voodoo.automation.Voodoo;
import com.sugarcrm.voodoo.automation.control.VControl;
import com.sugarcrm.sugar.Sugar;


public class Contacts {
	
	public static void create(Voodoo voodoo, Sugar sugar, Contact contact) throws Exception {
		(new VControl(sugar.getHook("navbar_menu_contacts"), voodoo.auto)).hover();
		(new VControl(sugar.getHook("navbar_menuitem_createcontact"), voodoo.auto)).waitOn();
		(new VControl(sugar.getHook("navbar_menuitem_createcontact"), voodoo.auto)).click();
		(new VControl(sugar.getHook("contacts_textfield_lastname"), voodoo.auto)).waitOn();
		(new VControl(sugar.getHook("contacts_textfield_lastname"), voodoo.auto)).hover();
		(new VControl(sugar.getHook("contacts_textfield_lastname"), voodoo.auto)).sendString(contact.lastName());
		(new VControl(sugar.getHook("contacts_button_accountselect"), voodoo.auto)).click();
		voodoo.auto.focusByIndex(1);
		(new VControl(sugar.getHook("accountsearch_textfield_name"), voodoo.auto)).waitOn();
		(new VControl(sugar.getHook("accountsearch_textfield_name"), voodoo.auto)).sendString(contact.account().name());
		(new VControl(sugar.getHook("accountsearch_button_search"), voodoo.auto)).click();
		(new VControl(sugar.getHook("accountsearch_link_firstitem"), voodoo.auto)).waitOn();
		(new VControl(sugar.getHook("accountsearch_link_firstitem"), voodoo.auto)).click();
		voodoo.auto.focusByIndex(0);
		(new VControl(sugar.getHook("contacts_panel_portalinfo"), voodoo.auto)).scroll();
		(new VControl(sugar.getHook("contacts_textfield_portalname"), voodoo.auto)).waitOn();
		(new VControl(sugar.getHook("contacts_textfield_portalname"), voodoo.auto)).sendString(contact.portalName());
		if (contact.portalActive())	(new VControl(sugar.getHook("contacts_checkbox_portalactive"), voodoo.auto)).click();
		(new VControl(sugar.getHook("contacts_password_portalpassword1"), voodoo.auto)).sendString(contact.portalPassword());
		(new VControl(sugar.getHook("contacts_password_portalpassword2"), voodoo.auto)).sendString(contact.portalPassword());
		(new VControl(sugar.getHook("contacts_div_bottomlinks"), voodoo.auto)).scroll();
		(new VControl(sugar.getHook("contacts_button_savefooter"), voodoo.auto)).click();
	}
	
	public static void deleteAll(Voodoo voodoo, Sugar sugar) throws Exception {
		(new VControl(sugar.getHook("navbar_menu_contacts"), voodoo.auto)).click();
		(new VControl(sugar.getHook("navbar_menu_user"), voodoo.auto)).hover();
		(new VControl(sugar.getHook("contactsearch_checkbox_selectall"), voodoo.auto)).waitOn();
		(new VControl(sugar.getHook("contactsearch_checkbox_selectall"), voodoo.auto)).click();
		(new VControl(sugar.getHook("contactsearch_button_deleteall"), voodoo.auto)).waitOn();
		(new VControl(sugar.getHook("contactsearch_button_deleteall"), voodoo.auto)).click();
		voodoo.auto.acceptDialog();
	}
}

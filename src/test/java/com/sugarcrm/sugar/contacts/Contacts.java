package com.sugarcrm.sugar.contacts;


import com.sugarcrm.voodoo.automation.IInterface;
import com.sugarcrm.sugar.Sugar;


public class Contacts {
	
	public static void create(Sugar sugar, IInterface iface, Contact contact) throws Exception {
		iface.getControl(sugar.getHook("navbar_menu_contacts")).hover();
		iface.getControl(sugar.getHook("navbar_menuitem_createcontact")).waitOn();
		iface.getControl(sugar.getHook("navbar_menuitem_createcontact")).click();
		iface.getControl(sugar.getHook("contacts_textfield_lastname")).waitOn();
		iface.getControl(sugar.getHook("contacts_textfield_lastname")).hover();
		iface.getControl(sugar.getHook("contacts_textfield_lastname")).sendString(contact.lastName());
		iface.getControl(sugar.getHook("contacts_button_accountselect")).click();
		iface.focusByIndex(1);
		iface.getControl(sugar.getHook("accountsearch_textfield_name")).waitOn();
		iface.getControl(sugar.getHook("accountsearch_textfield_name")).sendString(contact.account().name());
		iface.getControl(sugar.getHook("accountsearch_button_search")).click();
		iface.getControl(sugar.getHook("accountsearch_link_firstitem")).waitOn();
		iface.getControl(sugar.getHook("accountsearch_link_firstitem")).click();
		iface.focusByIndex(0);
		iface.getControl(sugar.getHook("contacts_panel_portalinfo")).scroll();
		iface.getControl(sugar.getHook("contacts_textfield_portalname")).waitOn();
		iface.getControl(sugar.getHook("contacts_textfield_portalname")).sendString(contact.portalName());
		if (contact.portalActive())	iface.getControl(sugar.getHook("contacts_checkbox_portalactive")).click();
		iface.getControl(sugar.getHook("contacts_password_portalpassword1")).sendString(contact.portalPassword());
		iface.getControl(sugar.getHook("contacts_password_portalpassword2")).sendString(contact.portalPassword());
		iface.getControl(sugar.getHook("contacts_div_bottomlinks")).scroll();
		iface.getControl(sugar.getHook("contacts_button_savefooter")).click();
	}
	
	public static void deleteAll(Sugar sugar, IInterface iface) throws Exception {
		iface.getControl(sugar.getHook("navbar_menu_contacts")).click();
		iface.getControl(sugar.getHook("navbar_menu_user")).hover();
		iface.getControl(sugar.getHook("contactsearch_checkbox_selectall")).waitOn();
		iface.getControl(sugar.getHook("contactsearch_checkbox_selectall")).click();
		iface.getControl(sugar.getHook("contactsearch_button_deleteall")).waitOn();
		iface.getControl(sugar.getHook("contactsearch_button_deleteall")).click();
		iface.acceptDialog();
	}
}

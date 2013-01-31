package com.sugarcrm.sugar.accounts;


import com.sugarcrm.voodoo.automation.IInterface;
import com.sugarcrm.sugar.Sugar;


public class Accounts {
	
	public static void create(Sugar sugar, IInterface iface, Account account) throws Exception {
		iface.getControl(sugar.getHook("navbar_menu_accounts")).hover();
		iface.getControl(sugar.getHook("navbar_menuitem_createaccount")).halt(4);
		iface.getControl(sugar.getHook("navbar_menuitem_createaccount")).click();
		iface.getControl(sugar.getHook("accounts_textfield_name")).halt(4);
		iface.getControl(sugar.getHook("accounts_textfield_name")).hover();
		iface.getControl(sugar.getHook("accounts_textfield_name")).sendString(account.name());
		iface.getControl(sugar.getHook("accounts_button_saveheader")).click();
	}
	
	public static void deleteAll(Sugar sugar, IInterface iface) throws Exception {
		iface.getControl(sugar.getHook("navbar_menu_accounts")).click();
		iface.getControl(sugar.getHook("navbar_menu_user")).hover();
		iface.getControl(sugar.getHook("accountsearch_checkbox_selectall")).halt(4);
		iface.getControl(sugar.getHook("accountsearch_checkbox_selectall")).click();
		iface.getControl(sugar.getHook("accountsearch_button_deleteall")).halt(4);
		iface.getControl(sugar.getHook("accountsearch_button_deleteall")).click();
		iface.acceptDialog();
	}
}

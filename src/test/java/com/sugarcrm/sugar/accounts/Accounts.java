package com.sugarcrm.sugar.accounts;

import com.sugarcrm.voodoo.automation.Voodoo;
import com.sugarcrm.sugar.Sugar;


public class Accounts {
	
	public static void create(Voodoo voodoo, Sugar sugar, Account account) throws Exception {
		voodoo.hover(sugar.getHook("navbar_menu_accounts"));
		voodoo.pause(200);
		voodoo.click(sugar.getHook("navbar_menuitem_createaccount"));
		voodoo.pause(400);
		voodoo.hover(sugar.getHook("accounts_textfield_name"));
		voodoo.input(sugar.getHook("accounts_textfield_name"), account.name());
		voodoo.click(sugar.getHook("accounts_button_saveheader"));
	}
	
	public static void delete(Voodoo voodoo, Sugar sugar, Account account) throws Exception {
		throw new Exception("deleteAccount not yet supported");
	}
}

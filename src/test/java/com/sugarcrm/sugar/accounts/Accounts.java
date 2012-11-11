package com.sugarcrm.sugar.accounts;

import com.sugarcrm.voodoo.automation.Voodoo;
import com.sugarcrm.voodoo.automation.control.VControl;
import com.sugarcrm.sugar.Sugar;


public class Accounts {
	
	public static void create(Voodoo voodoo, Sugar sugar, Account account) throws Exception {
		(new VControl(sugar.getHook("navbar_menu_accounts"), voodoo.auto)).hover();
		voodoo.pause(200);
		(new VControl(sugar.getHook("navbar_menuitem_createaccount"), voodoo.auto)).click();
		voodoo.pause(400);
		(new VControl(sugar.getHook("accounts_textfield_name"), voodoo.auto)).hover();
		(new VControl(sugar.getHook("accounts_textfield_name"), voodoo.auto)).sendString(account.name());
		(new VControl(sugar.getHook("accounts_button_saveheader"), voodoo.auto)).click();
	}
	
	public static void delete(Voodoo voodoo, Sugar sugar, Account account) throws Exception {
		throw new Exception("deleteAccount not yet supported");
	}
}

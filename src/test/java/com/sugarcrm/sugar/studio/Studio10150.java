package com.sugarcrm.sugar.studio;


import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.sugarcrm.sugar.Sugar;
import com.sugarcrm.sugar.SugarTest;
import com.sugarcrm.sugar.accounts.Account;
import com.sugarcrm.sugar.accounts.Account.AccountBuilder;
import com.sugarcrm.sugar.accounts.Accounts;
import com.sugarcrm.sugar.admin.Administration;
import com.sugarcrm.sugar.cases.Case;
import com.sugarcrm.sugar.cases.Case.CaseBuilder;
import com.sugarcrm.sugar.contacts.Contact;
import com.sugarcrm.sugar.contacts.Contact.ContactBuilder;
import com.sugarcrm.sugar.contacts.Contacts;
import com.sugarcrm.sugar.cases.Cases;
import com.sugarcrm.sugar.teams.Team;
import com.sugarcrm.sugar.teams.Team.TeamBuilder;
import com.sugarcrm.sugar.users.User;
import com.sugarcrm.sugar.users.Users;
import com.sugarcrm.voodoo.automation.control.VHook.Strategy;


public class Studio10150 extends SugarTest {
	
	@BeforeClass
	public static void setupOnce() throws Exception { SugarTest.setupOnce(); }

	@Override
	@Before
	public void setup() throws Exception {
		super.setup();
		// Assume you're already logged into Sugar -- perform setup here
	}
	
	@Test
	public void test() throws Exception {
		// Add test steps and automation here; some examples:
		iface.getControl(Strategy.ID, "globalLinksModule").click();
		iface.pause(1000);
		iface.getControl(Strategy.ID, "admin_link").click();
		iface.pause(2000);
		iface.getControl(Strategy.ID, "arrow").click();
		iface.pause(2000);
		iface.getControl(Strategy.ID, "studio").click();
		iface.pause(1000);
		iface.getControl(Strategy.ID, "studiolink_Accounts").click();
		iface.pause(1000);
		iface.getControl(Strategy.CSS, "#fieldsBtn table.wizardButton tbody tr td a.studiolink").click();
		iface.pause(1000);
		iface.getControl(Strategy.ID, "description").click();
		iface.pause(1000);
		iface.getControl(Strategy.ID, "calculated").click();
		iface.pause(1000);
		iface.getControl(Strategy.CSS, "#formulaRow td input.button:nth-of-type(3)").click();
		iface.pause(1000);
		iface.getControl(Strategy.ID, "formulaInput").halt(4);
		iface.getControl(Strategy.ID, "formulaInput").sendString("strToUpper($name)");
		iface.getControl(Strategy.ID, "fomulaSaveButton").click();
		iface.getControl(Strategy.NAME, "fsavebtn").click();
		iface.getControl(sugar.getHook("navbar_menu_accounts")).hover();
		iface.getControl(sugar.getHook("navbar_menuitem_createaccount")).halt(4);
		iface.getControl(sugar.getHook("navbar_menuitem_createaccount")).click();
		iface.getControl(sugar.getHook("accounts_textfield_name")).halt(4);
		iface.getControl(sugar.getHook("accounts_textfield_name")).hover();
		String str = "New Account";
		iface.getControl(sugar.getHook("accounts_textfield_name")).sendString(str);
		iface.getControl(sugar.getHook("accounts_button_saveheader")).click();
		String fromDetailView = iface.getControl(Strategy.ID, "description").getText();
		if(fromDetailView.equals(str.toUpperCase()))
			voodoo.log.info("Pass");
		else
			voodoo.log.info("Failed");
				
	}

	@Override
	@After
	public void cleanup() throws Exception {
		// Perform cleanup here -- remove any records that you created so you can rerun this test without conflict
		iface.getControl(Strategy.ID,"moduleTab_AllAccounts").click(); //ViewAccountsAll
		iface.getControl(sugar.getHook("accountsearch_checkbox_selectall")).halt(4);
		iface.getControl(sugar.getHook("accountsearch_checkbox_selectall")).click();
		iface.getControl(sugar.getHook("accountsearch_button_deleteall")).halt(4);
		iface.getControl(sugar.getHook("accountsearch_button_deleteall")).click();
		iface.acceptDialog();
		iface.getControl(Strategy.ID, "globalLinksModule").click();
		iface.pause(1000);
		iface.getControl(Strategy.ID, "admin_link").click();
		iface.pause(2000);
		iface.getControl(Strategy.ID, "studio").click();
		iface.pause(1000);
		iface.getControl(Strategy.ID, "studiolink_Accounts").click();
		iface.pause(1000);
		iface.getControl(Strategy.CSS, "#fieldsBtn table.wizardButton tbody tr td a.studiolink").click();
		iface.pause(1000);
		iface.getControl(Strategy.ID, "description").click();
		iface.pause(1000);
		iface.getControl(Strategy.ID, "calculated").click();
		iface.pause(1000);
		iface.getControl(Strategy.NAME, "fsavebtn").click();
		super.cleanup();
	}

	@AfterClass
	public static void cleanupOnce() { SugarTest.cleanupOnce(); }
}

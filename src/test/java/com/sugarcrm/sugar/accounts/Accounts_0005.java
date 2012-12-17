package com.sugarcrm.sugar.accounts;


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


public class TemplateTest extends SugarTest {
	
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
		iface.getControl(Strategy.ID, "moduleTab_AllAccounts").hover();
		iface.getControl(Strategy.ID, "CreateAccountAll").waitOn();
		iface.getControl(Strategy.ID, "CreateAccountAll").click();
		iface.getControl(Strategy.ID, "name").hover();
		iface.getControl(Strategy.ID, "name").sendString("Acct005");
		iface.getControl(Strategy.ID, "SAVE_HEADER").click();
		iface.getControl(Strategy.XPATH, "//*[@id=\"detail_header_action_menu\"]/li/span").click();
		iface.getControl(Strategy.ID, "duplicate_button").click();
		iface.getControl(Strategy.ID, "name").sendString("Acct005-dup");
		iface.getControl(Strategy.ID, "SAVE_HEADER").click();
	}

	@Override
	@After
	public void cleanup() throws Exception {
		// Perform cleanup here -- remove any records that you created so you can rerun this test without conflict
		super.cleanup();
	}

	@AfterClass
	public static void cleanupOnce() { SugarTest.cleanupOnce(); }
}

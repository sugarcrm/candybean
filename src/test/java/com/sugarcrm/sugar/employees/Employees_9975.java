package com.sugarcrm.sugar.employees;


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


public class Employees_9975 extends SugarTest {
	
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
		iface.getControl(Strategy.ID, "welcome_link").click();
		iface.getControl(Strategy.ID, "employees_link").click();
		iface.getControl(Strategy.ID, "moduleTab_AllEmployees").hover();
		iface.getControl(Strategy.ID, "CreateEmployeeAll").halt(4);
		iface.getControl(Strategy.ID, "CreateEmployeeAll").click();
		iface.getControl(Strategy.ID, "first_name").sendString("Firstname");
		iface.getControl(Strategy.ID, "last_name").sendString("Lastname");
		iface.getControl(Strategy.ID, "title").sendString("Manager");
		iface.getControl(Strategy.ID, "phone_work").sendString("1231111111");
		iface.getControl(Strategy.ID, "department").sendString("Engineering");
		iface.getControl(Strategy.ID, "phone_mobile").sendString("1112345678");
		iface.getControl(Strategy.NAME, "btn_reports_to_name").click();
		iface.focusByIndex(1);
		iface.getControl(Strategy.CSS, "table.list.view tbody tr:nth-of-type(3) td:nth-of-type(1) a").click();
        iface.focusByIndex(0);
        iface.getControl(Strategy.ID, "address_street").sendString("1 abc ct");
        iface.getControl(Strategy.ID, "address_city").sendString("Cupertino");
        iface.getControl(Strategy.ID, "address_state").sendString("California");
        iface.getControl(Strategy.ID, "address_postalcode").sendString("12345");
        iface.getControl(Strategy.ID, "address_country").sendString("USA");
        iface.getControl(Strategy.ID, "Users0emailAddress0").sendString("a@sugarcrm.com");
        iface.getControl(Strategy.ID, "SAVE_HEADER").click();
//		iface.getControl(Strategy.XPATH, "//td[3]/a").click();
//		iface.getControl(Strategy.ID, "//div[4]/div[2]/a").hover();
//		iface.getControl(Strategy.PLINK, "accounts").click();
//		iface.getControl(Strategy.ID, "username").sendString("username");
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

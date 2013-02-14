package com.sugarcrm.sugar.leads;


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


public class Leads_1894 extends SugarTest {
	
	@BeforeClass
	public static void first() throws Exception { SugarTest.first(); }

	@Override
	@Before
	public void setup() throws Exception {
		super.setup();
		// Assume you're already logged into Sugar -- perform setup here
	}
	
	@Test
	public void test() throws Exception {
		// Add test steps and automation here; some examples:
//		iface.getControl(Strategy.XPATH, "//td[3]/a").click();
//		iface.getControl(Strategy.ID, "//div[4]/div[2]/a").hover();
//		iface.getControl(Strategy.PLINK, "accounts").click();
//		iface.getControl(Strategy.ID, "username").sendString("username");
		String lastName = "chen";
		String lastNameDup = "Sugar";
//		Lead lead = new Lead(lastName);
//		Leads.create(lead);
		
		iface.getControl(Strategy.ID, "moduleTabExtraMenuAll").hover();
		iface.getControl(Strategy.ID, "moduleTab_AllLeads").halt(4);
		iface.getControl(Strategy.ID, "moduleTab_AllLeads").click();
		iface.getControl(Strategy.ID, "CreateLeadAll").halt(4);
		iface.getControl(Strategy.ID, "CreateLeadAll").click();
//		iface.getControl(Strategy.ID, "last_name").hover();
		iface.getControl(Strategy.ID, "last_name").sendString(lastName);
		iface.getControl(Strategy.ID, "SAVE_HEADER").click();
		iface.getControl(Strategy.XPATH, "//*[@id=\"detail_header_action_menu\"]/li/span").click();
		iface.getControl(Strategy.ID, "duplicate_button").click();
		iface.getControl(Strategy.ID, "last_name").halt(4);
		iface.getControl(Strategy.ID, "last_name").sendString(lastNameDup);
		iface.getControl(Strategy.ID, "SAVE_HEADER").click();
		
	}

	@Override
	@After
	public void cleanup() throws Exception {
		// Perform cleanup here -- remove any records that you created so you can rerun this test without conflict
		super.cleanup();
	}

	@AfterClass
	public static void last() throws Exception { SugarTest.last(); }
}

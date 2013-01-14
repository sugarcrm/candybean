package com.sugarcrm.sugar;


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


public class Activities_0459 extends SugarTest {
	
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
		
//// Activities_0459 - navigate to Calendar tab and assert Calender is displayed
		
		
		// Hover on More tab
		iface.getControl(Strategy.ID, "moduleTabExtraMenuAll").hover();
		
		// Wait until Calendar tab is visible
		iface.getControl(Strategy.ID, "moduleTab_AllCalendar").halt(4);
		
		// Click on Calendar Tab
		iface.getControl(Strategy.ID, "moduleTab_AllCalendar").click();
		
		// Save value from 'moduleTitle' div (Calendar) to a variable
		String elementText = iface.getControl(Strategy.CSS, "#content div.moduleTitle h2").getText();
		
		// Check if variable contains expected value
		if (elementText.equals("Calendar")){ voodoo.log.info("Test Passed: Calendar displayed as expected"); }
		else{ voodoo.log.info("Test Failed: Calendar not found for moduleTitle element"); }
		
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

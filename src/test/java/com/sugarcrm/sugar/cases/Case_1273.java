package com.sugarcrm.sugar.cases;


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


public class Case_1273 extends SugarTest {
	
	@BeforeClass
	public static void setupOnce() throws Exception { SugarTest.setupOnce(); }

	String testName = "Accounts_0001";
	String testCaseName = "Cases_0001";
	
	@Override
	@Before
	public void setup() throws Exception {
		super.setup();
		//create an account
        iface.getControl(Strategy.ID, "moduleTab_AllAccounts").hover();
        iface.pause(1000);
        iface.getControl(Strategy.ID, "CreateAccountAll").click();
        iface.pause(2000);
        iface.getControl(Strategy.ID, "name").click(); 
        //workaround for home menu issue
        iface.getControl(Strategy.ID, "name").sendString(testName);
        iface.pause(1000);
        iface.getControl(Strategy.ID, "SAVE_HEADER").click();
        iface.pause(3000);
        //need assertion
		//create an case
		iface.getControl(Strategy.ID, "moduleTabExtraMenuAll").click();
        iface.pause(1000);
        iface.getControl(Strategy.ID, "moduleMenuOverFlowMoreAll").click();
        iface.pause(1000);
        iface.getControl(Strategy.ID, "moduleTab_AllCases").hover();
        iface.pause(1000);
        iface.getControl(Strategy.ID, "CreateCaseAll").click();
        iface.pause(2000);
        iface.getControl(Strategy.ID, "account_name").sendString(testName);
        iface.pause(300); //relate to account
        iface.getControl(Strategy.ID, "name").sendString(testCaseName);
        iface.pause(1000);
        iface.getControl(Strategy.ID, "SAVE_HEADER").click();
        iface.pause(3000);
        //need assertion
	}
	
	@Test
	public void test() throws Exception {
		// basic search case
		iface.getControl(Strategy.ID, "moduleTab_AllCases").hover();
		iface.pause(2000);
		iface.getControl(Strategy.ID, "ViewCasesAll").click();
		iface.pause(2000);
		iface.getControl(Strategy.ID, "search_form_clear").click();
        iface.pause(2000);
        iface.getControl(Strategy.ID, "name_basic").sendString(testCaseName);
        iface.pause(2000);
        iface.getControl(Strategy.ID, "search_form_submit").click();
        iface.pause(2000);
        //need assertion on result case
        String caseLinkText=iface.getControl(Strategy.PLINK, testCaseName).getText();
        if (caseLinkText.equals(testCaseName)) {
        	//pass
        }
        else {
        	//failed
        }
        
        iface.pause(2000); 
		
	}

	@Override
	@After
	public void cleanup() throws Exception {
		// delete case
		iface.getControl(Strategy.ID, "moduleTab_AllCases").hover();
		iface.pause(2000);
		iface.getControl(Strategy.ID, "ViewCasesAll").click();
		iface.pause(2000); //stuck in loading page ...
		iface.getControl(Strategy.ID, "search_form_clear").click();
        iface.pause(2000);
        iface.getControl(Strategy.ID, "search_form_submit").click();
        iface.pause(2000);
        iface.getControl(Strategy.ID, "massall_top").click();
        iface.pause(2000);
        iface.getControl(Strategy.ID, "delete_listview_top").click();
        iface.pause(2000);
        iface.acceptDialog();
        iface.pause(2000);
		//delete account
        iface.getControl(Strategy.ID, "moduleTab_AllAccounts").hover();
        iface.pause(1000);
		iface.getControl(Strategy.ID, "ViewAccountsAll").click();
		iface.pause(2000);
		iface.getControl(Strategy.ID, "search_form_clear").click();
        iface.pause(2000);
        iface.getControl(Strategy.ID, "search_form_submit").click();
        iface.pause(2000);
        iface.getControl(Strategy.ID, "massall_top").click();
        iface.pause(2000);
        iface.getControl(Strategy.ID, "delete_listview_top").click();
        iface.pause(2000);	
        iface.acceptDialog();
		super.cleanup();
	}

	@AfterClass
	public static void cleanupOnce() { SugarTest.cleanupOnce(); }
}

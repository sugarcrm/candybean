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
import com.sugarcrm.sugar.portal.Portal;
import com.sugarcrm.sugar.teams.Team;
import com.sugarcrm.sugar.teams.Team.TeamBuilder;
import com.sugarcrm.sugar.users.User;
import com.sugarcrm.sugar.users.Users;
import com.sugarcrm.voodoo.automation.control.VHook.Strategy;


public class Campaign_0839Test extends SugarTest {
	
	private Team team;
	@BeforeClass
	public static void first() throws Exception { SugarTest.first(); }

	@Override
	@Before
	public void setup() throws Exception {
		super.setup();
		// Assume you're already logged into Sugar -- perform setup here
		iface.getControl(sugar.getHook("navbar_menu_campaigns")).hover();
		iface.getControl(sugar.getHook("navbar_menuitem_createcampaign")).halt(4);
		iface.getControl(sugar.getHook("navbar_menuitem_createcampaign")).click();
		iface.getControl(sugar.getHook("campaigns_textfield_name")).halt(4);
		iface.getControl(sugar.getHook("campaigns_textfield_name")).hover();
		iface.getControl(sugar.getHook("campaigns_textfield_name")).sendString("HackCamp");
		iface.getSelect(sugar.getHook("campaigns_dropdown_status")).halt(4);
		iface.getSelect(sugar.getHook("campaigns_dropdown_status")).select("Active");
		iface.getSelect(sugar.getHook("campaigns_dropdown_type")).halt(4);
		iface.getSelect(sugar.getHook("campaigns_dropdown_type")).select("Mail");
		iface.getControl(sugar.getHook("campaigns_date_enddate")).halt(4);
		iface.getControl(sugar.getHook("campaigns_date_enddate")).sendString("12/21/2012");
		iface.getControl(sugar.getHook("campaigns_button_saveheader")).click();

		//Assertion to be added
		

	}
	
	@Test
	public void test() throws Exception {
		// Add test steps and automation here; some examples:
//		iface.getControl(Strategy.XPATH, "//td[3]/a").click();
//		iface.getControl(Strategy.ID, "//div[4]/div[2]/a").hover();
//		iface.getControl(Strategy.PLINK, "accounts").click();
//		iface.getControl(Strategy.ID, "username").sendString("username");
		iface.getControl(sugar.getHook("campaigns_button_edit")).click();	
		iface.getControl(sugar.getHook("campaigns_textfield_name")).halt(4);
		iface.getControl(sugar.getHook("campaigns_textfield_name")).hover();
		iface.getControl(sugar.getHook("campaigns_textfield_name")).sendString("HackCamp-Modified");
		iface.getSelect(sugar.getHook("campaigns_dropdown_status")).halt(4);
		iface.getSelect(sugar.getHook("campaigns_dropdown_status")).select("Planning");
		iface.getSelect(sugar.getHook("campaigns_dropdown_type")).halt(4);
		iface.getSelect(sugar.getHook("campaigns_dropdown_type")).select("Web");
		iface.getControl(sugar.getHook("campaigns_date_enddate")).halt(4);
		iface.getControl(sugar.getHook("campaigns_date_enddate")).sendString("12/21/2012");
		iface.getControl(sugar.getHook("campaigns_button_saveheader")).click();		

//		Assertion to be added		
	}

	@Override
	@After
	public void cleanup() throws Exception {
		// Perform cleanup here -- remove any records that you created so you can rerun this test without conflict
		iface.getControl(sugar.getHook("navbar_menu_campaigns")).click();
		//iface.getControl(sugar.getHook("navbar_menu_user")).hover();
		iface.getControl(sugar.getHook("campaignsearch_checkbox_selectall")).halt(4);
		iface.getControl(sugar.getHook("campaignsearch_checkbox_selectall")).click();
		iface.getControl(sugar.getHook("campaignsearch_button_deleteall")).halt(4);
		iface.getControl(sugar.getHook("campaignsearch_button_deleteall")).click();
		iface.acceptDialog();
		
		//super.cleanup();
	}

	@AfterClass
	public static void last() throws Exception { SugarTest.last(); }
}

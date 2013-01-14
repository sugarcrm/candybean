package com.sugarcrm.sugar;


import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.android.library.AlertManager;

//import com.sugarcrm.library.ContactRecord;
import com.gargoylesoftware.htmlunit.AlertHandler;
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


public class contacts_1460 extends SugarTest {
	
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
//		iface.getControl(Strategy.XPATH, "//td[3]/a").click();
//		iface.getControl(Strategy.ID, "//div[4]/div[2]/a").hover();
//		iface.getControl(Strategy.PLINK, "accounts").click();
//		iface.getControl(Strategy.ID, "username").sendString("username");
//		ContactRecord myContact = (ContactRecord)sugar.contacts.create();
		String lastName = "asdf";
		//ContactBuilder cb = new ContactBuilder("asdf");
		//Contact contact = cb.build();
		//Contacts.create(sugar, iface, contact);
		
		iface.getControl(sugar.getHook("navbar_menu_contacts")).hover();
		iface.getControl(sugar.getHook("navbar_menuitem_createcontact")).halt(4);
		iface.getControl(sugar.getHook("navbar_menuitem_createcontact")).click();
		iface.getControl(sugar.getHook("contacts_textfield_lastname")).halt(4);
		iface.getControl(sugar.getHook("contacts_textfield_lastname")).hover();
		iface.getControl(sugar.getHook("contacts_textfield_lastname")).sendString(lastName);
		iface.getControl(sugar.getHook("contacts_div_bottomlinks")).scroll();
		iface.getControl(sugar.getHook("contacts_button_savefooter")).click();

		// Verify the returned data object using the GUI
		iface.getControl(Strategy.ID, "moduleTab_AllContacts").click();
		iface.getControl(Strategy.ID,"search_name_basic").sendString(lastName);
		iface.getControl(Strategy.ID, "search_form_submit").click();
		iface.getControl(Strategy.PLINK, lastName).halt(4);
		String results = iface.getControl(Strategy.CSS, "#MassUpdate table.list tbody tr.pagination td table.paginationTable tbody tr td.paginationChangeButtons span.pageNumbers").getText();
		Assert.assertTrue(results.equalsIgnoreCase("(1 - 1 of 1)"));
	}

	@Override
	@After
	public void cleanup() throws Exception {
		// Perform cleanup here -- remove any records that you created so you can rerun this test without conflict
		//Contacts.deleteAll(sugar, iface);
		iface.getControl(Strategy.ID, "massall_top").click();
		iface.getControl(Strategy.ID, "delete_listview_top").click();
	    iface.acceptDialog();
	    iface.getControl(Strategy.ID, "search_form_clear").click();
	    iface.getControl(Strategy.ID, "search_form_submit").click();
		super.cleanup();
	}

	@AfterClass
	public static void cleanupOnce() { SugarTest.cleanupOnce(); }
}

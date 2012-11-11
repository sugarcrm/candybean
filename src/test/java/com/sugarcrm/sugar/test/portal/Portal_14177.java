package com.sugarcrm.sugar.test.portal;

import javax.swing.JOptionPane;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sugarcrm.sugar.Sugar;
import com.sugarcrm.sugar.SugarTest;
import com.sugarcrm.sugar.accounts.Account;
import com.sugarcrm.sugar.accounts.Accounts;
import com.sugarcrm.sugar.accounts.Account.AccountBuilder;
import com.sugarcrm.sugar.admin.Administration;
import com.sugarcrm.sugar.cases.Case;
import com.sugarcrm.sugar.cases.Case.CaseBuilder;
import com.sugarcrm.sugar.contacts.Contact;
import com.sugarcrm.sugar.contacts.Contact.ContactBuilder;
import com.sugarcrm.sugar.contacts.Contacts;
import com.sugarcrm.sugar.cases.Cases;
import com.sugarcrm.sugar.portal.Portal;
import com.sugarcrm.voodoo.automation.IAutomation.Strategy;
import com.sugarcrm.voodoo.automation.control.VControl;
import com.sugarcrm.voodoo.automation.control.VHook;


public class Portal_14177 extends SugarTest {
	
	private Contact portalContact;
	
	@BeforeClass
	public static void setupOnce() throws Exception { SugarTest.setupOnce(); }

	@Override
	@Before
	public void setup() throws Exception {
		super.setup();
//		1. Portal side is installed successfully.
		Administration.selectPortalEnable(voodoo, sugar);
//		2. Create one portal user in Sugar server side.
//		portalUser = (new UserBuilder("cw_test", "Conrad Warmbold", "Password1", "Password1"), voodoo.auto)).buildPortalUser();
//		Users.create(voodoo, sugar, portalUser);
//		3. Create a contact record with portal name, portal access URL, password and account fields filled, such as:
//		Contact Name -> contact1, Account Name -> account1, Portal Name -> PortalTest1, Password-> a, check box "Portal Active"
		ContactBuilder cb = new ContactBuilder("Warmbold");
		Account account = (new AccountBuilder("acct_cwarmbold")).build();
		cb.withAccount(account).withPortalName("cwarmbold").withPortalActive(true).withPortalPassword("Sugar123");
		portalContact = cb.build();
		Accounts.create(voodoo, sugar, account);
		Contacts.create(voodoo, sugar, portalContact);
	}
	
	@Test
	public void test() throws Exception {
//		1. Login to Portal side as a valid user mentioned in precondition2.
		Sugar.logout(voodoo, sugar);
		Portal.login(voodoo, sugar, portalContact.portalName(), portalContact.portalPassword());
//		2. Go to Cases module.		
//		3. Click "Create New" link.
//		4. Fill all mandatory fields and click "Save" button.
		CaseBuilder portalCB = new CaseBuilder("portalCase");
		CaseBuilder sugarCB = new CaseBuilder("sugarCase");
		Case portalCase = portalCB.build();
		Portal.Cases.create(voodoo, sugar, portalCase);
//		5. Login to Sugar server side as a valid user, such as admin user.
		Portal.logout(voodoo, sugar);
		Sugar.login(voodoo, sugar, "admin", "asdf");
//		6. Go to Cases module in server side. 
//		7. Search the case created from above.
//			7. ***The new case is found.
//		8. Edit the case record, such as all fields.
//		8. ***The case is updated successfully in Sugar server side.
		Case sugarCase = sugarCB.build();
		Cases.read(voodoo, sugar, portalCase);
		(new VControl(new VHook(Strategy.PLINK, portalCase.subject()), voodoo.auto)).click();
		voodoo.pause(400);
		(new VControl(sugar.getHook("cases_button_edit"), voodoo.auto)).click();
		(new VControl(sugar.getHook("cases_textfield_subject"), voodoo.auto)).sendString(sugarCase.subject());
		(new VControl(new VHook(Strategy.ID, "teamSelect"), voodoo.auto)).click();
		voodoo.auto.focusByIndex(1);
		(new VControl(new VHook(Strategy.PLINK, "Administrator"), voodoo.auto)).click();
		voodoo.auto.focusByIndex(0);
//		9. Click Save button.
		(new VControl(sugar.getHook("cases_button_saveheader"), voodoo.auto)).click();
//		10. The same Portal user log in to Portal again.
		Sugar.logout(voodoo, sugar);
		Portal.login(voodoo, sugar, portalContact.portalName(), portalContact.portalPassword());
//			10.***The changes are also shown on the portal side.
//		11. Navigate to Cases tab and search the same case.
		Portal.Cases.read(voodoo, sugar);
		String caseSubject = (new VControl(new VHook(Strategy.XPATH, "/html/body/div/div/div[3]/div/div/div/div/div[3]/table/tbody/tr/td[2]/span/a"), voodoo.auto)).getText();
		Assert.assertEquals("Expected case subject does not match actual subject: " + caseSubject, caseSubject, sugarCase.subject());
		Portal.logout(voodoo, sugar);
		Sugar.login(voodoo, sugar, "admin", "asdf");
	}

	@Override
	@After
	public void cleanup() throws Exception {
//		Users.deleteUser(voodoo, sugar, portalUser);
		Administration.selectPortalEnable(voodoo, sugar);
		super.cleanup();
	}

	@AfterClass
	public static void cleanupOnce() { SugarTest.cleanupOnce(); }
}

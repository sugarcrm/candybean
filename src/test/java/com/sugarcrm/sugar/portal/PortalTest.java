package com.sugarcrm.sugar.portal;


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


public class PortalTest extends SugarTest {
	
	private Contact portalContact;
	private User portalUser;
	private Team team;
	
	@BeforeClass
	public static void setupOnce() throws Exception { SugarTest.setupOnce(); }

	@Override
	@Before
	public void setup() throws Exception {
		super.setup();
//		1. Portal side is installed successfully.
		Administration.setPortalEnabled(voodoo, sugar, true);
//		2. Create one portal user in Sugar server side.
//		3. Create a contact record with portal name, portal access URL, password and account fields filled, such as:
//		Contact Name -> contact1, Account Name -> account1, Portal Name -> PortalTest1, Password-> a, check box "Portal Active"
		ContactBuilder cb = new ContactBuilder("Warmbold");
		Account account = (new AccountBuilder("acct_cwarmbold")).build();
		cb.withAccount(account).withPortalName("cwarmbold").withPortalActive(true).withPortalPassword("Sugar123");
		portalContact = cb.build();
		Accounts.create(voodoo, sugar, account);
		Contacts.create(voodoo, sugar, portalContact);
		team = (new TeamBuilder("Administrator")).build();
	}
	
	@Test
	public void test14177() throws Exception {
//		1. Login to Portal side as a valid user mentioned in precondition2.
		Sugar.logout(voodoo, sugar);
		Portal.login(voodoo, sugar, portalContact.portalName(), portalContact.portalPassword());
//		2. Go to Cases module.		
//		3. Click "Create New" link.
//		4. Fill all mandatory fields and click "Save" button.
		CaseBuilder portalCB = new CaseBuilder("portalCase", this.team);
		CaseBuilder sugarCB = new CaseBuilder("sugarCase", this.team);
		Case portalCase = portalCB.build();
		Portal.Cases.create(voodoo, sugar, portalCase);
//		5. Login to Sugar server side as a valid user, such as admin user.
		Portal.logout(voodoo, sugar);
		Sugar.login(voodoo, sugar, SugarTest.admin.username(), SugarTest.admin.password1());
//		6. Go to Cases module in server side. 
//		7. Search the case created from above.
//			7. ***The new case is found.
//		8. Edit the case record, such as all fields.
//		8. ***The case is updated successfully in Sugar server side.
//		9. Click Save button.
		Case sugarCase = sugarCB.build();
		Cases.modify(voodoo, sugar, portalCase, sugarCase);
//		10. The same Portal user log in to Portal again.
		Sugar.logout(voodoo, sugar);
		Portal.login(voodoo, sugar, portalContact.portalName(), portalContact.portalPassword());
//			10.***The changes are also shown on the portal side.
//		11. Navigate to Cases tab and search the same case.
		String firstCaseSubjectText = Portal.Cases.readFirstCaseSubjectText(voodoo, sugar);
		Assert.assertEquals("Expected case subject does not match actual subject: " + firstCaseSubjectText, firstCaseSubjectText, sugarCase.subject());
		Portal.logout(voodoo, sugar);
		Sugar.login(voodoo, sugar, SugarTest.admin.username(), SugarTest.admin.password1());
	}

	@Override
	@After
	public void cleanup() throws Exception {
		// TODO this doesn't appear to clean up the portal user entirely and it may be a bug; the username created can't be completely 'reset' for reuse
//		Users.delete(voodoo, sugar, portalUser);
		Contacts.deleteAll(voodoo, sugar);
		Accounts.deleteAll(voodoo, sugar);
		Administration.setPortalEnabled(voodoo, sugar, false);
		super.cleanup();
	}

	@AfterClass
	public static void cleanupOnce() { SugarTest.cleanupOnce(); }
}

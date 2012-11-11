package com.sugarcrm.sugar.test.forecasts;

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
import com.sugarcrm.voodoo.IAutomation.Strategy;
import com.sugarcrm.voodoo.automation.VHook;


public class Forecasts_10968 extends SugarTest {
	
	@BeforeClass
	public static void setupOnce() throws Exception { SugarTest.setupOnce(); }

	@Override
	@Before
	public void setup() throws Exception {
		super.setup();
//		1. All default Time Periods are removed from the system 
//		2. 5 custom time periods exist with the following data (YY represents the current year, MM represents the current month, 2M represents month MM+2, 3M = MM+3, 6M = MM+6)
//        Name      -     Start Date       -       End Date     - Is Fiscal Year    -    Fiscal Year    - Forecast Start Date
//        Time1     -       MM/1/YY        -       12/31/YY     - No                -    None           - 1/1/YY
//        Time2     -       1/1/YY         -       12/31/YY     - Yes               -    N/A            - 1/1/YY
//        Time3     -       MM/1/YY        -       2M/30/YY     - No                -    Time2          - 1/1/YY
//        Time4     -       MM/1/YY        -       3M/30/YY     - No                -    None           - 1/1/YY
//        Time5     -       MM/1/YY        -       6M/30/YY     - No                -    None           - 1/1/YY
	}
	
	@Test
	public void test() throws Exception {
//		Log in a regular user of Sugar
//		Navigate to Forecasts module
//		Click on the Time Period drop down
//
//		Verify that sort order of the time periods is by start date first, then end date
	}

	@Override
	@After
	public void cleanup() throws Exception {
		super.cleanup();
	}

	@AfterClass
	public static void cleanupOnce() { SugarTest.cleanupOnce(); }
}

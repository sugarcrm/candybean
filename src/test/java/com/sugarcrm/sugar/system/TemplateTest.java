package com.sugarcrm.sugar.system;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sugarcrm.sugar.system.SugarTest;

public class TemplateTest extends SugarTest {
	
	@BeforeClass
	public static void first() {
		try { SugarTest.first(); }
		catch (Exception e) { e.printStackTrace(); }
	}
	
	@Before
	public void setup() {
		try {
			super.setup();
			sugar.login(sugar.admin.username, sugar.admin.password1);
		} catch (Exception e) {
			e.printStackTrace();			
			/* QA ENTERS SETUP HERE */
		}
	}

	@Test
	public void execute() {
		try {
			/* QA ENTERS TEST STEPS HERE */
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@After
	public void cleanup() {
		try {
			sugar.logout();
			super.cleanup();
			/* QA ENTERS CLEANUP HERE */
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@AfterClass
	public static void last() {
		try { SugarTest.last();	}
		catch (Exception e) { e.printStackTrace(); }
	}
}

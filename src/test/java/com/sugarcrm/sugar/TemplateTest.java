package com.sugarcrm.sugar;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sugarcrm.sugar.SugarTest;

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
			Sugar.login(sugar, iface, SugarTest.admin.username(), SugarTest.admin.password1());
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
			Sugar.logout(sugar, iface);
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

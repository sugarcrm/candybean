package com.sugarcrm.voodooo.android;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class CalculatorTest_0001 extends AndroidSugarTest {

	@BeforeClass
	public static void setupOnce() throws Exception {
		AndroidSugarTest.setupOnce();
	}

	@Override
	@Before 
	public void setup() throws Exception {
		super.setup();
	}

	@Test
	public void test() throws Exception {	
		//Enter 10 in first editfield
		iface.getAControl().enterText(0, "10");

		//Enter 20 in first editfield
		iface.getAControl().enterText(1, "20");

		//Click on Multiply button
		iface.getAControl().clickOnButton("Multiply");

		//Verify that resultant of 10 x 20 
		assertTrue(iface.getAControl().searchText("200"));
		
	}

	@Override
	@After
	public void cleanup() throws Exception {
		super.cleanup();
	}

	@AfterClass
	public static void cleanupOnce() {
		AndroidSugarTest.cleanupOnce(); 
	}
}
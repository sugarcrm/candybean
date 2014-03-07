package com.sugarcrm.candybean.examples.wolfram;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.sugarcrm.candybean.exceptions.CandybeanException;
import com.sugarcrm.candybean.test.BrowserTest;

/**
 * Wolfram alpha test class defining any tests using the {@link Test } annotation.
 * @author Shehryar Farooq
 *
 */
public class WolframAlphaTest extends BrowserTest {

	public WolframAlphaTest() throws Exception {
		super();
	}

	/**
	 * Association class containing test operations for this test.
	 */
	private static WolframAlpha wolfram;
	
	/**
	 * Automated addition test that verifies Wolfram Alpha's addition capabilities
	 * @throws Exception 
	 */
	@Test
	public void variousInputsTest() throws Exception{
		for(WolframInputType testType : WolframInputType.values()){
			wolfram.goToLandingPage();
			wolfram.askWolfram(testType.getInput());
			wolfram.waitForResult(2000);
			assertEquals(wolfram.verifyAnswer(testType.getAnswer()),true);
		}
	}
	
	@Override
	@Before
	public void setUp() throws CandybeanException {
		wolfram = new WolframAlpha(iface);
	}

	@Override
	@After
	public void tearDown() throws CandybeanException {
		iface.stop();
	}
}

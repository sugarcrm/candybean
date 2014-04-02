package com.sugarcrm.candybean.examples.wolfram;

import static org.junit.Assert.assertEquals;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import com.sugarcrm.candybean.automation.AutomationInterfaceBuilder;
import com.sugarcrm.candybean.automation.Candybean;
import com.sugarcrm.candybean.automation.AutomationInterface.Type;
import com.sugarcrm.candybean.automation.webdriver.WebDriverInterface;
import com.sugarcrm.candybean.exceptions.CandybeanException;

/**
 * Wolfram alpha test class defining any tests using the {@link Test } annotation.
 * @author Shehryar Farooq
 *
 */
public class WolframAlphaTest {
	/**
	 * Association class containing test operations for this test.
	 */
	private static WolframAlpha wolfram;
	
	public static WebDriverInterface iface;

	@BeforeClass
	public static void beforeClass() throws CandybeanException{
		Candybean candybean = Candybean.getInstance();
		AutomationInterfaceBuilder builder = candybean.getAIB(WolframAlphaTest.class);
		builder.setType(Type.CHROME);
		iface = builder.build();
	}
	
	@Before
	public void setUp() throws CandybeanException {
		iface.start();
		wolfram = new WolframAlpha(iface);
	}
	
	@After
	public void tearDown() throws CandybeanException {
		iface.stop();
	}
	
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
}

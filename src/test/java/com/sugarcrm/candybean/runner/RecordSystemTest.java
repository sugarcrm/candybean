package com.sugarcrm.candybean.runner;

import static org.junit.Assert.*;

import com.sugarcrm.candybean.configuration.Configuration;
import com.sugarcrm.candybean.testUtilities.TestConfiguration;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import com.sugarcrm.candybean.automation.AutomationInterfaceBuilder;
import com.sugarcrm.candybean.automation.Candybean;
import com.sugarcrm.candybean.automation.AutomationInterface.Type;
import com.sugarcrm.candybean.automation.webdriver.WebDriverInterface;
import com.sugarcrm.candybean.exceptions.CandybeanException;
import com.sugarcrm.candybean.runner.Duration;
import com.sugarcrm.candybean.runner.Record;
import com.sugarcrm.candybean.runner.VTag;
import com.sugarcrm.candybean.runner.VRunner;

/**
 * Show-cases the ability to record failing tests using {@link Record} annotation.
 * 
 * @author Shehryar Farooq
 */
@RunWith(VRunner.class)
public class RecordSystemTest {

	private WebDriverInterface iface;
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Before
	public void setUp() throws Exception {
		Configuration config = TestConfiguration.getTestConfiguration("systemtest.webdriver.config");
		Candybean candybean = Candybean.getInstance(config);
		AutomationInterfaceBuilder builder = candybean.getAIB(RecordSystemTest.class);
		builder.setType(Type.CHROME);
		iface = builder.build();
		iface.start();
	}
	
	@Test
	@Record(duration = Duration.FINAL_FAILED)
	@VTag(tags={"mac", "windows", "linux"}, tagLogicClass="com.sugarcrm.candybean.runner.VTagUnitTest", tagLogicMethod="processTags")
	public void passedUrlTest() throws Exception {
		String googleUrl = "https://www.google.com/";
		iface.go(googleUrl);
		assertEquals(googleUrl, iface.getURL());
	}
	
	@Test
	@Record(duration = Duration.FINAL_FAILED)
	@VTag(tags={"mac", "windows", "linux"}, tagLogicClass="com.sugarcrm.candybean.runner.VTagUnitTest", tagLogicMethod="processTags")
	public void failedUrlTest() throws Exception {
		String amazonUrl = "https://www.amazon.com/";
		String yahooUrl = "https://www.yahoo.com/";
		iface.go(amazonUrl);
		iface.go(yahooUrl);
		assertEquals(yahooUrl, iface.getURL());
	}

	@After
	public void last() throws Exception {
		iface.stop();
	}
}

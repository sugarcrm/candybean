package com.sugarcrm.candybean.runner;

import com.sugarcrm.candybean.automation.AutomationInterface.Type;
import com.sugarcrm.candybean.automation.AutomationInterfaceBuilder;
import com.sugarcrm.candybean.automation.Candybean;
import com.sugarcrm.candybean.automation.webdriver.WebDriverInterface;
import com.sugarcrm.candybean.configuration.Configuration;
import com.sugarcrm.candybean.testUtilities.TestConfiguration;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

/**
 * Show-cases the ability to record failing tests using {@link Record} annotation.
 *
 * @author Shehryar Farooq
 */
@RunWith(VRunner.class)
public class RecordSystemTest {
	final String testPlaygroundPage = "file://" + System.getProperty("user.dir") + "/resources/html/test/testPlayground.html";
	final String onOffScreenPage = "file://" + System.getProperty("user.dir") + "/resources/html/test/onOffScreen.html";

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
	@VTag(tags = {"mac", "windows", "linux"}, tagLogicClass = "com.sugarcrm.candybean.runner.VTagUnitTest", tagLogicMethod = "processTags")
	public void passedUrlTest() throws Exception {
		iface.go(testPlaygroundPage);
		assertEquals(testPlaygroundPage, iface.getURL());
	}

	@Test
	@Record(duration = Duration.FINAL_FAILED)
	@VTag(tags = {"mac", "windows", "linux"}, tagLogicClass = "com.sugarcrm.candybean.runner.VTagUnitTest", tagLogicMethod = "processTags")
	public void failedUrlTest() throws Exception {
		iface.go(testPlaygroundPage);
		iface.go(onOffScreenPage);
		assertEquals(onOffScreenPage, iface.getURL());
	}

	@After
	public void last() throws Exception {
		iface.stop();
	}
}

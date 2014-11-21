package com.sugarcrm.candybean.automation.webdriver;

import com.sugarcrm.candybean.automation.AutomationInterface;
import com.sugarcrm.candybean.automation.AutomationInterfaceBuilder;
import com.sugarcrm.candybean.automation.Candybean;
import com.sugarcrm.candybean.configuration.Configuration;
import com.sugarcrm.candybean.testUtilities.TestConfiguration;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;

import static org.junit.Assert.assertEquals;

/**
 * Created by etam on 10/14/14.
 */
public class GridSystemTest {

	private WebDriverInterface iface;

	@Before
	public void setUp() throws Exception {
		Configuration config = TestConfiguration.getTestConfiguration("systemtest.grid.config");
		Candybean candybean = Candybean.getInstance(config);

		AutomationInterfaceBuilder builder = candybean.getAIB(GridSystemTest.class);
		builder.setType(AutomationInterface.Type.CHROME);
		iface = builder.build();
		iface.start();
	}

	// CB-219: Getting a Selenium Grid Hub to run the GridSystemTest
	@Ignore
	@Test
	public void test() throws Exception {
		iface.go("http://www.google.com");
		assertEquals("Google", iface.wd.getTitle());
	}

	@After
	public void tearDown() throws Exception {
		iface.stop();
	}
}

package com.sugarcrm.candybean.runner;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import com.sugarcrm.candybean.automation.Candybean;
import com.sugarcrm.candybean.automation.webdriver.WebDriverInterface;
import com.sugarcrm.candybean.configuration.Configuration;
import com.sugarcrm.candybean.runner.Duration;
import com.sugarcrm.candybean.runner.Record;
import com.sugarcrm.candybean.runner.VTag;
import com.sugarcrm.candybean.runner.VRunner;
import com.sugarcrm.candybean.utilities.Utils;

/**
 * Show-cases the ability to record failing tests using {@link Record} annotation.
 * @author Shehryar Farooq
 *
 */
@RunWith(VRunner.class)
public class RecordSystemTest {
	
	protected static Candybean candybean;
	protected static WebDriverInterface iface;
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@BeforeClass
	public static void instantiateCb() throws Exception {
		String candybeanConfigStr = System.getProperty("candybean_config");
		if (candybeanConfigStr == null) candybeanConfigStr = Candybean.CONFIG_DIR.getCanonicalPath() + File.separator + "candybean.config";
		Configuration candybeanConfig = new Configuration(new File(Utils.adjustPath(candybeanConfigStr)));
		candybean = Candybean.getInstance(candybeanConfig);
	}
	
	@Test
	@Record(duration = Duration.FINAL_FAILED)
	@VTag(tags={"mac", "windows", "linux"}, tagLogicClass="com.sugarcrm.candybean.runner.VTagUnitTest", tagLogicMethod="processTags")
	public void passedUrlTest() throws Exception {
		String amazonUrl = "http://www.amazon.com/";
		iface.go(amazonUrl);
		assertEquals(amazonUrl, iface.getURL());		
	}
	
	@Ignore
	@Test
	@Record(duration = Duration.FINAL_FAILED)
	@VTag(tags={"mac", "windows", "linux"}, tagLogicClass="com.sugarcrm.candybean.runner.VTagUnitTest", tagLogicMethod="processTags")
	public void failedUrlTest() throws Exception {
		String amazonUrl = "http://www.amazon.com/";
		String yahooUrl = "https://yahoo.com";
		iface.go(amazonUrl);
		iface.go(yahooUrl);
		assertEquals(iface.getURL(), amazonUrl);		
	}

	@After
	public void last() throws Exception {
		iface.stop();
	}
}

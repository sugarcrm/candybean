package com.sugarcrm.candybean.test;

import java.io.File;
import java.io.IOException;
import org.junit.Before;
import org.junit.BeforeClass;
import com.sugarcrm.candybean.automation.Candybean;
import com.sugarcrm.candybean.configuration.Configuration;
import com.sugarcrm.candybean.exceptions.CandybeanException;

public abstract class MobileTest extends WebDriverTest {
	
	protected Configuration config;

	public MobileTest() throws IOException, CandybeanException {
		super();
		this.config = new Configuration(new File(Candybean.CONFIG_DIR + File.separator + "capabilities.config"));
	}

	@BeforeClass
	public static void instantiateInterface() throws CandybeanException {
		candybean = Candybean.getInstance();
		iface = candybean.getWebDriverInterface();
	}

	@Override
	@Before
	public abstract void setUp() throws CandybeanException;

	@Override
	@Before
	public abstract void tearDown() throws CandybeanException;
}

package com.sugarcrm.candybean.test;

import java.io.File;
import org.junit.Before;

import com.sugarcrm.candybean.configuration.Configuration;

public abstract class MobileTest extends AbstractTest {
	
	protected Configuration config;

	public MobileTest() throws Exception {
		super();
		this.config = new Configuration(new File(new File(System.getProperty("user.dir")
						+ File.separator + "config" + File.separator).getCanonicalPath() +File.separator+"capabilities.config"));
	}

}

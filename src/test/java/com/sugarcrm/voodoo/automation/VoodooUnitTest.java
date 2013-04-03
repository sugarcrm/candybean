package com.sugarcrm.voodoo.automation;

import java.io.File;

import org.junit.Ignore;
import org.junit.Test;

import com.sugarcrm.voodoo.automation.Voodoo;
import com.sugarcrm.voodoo.configuration.Configuration;


public class VoodooUnitTest {

	@Ignore
	@Test
	public void testVoodooLog() throws Exception {
		Configuration config = new Configuration();
		File testPropsFile = new File(System.getProperty("user.dir") + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "test.properties");
		config.load(testPropsFile);
		Voodoo voodoo = Voodoo.getInstance(config);
		voodoo.log.info("TEST VOODOO LOG");
	}
}

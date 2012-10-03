package com.sugarcrm.unit.voodoo;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import org.junit.Ignore;
import org.junit.Test;

import com.sugarcrm.voodoo.Voodoo;


public class TestVoodoo {

	@Ignore
	@Test
	public void testVoodooLog() throws Exception {
		Properties props = new Properties();
		File testPropsFile = new File(System.getProperty("user.dir") + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "test.properties");
		props.load(new FileInputStream(testPropsFile));
		Voodoo voodoo = Voodoo.getInstance(props);
		voodoo.log.info("TEST VOODOO LOG");
	}
}

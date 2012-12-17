package com.sugarcrm.voodoo.runner;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.sugarcrm.voodoo.runner.VoodooTag;
import com.sugarcrm.voodoo.runner.VoodooTagRunner;

@RunWith(VoodooTagRunner.class)
public class PlatformAnnotationTest {

	@Test
	@VoodooTag(OS="MAC")
	public void macSystemTest() throws Exception {
		System.out.println("[Tag Test]: Printing for Mac system only");
	}

	@Test
	@VoodooTag(OS="WINDOWS")
	public void windowsSystemTest() throws Exception {
		System.out.println("[Tag Test]: Printing for Windows system only");
	}

	@Test
	@VoodooTag(OS="UNIX")
	public void unixSystemTest() throws Exception {
		System.out.println("[Tag Test]: Printing for Unix system only");
	}

	@Test
	@VoodooTag(OS="SOLARIS")
	public void solarisSystemTest() throws Exception {
		System.out.println("[Tag Test]: Printing for Solaris system only");
	}

	@Test
	@VoodooTag(OS="MAC WINDOWS SOLARIS UNIX")
	public void AllPlatformTest() throws Exception {
		System.out.println("[Tag Test]: Printing for Mac, Windows, Solaris, and Unix systems");
	}

	@Test
	@VoodooTag
	public void defaultPlatformTest() throws Exception {
		System.out.println("[Tag Test]: Printing as default setting (Any OS)");
	}

}

package com.sugarcrm.voodoo.utilities;

import java.io.File;
import java.io.FileOutputStream;
import org.junit.Assert;

import org.junit.Test;

import com.sugarcrm.voodoo.configuration.Configuration;
import com.sugarcrm.voodoo.utilities.Utils;
import com.sugarcrm.voodoo.utilities.Utils.Pair;
import com.sugarcrm.voodoo.utilities.Utils.Triplet;


public class UtilsUnitTest {

	@Test
	public void testGetCascadingPropertyValue() {
		try {
			String currentWorkingPath = System.getProperty("user.dir") + File.separator;
			String propsFilePath = currentWorkingPath + "testutils.properties";
			//			System.out.println("Props file path: " + propsFilePath);
			String propKey = "key";
			String propSysKey = "syskey";
			String propConfigVal = "configvalue";
			String propDefaultVal = "defaultvalue";
			String propSysVal = "systemvalue";

			// Resource setup
			File propsFile = new File(propsFilePath);
			propsFile.createNewFile();
			Configuration voodooConfig = new Configuration();
			voodooConfig.setProperty(propKey, propConfigVal);
			voodooConfig.setProperty(propSysKey, propConfigVal);
			System.setProperty(propSysKey, propSysVal);
//			voodooConfig.store(new FileOutputStream(propsFile), null);
			//			JOptionPane.showInputDialog("pause");

			// Test
			String actualDefaultVal = voodooConfig.getValue("NULL", propDefaultVal);
			//			System.out.println("actualDefaultVal: " + actualDefaultVal);
			Assert.assertEquals("Expected default value.", propDefaultVal, actualDefaultVal);
			String actualConfigVal = voodooConfig.getValue(propKey, propDefaultVal);
			//			System.out.println("actualConfigVal: " + actualConfigVal);
			Assert.assertEquals("Expected configuration value.", propConfigVal, actualConfigVal);
			String actualSysVal = voodooConfig.getValue(propSysKey, propDefaultVal);
			//			System.out.println("actualSysVal: " + actualSysVal);
			Assert.assertEquals("Expected system value.", propSysVal, actualSysVal);

			// Resource cleanup
			System.clearProperty(propSysKey);
			propsFile.delete();
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Exception caught.");
		}
	}


	@Test
	public void testPretruncate() {
		String original = "ffour";
		String expected = "four";
		String actual = Utils.pretruncate(original, 4);
		Assert.assertEquals("Actual pretruncated string length does not match expected.", expected, actual);
	}

	@Test
	public void testAdjustPath() {
		String path1 = "~/computer\\  science\\Hello\\";
		String expected1 = "~/computer\\  science/Hello/";
		String path2 = "c:\\computer\\\"science\"\\";
		String expected2 = "c:/computer/\"science\"/";
		String path3 = "\\\"computer science\"\\";
		String expected3 = "/\"computer science\"/";
		String path4 = "cd /computer\\ science\\";
		String expected4 = "cd /computer\\ science/";
		String path5 = "computer\\ \\ \\ \\ \\ science/";
		String expected5 = "computer\\ \\ \\ \\ \\ science/";

		Assert.assertEquals(expected1, Utils.adjustPath(path1));
		Assert.assertEquals(expected2, Utils.adjustPath(path2));
		Assert.assertEquals(expected3, Utils.adjustPath(path3));
		Assert.assertEquals(expected4, Utils.adjustPath(path4));
		Assert.assertEquals(expected5, Utils.adjustPath(path5));
	}

	@Test
	public void testPair() {
		Object o1 = new Object();
		Object o2 = new Object();
		Pair<Object, Object> pair = new Pair<Object, Object>(o1, o2);
		Assert.assertEquals("Actual pair object x does not match expected, original object x.", pair.x, o1);
		Assert.assertEquals("Actual pair object y does not match expected, original object y.", pair.y, o2);
	}


	@Test
	public void testTriplet() {
		Object o1 = new Object();
		Object o2 = new Object();
		Object o3 = new Object();
		Triplet<Object, Object, Object> pair = new Triplet<Object, Object, Object>(o1, o2, o3);
		Assert.assertEquals("Actual pair object x does not match expected, original object x.", pair.x, o1);
		Assert.assertEquals("Actual pair object y does not match expected, original object y.", pair.y, o2);
		Assert.assertEquals("Actual pair object z does not match expected, original object z.", pair.z, o3);
	}
}

package com.sugarcrm.unit.voodoo;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Properties;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.voodoo.automation.Utils;
import com.sugarcrm.voodoo.automation.IAutomation.Strategy;
import com.sugarcrm.voodoo.automation.Utils.Pair;
import com.sugarcrm.voodoo.automation.Utils.Triplet;
import com.sugarcrm.voodoo.automation.control.VHook;


public class TestUtils {

	@Test
	public void testGetHooks() {
		try {
			String currentWorkingPath = System.getProperty("user.dir") + File.separator;
			String hooksFilePath = currentWorkingPath + "testutils.hooks";
			//			System.out.println("Hooks file path: " + hooksFilePath);

			// Resource setup
			File hooksFile = new File(hooksFilePath);
			hooksFile.createNewFile();
			Properties hooksProps = new Properties();
			String hook1Name = "hook1name";
			Strategy hook1Strategy = Strategy.ID;
			String hook1String = "hook1string";
			String hook2Name = "hook2name";
			Strategy hook2Strategy = Strategy.XPATH;
			String hook2String = "hook2string";
			hooksProps.setProperty(hook1Name, hook1Strategy + Utils.HOOK_DELIMITER + hook1String);
			hooksProps.setProperty(hook2Name, hook2Strategy + Utils.HOOK_DELIMITER + hook2String);
			hooksProps.store(new FileOutputStream(hooksFile), null);
			//			JOptionPane.showInputDialog("pause");

			// Test
			HashMap<String, VHook> hooksMap = Utils.getHooks(hooksProps);
			VHook hook1 = hooksMap.get(hook1Name);
			Assert.assertEquals("Test hook1 strategy doesn't match expected: " + hook1Strategy, hook1Strategy, hook1.hookStrategy);
			Assert.assertEquals("Test hook1 string doesn't match expected: " + hook1String, hook1String, hook1.hookString);
			VHook hook2 = hooksMap.get(hook2Name);
			Assert.assertEquals("Test hook2 strategy doesn't match expected: " + hook2Strategy, hook2Strategy, hook2.hookStrategy);
			Assert.assertEquals("Test hook2 string doesn't match expected: " + hook2String, hook2String, hook2.hookString);

			// Resource cleanup
			hooksFile.delete();
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Exception caught.");
		}

	}

	@Test
	public void testGetStrategy() {
		try {
			String[] strategyStrings = { "CSS", "ID", "NAME", "XPATH" };
			Strategy[] strategies = { Strategy.CSS, Strategy.ID, Strategy.NAME, Strategy.XPATH };
			for (int i = 0; i < strategyStrings.length; i++) {
				Strategy expectedStrategy = strategies[i];
				Strategy actualStrategy = Utils.getStrategy(strategyStrings[i]);
				Assert.assertEquals("Strategy doesn't match for given string: " + strategyStrings[i], expectedStrategy, actualStrategy);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Exception caught.");
		}
	}

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
			Properties voodooProps = new Properties();
			voodooProps.setProperty(propKey, propConfigVal);
			voodooProps.setProperty(propSysKey, propConfigVal);
			System.setProperty(propSysKey, propSysVal);
			voodooProps.store(new FileOutputStream(propsFile), null);
			//			JOptionPane.showInputDialog("pause");

			// Test
			String actualDefaultVal = Utils.getCascadingPropertyValue(voodooProps, propDefaultVal, "NULL");
			//			System.out.println("actualDefaultVal: " + actualDefaultVal);
			Assert.assertEquals("Expected default value.", propDefaultVal, actualDefaultVal);
			String actualConfigVal = Utils.getCascadingPropertyValue(voodooProps, propDefaultVal, propKey);
			//			System.out.println("actualConfigVal: " + actualConfigVal);
			Assert.assertEquals("Expected configuration value.", propConfigVal, actualConfigVal);
			String actualSysVal = Utils.getCascadingPropertyValue(voodooProps, propDefaultVal, propSysKey);
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

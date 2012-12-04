package com.sugarcrm.voodoo.automation.control;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Properties;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.voodoo.automation.control.VHook;
import com.sugarcrm.voodoo.automation.control.VHook.Strategy;
import com.sugarcrm.voodoo.utilities.Utils;
import com.sugarcrm.voodoo.utilities.Utils.Pair;
import com.sugarcrm.voodoo.utilities.Utils.Triplet;


public class VHookUnitTest {

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
			hooksProps.setProperty(hook1Name, hook1Strategy + VHook.HOOK_DELIMITER + hook1String);
			hooksProps.setProperty(hook2Name, hook2Strategy + VHook.HOOK_DELIMITER + hook2String);
			hooksProps.store(new FileOutputStream(hooksFile), null);
//			JOptionPane.showInputDialog("pause");

			// Test
			HashMap<String, VHook> hooksMap = VHook.getHooks(hooksProps);
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
				Strategy actualStrategy = VHook.getStrategy(strategyStrings[i]);
				Assert.assertEquals("Strategy doesn't match for given string: " + strategyStrings[i], expectedStrategy, actualStrategy);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Exception caught.");
		}
	}
}

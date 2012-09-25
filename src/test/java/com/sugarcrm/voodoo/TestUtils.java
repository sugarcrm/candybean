package com.sugarcrm.voodoo;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Properties;

import javax.swing.JOptionPane;

import org.junit.Assert;
import org.junit.Test;

import com.sugarcrm.voodoo.Utils.Pair;
import com.sugarcrm.voodoo.Utils.Triplet;


public class TestUtils {

	
	@Test
	public void testGetCascadingPropertyValue() {
		try {
			String currentWorkingPath = System.getProperty("user.dir") + File.separator;
			String propsFilePath = currentWorkingPath + "testutils.properties";
//			System.out.println("Props file path: " + propsFilesPath);
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

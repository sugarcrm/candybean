package com.sugarcrm.voodoo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import org.junit.Assert;
import org.junit.Test;


public class TestUtils {

	
	@Test
	public void testGetCascadingPropertyValue() {
		try {
			String propsFilePath = ".//src/test/resources/";
			String propsFilePrefix = "testutils";
			String propsFileSuffix = "_en_US.properties";
			String propsFileName = propsFilePath + propsFilePrefix + propsFileSuffix;
			String propKey = "key";
			String propSysKey = "syskey";
			String propConfigVal = "configvalue";
			String propDefaultVal = "defaultvalue";
			String propSysVal = "systemvalue";
		
			// Resource setup
//			File propsFile = new File(propsFileName);
//			propsFile.createNewFile();
//			BufferedWriter propsWriter = new BufferedWriter(new FileWriter(propsFile));
//			propsWriter.write(propKey + " = " + propConfigVal + System.lineSeparator());
//			propsWriter.write(propSysKey + " = " + propConfigVal + System.lineSeparator());
//			propsWriter.flush();
//			System.setProperty(propSysKey, propSysVal);
//			ResourceBundle rb = ResourceBundle.getBundle(propsFilePrefix);
			
			// Test
//			String actualDefaultVal = Utils.getCascadingPropertyValue(rb, propDefaultVal, "NULL");
//			System.out.println("actualDefaultVal: " + actualDefaultVal);
//			Assert.assertEquals("Expected default value.", propDefaultVal, actualDefaultVal);
//			String actualConfigVal = Utils.getCascadingPropertyValue(rb, propDefaultVal, propKey);
//			System.out.println("actualConfigVal: " + actualConfigVal);
//			Assert.assertEquals("Expected configuration value.", propConfigVal, actualConfigVal);
//			String actualSysVal = Utils.getCascadingPropertyValue(rb, propDefaultVal, propSysKey);
//			System.out.println("actualSysVal: " + actualSysVal);
//			Assert.assertEquals("Expected system value.", propSysVal, actualSysVal);

			// Resource cleanup
//			System.clearProperty(propKey);
//			propsWriter.close();
//			propsFile.delete();
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Exception caught.");
		}
	}

	
	@Test
	public void testTrimString() {
//		if (s.length() <= length)
//			return s;
//		return s.substring(s.length() - length);
	}

	
	@Test
	public void testPair() {
		
	}

	
	@Test
	public void testTriplet() {
	}
}

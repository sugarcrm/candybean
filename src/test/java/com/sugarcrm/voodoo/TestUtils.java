package com.sugarcrm.voodoo;

import java.io.File;


import org.junit.Test;


public class TestUtils {

	
	@Test
	public void testGetCascadingPropertyValue() {
		String propsFileName = "testutils.prop";
		String propKey = "key";
		String propConfigVal = "configvalue";
		String propDefaultVal = "defaultvalue";
		String propSysVal = "systemvalue";
	
		// Resource setup
		File propsFile = new File(propsFileName);
		
		// Test
//		BufferedWriter propsWriter = new BufferedWriter(new FileWriter(propsFile));
//		propsWriter.write(propKey + " = " + propConfigVal);
//		ResourceBundle rb = ResourceBundle.getBundle(propsFileName);
//		String actualDefaultVal = Utils.getCascadingPropertyValue(rb, propsDefault, key);
//		Assert.assertEquals("", expected, actual)
		
		// Resource cleanup
		propsFile.delete();
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

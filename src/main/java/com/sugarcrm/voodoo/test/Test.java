package com.sugarcrm.voodoo.test;


/**
 * @author Conrad Warmbold
 *
 */
public class Test {
	
	public boolean assertEqual(Object expectedObject, Object actualObject, String failMessage) {
//		log(failMessage);
		return expectedObject.equals(actualObject);
	}
	
	public boolean assertTrue(String failMessage, boolean test) {
//		log(failMessage);
		return test;
	}

}

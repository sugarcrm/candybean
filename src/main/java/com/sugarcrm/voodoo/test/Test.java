package com.sugarcrm.voodoo.test;

import java.util.logging.Logger;


/**
 * @author Conrad Warmbold
 *
 */
public class Test {
	
	public static boolean assertEqual(Object expectedObject, Object actualObject, String failMessage, Logger log) {
		log.info(failMessage);
		return expectedObject.equals(actualObject);
	}
	
	public static boolean assertTrue(boolean test, String failMessage, Logger log) {
		log.info(failMessage);
		return test;
	}

}

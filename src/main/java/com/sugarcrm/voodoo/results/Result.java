package com.sugarcrm.voodoo.results;


import java.util.Date;
import java.util.HashSet;


/**
 * @author Conrad Warmbold
 *
 * Format: [TIMESTAMP] [TYPE] [CLASS] [TEST] [FAIL_MESSAGE] [TAG(S)]
 *
 */
public class Result {
	
	public static enum Type { PASS, FAIL, ERROR, NOTE; }
	
	private String line;
	private Date timeStamp;
	private Result.Type type;
	private String className;
	private String testName;
	private String failMessage;
	private HashSet<String> tags;

	public Result(String line) { 
		this.line = line;
		// split line and store each; check for optional tokens
	}
}

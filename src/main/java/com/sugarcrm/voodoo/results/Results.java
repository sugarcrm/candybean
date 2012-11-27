package com.sugarcrm.voodoo.results;


import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Scanner;


/**
 * @author Conrad Warmbold
 *
 */
public class Results {
	
	public static enum Type { PASS, FAIL, ERROR, NOTE; }
	private ArrayList<Results.Result> results = new ArrayList<Result>();
	
	public Results(File resultsFile) throws Exception {
		Scanner scanner = new Scanner(resultsFile);
		while (scanner.hasNextLine()) {
            results.add(new Result(scanner.nextLine()));
        }
		scanner.close();
	}
	
	public String html() throws Exception {
		throw new Exception("html not implemented yet...");
	}
	
	public String string() throws Exception {
		throw new Exception("string not implemented yet...");
	}
	
	public String xml() throws Exception {
		throw new Exception("xml not implemented yet...");
	}
	
	public boolean verifyFile(File candidate) throws Exception {
		throw new Exception("Verify not yet implemented...");
	}
	
	/**
	 * @author Conrad Warmbold
	 * 
	 * Format: [TIMESTAMP] [TYPE] [CLASS] [TEST] [FAIL_MESSAGE] [TAG(S)]
	 */
	private class Result {
		private String line;
		private Date timeStamp;
		private Results.Type type;
		private String className;
		private String testName;
		private String failMessage;
		private HashSet<String> tags;
		public Result(String line) { 
			this.line = line;
			// split line and store each; check for optional tokens
		}
	}
}

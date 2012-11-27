package com.sugarcrm.voodoo.results;


import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;


/**
 * @author Conrad Warmbold
 *
 */
public class Results {
	
	ArrayList<Results.Result> results = new ArrayList<Result>();
	
	public Results(File resultsFile) throws Exception {
		Scanner scanner = new Scanner(resultsFile);
		while (scanner.hasNextLine()) {
            results.add(new Result(scanner.nextLine()));
        }
		scanner.close();
	}
	
	public boolean verifyFile(File candidate) throws Exception {
		throw new Exception("Verify not yet implemented...");
	}
	
	private class Result {
		private String line;
		public Result(String line) { this.line = line; }
	}
}

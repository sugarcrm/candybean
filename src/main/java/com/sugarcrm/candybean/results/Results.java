/**
 * Candybean is a next generation automation and testing framework suite.
 * It is a collection of components that foster test automation, execution
 * configuration, data abstraction, results illustration, tag-based execution,
 * top-down and bottom-up batches, mobile variants, test translation across
 * languages, plain-language testing, and web service testing.
 * Copyright (C) 2013 SugarCRM, Inc. <candybean@sugarcrm.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.sugarcrm.candybean.results;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author Conrad Warmbold
 *
 */
public class Results {
	
	public static enum Type { PASS, FAIL, ERROR, NOTE; }
	private List<Result> results = new ArrayList<Result>();
	
	public Results(File resultsFile) throws FileNotFoundException {
		Scanner scanner = new Scanner(resultsFile);
		while (scanner.hasNextLine()) {
            results.add(new Result(/*scanner.nextLine()*/));
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
}

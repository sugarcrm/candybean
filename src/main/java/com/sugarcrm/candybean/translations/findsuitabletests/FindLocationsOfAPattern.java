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
package com.sugarcrm.candybean.translations.findsuitabletests;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class FindLocationsOfAPattern {
	private static int fileNumber = 0;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String patternsPath = args[0];
		String testPath = args[1];
		String outputPath = args[2];
		String listOfPatternsPath = args[3];	
		try {
			findAllPatterns(patternsPath, testPath, outputPath);
			int lineNumber = 0;
			Scanner s = new Scanner(new File(patternsPath));
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(outputPath + File.separator + listOfPatternsPath)));
			while (s.hasNext()) {
				lineNumber++;
				bw.write(lineNumber + ". " + s.nextLine().trim() + "\n");
			}
			s.close();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static boolean foundPatternInFile(String pattern, String testFile) throws IOException {
		Scanner s = new Scanner(new File(testFile));
		String line = null;

		while (s.hasNext()) {
			line = s.nextLine().trim();
			if (line.equals(pattern)) {
				s.close();
				return true;
			}
		}
		
		s.close();
		return false;
	}

	private static void recursivelyFindPatternInAllFiles(String pattern, String testPath, String outputPath) throws IOException {
		File inputFile = new File(testPath);

		if (inputFile.isFile() && inputFile.getName().contains(".xml") && foundPatternInFile(pattern, testPath)) {
			File outputFile = new File(outputPath + File.separator + fileNumber);
			File outputFileParent = outputFile.getParentFile();
			if (!outputFileParent.exists())
				outputFileParent.mkdirs();
			BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile, true));
			bw.write(testPath + "\n");
			bw.close();
		} else if (inputFile.isDirectory()) {
			File[] files = inputFile.listFiles();
			for (File file : files) {
				recursivelyFindPatternInAllFiles(pattern, testPath + File.separator + file.getName(), outputPath);
			}
		}
	}

	public static void findAllPatterns(String patternsPath, String testPath, String outputPath) throws IOException {
		Scanner s = new Scanner(new File(patternsPath));

		while (s.hasNext()) {
			fileNumber++;
			String pattern = s.nextLine().trim();
			File file = new File(outputPath);
			File parent = file.getParentFile();
			if (!parent.exists())
				parent.mkdirs();
			
			recursivelyFindPatternInAllFiles(pattern, testPath, outputPath);
		}
		
		s.close();
	}
}

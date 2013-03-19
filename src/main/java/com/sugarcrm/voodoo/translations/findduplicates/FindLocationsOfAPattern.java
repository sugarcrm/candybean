package com.sugarcrm.voodoo.translations.findduplicates;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class FindLocationsOfAPattern {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String patternsPath = args[0];
		String testPath = args[1];
		String outputPath = args[2];
		try {
			findAllPatterns(patternsPath, testPath, outputPath);
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
			//String tmp = pattern.substring(11);
			//String fileName = tmp.substring(0, tmp.length() - 3);
			File outputFile = new File(outputPath + File.separator + pattern.substring(11).substring(0, pattern.length() - 13));
			File outputFileParent = outputFile.getParentFile();
			if (!outputFileParent.exists())
				outputFileParent.mkdirs();
			FileWriter fw = new FileWriter(outputFile);
			fw.append(testPath);
			fw.close();
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

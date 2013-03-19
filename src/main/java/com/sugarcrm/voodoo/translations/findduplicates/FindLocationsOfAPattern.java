package com.sugarcrm.voodoo.translations.findduplicates;

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

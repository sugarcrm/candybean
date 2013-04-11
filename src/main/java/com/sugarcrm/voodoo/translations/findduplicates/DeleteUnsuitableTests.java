package com.sugarcrm.voodoo.translations.findduplicates;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class DeleteUnsuitableTests {

	public static void main(String[] args) {
		try {
			Scanner s = new Scanner(new File("/home/suga/SuitableTestsForTranslations.txt"));
			ArrayList<String> a = new ArrayList<String>();
			while (s.hasNext()) {
				String fullPath = "/home/suga/TranslatedSodaTests/" + s.nextLine();
				//System.out.println(fullPath);
				a.add(fullPath);
			}
			deleteFiles("/home/suga/TranslatedSodaTests", a);
			s.close();
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}

	private static void deleteFiles(String rootDir, ArrayList<String> suitableTests) {
		File dir = new File(rootDir);
		if (dir.isFile() && !suitableTests.contains(dir.getAbsolutePath())) {
			//System.out.println("Test file: " + dir.getAbsolutePath());
			dir.delete();
		}
		else if (dir.isDirectory()) {
			File[] files = dir.listFiles();
			for (File file : files) {
				deleteFiles(file.getAbsolutePath(), suitableTests);
			}
		}
	}
}

package com.sugarcrm.voodoo.translations.findsuitabletests;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class FindSuitableTests {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Scanner s1 = new Scanner(new File("/home/suga/AffectedSodaTestsFileNames.txt"));
			Scanner s2 = new Scanner(new File("/home/suga/ErrorFreeTranslatedTests.txt"));
			ArrayList<String> a1 = new ArrayList<String>();
			ArrayList<String> a2 = new ArrayList<String>();
			while (s1.hasNextLine()) {
				a1.add(s1.nextLine());
			}
			while (s2.hasNextLine()) {
				a2.add(s2.nextLine());
			}

			ArrayList<String> a3 = new ArrayList<String>();
			Writer output = new BufferedWriter(new FileWriter(new File("/home/suga/SuitableTestsForTranslations.txt")));
			
			for (String s : a2) {
				if (!a1.contains(s)) a3.add(s);
			}
			Collections.sort(a3);

			for (int i = 0; i < a3.size(); i++) {
				output.write(a3.get(i) + "\n");
			}
			
			s1.close();
			s2.close();
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

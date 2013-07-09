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
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class FindSuitableTests {

	/**
	 * 
	 * 
	 * @author Jason Lin (ylin)
	 * 
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

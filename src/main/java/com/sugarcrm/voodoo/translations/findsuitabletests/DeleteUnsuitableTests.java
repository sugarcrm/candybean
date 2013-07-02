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
package com.sugarcrm.voodoo.translations.findsuitabletests;

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

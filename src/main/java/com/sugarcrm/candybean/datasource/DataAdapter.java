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
package com.sugarcrm.candybean.datasource;

import java.io.File;
import java.io.FileFilter;
import java.util.Map;

import org.apache.commons.io.filefilter.RegexFileFilter;

import com.sugarcrm.candybean.configuration.Configuration;

/**
 * DataAdapter is an interface class a client uses to convert csv, xml, etc.
 * into a HashMap of DataSource
 * 
 */
public abstract class DataAdapter {
	public enum Selection {ALL, SINGLE};
	static Configuration configuration;
	static String dataBasePath;
	static Selection selection = Selection.ALL;

	public DataAdapter(Configuration config) {
		DataAdapter.configuration = config;
	}

	public DataAdapter setDataBasePath(String dataBasePath) {
		DataAdapter.dataBasePath = dataBasePath;
		return this;
	}
	
	public abstract Map<String, DataSource> getData(String testData);
	public abstract Map<String, DataSource> getData(String testData, DataAdapter.Selection select);

	protected static File[] getAllFilesBasedOnPattern(String fileFullDirPath,
			String testData, String ext) {
		File dir = new File(fileFullDirPath);
		File[] files;

		String pattern = testData + "." + ext + "|" + testData + "_[^_]+" + "."
				+ ext;
		
		if (selection == DataAdapter.Selection.SINGLE) {
			pattern = testData + "." + ext;
		}
		
		// System.out.println("\nFiles that match regular expression: " +
		// pattern);
		FileFilter filter = new RegexFileFilter(pattern);
		files = dir.listFiles(filter);

		return files;
	}
}

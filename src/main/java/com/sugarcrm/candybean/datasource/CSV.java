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

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import au.com.bytecode.opencsv.CSVParser;

/**
 * CSV class parses an input csv file. 
 * 
 * @author Trampus
 * @author Jon duSaint
 */

public class CSV extends DataSource {
	private static final long serialVersionUID = 1L;
	private ArrayList<String> keys = null;
	private CSVParser parser = null;

	public CSV(String csvfile) {
		super();

		this.filename = csvfile;
		FileInputStream fs = null;
		BufferedReader br = null;

		this.parser = new CSVParser(',', '"');

		try {
			this.keys = new ArrayList<String>();
			data = new DataSource();

			fs = new FileInputStream(csvfile);
			br = new BufferedReader(new InputStreamReader(fs));
			this.findKeys(br);
			this.createData(br);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Read the CSV file and process the file into an array of hashes.
	 * 
	 * @param br
	 *            BufferedReader for the CSV file with line 1 consumed
	 */
	private void createData(BufferedReader br) {
		String line = "";
		String[] linedata;

		try {
			while ((line = br.readLine()) != null) {
				line = line.replaceAll("\\n", "");
				
				if (line.isEmpty()) {
					continue;
				}

				linedata = this.parser.parseLine(line);
				
				int linelen = linedata.length - 1;
				FieldSet tmphash = new FieldSet();
				for (int i = 0; i <= this.keys.size() - 1; i++) {
					if (i <= linelen) {
						tmphash.put(this.keys.get(i).trim(), linedata[i].trim());  // remove leading and trailing spaces
					} else {
						tmphash.put(this.keys.get(i), "");
					}
				}
				this.data.add(tmphash);
			}
		} catch (Exception exp) {
			exp.printStackTrace();
		}
	}

	/**
	 * Find the CSV file's key line and process the line into an array.
	 * 
	 * @param br
	 *            BufferedReader for the open file
	 */
	private void findKeys(BufferedReader br) {
		String line = "";
		String[] lines;

		try {
			keys = new ArrayList<String>();

			while ((line = br.readLine()) != null) {
				line = line.replaceAll("\\n", "");
				if (line.isEmpty()) {
					continue;
				} else {
					break;
				}
			}

			lines = this.parser.parseLine(line);
			for (int i = 0; i <= lines.length - 1; i++) {
				this.keys.add(lines[i]);
			}

		} catch (Exception exp) {
			exp.printStackTrace();
		}
	}
	
	public DataSource getDataSource() {
		return this.data;
	}
}
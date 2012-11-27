package com.sugarcrm.voodoo.datasource;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import au.com.bytecode.opencsv.CSVParser;

//public class CSV {
public class CSV extends DataSource {

	private ArrayList<String> keys = null;
	//private FieldSetList data = null;
	private CSVParser parser = null;
	//private String filename = "";

	public CSV(String csvfile) {
		super(csvfile);
		
		this.filename = csvfile;
		FileInputStream fs = null;
		BufferedReader br = null;

		this.parser = new CSVParser(',', '"');

		try {
			this.keys = new ArrayList<String>();
			data = new FieldSetList();

			fs = new FileInputStream(csvfile);
			br = new BufferedReader(new InputStreamReader(fs));
			this.findKeys(br);
			this.createData(br);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//public String getFilename() {
	//	return this.filename;
	//}

	/**
	 * Return the data read from a CSV file.
	 * 
	 * @return CSVData object.
	 */

	//public FieldSetList getData() {
	//	return this.data;
	//}

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
						tmphash.put(this.keys.get(i), linedata[i]);
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

			// lines = line.split(",");
			// lines = this.processLine(line);
			lines = this.parser.parseLine(line);
			for (int i = 0; i <= lines.length - 1; i++) {
				this.keys.add(lines[i]);
			}

		} catch (Exception exp) {
			exp.printStackTrace();
		}
	}
}
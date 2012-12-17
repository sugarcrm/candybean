package com.sugarcrm.voodoo.datasource;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import au.com.bytecode.opencsv.CSVParser;

/**
 * CSV class parses an input csv file. It inherits from DataSource which acts as
 * a public interface.
 * 
 * @author Trampus
 * @author Jon duSaint
 */

public class CSV extends DataSource {
	// private static final long serialVersionUID = 1L;

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
			data = new FieldSetList();

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
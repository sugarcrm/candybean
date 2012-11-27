package com.sugarcrm.voodoo.datasource;

public class DataSource {
	
	protected FieldSetList data = null;
	protected String filename = "";

	public DataSource(String csvfile) {
	}
	
	public FieldSetList getData() {
		return this.data;
	}
	
	public String getFilename() {
		return this.filename;
	}
}
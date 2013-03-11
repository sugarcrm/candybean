package com.sugarcrm.voodoo.datasource;

import java.util.HashMap;

/**
 *  DataSource is a minimal class that provides a uniform type seen by a client.   
 *
 */
public class DataSource {
	
	protected FieldSetList data = null;
	protected String filename = "";

	public DataSource() {
	}
	
	public FieldSetList getData() {
		return this.data;
	}
	
	public String getFilename() {
		return this.filename;
	}
}
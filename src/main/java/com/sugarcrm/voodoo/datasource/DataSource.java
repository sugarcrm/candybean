package com.sugarcrm.voodoo.datasource;

import java.util.ArrayList;

/**
 * DataSource models a file containing a list of records. 
 * A record is mapped to a FieldSet.
 *
 */
public class DataSource extends ArrayList<FieldSet> {
	private static final long serialVersionUID = 1L;
	protected DataSource data = null;
	protected String filename = "";

	public DataSource() {
		super();
	}
	
	public String getFilename() {
		return this.filename;
	}
}
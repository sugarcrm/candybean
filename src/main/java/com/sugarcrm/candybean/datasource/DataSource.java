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

import java.util.ArrayList;

/**
 * DataSource models a file containing a list of records. 
 * A record is mapped to a FieldSet.
 *
 */
public class DataSource extends ArrayList<FieldSet> {
	private static final long serialVersionUID = 1L;
	private DataSource data = null;
	private String filename = "";

	public DataSource() {}
	
	public String getFilename() {
		return this.filename;
	}

	protected DataSource getData() {
		return data;
	}

	protected void setData(DataSource data) {
		this.data = data;
	}

	protected void setFilename(String filename) {
		this.filename = filename;
	}
}
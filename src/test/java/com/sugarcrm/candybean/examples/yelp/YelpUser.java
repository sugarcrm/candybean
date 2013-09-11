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
package com.sugarcrm.candybean.examples.yelp;

public class YelpUser {

	private YelpUserBuilder builder;
	private YelpUser(YelpUserBuilder b) { this.builder = b; }
	
	public String firstName() { return builder.firstName; }
	public String lastName() { return builder.lastName; }
	public String zipCode() { return builder.zipCode; }
	public String email() { return builder.email; }
	public String password() { return builder.password; }

	@Override
	public String toString() {
		String s = "YelpUser(" + firstName() + "," + lastName() + "," + zipCode() + "," + email() + "," + password();
		return s;
	}

	public static class YelpUserBuilder {
		private String firstName = null;
		private String lastName = null;
		private String zipCode = null;
		private String email = null;
		private String password = null;
		
		public YelpUserBuilder(String firstName, String lastName, String zipCode, String email, String password) {
			this.firstName = firstName;
			this.lastName = lastName;
			this.zipCode = zipCode;
			this.email = email;
			this.password = password;
		}
		
		public YelpUser build() { return new YelpUser(this); }
	}
}	

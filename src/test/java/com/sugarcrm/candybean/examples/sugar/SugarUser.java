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
package com.sugarcrm.candybean.examples.sugar;

import com.sugarcrm.candybean.model.ModelObject;
import com.sugarcrm.candybean.model.ModelObjectBuilder;

public class SugarUser extends ModelObject {
	
	private SugarUser(SugarUserBuilder builder) { 
		super.setBuilder(builder);
	}
	
	@Override
	public String toString() {
		return this.getClass().getName() + super.toString();
	}

	public static class SugarUserBuilder extends ModelObjectBuilder {
		public SugarUserBuilder(String username, String firstName, String email, String phoneNumber, String password) {
			super.getRequiredAttributes().put("username", username);
			super.getRequiredAttributes().put("firstName", firstName);
			super.getRequiredAttributes().put("email", email);
			super.getRequiredAttributes().put("phoneNumber", phoneNumber);
			super.getRequiredAttributes().put("password", password);
		}

		@Override
		public SugarUser build() { return new SugarUser(this); }
	}
}	

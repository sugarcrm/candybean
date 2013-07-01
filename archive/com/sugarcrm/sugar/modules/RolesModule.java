/**
 * ====
 *     Candybean is a next generation automation and testing framework suite.
 *     It is a collection of components that foster test automation, execution
 *     configuration, data abstraction, results illustration, tag-based execution,
 *     top-down and bottom-up batches, mobile variants, test translation across
 *     languages, plain-language testing, and web service testing.
 *     Copyright (C) 2013 <candybean@sugarcrm.com>
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * ====
 *
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
package com.sugarcrm.sugar.modules;

import com.sugarcrm.sugar.Admin;
import com.sugarcrm.sugar.Sugar;
import com.sugarcrm.voodoo.automation.control.VHook.Strategy;

/**
 * @author Conrad Warmbold
 *
 */
public class RolesModule {
	
	private final Sugar sugar;
	
	public RolesModule(Sugar sugar) { this.sugar = sugar; }
	
	public void createRole(RoleRecord role) throws Exception {
		Admin.navTo(sugar);
		sugar.i.getControl(Strategy.ID, "roles_management").click();
		sugar.i.getControl(Strategy.ID, "moduleTab_AllACLRoles").hover();
		sugar.i.getControl(Strategy.PLINK, "Create Role").click();
		sugar.i.getControl(Strategy.ID, "bottomLinks").hover();
		sugar.i.getControl(Strategy.NAME, "name").sendString(role.name);
		sugar.i.getControl(Strategy.ID, "save_button").click();
//		sugar.i.getControl(Strategy.ID, "ACLEditView").halt(4);
//		sugar.i.getControl(Strategy.ID, "moduleTab_AllHome").click();
	}
	
	public void deleteAllRoles() throws Exception {
		Admin.navTo(sugar);
		sugar.i.getControl(Strategy.ID, "roles_management").click();
		sugar.i.getControl(Strategy.ID, "massall_top").click();
		sugar.i.getControl(Strategy.ID, "delete_listview_top").click();
		sugar.i.acceptDialog();
		sugar.i.getControl(Strategy.ID, "moduleTab_AllHome").click();
	}
	
	public void editRole(RoleRecord oldRole, RoleRecord newRole) throws Exception {
		this.searchRoles(oldRole.name);
		sugar.i.getControl(Strategy.PLINK, oldRole.name).click();
		sugar.i.getControl(Strategy.ID, "edit_button").click();
		sugar.i.getControl(Strategy.NAME, "name").sendString(newRole.name);
		sugar.i.getControl(Strategy.ID, "save_button").click();
//		sugar.i.getControl(Strategy.ID, "ACLEditView").halt(4);
//		sugar.i.getControl(Strategy.ID, "moduleTab_AllHome").click();
	}
	
	public void searchRoles(String search) throws Exception {
		Admin.navTo(sugar);
		sugar.i.getControl(Strategy.ID, "roles_management").click();
		sugar.i.getControl(Strategy.ID, "name_basic").sendString(search);
		sugar.i.getControl(Strategy.ID, "search_form_submit").click();
	}
	
	public static class RoleRecord {
		
		public String name = null;

		public RoleRecord(String name) {
			this.name = name;
		}
	}
}
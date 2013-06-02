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
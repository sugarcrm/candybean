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
package com.sugarcrm.sugar;

import com.sugarcrm.voodoo.automation.control.VHook.Strategy;
import com.sugarcrm.sugar.Sugar;

public class Admin {
	
	public static void setModuleVisibility(Sugar sugar) throws Exception {
		Admin.navTo(sugar);
		sugar.i.getControl(Strategy.ID, "product_catalog").scroll();
		sugar.i.getControl(Strategy.ID, "configure_tabs").click();
		throw new Exception("setModuleVisibility not yet implemented");
//		sugar.i.getControl(sugar.getHook("portal_link_configureportal")).click();
//		sugar.i.getSelect(sugar.getHook("portal_checkbox_portalenable")).select(isEnabled);
//		sugar.i.getControl(sugar.getHook("portal_button_save")).click();
//		voodoo.interact("Wait...");
	}
	
	public static void setPortalEnabled(Sugar sugar, boolean isEnabled) throws Exception {
		Admin.navTo(sugar);
		sugar.i.getControl(sugar.getHook("admin_table_devtools")).scroll();
		sugar.i.getControl(sugar.getHook("admin_link_sugarportal")).click();
		sugar.i.getControl(sugar.getHook("portal_link_configureportal")).click();
		sugar.i.getSelect(sugar.getHook("portal_checkbox_portalenable")).select(isEnabled);
		sugar.i.getControl(sugar.getHook("portal_button_save")).click();
//		voodoo.interact("Wait...");
	}
	
	public static void navTo(Sugar sugar) throws Exception {
		sugar.i.getControl(sugar.getHook("navbar_menu_user")).click();
		sugar.i.getControl(sugar.getHook("navbar_menuitem_admin")).click();
//		sugar.i.getControl(sugar.getHook("admin_header")).halt(4);
	}
}

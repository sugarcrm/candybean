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
public class ManufacturersModule {
	
	private final Sugar sugar;
	
	public ManufacturersModule(Sugar sugar) { this.sugar = sugar; }
	
	public void createManufacturer(ManufacturerRecord manufacturer) throws Exception {
		Admin.navTo(sugar);
		sugar.i.getControl(Strategy.ID, "tax_rates").scroll();
		sugar.i.getControl(Strategy.ID, "manufacturers").click();
		sugar.i.getControl(Strategy.ID, "btn_create").click();
		sugar.i.getControl(Strategy.NAME, "name").sendString(manufacturer.name);
		sugar.i.getControl(Strategy.ID, "btn_save").click();
//		sugar.i.getControl(Strategy.ID, "moduleTab_AllHome").click();
	}
	
	public void deleteAllManufacturers() throws Exception {
		Admin.navTo(sugar);
		sugar.i.getControl(Strategy.ID, "tax_rates").scroll();
		sugar.i.getControl(Strategy.ID, "manufacturers").click();
		sugar.i.getControl(Strategy.ID, "massall").click();
		sugar.i.getControl(Strategy.ID, "delete_button").click();
		sugar.i.acceptDialog();
		sugar.i.getControl(Strategy.ID, "moduleTab_AllHome").click();
	}
	
	public void editManufacturer(ManufacturerRecord oldManufacturer, ManufacturerRecord newManufacturer) throws Exception {
		Admin.navTo(sugar);
		sugar.i.getControl(Strategy.ID, "tax_rates").scroll();
		sugar.i.getControl(Strategy.ID, "manufacturers").click();
		sugar.i.getControl(Strategy.PLINK, oldManufacturer.name).click();
		sugar.i.getControl(Strategy.NAME, "name").sendString(newManufacturer.name);
		sugar.i.getControl(Strategy.ID, "btn_save").click();
//		sugar.i.getControl(Strategy.ID, "moduleTab_AllHome").click();
	}
	
	public static class ManufacturerRecord {
		
		public String name = null;
		
		public ManufacturerRecord(String name) {
			this.name = name;
		}
	}
}
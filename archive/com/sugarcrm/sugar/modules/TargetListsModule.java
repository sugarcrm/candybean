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

import com.sugarcrm.sugar.Sugar;
import com.sugarcrm.voodoo.automation.control.VHook.Strategy;

/**
 * @author Conrad Warmbold
 *
 */
public class TargetListsModule {
	
	private final Sugar sugar;
	
	public TargetListsModule(Sugar sugar) { this.sugar = sugar; }
	
	public void createTargetList(TargetListRecord targetList) throws Exception {
		sugar.i.getControl(Strategy.ID, "moduleTabExtraMenuAll").click();
		sugar.i.getControl(Strategy.ID, "moduleMenuOverFlowMoreAll").click();
		sugar.i.getControl(Strategy.PLINK, "Target Lists").scroll();
		sugar.i.getControl(Strategy.PLINK, "Target Lists").hover();
		sugar.i.getControl(Strategy.PLINK, "Create Target List").click();
		sugar.i.getControl(Strategy.ID, "list_type_label").hover();
		sugar.i.getControl(Strategy.ID, "name").sendString(targetList.name);
		sugar.i.getControl(Strategy.ID, "SAVE_HEADER").click();
//		sugar.i.getControl(Strategy.ID, "edit_button").halt(4);
//		sugar.i.getControl(Strategy.ID, "moduleTab_AllHome").click();
	}
	
	public void deleteAllTargetLists() throws Exception {
		sugar.i.getControl(Strategy.ID, "moduleTabExtraMenuAll").click();
		sugar.i.getControl(Strategy.ID, "moduleMenuOverFlowMoreAll").click();
		sugar.i.getControl(Strategy.PLINK, "Target Lists").click();
		sugar.i.getControl(Strategy.ID, "massall_top").click();
		sugar.i.getControl(Strategy.ID, "delete_listview_top").click();
		sugar.i.acceptDialog();
		sugar.i.getControl(Strategy.ID, "moduleTab_AllHome").click();
	}
	
	public void editTargetList(TargetListRecord oldTargetList, TargetListRecord newTargetList) throws Exception {
		this.searchTargetLists(oldTargetList.name);
		sugar.i.getControl(Strategy.PLINK, oldTargetList.name).click();
		sugar.i.getControl(Strategy.ID, "edit_button").click();
		sugar.i.getControl(Strategy.ID, "name").sendString(newTargetList.name);
		sugar.i.getControl(Strategy.ID, "SAVE_HEADER").click();
//		sugar.i.getControl(Strategy.ID, "edit_button").halt(4);
//		sugar.i.getControl(Strategy.ID, "moduleTab_AllHome").click();
	}
	
	public void searchTargetLists(String search) throws Exception {
		sugar.i.getControl(Strategy.ID, "moduleTabExtraMenuAll").click();
		sugar.i.getControl(Strategy.ID, "moduleMenuOverFlowMoreAll").click();
		sugar.i.getControl(Strategy.PLINK, "Target Lists").scroll();
		sugar.i.getControl(Strategy.PLINK, "Target Lists").click();
		sugar.i.getControl(Strategy.ID, "name_basic").sendString(search);
		sugar.i.getControl(Strategy.ID, "search_form_submit").click();
	}
	
	public static class TargetListRecord {
		
		public String name = null;

		public TargetListRecord(String name) {
			this.name = name;
		}
	}
}
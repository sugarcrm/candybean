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
public class CurrenciesModule {
	
	private final Sugar sugar;
	
	public CurrenciesModule(Sugar sugar) { this.sugar = sugar; }
	
	public void createCurrency(CurrencyRecord currency) throws Exception {
		Admin.navTo(sugar);
		sugar.i.getControl(Strategy.ID, "currencies_management").click();
		sugar.i.getControl(Strategy.NAME, "name").sendString(currency.name);
		sugar.i.getControl(Strategy.NAME, "conversion_rate").sendString(currency.conversionRate);
		sugar.i.getControl(Strategy.NAME, "symbol").sendString(currency.symbol);
		sugar.i.getControl(Strategy.NAME, "button").click();
//		sugar.i.getControl(Strategy.ID, "moduleTab_AllHome").click();
	}
	
	public void editCurrency(CurrencyRecord oldCurrency, CurrencyRecord newCurrency) throws Exception {
		Admin.navTo(sugar);
		sugar.i.getControl(Strategy.ID, "currencies_management").click();
		sugar.i.getControl(Strategy.PLINK, oldCurrency.name).click();
		sugar.i.getControl(Strategy.NAME, "name").sendString(newCurrency.name);
		sugar.i.getControl(Strategy.NAME, "conversion_rate").sendString(newCurrency.conversionRate);
		sugar.i.getControl(Strategy.NAME, "symbol").sendString(newCurrency.symbol);
		sugar.i.getControl(Strategy.NAME, "button").click();
//		sugar.i.getControl(Strategy.ID, "moduleTab_AllHome").click();
	}
	
	public static class CurrencyRecord {
		
		public String name = null;
		public String conversionRate = null;
		public String symbol = null;
		
		public CurrencyRecord(String name, String conversionRate, String symbol) {
			this.name = name;
			this.conversionRate = conversionRate;
			this.symbol = symbol;
		}
	}
}
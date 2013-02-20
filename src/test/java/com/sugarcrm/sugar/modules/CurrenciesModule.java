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
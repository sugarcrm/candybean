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
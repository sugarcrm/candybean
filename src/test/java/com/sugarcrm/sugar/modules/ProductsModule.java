package com.sugarcrm.sugar.modules;

import com.sugarcrm.sugar.Sugar;
import com.sugarcrm.voodoo.automation.control.VHook.Strategy;
import com.sugarcrm.voodoo.utilities.Utils;

/**
 * @author Conrad Warmbold
 *
 */
public class ProductsModule {
	
	private final Sugar sugar;
	
	public ProductsModule(Sugar sugar) { this.sugar = sugar; }
	
	public void createProduct(ProductRecord product) throws Exception {
		String sugarURL = Utils.getCascadingPropertyValue(sugar.props, "http://localhost/ent670/", "env.base_url");
		sugar.i.go(sugarURL + "/index.php?module=Products&action=EditView");
		sugar.i.getControl(Strategy.ID, "name").sendString(product.name);
		sugar.i.getControl(Strategy.ID, "SAVE_HEADER").click();
//		sugar.i.getControl(Strategy.ID, "moduleTab_AllHome").click();
	}
	
	public void deleteAllProducts() throws Exception {
		String sugarURL = Utils.getCascadingPropertyValue(sugar.props, "http://localhost/ent670/", "env.base_url");
		sugar.i.go(sugarURL + "/index.php?module=Products&action=ListView");
		sugar.i.getControl(Strategy.ID, "massall_top").click();
		sugar.i.getControl(Strategy.ID, "delete_listview_top").click();
		sugar.i.acceptDialog();
		sugar.i.getControl(Strategy.ID, "moduleTab_AllHome").click();
	}
	
	public void editProduct(ProductRecord oldProduct, ProductRecord newProduct) throws Exception {
		this.searchProducts(oldProduct.name);
		sugar.i.getControl(Strategy.PLINK, oldProduct.name).click();
		sugar.i.getControl(Strategy.ID, "edit_button").click();
		sugar.i.getControl(Strategy.ID, "name").sendString(newProduct.name);
		sugar.i.getControl(Strategy.ID, "SAVE_HEADER").click();
//		sugar.i.getControl(Strategy.ID, "edit_button").halt(4);
//		sugar.i.getControl(Strategy.ID, "moduleTab_AllHome").click();
	}
	
	public void searchProducts(String search) throws Exception {
		String sugarURL = Utils.getCascadingPropertyValue(sugar.props, "http://localhost/ent670/", "env.base_url");
		sugar.i.go(sugarURL + "/index.php?module=Products&action=ListView");
		sugar.i.getControl(Strategy.ID, "name_basic").sendString(search);
		sugar.i.getControl(Strategy.ID, "search_form_submit").click();
	}
	
	public static class ProductRecord {
		
		public String name = null;

		public ProductRecord(String name) {
			this.name = name;
		}
	}
}
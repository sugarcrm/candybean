package com.sugarcrm.sugar.modules;

import com.sugarcrm.sugar.Sugar;
import com.sugarcrm.sugar.modules.AccountsModule.AccountRecord;
import com.sugarcrm.voodoo.automation.control.VHook.Strategy;

/**
 * @author Conrad Warmbold
 *
 */
public class ContractsModule {
	
	private final Sugar sugar;
	
	public ContractsModule(Sugar sugar) { this.sugar = sugar; }
	
	public void createContract(ContractRecord contract) throws Exception {
		String sugarURL = sugar.config.getProperty("env.base_url", "http://localhost/ent670/");
		sugar.i.go(sugarURL + "/index.php?module=Contracts&action=EditView");
		sugar.i.getControl(Strategy.ID, "name").sendString(contract.name);
		sugar.i.getSelect(Strategy.ID, "status").select(contract.status);
		sugar.i.getControl(Strategy.ID, "btn_account_name").click();
		sugar.i.focusWindow(1);
		sugar.i.getControl(Strategy.PLINK, contract.account.name).click();
		sugar.i.focusWindow(0);
		sugar.i.getControl(Strategy.ID, "SAVE_HEADER").click();
//		sugar.i.getControl(Strategy.ID, "edit_button").halt(4);
//		sugar.i.getControl(Strategy.ID, "moduleTab_AllHome").click();
	}
	
	public void deleteAllContracts() throws Exception {
		String sugarURL = sugar.config.getProperty("env.base_url", "http://localhost/ent670/");
		sugar.i.go(sugarURL + "/index.php?module=Contracts&action=ListView");
		sugar.i.getControl(Strategy.ID, "massall_top").click();
		sugar.i.getControl(Strategy.ID, "delete_listview_top").click();
		sugar.i.acceptDialog();
		sugar.i.getControl(Strategy.ID, "moduleTab_AllHome").click();
	}
	
	public void editContract(ContractRecord oldContract, ContractRecord newContract) throws Exception {
		this.searchContracts(oldContract.name);
		sugar.i.getControl(Strategy.PLINK, oldContract.name).click();
		sugar.i.getControl(Strategy.ID, "edit_button").click();
		sugar.i.getControl(Strategy.ID, "name").sendString(newContract.name);
		sugar.i.getControl(Strategy.ID, "SAVE_HEADER").click();
//		sugar.i.getControl(Strategy.ID, "edit_button").halt(4);
//		sugar.i.getControl(Strategy.ID, "moduleTab_AllHome").click();
	}
	
	public void searchContracts(String search) throws Exception {
		String sugarURL = sugar.config.getProperty("env.base_url", "http://localhost/ent670/");
		sugar.i.go(sugarURL + "/index.php?module=Contracts&action=ListView");
		sugar.i.getControl(Strategy.ID, "name_basic").sendString(search);
		sugar.i.getControl(Strategy.ID, "search_form_submit").click();
	}
	
	public static class ContractRecord {
		
		public String name = null;
		public String status = null;
		public AccountRecord account = null;
		
		public ContractRecord(String name, String status, AccountRecord account) {
			this.name = name;
			this.status = status;
			this.account = account;
		}
	}
}
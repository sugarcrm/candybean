package com.sugarcrm.sugar.modules;

import com.sugarcrm.sugar.Sugar;
import com.sugarcrm.sugar.modules.AccountsModule.AccountRecord;
import com.sugarcrm.voodoo.automation.control.VHook.Strategy;

/**
 * @author Conrad Warmbold
 *
 */
public class CasesModule {
	
	private final Sugar sugar;
	
	public CasesModule(Sugar sugar) { this.sugar = sugar; }
	
	public void createCase(CaseRecord caseRecord) throws Exception {
		sugar.i.getControl(Strategy.ID, "moduleTabExtraMenuAll").click();
		sugar.i.getControl(Strategy.ID, "moduleMenuOverFlowMoreAll").click();
		sugar.i.getControl(Strategy.PLINK, "Cases").scroll();
		sugar.i.getControl(Strategy.PLINK, "Cases").hover();
		sugar.i.getControl(Strategy.PLINK, "Create Case").click();
		sugar.i.getControl(Strategy.ID, "account_name_label").hover();
		sugar.i.getControl(Strategy.ID, "name").sendString(caseRecord.subject);
		sugar.i.getControl(Strategy.ID, "btn_account_name").click();
		sugar.i.focusWindow(1);
		sugar.i.getControl(Strategy.PLINK, caseRecord.account.name).click();
		sugar.i.focusWindow(0);
		sugar.i.getControl(Strategy.ID, "SAVE_HEADER").click();
//		sugar.i.getControl(Strategy.ID, "edit_button").halt(4);
//		sugar.i.getControl(Strategy.ID, "moduleTab_AllHome").click();
	}
	
	public void deleteAllCases() throws Exception {
		sugar.i.getControl(Strategy.ID, "moduleTabExtraMenuAll").click();
		sugar.i.getControl(Strategy.ID, "moduleMenuOverFlowMoreAll").click();
		sugar.i.getControl(Strategy.PLINK, "Cases").click();
		sugar.i.getControl(Strategy.ID, "massall_top").click();
		sugar.i.getControl(Strategy.ID, "delete_listview_top").click();
		sugar.i.acceptDialog();
		sugar.i.getControl(Strategy.ID, "moduleTab_AllHome").click();
	}
	
	public void editCase(CaseRecord oldCase, CaseRecord newCase) throws Exception {
		this.searchCases(oldCase.subject);
		sugar.i.getControl(Strategy.PLINK, oldCase.subject).click();
		sugar.i.getControl(Strategy.ID, "edit_button").click();
		sugar.i.getControl(Strategy.ID, "name").sendString(newCase.subject);
		sugar.i.getControl(Strategy.ID, "SAVE_HEADER").click();
//		sugar.i.getControl(Strategy.ID, "edit_button").halt(4);
//		sugar.i.getControl(Strategy.ID, "moduleTab_AllHome").click();
	}
	
	public void searchCases(String search) throws Exception {
		sugar.i.getControl(Strategy.ID, "moduleTabExtraMenuAll").click();
		sugar.i.getControl(Strategy.ID, "moduleMenuOverFlowMoreAll").click();
		sugar.i.getControl(Strategy.PLINK, "Cases").scroll();
		sugar.i.getControl(Strategy.PLINK, "Cases").click();
		sugar.i.getControl(Strategy.ID, "name_basic").sendString(search);
		sugar.i.getControl(Strategy.ID, "search_form_submit").click();
	}
	
	public static class CaseRecord {
		
		public String subject = null;
		public AccountRecord account = null;

		public CaseRecord(String subject, AccountRecord account) {
			this.subject = subject;
			this.account = account;
		}
	}
}
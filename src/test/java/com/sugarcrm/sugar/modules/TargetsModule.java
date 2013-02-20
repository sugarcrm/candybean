package com.sugarcrm.sugar.modules;

import com.sugarcrm.sugar.Sugar;
import com.sugarcrm.voodoo.automation.control.VHook.Strategy;

/**
 * @author Conrad Warmbold
 *
 */
public class TargetsModule {
	
	private final Sugar sugar;
	
	public TargetsModule(Sugar sugar) { this.sugar = sugar; }
	
	public void createTarget(TargetRecord target) throws Exception {
		sugar.i.getControl(Strategy.ID, "moduleTabExtraMenuAll").click();
		sugar.i.getControl(Strategy.ID, "moduleMenuOverFlowMoreAll").click();
		sugar.i.getControl(Strategy.PLINK, "Targets").scroll();
		sugar.i.getControl(Strategy.PLINK, "Targets").hover();
		sugar.i.getControl(Strategy.PLINK, "Create Target").click();
		sugar.i.getControl(Strategy.ID, "phone_work_label").hover();
		sugar.i.getControl(Strategy.ID, "last_name").sendString(target.lastName);
		sugar.i.getControl(Strategy.ID, "SAVE_HEADER").click();
//		sugar.i.getControl(Strategy.ID, "edit_button").halt(4);
//		sugar.i.getControl(Strategy.ID, "moduleTab_AllHome").click();
	}
	
	public void deleteAllTargets() throws Exception {
		sugar.i.getControl(Strategy.ID, "moduleTabExtraMenuAll").click();
		sugar.i.getControl(Strategy.ID, "moduleMenuOverFlowMoreAll").click();
		sugar.i.getControl(Strategy.PLINK, "Targets").click();
		sugar.i.getControl(Strategy.ID, "massall_top").click();
		sugar.i.getControl(Strategy.ID, "delete_listview_top").click();
		sugar.i.acceptDialog();
		sugar.i.getControl(Strategy.ID, "moduleTab_AllHome").click();
	}
	
	public void editTarget(TargetRecord oldTarget, TargetRecord newTarget) throws Exception {
		this.searchTargets(oldTarget.lastName);
		sugar.i.getControl(Strategy.PLINK, oldTarget.lastName).click();
		sugar.i.getControl(Strategy.ID, "edit_button").click();
		sugar.i.getControl(Strategy.ID, "last_name").sendString(newTarget.lastName);
		sugar.i.getControl(Strategy.ID, "SAVE_HEADER").click();
//		sugar.i.getControl(Strategy.ID, "edit_button").halt(4);
//		sugar.i.getControl(Strategy.ID, "moduleTab_AllHome").click();
	}
	
	public void searchTargets(String search) throws Exception {
		sugar.i.getControl(Strategy.ID, "moduleTabExtraMenuAll").click();
		sugar.i.getControl(Strategy.ID, "moduleMenuOverFlowMoreAll").click();
		sugar.i.getControl(Strategy.PLINK, "Targets").scroll();
		sugar.i.getControl(Strategy.PLINK, "Targets").click();
		sugar.i.getControl(Strategy.ID, "name_basic").sendString(search);
		sugar.i.getControl(Strategy.ID, "search_form_submit").click();
	}
	
	public static class TargetRecord {
		
		public String lastName = null;

		public TargetRecord(String lastName) {
			this.lastName = lastName;
		}
	}
}
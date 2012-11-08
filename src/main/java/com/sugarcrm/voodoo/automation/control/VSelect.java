package com.sugarcrm.voodoo.automation.control;

import com.sugarcrm.voodoo.automation.IAutomation;


/**
 * @author cwarmbold
 *
 */
public class VSelect extends VControl {
	
	public VSelect(VHook hook, IAutomation auto) { super(hook, auto); }
	
	public String getSelected() throws Exception { return super.auto.getSelected(this); }
	public void select(String value) throws Exception {	super.auto.select(this, value); }
    public void select(boolean isSelected) throws Exception { super.auto.select(this, isSelected); }
}

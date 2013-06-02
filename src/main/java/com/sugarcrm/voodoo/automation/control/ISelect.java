package com.sugarcrm.voodoo.automation.control;

import com.sugarcrm.voodoo.automation.control.VSelect;


public interface ISelect {
	
	// VSelect functionality
	public String getSelected(VSelect select) throws Exception;
	public void select(VSelect select, String value) throws Exception;
	public void select(VSelect select, boolean isSelected) throws Exception;
}

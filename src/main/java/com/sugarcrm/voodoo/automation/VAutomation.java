package com.sugarcrm.voodoo.automation;


public interface VAutomation {
	
	public enum Strategy { CSS, XPATH, ID, NAME; }

	public VControl getControl(Strategy strategy, String hook) throws Exception;
	public void click(VControl control) throws Exception;
	public void input(VControl control, String s) throws Exception;
}

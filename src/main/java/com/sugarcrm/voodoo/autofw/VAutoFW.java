package com.sugarcrm.voodoo.autofw;


public interface VAutoFW {
	
	public enum Strategy { CSS, XPATH, ID, NAME; }

	public VControl getControl(Strategy strategy, String hook) throws Exception;
	public void VClick(VControl control) throws Exception;
	public void VInput(VControl control, String s) throws Exception;
	
}

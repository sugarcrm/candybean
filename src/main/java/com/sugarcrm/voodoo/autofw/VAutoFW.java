package com.sugarcrm.voodoo.autofw;


/**
 * @author 
 *
 */
public interface VAutoFW {
	
	public enum Strategy { CSS, XPATH, ID, NAME; }

	/**
	 * 
	 * getControl() 
	 * @param strategy 
	 * @param hook 
	 * @return 
	 * @throws Exception 
	 */
	public VControl getControl(Strategy strategy, String hook) throws Exception;
	
	/**
	 * 
	 * VClick() 
	 * @param control 
	 * @throws Exception 
	 */
	public void VClick(VControl control) throws Exception;
	
	/**
	 * 
	 * VInput() 
	 * @param control 
	 * @param s 
	 * @throws Exception 
	 */
	public void VInput(VControl control, String s) throws Exception;
	
}

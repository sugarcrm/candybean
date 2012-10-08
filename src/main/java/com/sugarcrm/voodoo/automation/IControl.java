package com.sugarcrm.voodoo.automation;


/**
 * @author cwarmbold
 *
 */
public interface IControl {

	public enum Type { ELEMENT, HLINK, TEXT, BUTTON; }
	
	/**
	 * @return
	 * @throws Exception
	 */
	public String getText() throws Exception;
	
	/**
	 * @throws Exception
	 */
	public void click() throws Exception;
	
	/**
	 * @throws Exception
	 */
	public void hover() throws Exception;
	
	/**
	 * @param input
	 * @throws Exception
	 */
	public void input(String input) throws Exception;
	
	/**
	 * @throws Exception
	 */
	public void rightClick() throws Exception;
	
	/**
	 * @throws Exception
	 */
	public void scroll() throws Exception;
}

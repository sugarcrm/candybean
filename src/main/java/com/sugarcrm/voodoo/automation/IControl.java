package com.sugarcrm.voodoo.automation;


/**
 * @author cwarmbold
 *
 */
public interface IControl {

	public enum Type { ELEMENT, LINK, TEXT, BUTTON; }
	
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
	public void explicitWait() throws Exception;
    
	/**
	 * @throws Exception
	 */
	public void explicitWait(String attribute, String value) throws Exception;
	
	/**
	 * @throws Exception
	 */
	public String getSelected() throws Exception;
	
	/**
	 * @param value
	 * @throws Exception
	 */
	public void select(String value) throws Exception;
	
	/**
     * @param target
     * @throws Exception
     */
    public void dragAndDrop(IControl control) throws Exception;

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

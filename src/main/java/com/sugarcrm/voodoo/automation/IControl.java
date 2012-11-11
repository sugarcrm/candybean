package com.sugarcrm.voodoo.automation;


/**
 * @author cwarmbold
 *
 */
public interface IControl {

	public enum Type { ELEMENT, LINK, TEXT, BUTTON; }
	
	public String getText() throws Exception;
	public String getSelected() throws Exception;
	public void click() throws Exception;
	public void dragNDrop(IControl control) throws Exception;
	public void hover() throws Exception;
	public void input(String input) throws Exception;
	public void rightClick() throws Exception;
	public void select(String value) throws Exception;
	public void scroll() throws Exception;
	public void waitFor() throws Exception;
    public void waitFor(String attribute, String value) throws Exception;
    public void select(boolean isSelected) throws Exception;
    public String getAttributeValue(String attribute) throws Exception;
}

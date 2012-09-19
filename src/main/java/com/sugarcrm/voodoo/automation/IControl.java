package com.sugarcrm.voodoo.automation;


public interface IControl {

	public enum Type { ELEMENT, HLINK, TEXT, BUTTON; }
	
	public String getText() throws Exception;
	public void hover() throws Exception;
	public void click() throws Exception;
	public void input(String input) throws Exception;
}

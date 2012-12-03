package com.sugarcrm.voodoo.automation.control;


import com.sugarcrm.voodoo.automation.control.VControl;


public interface IControl {
	
	// VControl functionality
	public String getAttribute(String attribute) throws Exception;
	public String getText() throws Exception;
	public void click() throws Exception;
	public void doubleClick() throws Exception;
	public void dragNDrop(VControl dropControl) throws Exception;
	public void hover() throws Exception;
	public void rightClick() throws Exception;
	public void scroll() throws Exception;
	public void sendString(String input) throws Exception;
	public void waitOn() throws Exception;
	public void wait(String attribute, String value) throws Exception;
}

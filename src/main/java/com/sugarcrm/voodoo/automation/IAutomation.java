package com.sugarcrm.voodoo.automation;

import com.sugarcrm.voodoo.automation.control.VControl;
import com.sugarcrm.voodoo.automation.control.VSelect;


public interface IAutomation {
	
	public enum InterfaceType { FIREFOX, IE, CHROME, SAFARI; }
	public enum Strategy { CSS, XPATH, ID, NAME, LINK, PLINK; }
	
	// General automation functionality
	public void start() throws Exception;
	public void stop() throws Exception;
	public void acceptDialog() throws Exception;
	public void closeWindow() throws Exception;
	public void go(String url) throws Exception;
	public void focusByIndex(int index) throws Exception;
	public void focusByTitle(String title) throws Exception;
	public void focusByUrl(String url) throws Exception;
	
	// VControl functionality
	public String getAttribute(VControl control, String attribute) throws Exception;
    public String getText(VControl control) throws Exception;
	public void click(VControl control) throws Exception;
	public void dragNDrop(VControl control1, VControl control2) throws Exception;
    public void hover(VControl control) throws Exception;
	public void rightClick(VControl control) throws Exception;
	public void scroll(VControl control) throws Exception;
	public void sendString(VControl control, String input) throws Exception;
    public void waitOn(VControl control) throws Exception;
	public void wait(VControl control, String attribute, String value) throws Exception;
	
	// VSelect functionality
	public String getSelected(VSelect select) throws Exception;
	public void select(VSelect select, String value) throws Exception;
    public void select(VSelect select, boolean isSelected) throws Exception;
}

package com.sugarcrm.voodoo;

import com.sugarcrm.voodoo.automation.VControl;
import com.sugarcrm.voodoo.automation.VHook;


public interface IAutomation {
	
	public enum InterfaceType { FIREFOX, IE, CHROME, SAFARI; }
	public enum Strategy { CSS, XPATH, ID, NAME, LINK, PLINK; }

	public void start() throws Exception;
	public void stop() throws Exception;
	public void acceptDialog() throws Exception;
	public void closeWindow() throws Exception;
	public void focusByIndex(int index) throws Exception;
	public void focusByTitle(String title) throws Exception;
	public void focusByUrl(String url) throws Exception;
	public void go(String url) throws Exception;
	public void interact(String message) throws Exception;
	public void pause(long ms) throws Exception;
	public VControl getControl(VHook hook) throws Exception;
	public String getSelected(VControl control) throws Exception;
	public String getSelected(VHook hook) throws Exception;
	public String getText(VHook hook) throws Exception;
	public String getText(VControl control) throws Exception;
	public void click(VHook hook) throws Exception;
	public void click(VControl control) throws Exception;
    public void dragNDrop(VHook hook1, VHook hook2) throws Exception;
    public void dragNDrop(VControl control1, VControl control2) throws Exception;
	public void hover(VHook hook) throws Exception;
	public void hover(VControl control) throws Exception;
	public void input(VHook hook, String input) throws Exception;
	public void input(VControl control, String input) throws Exception;
	public void rightClick(VHook hook) throws Exception;
	public void rightClick(VControl control) throws Exception;
	public void scroll(VHook hook) throws Exception;
	public void scroll(VControl control) throws Exception;
	public void select(VHook hook, String value) throws Exception;
	public void select(VControl control, String value) throws Exception;
	public void waitFor(VControl control) throws Exception;
	public void waitFor(VHook hook) throws Exception;
	public void waitFor(VControl control, String attribute, String value) throws Exception;
	public void waitFor(VHook hook, String attribute, String value) throws Exception;
    public void select(VHook hook, boolean isSelected) throws Exception;
    public void select(VControl control, boolean isSelected) throws Exception;
    public String getAttributeValue(VHook hook, String attribute) throws Exception;
    public String getAttributeValue(VControl control, String attribute) throws Exception;
}

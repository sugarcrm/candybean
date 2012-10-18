package com.sugarcrm.voodoo;

import com.sugarcrm.voodoo.automation.VControl;
import com.sugarcrm.voodoo.automation.VHook;


public interface IAutomation {
	
	public enum InterfaceType { FIREFOX, IE, CHROME, SAFARI; }
	public enum Strategy { CSS, XPATH, ID, NAME, LINK; }

	public void start(String url) throws Exception;
	public void stop() throws Exception;
	public void pause(long ms) throws Exception;
	public void interact(String message) throws Exception;
	public void acceptDialog() throws Exception;
	public void switchToPopup() throws Exception;
	public void focusByIndex(int index) throws Exception;
	public void focusByTitle(String title) throws Exception;
	public void focusByUrl(String url) throws Exception;
	public VControl getControl(VHook hook) throws Exception;
	@Deprecated public VControl getControl(Strategy strategy, String hook) throws Exception;
	public String getText(VHook hook) throws Exception;
	public String getText(VControl control) throws Exception;
	public void explicitWait(VControl control) throws Exception;
	public void explicitWait(VHook hook) throws Exception;
	public void explicitWait(VControl control, String attribute, String value) throws Exception;
	public void explicitWait(VHook hook, String attribute, String value) throws Exception;
	@Deprecated public String getText(Strategy strategy, String hook) throws Exception;
	public void click(VHook hook) throws Exception;
	public void click(VControl control) throws Exception;
	@Deprecated public void click(Strategy strategy, String hook) throws Exception;
    public void dragAndDrop(VHook hook1, VHook hook2) throws Exception;
    public void dragAndDrop(VControl control1, VControl control2) throws Exception;
	public void hover(VHook hook) throws Exception;
	public void hover(VControl control) throws Exception;
	@Deprecated public void hover(Strategy strategy, String hook) throws Exception;
	public void input(VHook hook, String input) throws Exception;
	public void input(VControl control, String input) throws Exception;
	@Deprecated public void input(Strategy strategy, String hook, String input) throws Exception;
	public void rightClick(VHook hook) throws Exception;
	public void rightClick(VControl control) throws Exception;
	@Deprecated public void rightClick(Strategy strategy, String hook) throws Exception;
	public void scroll(VHook hook) throws Exception;
	public void scroll(VControl control) throws Exception;
}

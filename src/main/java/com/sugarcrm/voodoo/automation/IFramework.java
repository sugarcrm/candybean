package com.sugarcrm.voodoo.automation;

import com.sugarcrm.voodoo.automation.VControl;
import com.sugarcrm.voodoo.IAutomation.Strategy;


public interface IFramework {
	
	public void start() throws Exception;
	public void stop() throws Exception;
	public void closeWindow() throws Exception;
	public void go(String url) throws Exception;
	public void acceptDialog() throws Exception;
	public void switchToPopup() throws Exception;
	public void focusByIndex(int index) throws Exception;
	public void focusByTitle(String title) throws Exception;
	public void focusByUrl(String url) throws Exception;
	public VControl getControl(Strategy strategy, String hook) throws Exception;
	public String getText(VControl control) throws Exception;
	public String getText(Strategy strategy, String hook) throws Exception;
	public void explicitWait(VControl control) throws Exception;
	public void explicitWait(Strategy strategy, String hook) throws Exception;
	public void explicitWait(VControl control, String attribute, String value) throws Exception;
	public void explicitWait(Strategy strategy, String hook, String attribute, String value) throws Exception;
	public void click(Strategy strategy, String hook) throws Exception;
	public void click(VControl control) throws Exception;
	public void hover(VControl control) throws Exception;
	public void hover(Strategy strategy, String hook) throws Exception;
	public void input(Strategy strategy, String hook, String input) throws Exception;
	public void input(VControl control, String input) throws Exception;
	public void rightClick(Strategy strategy, String hook) throws Exception;
	public void rightClick(VControl control) throws Exception;
	public void scroll(Strategy strategy, String hook) throws Exception;
	public void scroll(VControl control) throws Exception;
    public void dragAndDrop(Strategy strategy, String hook1, String hook2) throws Exception;
    public void dragAndDrop(VControl control1, VControl control2) throws Exception;
}

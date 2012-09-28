package com.sugarcrm.voodoo.automation;

import com.sugarcrm.voodoo.IAutomation;
import com.sugarcrm.voodoo.automation.VControl;
import com.sugarcrm.voodoo.IAutomation.Strategy;


public interface IFramework {
	
	public void start(String url) throws Exception;
	public void stop() throws Exception;
	public VControl getControl(Strategy strategy, String hook) throws Exception;
	public String getText(VControl control) throws Exception;
	public String getText(IAutomation.Strategy strategy, String hook) throws Exception;
	public void hover(VControl control) throws Exception;
	public void hover(IAutomation.Strategy strategy, String hook) throws Exception;
	public void click(IAutomation.Strategy strategy, String hook) throws Exception;
	public void click(VControl control) throws Exception;
	public void rightClick(IAutomation.Strategy strategy, String hook) throws Exception;
	public void rightClick(VControl control) throws Exception;
	public void input(IAutomation.Strategy strategy, String hook, String input) throws Exception;
	public void input(VControl control, String input) throws Exception;
	public void acceptDialog() throws Exception;
	public void switchToPopup() throws Exception;
}

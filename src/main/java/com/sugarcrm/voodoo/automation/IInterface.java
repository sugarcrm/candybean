package com.sugarcrm.voodoo.automation;


import com.sugarcrm.voodoo.automation.control.VAControl;
import com.sugarcrm.voodoo.automation.control.VControl;
import com.sugarcrm.voodoo.automation.control.VHook;
import com.sugarcrm.voodoo.automation.control.VHook.Strategy;
import com.sugarcrm.voodoo.automation.control.VSelect;


public interface IInterface {
	public enum Type { FIREFOX, IE, CHROME, SAFARI, ANDROID, IOS; }
	
	// General functionality
	public void pause(long ms) throws Exception;
	public void interact(String message);
	
	// General automation functionality
	public void start() throws Exception;
	public void stop() throws Exception;
	public void acceptDialog() throws Exception;
	public void closeWindow() throws Exception;
	public void focusByIndex(int index) throws Exception;
	public void focusByTitle(String title) throws Exception;
	public void focusByUrl(String url) throws Exception;
	public void go(String url) throws Exception;
	public void maximize() throws Exception;

	// VControl functionality
	public VControl getControl(VHook hook) throws Exception;
	public VControl getControl(Strategy strategy, String hook) throws Exception;

	// VSelect functionality
	public VSelect getSelect(VHook hook) throws Exception;
	public VSelect getSelect(Strategy strategy, String hook) throws Exception;
	
	// Android Setup Functionality
	public void startApp() throws Exception;
	public void finishApp() throws Exception;
	public void setApkPath(String aut, String messenger, String testrunner);
	public void ignoreInstallAUT() throws Exception;
	public void ignoreInstallMessenger() throws Exception;
	public void ignoreInstallRunner() throws Exception;
	
	// Android VAControl functionality
	public VAControl getAControl() throws Exception;
}

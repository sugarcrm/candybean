package com.sugarcrm.voodoo.automation;


import com.sugarcrm.voodoo.automation.control.VAControl;
import com.sugarcrm.voodoo.automation.control.VControl;
import com.sugarcrm.voodoo.automation.control.VHook;
import com.sugarcrm.voodoo.automation.control.VHook.Strategy;
import com.sugarcrm.voodoo.automation.control.VSelect;


/**
 * <p>Voodoo2 tests use an implementation of IInterface to interact
 * with the web browser and to perform testing.	 For examples of how
 * to use <code>IInterface</code>, see the sample tests in
 * <code>com.sugarcrm.examples.*</code>.</p>
 *
 * @author cwarmbold
 */

public interface IInterface {

	/**
	 * Which web browser to run the test against.
	 */

	public enum Type { FIREFOX, IE, CHROME, SAFARI, ANDROID, IOS; }
	
	//////////// General functionality ////////////

	/**
	 * Pause the test for the specified duration.
	 *
	 * @param ms  duration of pause in milliseconds
	 * @throws Exception	 if the underlying {@link Thread#sleep} is interrupted
	 */

	public void pause(long ms) throws Exception;

	/**
	 * Display a modal dialog box to the test user.
	 *
	 * @param message	 String to display on the dialog box
	 * @throws Exception	 if the program is running headless (with no GUI)
	 */

	public void interact(String message);
	
	//////////// General automation functionality ////////////

	/**
	 * Launch and initialize a web browser.
	 * @throws Exception	 <i>not thrown</i>
	 */

	public void start() throws Exception;

	/**
	 * Close the web browser and perform final cleanup.
	 *
	 * @throws Exception	  <i>not thrown</i>
	 */

	public void stop() throws Exception;

	/**
	 * Click &quot;OK&quot; on a modal dialog box (usually referred to
	 * as a &quot;javascript dialog&quot;).
	 *
	 * @throws Exception	 if no dialog box is present
	 */

	public void acceptDialog() throws Exception;

	/**
	 * Close the current browser window.
	 *
	 * @throws Exception	  <i>not thrown</i>
	 */

	public void closeWindow() throws Exception;

	/**
	 * Focus a browser window by its index.
	 *
	 * <p>The order of browser windows is somewhat arbitrary and not
	 * guaranteed, although window creation time ordering seems to be
	 * the most common.</p>
	 *
	 * @param index  the window index
	 * @throws Exception	 if the specified window cannot be found
	 */

	public void focusByIndex(int index) throws Exception;

	/**
	 * Focus a browser window by its window title.
	 *
	 * <p>If more than one window has the same title, the first
	 * encountered is the one that is focused.</p>
	 *
	 * @param title  the exact window title to be matched
	 * @throws Exception	  if the specified window cannot be found
	 */

	public void focusByTitle(String title) throws Exception;

	/**
	 * Focus a browser window by its URL.
	 *
	 * <p>If more than one window has the same URL, the first
	 * encountered is the one that is focused.</p>
	 *
	 * @param url	the URL to be matched
	 * @throws Exception	  if the specified window cannot be found
	 */

	public void focusByUrl(String url) throws Exception;

	/**
	 * Load a URL in the browser window.
	 *
	 * @param url	the URL to be loaded by the browser
	 * @throws Exception		<i>not thrown</i>
	 */

	public void go(String url) throws Exception;

	/**
	 * Maximize the browser window.
	 *
	 * @throws Exception	 <i>not thrown</i>
	 */

	public void maximize() throws Exception;

	//////////// VControl functionality ////////////

	/**
	 * Get a control from the current page.
	 *
	 * @param hook	 description of how to find the control
	 * @throws Exception	 <i>not thrown</i>
	 */

	public VControl getControl(VHook hook) throws Exception;

	/**
	 * Get a control from the current page.
	 *
	 * @param strategy  method to use to search for the control
	 * @param hook		  string to find using the specified strategy
	 * @throws Exception	 <i>not thrown</i>
	 */

	public VControl getControl(Strategy strategy, String hook) throws Exception;

	//////////// VSelect functionality ////////////

	/**
	 * Get a &lt;SELECT&gt; control from the current page.
	 *
	 * @param hook	 description of how to find the control
	 * @throws Exception	 <i>not thrown</i>
	 */

	public VSelect getSelect(VHook hook) throws Exception;

	/**
	 * Get a &lt;SELECT&gt; control from the current page.
	 *
	 * @param strategy  method to use to search for the control
	 * @param hook		  string to find using the specified strategy
	 * @throws Exception	 <i>not thrown</i>
	 */

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

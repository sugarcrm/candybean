package com.sugarcrm.voodoo.automation.control;

/**
 * Interface for web element manipulation.
 *
 * @author cwarmbold
 */

public interface IControl {
	
   /**
    * Get the value of an attribute of the control.
    *
    * @param attribute  name of the attribute to get
    * @return the value of the specified attribute
    * @throws Exception  if the attribute does not exist or the element
    *                    cannot be found
    */

	public String getAttribute(String attribute) throws Exception;
	
   /**
    * Get the visible text of this element.
    *
    * @return the visible text of this element
    * @throws Exception  if the element cannot be found
    */

	public String getText() throws Exception;
	
   /**
    * Click the element.
    *
    * @throws Exception  if the element can not be found
    */

	public void click() throws Exception;
	
   /**
    * Double-click the element.
    *
    * @throws Exception  if the element cannot be found
    */

	public void doubleClick() throws Exception;
	
   /**
    * Drag this control and drop onto another control.
    *
    * @param dropControl  target of the drag and drop
    * @throws Exception  if either element cannot be found
    */

	public void dragNDrop(VControl dropControl) throws Exception;
	
   /**
    * Hover over this control.
    *
    * @throws Exception  if the element cannot be found
    */

	public void hover() throws Exception;
	
   /**
    * Right-click this control.
    *
    * @throws Exception  if the element cannot be found
    */

	public void rightClick() throws Exception;
	
   /**
    * Scroll the browser window to this control.
    *
    * @throws Exception  if the element cannot be found or if the scroll fails
    */

	public void scroll() throws Exception;
	
   /**
    * Send a string to this control.
    *
    * @param input  string to send
    * @throws Exception  if the element cannot be found
    */

	public void sendString(String input) throws Exception;
	
   /**
    * Wait until this control is displayed.
    *
    * <p>The voodoo property &quot;perf.explicit_wait&quot; sets the
    * timeout for the wait.  The default value is 12000
    * milliseconds.</p>
    *
    * @throws Exception if the element cannot be found before the
    *                   timeout or if the timeout value in
    *                   <code>voodoo.properties</code> is not an integer
    */

	public void waitOn() throws Exception;
	
   /**
    * Wait until an attribute of this control has a specified value.
    *
    * <p>The voodoo property &quot;perf.explicit_wait&quot; sets the
    * timeout for the wait.  The default value is 12000
    * milliseconds.</p>
    *
    * @param attribute  the attribute to wait on
    * @param value      the value of the attribute to wait on
    * @throws Exception if the element cannot be found before the
    *                   timeout or if the timeout value in
    *                   <code>voodoo.properties</code> is not an integer
    */

	public void wait(String attribute, String value) throws Exception;
}

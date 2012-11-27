package com.sugarcrm.voodoo.automation.control;

import com.sugarcrm.voodoo.automation.IAutomation;


/**
 * @author cwarmbold
 */
public class VControl {
	
	protected final VHook hook;
	protected final IAutomation auto;

	public VControl(VHook hook, IAutomation auto) {
		this.hook = hook;
		this.auto = auto;
	}
	
	public VHook getHook() throws Exception { return this.hook; }
	public String getAttribute(String attribute) throws Exception {	return this.auto.getAttribute(this, attribute); }
	public String getText() throws Exception { return this.auto.getText(this); }
	public void click() throws Exception { this.auto.click(this); }
	public void doubleClick() throws Exception { this.auto.doubleClick(this); }
    public void dragNDrop(VControl target) throws Exception { this.auto.dragNDrop(this, target); }
	public void hover() throws Exception { this.auto.hover(this); }
	public void rightClick() throws Exception { this.auto.rightClick(this); }
	public void scroll() throws Exception { this.auto.scroll(this); }
	public void sendString(String input) throws Exception {	this.auto.sendString(this, input); }
	public void wait(String attribute, String value) throws Exception { this.auto.wait(this, attribute, value); }	
	public void waitOn() throws Exception { this.auto.waitOn(this); }
	
	@Override
	public String toString() { return "VControl(" + this.hook.toString() + ")";}
}

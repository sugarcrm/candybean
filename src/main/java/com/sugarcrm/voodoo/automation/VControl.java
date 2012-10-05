package com.sugarcrm.voodoo.automation;


public abstract class VControl implements IControl {
	public enum Type { ELEMENT, HLINK, TEXT, BUTTON; }
	
	private final IFramework vAutomation;

	public VControl(IFramework vAutomation) {
		this.vAutomation = vAutomation;
	}
	
	@Override
	public String getText() throws Exception {
		this.vAutomation.getText(this);
		return null;
	}

	@Override
	public void hover() throws Exception {
		this.vAutomation.hover(this);
	}

	@Override
	public void click() throws Exception {
		this.vAutomation.click(this);
	}
	
	@Override
	public void rightClick() throws Exception {
		this.vAutomation.click(this);
	}

	@Override
	public void input(String input) throws Exception {
		this.vAutomation.input(this, input);
	}
}

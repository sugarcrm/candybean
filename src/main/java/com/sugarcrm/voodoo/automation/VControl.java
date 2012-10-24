package com.sugarcrm.voodoo.automation;


/**
 * @author cwarmbold
 *
 */
public abstract class VControl implements IControl {
	
	private final IFramework vAutomation;

	/**
	 * @param vAutomation
	 */
	public VControl(IFramework vAutomation) {
		this.vAutomation = vAutomation;
	}
	
	/* (non-Javadoc)
	 * @see com.sugarcrm.voodoo.automation.IControl#getText()
	 */
	@Override
	public String getText() throws Exception {
		this.vAutomation.getText(this);
		return null;
	}

	/* (non-Javadoc)
	 * @see com.sugarcrm.voodoo.automation.IControl#click()
	 */
	@Override
	public void click() throws Exception {
		this.vAutomation.click(this);
	}
	
	/* (non-Javadoc)
	 * @see com.sugarcrm.voodoo.automation.IControl#getSelected()
	 */
	@Override
	public String getSelected() throws Exception {
		return this.vAutomation.getSelected(this);
	}
	
	/* (non-Javadoc)
	 * @see com.sugarcrm.voodoo.automation.IControl#select(java.lang.String)
	 */
	@Override
	public void select(String value) throws Exception {
		this.vAutomation.select(this, value);
	}
	
	/* (non-Javadoc)
	 * @see com.sugarcrm.voodoo.automation.IControl#waitFor()
	 */
	@Override
	public void waitFor() throws Exception {
		this.vAutomation.waitFor(this);
	}
	
	/* (non-Javadoc)
	 * @see com.sugarcrm.voodoo.automation.IControl#waitFor(java.lang.String, java.lang.String)
	 */
	@Override
	public void waitFor(String attribute, String value) throws Exception {
		this.vAutomation.waitFor(this, attribute, value);
	}
	
	/* (non-Javadoc)
	 * @see com.sugarcrm.voodoo.automation.IControl#hover()
	 */
	@Override
	public void hover() throws Exception {
		this.vAutomation.hover(this);
	}

	/* (non-Javadoc)
	 * @see com.sugarcrm.voodoo.automation.IControl#input(java.lang.String)
	 */
	@Override
	public void input(String input) throws Exception {
		this.vAutomation.input(this, input);
	}

	/* (non-Javadoc)
	 * @see com.sugarcrm.voodoo.automation.IControl#rightClick()
	 */
	@Override
	public void rightClick() throws Exception {
		this.vAutomation.rightClick(this);
	}

	/* (non-Javadoc)
	 * @see com.sugarcrm.voodoo.automation.IControl#scroll()
	 */
	@Override
	public void scroll() throws Exception {
		this.vAutomation.scroll(this);
	}
	
    /* (non-Javadoc)
     * @see com.sugarcrm.voodoo.automation.IControl#dragNDrop(com.sugarcrm.voodoo.automation.IControl)
     */
    @Override
	public void dragNDrop(IControl target) throws Exception {
		this.vAutomation.dragNDrop(this, (VControl) target);
	}
}

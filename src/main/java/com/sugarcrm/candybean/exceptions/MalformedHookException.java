package com.sugarcrm.candybean.exceptions;


/**
 * Exception thrown when a hook file is not formatted correctly.
 * @author Shehryar Farooq
 */
public class MalformedHookException extends CandybeanException {

	private static final long serialVersionUID = 1L;
	
	/**
	 * The name of the malformed hook.
	 */
	private String hookName;
	
	public MalformedHookException(String hookName) {
		this.hookName = hookName;
	}

	@Override
	public String getMessage() {
		return "The hook " + hookName + " is not formatted correctly in the tests hook file ";
	}

	public String getHookName() {
		return hookName;
	}

	public void setHookName(String hookName) {
		this.hookName = hookName;
	}
}

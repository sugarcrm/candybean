package com.sugarcrm.candybean.utilities.exception;

/**
 * Default exception to throw for Candybean related tasks.
 * 
 * @author Shehryar Farooq
 *
 */
public class CandybeanException extends Exception {
	
	/**
	 * Generated
	 */
	private static final long serialVersionUID = 1L;
	
	private String message;
	
	public CandybeanException() {
		super();
	}

	public CandybeanException(String message) {
		super(message);
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
}

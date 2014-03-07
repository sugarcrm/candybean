package com.sugarcrm.candybean.exceptions;

/**
 * Default exception to throw for Candybean related tasks.
 * 
 * @author Shehryar Farooq, Conrad Warmbold
 *
 */
public class CandybeanException extends Exception {
	
	/**
	 * Generated
	 */
	private static final long serialVersionUID = 1L;
	
	public CandybeanException() {
		super();
	}

	public CandybeanException(String message) {
		super(message);
	}

	public CandybeanException(Exception e) {
		super(e.getMessage());
	}

	public String getMessage() {
		return super.getMessage();
	}
}

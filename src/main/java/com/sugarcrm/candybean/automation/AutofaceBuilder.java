package com.sugarcrm.candybean.automation;

import java.util.logging.Logger;
import com.sugarcrm.candybean.exceptions.CandybeanException;

public abstract class AutofaceBuilder {

	/*
	 * Logger for this class
	 */
	protected Logger logger = Logger.getLogger(this.getClass().getSimpleName());
	
	/*
	 * The class that calls this builder, required for logging and mobile interfaces.
	 */
	protected Class<?> cls;
	
	/*
	 * Candybean instance
	 */
	protected Candybean candybean = null;
	
	/**
	 * @param cls Typically the test class which uses this interface builder
	 */
	public AutofaceBuilder(Class<?> cls) {
		this.cls = cls;
	}

	public abstract Autoface build() throws CandybeanException;
	public abstract boolean isFullyConfigured();
}

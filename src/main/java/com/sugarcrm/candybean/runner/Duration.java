package com.sugarcrm.candybean.runner;

/**
 * Specific to the duration of how long a failing test should be recorded for.
 * @author Shehryar Farooq
 */
public enum Duration {
	/**
	 * Record the entire duration of the failing test
	 */
	ENTIRE,
	/**
	 * Record the final moments before a test fails
	 */
	FINAL;
}

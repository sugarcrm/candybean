package com.sugarcrm.candybean.runner;

/**
 * Specific to the duration of how long a failing test should be recorded for.
 * @author Shehryar Farooq
 */
public enum Duration {
	/**
	 * Record the entire duration of all tests
	 */
	ENTIRE,
	/**
	 * Record the final moments of all tests
	 */
	FINAL,
	/**
	 * Record the entire duration of a test that fails
	 */
	ENTIRE_FAILED,
	/**
	 * Record the final moments before a test fails
	 */
	FINAL_FAILED;
}

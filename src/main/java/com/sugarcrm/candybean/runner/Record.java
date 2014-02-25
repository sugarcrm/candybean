package com.sugarcrm.candybean.runner;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.Test;

/**
 * Provides the ability to capture the screen while a test is executed.
 * Usage: Annotate a {@link Test} method with @Record to keep a recording of the test if it fails.
 * 
 * @author Shehryar Farooq
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Record {
	/**
	 * Returns the {@link Duration} of the recorded test.
	 * @return The {@link Duration}
	 */
	Duration duration() default Duration.FINAL_FAILED;
}

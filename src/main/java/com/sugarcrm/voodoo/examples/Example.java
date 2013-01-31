/*
 * Example.java --
 *
 *      Definition of the @Example annotation.
 */

package com.sugarcrm.voodoo.examples;

import java.lang.annotation.*;

/**
 * The @Example annotation tags the method within a class that is to
 * be used as the entry point for running the VDD2 code example.
 */

@Retention(RetentionPolicy.RUNTIME)
public @interface Example {}

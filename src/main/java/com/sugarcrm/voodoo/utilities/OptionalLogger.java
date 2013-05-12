package com.sugarcrm.voodoo.utilities;

import java.util.logging.Logger;

/**
 * A Logger wrapper that can log INFO and SEVERE messages. Automatically checks
 * if Logger is null. If true, display message to console. Used to substitute a
 * Logger when a Logger's presence is optional.
 * 
 * @author ylin
 * 
 */
public class OptionalLogger {
	Logger log;

	public OptionalLogger() {
		this(null);
	}

	public OptionalLogger(Logger log) {
		this.log = log;
	}

	public void info(String msg, boolean writeBoth) {
		if (writeBoth) {
			if (log != null)
				log.info(msg);
			else {
				System.err.println("Logger is null!");
				System.out.print(msg);
			}
			System.out.print(msg);
		} else {
			if (log != null)
				log.info(msg);
			else
				System.out.print(msg);
		}
	}
	
	public void info(String msg) {
		info(msg, false);
	}

	public void severe(String msg, boolean writeBoth) {
		if (writeBoth) {
			if (log != null)
				log.severe(msg);
			else {
				System.err.println("Logger is null!");
				System.err.print(msg);
			}
		} else {
			if (log != null)
				log.severe(msg);
			else
				System.err.print(msg);
		}
	}
	
	public void severe(String msg) {
		severe(msg, false);
	}
}

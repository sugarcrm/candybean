package com.sugarcrm.candybean.utilities;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class CandybeanLogger extends Logger {
	
	/*
	 * List of handlers that have have been added to the candybean logger
	 */
	private Map<String,FileHandler> handlers = new HashMap<String, FileHandler>();

	/*
	 * Default constructor
	 */
	public CandybeanLogger(String name) {
		super(name, null);
	}

	/**
	 * Adds the handler to the logger
	 */
	public void addHandler(FileHandler fh, String simpleClassName) {
		handlers.put(simpleClassName, fh);
		super.addHandler(fh);
	}
	
	/**
	 * Removes all the handlers in the candybean logger
	 */
	public void removeAllHandlers(){
		// Since we are iterating over a list while removing items, use
		// an iterator to properly handle iteration
		for(Iterator<String> it = handlers.keySet().iterator(); it.hasNext();) {
			String str = it.next();
			FileHandler fh = handlers.get(str);
			fh.close();
			super.removeHandler(fh);
			it.remove();
		}
	}
	
	/**
	 * Removes all the handlers in the candybean logger
	 */
	public void removeAllHandlersExcept(String simpleClassName){
		// Since we are iterating over a list while removing items, use
		// an iterator to properly handle iteration
		for(Iterator<String> it = handlers.keySet().iterator(); it.hasNext();) {
			String str = it.next();
			if (!str.equals(simpleClassName)) {
				FileHandler fh = handlers.get(str);
				fh.close();
				super.removeHandler(fh);
				it.remove();
			}
		}
	}
	
	/**
	 * Returns whether the logger contains a file handler for this class name.
	 */
	public boolean containsHandler(String className){
		return handlers.containsKey(className);
	}

}

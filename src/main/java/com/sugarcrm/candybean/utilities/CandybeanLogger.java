package com.sugarcrm.candybean.utilities;

import java.util.HashMap;
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
	 * Removes the handler from the list of file handlers
	 */
	public void removeHandler(String simpleClassName) throws SecurityException { 
		FileHandler handler = handlers.get(simpleClassName);
		if(handler != null){
			handlers.remove(simpleClassName);
			handler.close();
			super.removeHandler(handler);
		}
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
		for(String key: handlers.keySet()){
			removeHandler(key);
		}
	}
	
	/**
	 * Removes all the handlers in the candybean logger
	 */
	public void removeAllHandlersExcept(String simpleClassName){
		for(String key: handlers.keySet()){
			if (!key.equals(simpleClassName)) {
				removeHandler(key);
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

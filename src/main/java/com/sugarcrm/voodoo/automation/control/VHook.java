package com.sugarcrm.voodoo.automation.control;

import java.util.HashMap;
import java.util.Properties;

public class VHook {
	
	public static String HOOK_DELIMITER = ":";
	
	public enum Strategy { CSS, XPATH, ID, NAME, LINK, PLINK, CLASS, TAG; }
	public final Strategy hookStrategy;
	public final String hookString;

	public VHook(Strategy hookStrategy, String hookString) {
		this.hookStrategy = hookStrategy;
		this.hookString = hookString;
	}
	
	/**
	 * Returns a preloaded hashmap based on the given, formatted hooks (Properties) file.
	 * 
	 * @param hooks
	 * @return
	 * @throws Exception
	 */
	public static HashMap<String, VHook> getHooks(Properties hooks) throws Exception {
		HashMap<String, VHook> hooksMap = new HashMap<String, VHook>();
		for(String name : hooks.stringPropertyNames()) {
//			System.out.println("hook name: " + name);
			String[] strategyNHook = hooks.getProperty(name).split(HOOK_DELIMITER);
			if (strategyNHook.length != 2) throw new Exception("Malformed hooks file for name: " + name);
			else {
//				System.out.println("strategy: " + strategyNHook[0] + ", hook: " + strategyNHook[1]);
				Strategy strategy = VHook.getStrategy(strategyNHook[0]);
				String hook = strategyNHook[1];
				hooksMap.put(name, new VHook(strategy, hook));
			}
		}
		return hooksMap;
	}
	
	/**
	 * Returns the Voodoo-defined hook strategy based on the given string.
	 * 
	 * @param strategy
	 * @return
	 * @throws Exception
	 */
	public static Strategy getStrategy(String strategy) throws Exception {
		switch(strategy) {
		case "CSS": return Strategy.CSS;
		case "ID": return Strategy.ID;
		case "NAME": return Strategy.NAME;
		case "XPATH": return Strategy.XPATH;
		case "LINK": return Strategy.LINK;
		case "PLINK": return Strategy.PLINK;
		case "CLASS": return Strategy.CLASS;
		case "TAG": return Strategy.TAG;
		default:
			throw new Exception("Strategy not recognized: " + strategy);
		}
	}
	
	public String toString() {
		return "VHook(" + this.hookStrategy + "," + this.hookString + ")";
	}
}

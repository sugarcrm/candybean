/**
 * Candybean is a next generation automation and testing framework suite.
 * It is a collection of components that foster test automation, execution
 * configuration, data abstraction, results illustration, tag-based execution,
 * top-down and bottom-up batches, mobile variants, test translation across
 * languages, plain-language testing, and web service testing.
 * Copyright (C) 2013 SugarCRM, Inc. <candybean@sugarcrm.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.sugarcrm.candybean.automation.control;

import java.util.HashMap;
import java.util.Properties;

public class VHook {
	
	public final static String HOOK_DELIMITER = ":";
	
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

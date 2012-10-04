package com.sugarcrm.voodoo;

import java.io.File;
import java.util.HashMap;
import java.util.Properties;

import com.sugarcrm.voodoo.IAutomation.Strategy;
import com.sugarcrm.voodoo.automation.VHook;


/**
 * Utils is simply a container for automation/Java-related helper functions that
 * are relatively non-specific to any particular library/framework.
 * 
 * @author cwarmbold
 *
 */
public class Utils {
	
	public static String HOOK_DELIMITER = ":";
	
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
				Strategy strategy = Utils.getStrategy(strategyNHook[0]);
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
		default:
			throw new Exception("Strategy not recognized: " + strategy);
		}
	}
	
	/**
	 * Given a properties file, a default key-value pair value, and a key, this
	 * function returns:\n a) the default value\n b) or, if exists, the
	 * key-value value in the properties file\n c) or, if exists, the system
	 * property key-value value. This function is used to override configuration
	 * files in cascading fashion.
	 * 
	 * @param props
	 * @param defaultValue
	 * @param key
	 * @return
	 */
	public static String getCascadingPropertyValue(Properties props,
			String defaultValue, String key) {
		String value = defaultValue;
		if (props.containsKey(key))
			value = props.getProperty(key);
		if (System.getProperties().containsKey(key))
			value = System.getProperty(key);
		return value;
	}

	
	/**
	 * Given a string, this function returns the suffix of that string matching the given length.
	 * 
	 * @param s
	 * @param length
	 * @return
	 */
	public static String pretruncate(String s, int length) {
		if (s.length() <= length)
			return s;
		return s.substring(s.length() - length);
	}

	
	/**
	 * Pair is a python-2-tuple lightweight equivalent for convenience.
	 * 
	 * @author cwarmbold
	 * 
	 * @param <X>
	 * @param <Y>
	 */
	public static class Pair<X, Y> {
		public final X x;
		public final Y y;

		public Pair(X x, Y y) {
			this.x = x;
			this.y = y;
		}

		@Override
		public String toString() {
			return "x:" + x.toString() + ",y:" + y.toString();
		}
	}

	/**
	 * adjustPath - Modify the given path to support different Operating Systems' path type.
	 * 
	 * @author wli
	 * 
	 * @param path
	 * @return
	 */
	public static String adjustPath(String path){
		path = path.replace("\\", "/");
		path = path.replaceAll("/+", File.separator);
		return path;
	}
	
	
	/**
	 * Triplet is a python-3-tuple lightweight equivalent for convenience.
	 * 
	 * @author cwarmbold
	 * 
	 * @param <X>
	 * @param <Y>
	 * @param <Z>
	 */
	public static class Triplet<X, Y, Z> { 
		public final X x; 
		public final Y y; 
		public final Z z;
		
		public Triplet(X x, Y y, Z z) { 
			this.x = x; 
			this.y = y;
			this.z = z;
		} 
		
		@Override
		public String toString() {
			return "x:" + x.toString() + ",y:" + y.toString() + ",z:" + z.toString();
		}
	}
}

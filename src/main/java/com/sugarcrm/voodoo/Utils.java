package com.sugarcrm.voodoo;
import java.io.File;
import java.util.Properties;


/**
 * Utils is simply a container for automation/Java-related helper functions that
 * are relatively non-specific to any particular library/framework.
 * 
 * @author cwarmbold
 *
 */
public class Utils {
	
	/**
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
			/**
			 *
			 * toString() 
			 * @param <X> 
			 * @param <Y> 
			 */
		@Override public String toString() {
			  return "x:" + x.toString() + ",y:" + y.toString() + ",z:" + z.toString();
		  }
	}
}

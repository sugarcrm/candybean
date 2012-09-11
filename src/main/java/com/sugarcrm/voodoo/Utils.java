package com.sugarcrm.voodoo;

<<<<<<< HEAD
import java.util.ResourceBundle;


/**
 * Utils is simply a container for automation/Java-related helper functions that
 * are relatively non-specific to any particular library/framework.
 * 
 * @author cwarmbold
 * 
=======
/**
 * @author 
 *
>>>>>>> 45f87a95674cf2ebb29e3d91e955111128e53d9b
 */
public class Utils {

	
	/**
	 * 
	 * @param props
	 * @param defaultValue
	 * @param key
	 * @return
	 */
	public static String getCascadingPropertyValue(ResourceBundle props,
			String defaultValue, String key) {
		String value = defaultValue;
		if (props.containsKey(key))
			value = props.getString(key);
		if (System.getProperties().containsKey(key))
			value = System.getProperty(key);
		return value;
	}

	
	/**
	 * 
<<<<<<< HEAD
	 * @param props
	 * @param defaultValue
	 * @param key
	 * @return
=======
	 * trimString() 
	 * @param s 
	 * @param length 
	 * @return 
>>>>>>> 45f87a95674cf2ebb29e3d91e955111128e53d9b
	 */
	public static String trimString(String s, int length) {
		if (s.length() <= length)
			return s;
		return s.substring(s.length() - length);
	}

<<<<<<< HEAD
	
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
=======
	/**
	 * 
	 * Pair<X,Y> 
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
			/**
			 * @author wli
			 *
			 * @param <X>
			 * @param <Y>
			 */
		@Override public String toString() {
			  return "x:" + x.toString() + ",y:" + y.toString();
		  }
>>>>>>> 45f87a95674cf2ebb29e3d91e955111128e53d9b
	}

	
	/**
<<<<<<< HEAD
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
			return "x:" + x.toString() + ",y:" + y.toString() + ",z:"
					+ z.toString();
		}
=======
	 * 
	 * Triplet<X, Y, Z> 
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
>>>>>>> 45f87a95674cf2ebb29e3d91e955111128e53d9b
	}
}

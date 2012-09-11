package com.sugarcrm.voodoo;

/**
 * @author 
 *
 */
public class Utils {
	
	/**
	 * 
	 * trimString() 
	 * @param s 
	 * @param length 
	 * @return 
	 */
	public static String trimString(String s, int length) {
		if (s.length() <= length) return s;
		return s.substring(s.length() - length); 
	}

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
	}
	
	/**
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
	}
}

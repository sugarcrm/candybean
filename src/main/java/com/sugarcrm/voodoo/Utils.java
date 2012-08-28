package com.sugarcrm.voodoo;

public class Utils {
	
	public static String trimString(String s, int length) {
		if (s.length() <= length) return s;
		return s.substring(s.length() - length); 
	}

	public static class Pair<X, Y> { 
		  public final X x; 
		  public final Y y; 
		  public Pair(X x, Y y) { 
			  this.x = x; 
			  this.y = y; 
		  }
		  @Override public String toString() {
			  return "x:" + x.toString() + ",y:" + y.toString();
		  }
	}
	
	public static class Triplet<X, Y, Z> { 
		  public final X x; 
		  public final Y y; 
		  public final Z z; 
		  public Triplet(X x, Y y, Z z) { 
			  this.x = x; 
			  this.y = y;
			  this.z = z;
		  } 
		  @Override public String toString() {
			  return "x:" + x.toString() + ",y:" + y.toString() + ",z:" + z.toString();
		  }
	}
}

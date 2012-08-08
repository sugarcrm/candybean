package com.voodoo2;

public class DataStructure {
	
	public static class Pair<X, Y> {
		public final X x;
		public final Y y;
		public Pair(X x, Y y) {
			this.x = x;
			this.y = y;
		}
	}

	public static class Triplet<X, Y, Z> extends Pair<X, Y> {
		public final Z z;
		public Triplet(X x, Y y, Z z) {
			super(x, y);
			this.z = z;
		}
	}
}

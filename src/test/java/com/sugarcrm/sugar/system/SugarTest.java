package com.sugarcrm.sugar.system;

import com.sugarcrm.sugar.Sugar;

public class SugarTest {
	
	protected static Sugar sugar;
	
	public static void first() throws Exception { sugar = new Sugar(); sugar.i.start(); }

	public void setup() throws Exception {}

	public void cleanup() throws Exception {}

	public static void last() throws Exception { sugar.i.stop(); }
}

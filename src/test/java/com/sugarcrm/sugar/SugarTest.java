package com.sugarcrm.sugar;


import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import com.sugarcrm.voodoo.Voodoo;


public abstract class SugarTest {
	
	protected static Voodoo voodoo;
	protected static Properties sugarProps;
	protected static Sugar sugar;

	private static final String curWorkDir = System.getProperty("user.dir");
	private static final String relPropsPath = curWorkDir + File.separator + "src" + File.separator + "test" + File.separator + "resources";
	private static final String voodooPropsPath = relPropsPath + File.separator + "voodoo.properties";
	private static final String sugarPropsPath = relPropsPath + File.separator + "sugar.properties";
	private static final String sugarHooksPath = relPropsPath + File.separator + "sugar.hooks";
	
	public static void setupOnce() throws Exception {
		Properties voodooProps = new Properties();
		voodooProps.load(new FileInputStream(new File(voodooPropsPath)));
		voodoo = Voodoo.getInstance(voodooProps);
		sugarProps = new Properties();
		sugarProps.load(new FileInputStream(new File(sugarPropsPath)));
		Properties sugarHooksProps = new Properties();
		sugarHooksProps.load(new FileInputStream(new File(sugarHooksPath)));
		sugar = Sugar.getInstance(sugarHooksProps);
	}

	public void setup() throws Exception {
		voodoo.start(sugarProps.getProperty("env.base_url"));
		sugar.login(SugarTest.voodoo, "admin", "asdf");
	}

	public void cleanup() throws Exception {
		sugar.logout(SugarTest.voodoo);
		voodoo.stop();
	}

	public static void cleanupOnce() {}
}

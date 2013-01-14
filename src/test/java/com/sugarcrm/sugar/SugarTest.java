package com.sugarcrm.sugar;


import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import com.sugarcrm.voodoo.automation.IInterface;
import com.sugarcrm.voodoo.automation.Voodoo;
import com.sugarcrm.sugar.users.User;
import com.sugarcrm.sugar.users.User.UserBuilder;
import com.sugarcrm.voodoo.utilities.Utils;

import org.junit.BeforeClass;
import org.junit.Before;
import org.junit.After;
import org.junit.AfterClass;


public abstract class SugarTest {
	
	public static Properties sugarProps;
	
	protected static Voodoo voodoo;
	protected static IInterface iface;
	protected static Sugar sugar;
	protected static User admin;

	private static final String curWorkDir = System.getProperty("user.dir");
	private static final String relPropsPath = curWorkDir + File.separator + "src" + File.separator + "test" + File.separator + "resources";
	private static final String voodooPropsPath = relPropsPath + File.separator + "voodoo.properties";
	private static final String sugarPropsPath = relPropsPath + File.separator + "sugar.properties";
	private static final String sugarHooksPath = relPropsPath + File.separator + "sugar.hooks";
	
	public static void setupOnce() throws Exception {
		Properties voodooProps = new Properties();
		voodooProps.load(new FileInputStream(new File(voodooPropsPath)));
		voodoo = Voodoo.getInstance(voodooProps);
		iface = voodoo.getInterface();
		sugarProps = new Properties();
		sugarProps.load(new FileInputStream(new File(sugarPropsPath)));
		Properties sugarHooksProps = new Properties();
		sugarHooksProps.load(new FileInputStream(new File(sugarHooksPath)));
		sugar = Sugar.getInstance(sugarHooksProps);
		String adminUser = Utils.getCascadingPropertyValue(SugarTest.sugarProps, "admin", "sugar.user");
		String adminPass = Utils.getCascadingPropertyValue(SugarTest.sugarProps, "asdf", "sugar.pass");
		String adminName = Utils.getCascadingPropertyValue(SugarTest.sugarProps, "Administrator", "sugar.name");
		UserBuilder ub = new UserBuilder(adminUser, adminName, adminPass, adminPass);
		admin = ub.build();
	}

	public void setup() throws Exception {
		Sugar.login(sugar, iface, "admin", "asdf");
	}

	public void cleanup() throws Exception {
		Sugar.logout(sugar, iface);
		iface.stop();
	}

	public static void cleanupOnce() {}
}

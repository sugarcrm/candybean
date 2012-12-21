package com.sugarcrm.voodooo.android;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import com.sugarcrm.voodoo.automation.IInterface;
import com.sugarcrm.voodoo.automation.Voodoo;

public abstract class AndroidSugarTest {

	protected static Voodoo voodoo;
	protected static IInterface iface;
	
	private static final String curWorkDir = System.getProperty("user.dir");
	private static final String relPropsPath = curWorkDir + File.separator + "src" + File.separator + "test" + File.separator + "resources";
	private static final String voodooPropsPath = relPropsPath + File.separator + "voodooAndroid.properties";

	public static void setupOnce() throws Exception {
		Properties voodooProps = new Properties();
		voodooProps.load(new FileInputStream(new File(voodooPropsPath)));
		voodoo = Voodoo.getInstance(voodooProps);
		iface = voodoo.getInterface();
	}
	
	public void setup() throws Exception {
		//iface.setApkPath("./android/AndroidCalculator.apk", "./android/SAFSTCPMessenger-debug.apk", "./android/RobotiumTestRunner-debug.apk");
		iface.startApp();
		// SugarAndroid.login(...)
	}

	public void cleanup() throws Exception {
		// SugarAndroid.logout(...)
		iface.finishApp();
	}

	public static void cleanupOnce() {}
		
}

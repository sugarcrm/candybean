/**
 * Candybean is a next generation automation and testing framework suite.
 * It is a collection of components that foster test automation, execution
 * configuration, data abstraction, results illustration, tag-based execution,
 * top-down and bottom-up batches, mobile variants, test translation across
 * languages, plain-language testing, and web service testing.
 * Copyright (C) 2013 SugarCRM, Inc. <candybean@sugarcrm.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.sugarcrm.candybean.automation;

//import java.util.List;
//import java.util.Properties;
//import java.util.concurrent.TimeoutException;

//import org.safs.android.auto.lib.DUtilities;
//import org.safs.sockets.RemoteException;
//import org.safs.sockets.ShutdownInvocationException;
//
//import com.jayway.android.robotium.remotecontrol.solo.Solo;
//import com.sugarcrm.candybean.utilities.Utils;

// For more functionality Visit the remoteControl.solo.SoloTest
public class AInterface{
//	public Solo solo = null;
//	private final Properties props;
//
//	//Optional Variables
//	private boolean enableProtocolDebug = true;
//	private boolean enableRunnerDebug = true;
//
//	private boolean installAUT = true;
//	private boolean installMessenger = true;
//	private boolean installRunner = true;
//
//	// Default will be retrieved from the Properties File
//	public static String DEFAULT_AUT_APK;
//	public static String DEFAULT_MESSENGER_APK;
//	public static String DEFAULT_TESTRUNNER_APK;
//	public static String DEFAULT_INSTRUMENT_ARG;
//	public static String DEFAULT_ANDROID_SDK;
//
//	protected String avdSerialNo = "";
//	protected boolean persistEmulators = false;
//	protected boolean weLaunchedEmulator = false;
//	protected boolean unlockEmulatorScreen = true;
//	protected String mainActivityUID = null;
//
//	public AndroidInterface(Properties props){
//		this.props = props;
//		solo = new Solo();
//
//		setDefaultAPK();
//		setupSDKHome();//set AndroidSDK env
//
//		//By default, we disable the debug message of Protocol/Runner so that the console
//		//show only the test log message.
//		setProtocolDebug(false);
//		setRunnerDebug(false);
//	}
//	
//	/**
//	 * This allows the user to set the path for the aut.apk, messenger.apk, and testrunner.apk
//	 * 
//	 * @author Wilson Li
//	 * @param aut - path to the AUT 
//	 * @param messenger - path to the SAFS messenger 
//	 * @param testrunner - path to the testrunner 
//	 */
//	public void setApkPath(String aut, String messenger, String testrunner) {
//		if (aut != null) DEFAULT_AUT_APK = aut;
//		if (messenger != null) DEFAULT_MESSENGER_APK = messenger;
//		if (testrunner != null) DEFAULT_TESTRUNNER_APK = testrunner;
//	}
//
//	/**
//	 * Set variables with Properties File
//	 */
//	private void setDefaultAPK() {
//		//TODO: May somehow use the Voodoo Configuration implemented by Wilson for Cascading feature
//		DEFAULT_ANDROID_SDK = this.props.getProperty("automation.androidsdk");
//		DEFAULT_AUT_APK = this.props.getProperty("automation.apk.aut");
//		DEFAULT_MESSENGER_APK = this.props.getProperty("automation.apk.messenger"); 
//		DEFAULT_TESTRUNNER_APK = this.props.getProperty("automation.apk.runner");
//		String instrument = "com.jayway.android.robotium.remotecontrol.client/com.jayway.android.robotium.remotecontrol.client.RobotiumTestRunner";
//		DEFAULT_INSTRUMENT_ARG = Utils.getCascadingPropertyValue(this.props, instrument, "automation.apk.runner.instrument");
//	}
//
//	/**
//	 * To avoid the AUT installation
//	 * This is used when the apk has already been installed in the device
//	 * 
//	 * @throws Exception
//	 */
//	final public void ignoreInstallAUT() throws Exception {
//		this.installAUT = false;
//	}
//
//	/**
//	 * To avoid the Messenger installation
//	 * This is used when the apk has already been installed in the device
//	 * 
//	 * @throws Exception
//	 */
//	final public void ignoreInstallMessenger() throws Exception {
//		this.installMessenger = false;
//	}
//
//	/**
//	 * To avoid the Runner installation
//	 * This is used when the apk has already been installed in the device
//	 * 
//	 * @throws Exception
//	 */
//	final public void ignoreInstallRunner() throws Exception {
//		this.installRunner = false;
//	}
//
//	/**
//	 * Turn on or off the protocol's debug message
//	 * @param enable
//	 */
//	final public void setProtocolDebug(boolean enableProtocolDebug){
//		this.enableProtocolDebug = enableProtocolDebug;
//	}
//
//	/**
//	 * Turn on or off the runner's debug message
//	 * @param enable
//	 */
//	final public void setRunnerDebug(boolean enableRunnerDebug){
//		this.enableRunnerDebug = enableRunnerDebug;
//	}
//
//	/**
//	 * Prepare and initialize AUT
//	 * @author Wilson Li
//	 * @throws Exception
//	 */
//	final public void startApp() throws Exception {
//		if(!preparation()){
//			error("Preparation error");
//			//stop the emulator if we have launched it.
//			if(!stopEmulator()){
//				warn("We fail to stop the emulator launched by us.");
//			}
//			return;
//		}
//
//		if(initialize()){ debug("Begin Test."); } 
//		else { debug("Test phase cannot begin!"); }
//	}
//
//	/**
//	 * Close Activities and terminate application
//	 * 
//	 * @author Wilson
//	 * @throws Exception
//	 */
//	final public void finishApp() throws Exception {
//		try { 
//			// SHUTDOWN all Activities.  Done Testing.
//			if(solo.finishOpenedActivities()){
//				info("Application finished/shutdown without error.");				
//			}else{
//				warn("Application finished/shutdown with error.");
//			}
//
//			// Check Termination
//			if(!terminate()){
//				warn("Termination of Solo fail!");
//			} 
//		} catch (IllegalThreadStateException e) {
//			e.printStackTrace();
//		} catch (RemoteException e) {
//			e.printStackTrace();
//		} catch (TimeoutException e) {
//			e.printStackTrace();
//		} catch (ShutdownInvocationException e) {
//			e.printStackTrace();
//		} 
//	}
//
//	/**
//	 * Install the apk of SAFSTCPMessenger and RobotiumTestRunner<br>
//	 * Forward the tcp port from on-computer-2411 to on-device-2410<br>
//	 * 
//	 * @return	True if the preparation is finished successfully.
//	 */
//	final protected boolean preparation(){
//		try{
//			if(!prepareDevice()){
//				throw new RuntimeException("Can't detect connected device/emulator!");
//			}
//
//			// 1. Install apks
//			if (installAUT) {
//				debug("INSTALLING " + DEFAULT_AUT_APK);
//				DUtilities.installReplaceAPK(DEFAULT_AUT_APK);
//			} else { debug("BYPASSING INSTALLATION of target AUT..."); }
//
//			if (installMessenger) {
//				debug("INSTALLING " + DEFAULT_MESSENGER_APK);
//				DUtilities.installReplaceAPK(DEFAULT_MESSENGER_APK);
//			} else { debug("BYPASSING INSTALLATION of SAFS Messenger..."); }
//
//			if (installRunner) {
//				debug("INSTALLING " + DEFAULT_TESTRUNNER_APK);
//				DUtilities.installReplaceAPK(DEFAULT_TESTRUNNER_APK);
//			} else {
//				debug("BYPASSING INSTALLATION of Instrumentation Test Runner...");
//			}
//
//			// 2. Launch the InstrumentationTestRunner
//			if(!DUtilities.launchTestInstrumentation(DEFAULT_INSTRUMENT_ARG)){
//				throw new RuntimeException("Fail to launch instrument '"+DEFAULT_INSTRUMENT_ARG+"'");
//			}
//			// 3. Forward tcp port is needed? how to know the way of connection, by
//			// USB? by WIFI?
//			boolean portForwarding = true;
//			solo.setPortForwarding(portForwarding);
//
//			debug("Prepare for test successfully.");
//
//		}catch(RuntimeException e){
//			error("During preparation, met exception="+e.getMessage());
//			return false;
//		}
//
//		return true;
//	}
//
//	public boolean prepareDevice(){
//		// see if any devices is attached
//		boolean havedevice = false;
//
//		List<String> devices = null;
//		try{
//			devices = DUtilities.getAttachedDevices();
//			info("Detected "+ devices.size() +" device/emulators attached.");
//			if(devices.size() == 0){
//				DUtilities.DEFAULT_EMULATOR_AVD = avdSerialNo;
//				if((DUtilities.DEFAULT_EMULATOR_AVD != null) && (DUtilities.DEFAULT_EMULATOR_AVD.length()> 0)){
//					//DUtilities.killADBServer();
//					//try{Thread.sleep(5000);}catch(Exception x){}
//					info("Attempting to launch EMULATOR_AVD: "+ DUtilities.DEFAULT_EMULATOR_AVD);
//					if (! DUtilities.launchEmulatorAVD(DUtilities.DEFAULT_EMULATOR_AVD)){
//						String msg = "Unsuccessful launching EMULATOR_AVD: "+DUtilities.DEFAULT_EMULATOR_AVD +", or TIMEOUT was reached.";
//						debug(msg);
//						return false;							
//					}else{
//						weLaunchedEmulator = true;
//						info("Emulator launch appears to be successful...");
//						havedevice = true;
//						if(unlockEmulatorScreen) {
//							String stat = DUtilities.unlockDeviceScreen()? " ":" NOT ";
//							info("Emulator screen was"+ stat +"successfully unlocked!");
//						}						
//					}
//				}else{
//					String msg = "No Devices found and no EMULATOR_AVD specified in configuration file.";
//					debug(msg);
//					return false;							
//				}				
//			}else if(devices.size() > 1){
//				// if multiple device attached then user DeviceSerial to target device
//				DUtilities.DEFAULT_DEVICE_SERIAL = avdSerialNo;
//				if(DUtilities.DEFAULT_DEVICE_SERIAL.length() > 0){
//					boolean matched = false;
//					int d = 0;
//					String lcserial = DUtilities.DEFAULT_DEVICE_SERIAL.toLowerCase();
//					String lcdevice = null;
//					for(;(d < devices.size())&&(!matched);d++){
//						lcdevice = ((String)devices.get(d)).toLowerCase();
//						info("Attempting match device '"+ lcdevice +"' with default '"+ lcserial +"'");
//						matched = lcdevice.startsWith(lcserial);
//					}
//					// if DeviceSerial does not match one of multiple then abort
//					if(matched){
//						havedevice = true;
//						DUtilities.USE_DEVICE_SERIAL = " -s "+ DUtilities.DEFAULT_DEVICE_SERIAL +" ";
//					}else{
//						String msg = "Requested Device '"+ DUtilities.DEFAULT_DEVICE_SERIAL +"' was not found.";
//						debug(msg);
//						return false;							
//					}
//				}else{
//					// if no DeviceSerial present then use first device
//					String device = null;
//					String tdev = (String)devices.get(0);
//					if(tdev.endsWith("device")){
//						device = tdev.substring(0, tdev.length() -6).trim();
//					}else if(tdev.endsWith("emulator")){// not known to be used
//						device = tdev.substring(0, tdev.length() -8).trim();
//					}else{
//						String msg = "Unknown Device Listing Format: "+ tdev;
//						debug(msg);
//						return false;							
//					}
//					havedevice = true;
//					DUtilities.USE_DEVICE_SERIAL = " -s "+ device +" ";						
//				}
//			}else{
//				// if one device, we don't need to specify -s DEVICE_SERIAL
//				// DUtilities.USE_DEVICE_SERIAL should already be empty ("");
//				havedevice = true;
//			}
//
//		}catch(Exception x){
//			debug("Aborting due to "+x.getClass().getSimpleName()+", "+ x.getMessage());
//			return false;
//		}			
//
//		return havedevice;
//	}
//
//	/**
//	 * Initialize the Solo object and launch the main application.<br>
//	 * You will not modify this method in the sub-class, normally<br>
//	 * 
//	 * @return true if the initialization is successful.
//	 */
//	final protected boolean initialize(){
//		boolean success = false;
//
//		try {
//			solo.initialize();
//
//			//We can enable/disable the debug message of Protocol or Runner
//			solo.turnProtocolDebug(enableProtocolDebug);
//			solo.turnRunnerDebug(enableRunnerDebug);
//
//			//Start the main Activity
//			success = solo.startMainLauncher();
//			//Set the mainActivityUID
//			mainActivityUID = solo.getCurrentActivity();
//			debug("mainActivityUID="+mainActivityUID);
//
//		} catch (IllegalThreadStateException e) {
//			e.printStackTrace();
//		} catch (RemoteException e) {
//			e.printStackTrace();
//		} catch (TimeoutException e) {
//			e.printStackTrace();
//		} catch (ShutdownInvocationException e) {
//			e.printStackTrace();
//		}
//
//		if(success){
//			debug("Launch application successfully.");
//		}else{
//			error("Fail to launch application.");
//		}
//		return success; 
//	}
//
//	/**
//	 * Terminate the remote engine and Solo object.<br>
//	 * You will not modify this method in the sub-class, normally<br>
//	 * 
//	 * @return true if the initialization is successful.
//	 */
//	final protected boolean terminate(){
//		boolean success = false;
//		try {			
//			solo.shutdown();
//			if(!stopEmulator()){
//				warn("We fail to stop the emulator launched by us.");
//			}
//			success = true;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		if(success){
//			debug("terminate successfully.");
//		}else{
//			warn("Fail to terminate.");
//		}
//		return success;
//	}
//	
//	public void setupSDKHome() {
//		info("Setting SDK_HOME as: " + DEFAULT_ANDROID_SDK);
//		System.setProperty("android-home", DEFAULT_ANDROID_SDK); 
//	}
//
//	/**
//	 * Stop the emulator launched by us only if we have launched it and<br>
//	 * we don't want to persist it.<br>
//	 * 
//	 * @return boolean, true if the emulator is stopped successfully or we don't need to stop it.
//	 */
//	final protected boolean stopEmulator(){
//		boolean stopped = true;
//
//		if(weLaunchedEmulator){
//			//Then we will shutdown any emulator lauched by us.
//			if(!persistEmulators) {
//				info(" checking for launched emulators...");
//				stopped = DUtilities.shutdownLaunchedEmulators(weLaunchedEmulator);		  	
//			}else{
//				info(" attempting to PERSIST any launched emulators...");
//			}
//		}else{
//			info("we didn't start any emulators.");
//		}
//
//		return stopped;
//	}
//
//	private void error(String message) {
//		System.out.println("[Android Test ERROR]: " + message);
//	}
//
//	private void warn(String message) {
//		System.out.println("[Android Test WARN]: " + message);
//	}
//
//	private void debug(String message) {
//		System.out.println("[Android Test DEBUG]: " + message);
//	}
//
//	private void info(String message) {
//		System.out.println("[Android Test INFO]: " + message);
//	}
}
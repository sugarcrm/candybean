package com.sugarcrm.voodoo.automation;

import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.swing.JOptionPane;

import junit.framework.Assert;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;

import com.sugarcrm.voodoo.automation.control.VControl;
import com.sugarcrm.voodoo.automation.control.VHook;
import com.sugarcrm.voodoo.automation.control.VHook.Strategy;
import com.sugarcrm.voodoo.automation.control.VSelect;
import com.sugarcrm.voodoo.utilities.Utils;

public class VInterfaceSystemTest {

	protected static Voodoo voodoo;
	protected static VInterface iface;

	@BeforeClass
	public static void first() throws Exception {
		String curWorkDir = System.getProperty("user.dir");
		String relPropsPath = curWorkDir + File.separator + "src"
				+ File.separator + "test" + File.separator + "resources";
		String voodooPropsPath = relPropsPath + File.separator;
		String voodooPropsFilename = System.getProperty("voodoo_prop_filename");
		if (voodooPropsFilename == null)
			voodooPropsFilename = "voodoo-mac.properties";
		voodooPropsPath += voodooPropsFilename;
		Properties voodooProps = new Properties();
		voodooProps.load(new FileInputStream(new File(voodooPropsPath)));
		voodoo = Voodoo.getInstance(voodooProps);
		iface = voodoo.getInterface();
		iface.start();
	}
	
	@Ignore
	@Test
	public void pauseTest() throws Exception {
//		this.iface.pause(ms);
	}

	@Ignore
	@Test
	public void interactTest() {
//		this.iface.interact("");
	}

	@Ignore
	@Test
	public void stopTest() throws Exception {
//		this.iface.stop();
	}

	@Ignore
	@Test
	public void closeWindowTest() throws Exception {
//		this.iface.closeWindow();
	}

	@Ignore
	@Test
	public void goTest() throws Exception {
//		this.iface.go("");
	}

	@Ignore
	@Test
	public void acceptDialogTest() throws Exception {
//		this.iface.acceptDialog();
	}

	@Ignore
	@Test
	public void dismissDialogTest() throws Exception {
//		this.iface.dismissDialog();
	}

	@Ignore
	@Test
	public void focusDefaultTest() throws Exception {
//		this.iface.focusDefault();
	}

	@Test
	public void focusFrameTest() throws Exception {
		String expDefStr = "Your Guide To Web Design";
		String expFrmStr = "http://www.littlewebhut.com/images/eightball.gif";
		iface.go("http://www.littlewebhut.com/articles/html_iframe_example/");
		String actDefStr = iface.getControl(Strategy.TAG, "p").getText();
		Assert.assertEquals("Expecting: " + expDefStr + ", actual: " + actDefStr, expDefStr, actDefStr);
		iface.focusFrame(1);
//		System.out.println("SOURCE:\n" + iface.wd.getPageSource());
		String actFrmStr = iface.getControl(Strategy.TAG, "img").getAttribute("src");
		Assert.assertEquals("Expecting: " + expFrmStr + ", actual: " + actFrmStr, expFrmStr, actFrmStr);
		iface.focusDefault();
		actDefStr = iface.getControl(Strategy.TAG, "p").getText();
		Assert.assertEquals("Expecting: " + expDefStr + ", actual: " + actDefStr, expDefStr, actDefStr);
		iface.focusFrame("imgbox");
		actFrmStr = iface.getControl(Strategy.TAG, "img").getAttribute("src");
		Assert.assertEquals("Expecting: " + expFrmStr + ", actual: " + actFrmStr, expFrmStr, actFrmStr);
		iface.focusDefault();
		actDefStr = iface.getControl(Strategy.TAG, "p").getText();
		Assert.assertEquals("Expecting: " + expDefStr + ", actual: " + actDefStr, expDefStr, actDefStr);
		iface.focusFrame(new VControl(voodoo, iface, Strategy.ID, "imgbox"));
		actFrmStr = iface.getControl(Strategy.TAG, "img").getAttribute("src");
		Assert.assertEquals("Expecting: " + expFrmStr + ", actual: " + actFrmStr, expFrmStr, actFrmStr);
		iface.focusDefault();
		actDefStr = iface.getControl(Strategy.TAG, "p").getText();
		Assert.assertEquals("Expecting: " + expDefStr + ", actual: " + actDefStr, expDefStr, actDefStr);
	}

	@Ignore
	@Test
	public void focusWindowTest() throws Exception {
//		this.iface.focusWindow(index);
//		this.iface.focusWindow("");
	}

	@Ignore
	@Test
	public void maximizeTest() {
//		this.iface.maximize();
	}

	@Ignore
	@Test
	public void getControlTest() throws Exception {
//		this.iface.getControl(null);
	}

	@Ignore
	@Test
	public void getSelectTest() throws Exception {
//		this.iface.getSelect(null);
	}

	@AfterClass
	public static void last() throws Exception {
		iface.stop();
	}
}

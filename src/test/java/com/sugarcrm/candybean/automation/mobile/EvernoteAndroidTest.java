package com.sugarcrm.candybean.automation.mobile;

import static org.junit.Assert.*;

import java.io.File;
import java.net.URL;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.HasTouchScreen;
import org.openqa.selenium.interactions.TouchScreen;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteTouchScreen;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteWebElement;

import com.sugarcrm.candybean.examples.AbstractTest;

public class EvernoteAndroidTest extends AbstractTest{

	private SwipeableWebDriver driver;

	@Before
	public void setUp() throws Exception {
		// set up appium
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability(CapabilityType.BROWSER_NAME, "Android");
		capabilities.setCapability(CapabilityType.VERSION, "4.4.2");
		capabilities.setCapability("device", "Android");
		// Set path to evernote APK if using an emulator, instead of a physical device
		capabilities.setCapability("app", new File("lib/evernote.apk").getAbsolutePath());
		capabilities.setCapability("app-package", "com.evernote");
		capabilities.setCapability("app-activity", ".ui.HomeActivity");
		driver = new SwipeableWebDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
		// Login to the application before conducting the tests
		iface.pause(3000);
		login();
		closeWelcomeOverlay();
	}

	


	@After
	public void tearDown() throws Exception {
		driver.quit();
	}
	

	@Test
	public void openNotes() throws Exception {
		openUsersMenu();
		WebElement notesAction = driver.findElements(
				By.id("com.evernote:id/home_list_text")).get(0);
		notesAction.click();
		iface.pause(3000);
	}
	
	

	@Test
	public void newNote() throws Exception {
		openUsersMenu();
		WebElement newNoteButton = driver.findElement(By
				.id("com.evernote:id/btn_new_note"));
		newNoteButton.click();
		iface.pause(2000);
		WebElement noteTitleField = driver.findElement(By
				.id("com.evernote:id/note_title"));
		noteTitleField.click();
		iface.pause(1000);
		noteTitleField.sendKeys(Calendar.getInstance().getTime().toString());
		WebElement noteField = driver
				.findElement(By.id("com.evernote:id/text"));
		noteField.click();
		iface.pause(1000);
		noteField
				.sendKeys("Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor "
						+ "incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud "
						+ "exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute "
						+ " dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. "
						+ "Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit "
						+ "anim id est laborum");
		driver.findElement(By.className("android.widget.ImageButton")).click();
		iface.pause(2000);
	}


	
	
	@Test
	public void deleteAllNotes() throws Exception {
		openNotes();
		List<WebElement> notes = driver.findElements(By
				.id("com.evernote:id/title"));
		while (notes.size() != 0) {
			WebElement note = notes.get(0);
			HashMap<String, String> values = new HashMap<String, String>();
			values.put("element", ((RemoteWebElement) note).getId());
			driver.executeScript("mobile: longClick", values);
			iface.pause(1000);
			WebElement footer = driver
					.findElementById("com.evernote:id/efab_menu_footer");
			List<WebElement> footerItems = footer.findElements(By
					.className("android.widget.ImageButton"));
			WebElement moreOptions = footerItems.get(footerItems.size() - 1);
			moreOptions.click();
			iface.pause(1000);
			WebElement deleteButton = driver.findElements(
					By.id("com.evernote:id/item_title")).get(5);
			deleteButton.click();
			iface.pause(1000);
			WebElement deleteConfirmation = driver.findElement(By
					.id("android:id/button1"));
			deleteConfirmation.click();
			iface.pause(1000);
			notes = driver.findElements(By.id("com.evernote:id/title"));
		}
		assertEquals(driver.findElements(By.id("com.evernote:id/title")).size(), 0);
	}


	
	@Test
	public void openNotebookFromShortcut() throws Exception {
		openShortcutsMenu();
		WebElement shortcut = driver.findElement(By
				.id("com.evernote:id/shortcut_name"));
		shortcut.click();
		iface.pause(1000);
	}


	

	@Test
	public void signOut() throws Exception {
		openUsersMenu();
		openUserMenu();
		WebElement signoutButton = driver.findElements(By.id("com.evernote:id/item_title")).get(1);
		signoutButton.click();
		iface.pause(1000);
		closeConfirmation(true);
		iface.pause(4000);
	}
	

	
	private boolean isLoggedIn() {
		try {
			openUsersMenu();
			WebElement userDropDown = driver.findElement(By.id("com.evernote:id/signed_in_user_area"));
			return userDropDown.isDisplayed();
		} catch (Exception e) {
			return false;
		}
	}

	
	private boolean overlayExists(){
		try {
			WebElement welcomeOverlay = driver.findElement(By.id("com.evernote:id/fd_layout"));
			return welcomeOverlay.isDisplayed();
		} catch (Exception e) {
			return false;
		}
	}
	
	private boolean isLeftMenuOpen(){
		try {
			WebElement header = driver.findElement(By.id("com.evernote:id/efab_menu_hdr"));
			List<WebElement> children = header.findElements(By.xpath("android.widget.LinearLayout"));
			return children.size() == 2;
		} catch (Exception e) {
			return false;
		}
	}
	
	

	public void closeWelcomeOverlay() throws Exception {
		if(overlayExists()){
			try {
				WebElement welcomeOverlay = driver.findElement(By.id("com.evernote:id/fd_layout"));
				WebElement closeOverlayButton = welcomeOverlay.findElement(By.className("android.widget.ImageView"));
				closeOverlayButton.click();
				iface.pause(1000);
			} catch (NoSuchElementException e) {
				return;
			}
		}
	}

	public void login() throws Exception {
		if(isLoggedIn())
			return;
		for(int i = 0; i < 2; i++){
			openRightSideMenu();
			iface.pause(1000);
		}
		WebElement emailTextField = driver.findElement(By
				.id("com.evernote:id/landing_username"));
		emailTextField.clear();
		emailTextField.sendKeys("sfarooq@sugarcrm.com");
		WebElement passwordField = driver.findElement(By
				.id("com.evernote:id/landing_password"));
		passwordField.click();
		passwordField.sendKeys("candybeantest");
		WebElement logInButton = driver.findElement(By
				.id("com.evernote:id/landing_sign_in_button"));
		logInButton.click();
		iface.pause(5000);
	}

	public void openUserMenu() throws Exception {
		WebElement userDropDown = driver.findElement(By
				.id("com.evernote:id/signed_in_user_area"));
		userDropDown.click();
		iface.pause(1000);
	}
	

	public void closeConfirmation(boolean option) throws Exception {
		WebElement confirmation = driver.findElement(By
				.id(option ? "android:id/button1" : "android:id/button2"));
		confirmation.click();
		iface.pause(1000);
	}


	public String getFooterTitleText() {
		WebElement startingElement = driver.findElement(By
				.id("com.evernote:id/footer_title"));
		return startingElement.getText();
	}

	public void swipe(double startX, double startY, double endX, double endY,
			double duration) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		HashMap<String, Double> swipeObject = new HashMap<String, Double>();
		swipeObject.put("startX", startX);
		swipeObject.put("startY", startY);
		swipeObject.put("endX", endX);
		swipeObject.put("endY", endY);
		swipeObject.put("duration", duration);
		js.executeScript("mobile: swipe", swipeObject);
	}
	

	
	public void openShortcutsMenu() throws Exception {
		openRightSideMenu();
		openRightSideMenu();
		iface.pause(3000);
	}

	public void openRightSideMenu() throws Exception {
		try {
			swipe(0.9, 0.5, 0.1, 0.5, 0.2);
			iface.pause(1000);
		} catch (WebDriverException e) {
			return;
		}
	}


	public void openUsersMenu() throws Exception {
		try {
			if(isLeftMenuOpen())
				return;
			swipe(0.1, 0.5, 0.9, 0.5, 0.2);
			iface.pause(1000);
		} catch (WebDriverException e) {
			return;
		}
	}

	public class SwipeableWebDriver extends RemoteWebDriver implements
			HasTouchScreen {
		private RemoteTouchScreen touch;

		public SwipeableWebDriver(URL remoteAddress,
				Capabilities desiredCapabilities) {
			super(remoteAddress, desiredCapabilities);
			touch = new RemoteTouchScreen(getExecuteMethod());
		}

		public TouchScreen getTouch() {
			return touch;
		}
	}

}


package com.sugarcrm.voodoo;


public class TestVoodoo {

	public void testStart() throws Exception {
//		this.log.info("Starting voodoo with url: " + url);
//		this.vAutomation.start(url);
	}

	public void testStop() throws Exception {
//		this.log.info("Stopping voodoo.");
//		this.vAutomation.stop();
	}

	public void testAcceptDialog() throws Exception {
//		this.vAutomation.acceptDialog();
	}

	public void testSwitchToPopup() throws Exception {
//		this.vAutomation.switchToPopup();
	}

	public void testPause() throws Exception {
//		Thread.sleep(ms);
	}
	
	public void testInteract() throws Exception {
//		JOptionPane.showInputDialog(s);
	}

	public void testGetText() throws Exception {
//		return this.vAutomation.getText(strategy, hook);
	}

	public void testHover() throws Exception {
//		vAutomation.hover(strategy, hook);
	}

	public void testGetControl() throws Exception {
//		return vAutomation.getControl(strategy, hook);
	}

	public void testClick() throws Exception {
//		vAutomation.click(strategy, hook);
	}

	public void testInput() throws Exception {
//		
	}

	public void testGetAutomation() throws Exception {
//		VAutomation vAutomation = null;
//		Voodoo.BrowserType browserType = this.getBrowserType();
//		String vAutomationString = Utils.getCascadingPropertyValue(this.props, "selenium", "AUTOMATION.FRAMEWORK");
//		switch (vAutomationString) {
//		case "selenium":
//			this.log.info("Instantiating Selenium automation with browserType: " + browserType.name());
//			vAutomation = new TestSeleniumAutomation(this, props, browserType);
//			break;
//		case "robotium":
//			throw new Exception("Robotium automation not yet supported.");
//		default:
//			throw new Exception("Automation framework not recognized.");
//		}
//		return vAutomation;
    }

	public void testGetBrowserType() throws Exception {
//		Voodoo.BrowserType browserType = null;
//		String browserTypeString = Utils.getCascadingPropertyValue(this.props, "chrome", "AUTOMATION.BROWSER");
//		for (Voodoo.BrowserType browserTypeIter : Voodoo.BrowserType.values()) {
//			if (browserTypeIter.name().equalsIgnoreCase(browserTypeString)) {
//				browserType = browserTypeIter;
//				break;
//			}
//		}
//		return browserType;
	}


	public void testGetLogger() throws Exception {
//		Logger logger = Logger.getLogger(Voodoo.class.getName());
//		FileHandler fh = new FileHandler(this.getLogPath());
//		fh.setFormatter(new SimpleFormatter());
//		logger.addHandler(fh);
//		logger.setLevel(this.getLogLevel());
//		return logger;

//		Logger logger = LoggerFactory.getLogger(Voodoo.class.getName());
//      logger.info("Trying out sl4j and logback");
//      logger.info("Using {}", "parameterized logging");
//		return logger;
	}
	
	public void testGetLogPath() {
//		return props.getString("SYSTEM.LOG_PATH");
	}

	public void testGetLogLevel() {
//		switch(props.getString("SYSTEM.LOG_LEVEL")) {
//		case "SEVERE": return Level.SEVERE;
//		case "WARNING": return Level.WARNING;
//		case "INFO": return Level.INFO;
//		case "FINE": return Level.FINE;
//		case "FINER": return Level.FINER;
//		case "FINEST": return Level.FINEST;
//		default:
//			log.warning("Configured SYSTEM.LOG_LEVEL not recognized; defaulting to Level.INFO");
//			return Level.INFO;
//		}
	}
}

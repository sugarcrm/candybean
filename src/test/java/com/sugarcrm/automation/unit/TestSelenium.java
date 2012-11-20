package com.sugarcrm.automation.unit;


public class TestSelenium {
	
	public void testStart() throws Exception {
//		browser.get(url);
	}

	public void testStop() throws Exception {
//		browser.close();
	}
	
	public void testText() throws Exception {
//		WebElement we = ((VSeleniumControl) this.getControl(strategy, hook)).webElement;
//		return we.getText();
	}
	
	public void testHover() throws Exception {
//		WebElement we = ((VSeleniumControl) this.getControl(strategy, hook)).webElement;
//		Actions action = new Actions(browser);
//		action.moveToElement(we).perform();
	}
	
	public void testGetControl() throws Exception {
//		WebElement webElement = null;
//		switch (strategy) {
//		case CSS:
//			webElement = this.browser.findElement(By.cssSelector(hook));
//			break;
//		case XPATH:
//			webElement = this.browser.findElement(By.xpath(hook));
//			break;
//		case ID:
//			webElement = this.browser.findElement(By.id(hook));
//			break;
//		case NAME:
//			webElement = this.browser.findElement(By.name(hook));
//			break;
//		}
//		return new VSeleniumControl(webElement);
	}

	public void testClick() throws Exception {
//		this.click(this.getControl(strategy, hook));
	}
	
	public void testInput() throws Exception {
//		this.input(this.getControl(strategy, hook), input);
	}
	
	public void testAcceptDialog() throws Exception {
//		Alert alert = browser.switchTo().alert();
//		alert.accept();
	}
	
	public void testSwitchToPopup() throws Exception {
//		String currentWindowHandle = browser.getWindowHandle();
//		Set<String> windowHandles = browser.getWindowHandles();
//		windowHandles.remove(currentWindowHandle);
//		if (windowHandles.size() > 1) throw new Exception("Selenium: more than one popup/window detected");
//		else browser.switchTo().window(windowHandles.iterator().next());
	}
	
	public void testMaximizeBrowserWindow() {
//		java.awt.Dimension screenSize = Toolkit.getDefaultToolkit()
//				.getScreenSize();
//		browser.manage().window()
//				.setSize(new Dimension(screenSize.width, screenSize.height));
	}
	
	public void testWebElementToString() {
//		List<WebElement> childElements = element.findElements(By.xpath("*"));
//		String s = element.getTagName() + ":" + element.getText() + " ";
//		for(WebElement we : childElements) {
//			s += we.getTagName() + ":" + we.getText() + " ";
//		}
//		return s;
	}
	
	public void testOptionValuesEqual() {
//		Set<String> nativeOptionNames = new HashSet<String>();
//		for (WebElement option : nativeOptions) {
//			nativeOptionNames.add(option.getText());
//		}
//		if (nativeOptionNames.containsAll(queryOptionNames) && queryOptionNames.containsAll(nativeOptionNames)) return true;
//		else return false;
//		
	}
	
	public void testAllOptionsAction() {
//		List<WebElement> options = selectElement.getOptions(); 
//		for(WebElement option : options) {
//			selectElement.selectByVisibleText(option.getText());
//			actionElement.click();
//		}
	}
	
	public void testOptionAction() throws Exception {
//		List<WebElement> allOptions = selectElement.getOptions();
//		HashSet<String> optionValues = new HashSet<String>();
//		for(WebElement option : allOptions) {
//			optionValues.add(option.getText());
////			System.out.println("Adding to options set:" + option.getText());
//		}
//		if(optionValues.containsAll(actionOptionValues)) {
//			for(String option : actionOptionValues) {
//				selectElement.selectByVisibleText(option);
//				actionElement.click();
//			}
//		} else throw new Exception("Specified select option unavailable...");
	}
	
	public void testTableContainsValue() {
//		List<WebElement> rows = tableElement.findElements(By.tagName("tr"));
//		for (WebElement row : rows) {
//			if (row.findElement(By.xpath(rowRelativeXPathTextKey)).getText().equalsIgnoreCase(value)) return true;
//		}
//		return false;
	}
	
	public void testLoadMapFromTable() throws Exception {
//		Map<String, WebElement>	rowMap = new HashMap<String, WebElement>();
//		List<WebElement> rows = table.findElements(By.tagName("tr"));
////		System.out.println("table # rows:" + rows.size());
//		for (WebElement row : rows) {
////			List<WebElement> childTDs = row.findElements(By.tagName("td"));
////			for (WebElement childTD : childTDs) System.out.println("td text:" + childTD.getText());
//			String k = row.findElement(By.xpath(rowRelativeXPathTextKey)).getText();
//			WebElement v = row.findElement(By.xpath(rowRelativeXPathElementValue));
////			System.out.println("key text:" + k + ", value we:" + v.getTagName() + "/" + v.getText());
//			rowMap.put(k, v);
//		}
//		return rowMap;
	}
	
	public void testExplicitWait() throws Exception {
//		(new WebDriverWait(browser, timeout)).until(new ExpectedCondition<WebElement>(){
//			public WebElement apply(WebDriver wd) {
//				return wd.findElement(by);
//			}});
	}
}

package com.sugarcrm.candybean.examples.wolfram;

import java.util.List;

import com.sugarcrm.candybean.automation.Candybean;
import com.sugarcrm.candybean.automation.element.Hook.Strategy;
import com.sugarcrm.candybean.automation.webdriver.WebDriverElement;
import com.sugarcrm.candybean.automation.webdriver.WebDriverInterface;

/**
 * Contains specific helper methods to conduct wolfram alpha tests.
 * This class directly utilizes with the {@link Candybean} and {@link VInterface} objects.
 * 
 * @author Shehryar Farooq
 *
 */
public class WolframAlpha {
	
	/**
	 * The URL for the result page of any wolfram query
	 */
	private static final String WOLFRAM_RESULT_PAGE = "http://www.wolframalpha.com/input/";

	/**
	 * The id of the search button element on the wolfram landing page.
	 */
	private static final String WOLFRAM_SEARCH_BTN_ID = "equal";

	/**
	 * The name of the main search box element on the wolfram landing page.
	 */
	private static final String WOLFRAM_SEARCHBAR_NAME = "i";

	/**
	 * The wolfram alpha landing page url
	 */
	private static final String WOLFRAM_URL = "http://www.wolframalpha.com/";
	
	/**
	 * The interface instance used to build automation tasks.
	 */
	private WebDriverInterface iface;

	/**
	 * Default field constructor
	 * @param iface
	 */
	public WolframAlpha(WebDriverInterface iface) {
		this.iface = iface;
	}

	/**
	 * Navigates to the landing page of wolfram alpha
	 * @throws Exception
	 */
	public void goToLandingPage() throws Exception {
		if(!iface.getURL().startsWith(WOLFRAM_RESULT_PAGE))
			iface.go(WOLFRAM_URL);
	}
	
	/**
	 * Performs an operation query on wolfram alpha
	 * @param input The input to enter into wolfram alpha landing page
	 * @throws Exception
	 */
	public void askWolfram(String input) throws Exception{
		WebDriverElement searchBar = iface.getWebDriverElement(Strategy.ID, WOLFRAM_SEARCHBAR_NAME);
		searchBar.sendString(input);
		iface.getWebDriverElement(Strategy.ID, WOLFRAM_SEARCH_BTN_ID).click();
	}
	
	/**
	 * Verifies the answer produced by the wolfram operation.
	 * @param expectedAnswer The expected answer
	 * @return Whether the expected answer was found in the resulting page.
	 * @throws Exception 
	 */
	public boolean verifyAnswer(String expectedAnswer) throws Exception{
		List<WebDriverElement> elements = iface.getWebDriverElements(Strategy.XPATH, "//section[starts-with(@id, 'pod_')]");
		boolean resultFound = false;
		for(WebDriverElement element : elements){
			if(resultFound) break;
			element.hover();
			String xpath = "//section[@id='" + element.getAttribute("id")+"']//a[@class='subpod-copyablept ']";
			WebDriverElement plainTextButton = iface.getWebDriverElement(Strategy.XPATH,xpath );
			plainTextButton.click();
			List<WebDriverElement> answers = iface.getWebDriverElements(Strategy.XPATH, "//div[@id='mov_sub" + element.getAttribute("id")+"_1_popup_dyn']//pre");
			for(WebDriverElement answer : answers){
				answer.click();
				if(answer.getText().equals(expectedAnswer)){
					resultFound = true;
					break;
				}
			}
			WebDriverElement closeButton = iface.getWebDriverElement(Strategy.XPATH, "//div[@id='mov_sub" + element.getAttribute("id")+"_1_popup_dyn']//a[@class='close']");
			closeButton.click();
		}
		return resultFound;
	}

	/**
	 * Waits for a certain amount of time
	 * @param seconds the specified time to wait.
	 * @throws Exception 
	 */
	void waitForResult(long seconds) throws Exception {
		iface.pause(seconds);
	}

}
package com.sugarcrm.candybean.examples.wolfram;

import java.util.List;

import com.sugarcrm.candybean.automation.Candybean;
import com.sugarcrm.candybean.automation.VInterface;
import com.sugarcrm.candybean.automation.control.VControl;
import com.sugarcrm.candybean.automation.control.VHook.Strategy;

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
	 * The VInterface instance used to build automation tasks.
	 */
	private VInterface iface;

	/**
	 * Default field constructor
	 * @param iface
	 */
	public WolframAlpha(VInterface iface) {
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
		VControl searchBar = iface.widget(Strategy.ID, WOLFRAM_SEARCHBAR_NAME);
		searchBar.sendString(input);
		iface.widget(Strategy.ID, WOLFRAM_SEARCH_BTN_ID).click();
	}
	
	/**
	 * Verifies the answer produced by the wolfram operation.
	 * @param expectedAnswer The expected answer
	 * @return Whether the expected answer was found in the resulting page.
	 * @throws Exception 
	 */
	public boolean verifyAnswer(String expectedAnswer) throws Exception{
		List<VControl> controls = iface.getControls(Strategy.XPATH, "//section[starts-with(@id, 'pod_')]");
		boolean resultFound = false;
		for(VControl control: controls){
			if(resultFound) break;
			control.hover();
			String xpath = "//section[@id='"+control.getAttribute("id")+"']//a[@class='subpod-copyablept ']";
			VControl plainTextButton = iface.getControl(Strategy.XPATH,xpath );
			plainTextButton.click();
			List<VControl> answers = iface.getControls(Strategy.XPATH, "//div[@id='mov_sub"+control.getAttribute("id")+"_1_popup_dyn']//pre");
			for(VControl answer : answers){
				answer.click();
				if(answer.getText().equals(expectedAnswer)){
					resultFound = true;
					break;
				}
			}
			VControl closeButton = iface.getControl(Strategy.XPATH, "//div[@id='mov_sub"+control.getAttribute("id")+"_1_popup_dyn']//a[@class='close']");
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
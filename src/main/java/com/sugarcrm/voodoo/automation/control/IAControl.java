package com.sugarcrm.voodoo.automation.control;

public interface IAControl {
	String getCurrentActivity() throws Exception;
	String getButton(int index) throws Exception;
	String getButton(String text) throws Exception;
	void clickOnText(String text) throws Exception;
	boolean searchText(String text) throws Exception;
	void clickOnButton(String text) throws Exception;
	void enterText(int index, String text) throws Exception;
}

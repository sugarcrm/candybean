package com.sugarcrm.candybean.model;

import java.util.Set;

public interface IModel {
	
	Set<Page> getStartPages();
	void executeRandomStartPage();
	void run(int timeoutInMinutes);
}

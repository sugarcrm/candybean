package com.sugarcrm.candybean.model;

import java.util.Set;

public interface IModel {
	
	public Set<Page> getStartPages();
	public void executeRandomStartPage();
	public void run(int timeout_in_minutes);
}

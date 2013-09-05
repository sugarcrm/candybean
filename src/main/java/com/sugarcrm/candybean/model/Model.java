package com.sugarcrm.candybean.model;

import java.util.List;

public class Model {
	
	public List<Page> startPages;
	
	public Model(Page startPage) {
		this.startPages.add(startPage);
	}
	
	public void addStartPage(Page startPage) {
		this.startPages.add(startPage);
	}
}

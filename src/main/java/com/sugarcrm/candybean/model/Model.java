package com.sugarcrm.candybean.model;

import java.util.List;

public class Model {
	
	private List<Page> startPages;
	
	public Model(Page startPage) {
		this.getStartPages().add(startPage);
	}
	
	public void addStartPage(Page startPage) {
		this.getStartPages().add(startPage);
	}

	public List<Page> getStartPages() {
		return startPages;
	}

	public void setStartPages(List<Page> startPages) {
		this.startPages = startPages;
	}
}

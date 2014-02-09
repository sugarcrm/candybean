package com.sugarcrm.candybean.model;

//import java.util.List;

public class Link {
	
	private Page fromPage;
	private Page toPage;
//	public List<LinkValue> requiredActions;
	public Page getFromPage() {
		return fromPage;
	}
	public void setFromPage(Page fromPage) {
		this.fromPage = fromPage;
	}
	public Page getToPage() {
		return toPage;
	}
	public void setToPage(Page toPage) {
		this.toPage = toPage;
	}

}

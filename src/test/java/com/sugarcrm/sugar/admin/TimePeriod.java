package com.sugarcrm.sugar.admin;


public class TimePeriod {
	
	public String name = null;
	public boolean isFiscalYear = false;
	public String startDate = null;
	public String endDate = null;
		
	public TimePeriod(String name, boolean isFiscalYear, String startDate, String endDate) { 
		this.name = name;
		this.isFiscalYear = isFiscalYear;
		this.startDate = startDate;
		this.endDate = endDate;
	}
	
	public String toString() {
		String s = "TimePeriod(";
		s += "name:" + name + ",isFiscalYear:" + isFiscalYear + ",startDate:" + startDate + ",endDate:" + endDate;
		return s += ")";
	}
}

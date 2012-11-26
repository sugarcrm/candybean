package com.sugarcrm.sugar.cases;

import com.sugarcrm.sugar.accounts.Account;


public class Case {
	
	private CaseBuilder builder;
	private Case(CaseBuilder builder) { this.builder = builder; }
	
	public String subject() { return builder.subject; }
	public Account team() {return builder.team; }
	
	
	@Override
	public String toString() {
		String s = "Case(subject:" + subject() + ",team:" + team().toString() + ")";
		return s;
	}

	public static class CaseBuilder {
		
		private String subject = null;
		private Account team = null;
		
		public CaseBuilder(String subject, Account team) { this.subject = subject; this.team = team; }
		
		public Case build() { return new Case(this); }
	}
}

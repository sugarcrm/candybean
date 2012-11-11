package com.sugarcrm.sugar.cases;


public class Case {
	
	private CaseBuilder builder;
	private Case(CaseBuilder builder) { this.builder = builder; }
	
	public String subject() { return builder.subject; }
	
	
	@Override
	public String toString() {
		String s = "Case(subject:" + subject() + ")";
		return s;
	}

	public static class CaseBuilder {
		
		private String subject = null;
		
		public CaseBuilder(String subject) { this.subject = subject; }
		
		public Case build() { return new Case(this); }
	}
}

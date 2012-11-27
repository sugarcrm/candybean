package com.sugarcrm.sugar.cases;

import com.sugarcrm.sugar.teams.Team;


public class Case {
	
	private CaseBuilder builder;
	private Case(CaseBuilder builder) { this.builder = builder; }
	
	public String subject() { return builder.subject; }
	public Team team() {return builder.team; }
	
	
	@Override
	public String toString() {
		String s = "Case(subject:" + subject() + ",team:" + team().toString() + ")";
		return s;
	}

	public static class CaseBuilder {
		
		private String subject = null;
		private Team team = null;
		
		public CaseBuilder(String subject, Team team) { this.subject = subject; this.team = team; }
		
		public Case build() { return new Case(this); }
	}
}

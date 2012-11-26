package com.sugarcrm.sugar.teams;


public class Team {
	
	private TeamBuilder builder;
	private Team(TeamBuilder builder) { this.builder = builder; }
	
	public String name() { return builder.name; }
	
	
	@Override
	public String toString() {
		String s = "Team(name:" + name() + ")";
		return s;
	}

	public static class TeamBuilder {
		
		private String name = null;
		
		public TeamBuilder(String name) { this.name = name; }
		
		public Team build() { return new Team(this); }
	}
}

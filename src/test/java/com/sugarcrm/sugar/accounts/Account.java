package com.sugarcrm.sugar.accounts;

public class Account {
	
	private AccountBuilder builder;
	private Account(AccountBuilder builder) { this.builder = builder; }
	
	public String name() { return builder.name; }
	
	@Override
	public String toString() {
		String s = "Account(name:" + name() + ")";
		return s;
	}

	public static class AccountBuilder {
		
		private String name = null;
		
		public AccountBuilder(String name) { this.name = name; }
		
		public Account build() { return new Account(this); }
	}
}

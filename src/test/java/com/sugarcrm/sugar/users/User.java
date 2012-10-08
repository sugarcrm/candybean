package com.sugarcrm.sugar.users;

public class User {
	
	private UserBuilder builder;
	private User(UserBuilder builder) { this.builder = builder; }
	
	public String username() { return builder.username; }
	public String name() { return builder.name; }
	public String password1() { return builder.password1; }
	public String password2() { return builder.password2; }
	public PortalUser buildPortalUser() { 
		return new PortalUser(builder); 
	}
	
	@Override
	public String toString() {
		String s = "User(" + username() + "," + name() + "," + password1()
				+ "," + password2() + ")";
		return s;
	}

	public static class UserBuilder {
		
		private String username = null;
		private String name = null;
		private String password1 = null;
		private String password2 = null;
		
		public UserBuilder(String username, String name, String password1, String password2) { 
			this.username = username;
			this.name = name;
			this.password1 = password1;
			this.password2 = password2;
		}
		
		public User build() { return new User(this); }
	}
	
	public static class PortalUser extends User {
		private PortalUser(UserBuilder builder) {
			super(builder);
		}
		
		@Override
		public String toString() {
			return super.toString();
		}
	}
}

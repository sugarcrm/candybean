package com.sugarcrm.sugar.modules;

import com.sugarcrm.sugar.Sugar;

/**
 * @author Conrad Warmbold
 *
 */
public class UsersModule {
	
	private final Sugar sugar;
	
	public UsersModule(Sugar sugar) { this.sugar = sugar; }
	
	public void createUser() throws Exception {
		throw new Exception("createUser not yet implemented.");
	}
	
	public static class UserRecord {
		public String username = null;
		public String password1 = null;
		public String password2 = null;
		public String name = "";
		
		public UserRecord(String username, String password1, String password2, String name) {
			this.username = username;
			this.password1 = password1;
			this.password2 = password2;
			this.name = name;
		}
	}
}
package com.sugarcrm.sugar.contacts;

import com.sugarcrm.sugar.accounts.Account;

public class Contact {
	
	private ContactBuilder builder;
	private Contact(ContactBuilder builder) { this.builder = builder; }
	
	public String lastName() { return builder.lastName; }
	public Account account() { return builder.account; }
	public String portalName() { return builder.portalName; }
	public boolean portalActive() { return builder.portalActive; }
	public String portalPassword() { return builder.portalPassword; }
	
	@Override
	public String toString() {
		String s = "Contact(lastName:" + lastName() + ",account:"
				+ account().toString() + ",portalName:" + portalName()
				+ ",portalActive:" + portalActive() + ",portalPassword:"
				+ portalPassword() + ")";
		return s;
	}

	public static class ContactBuilder {
		
		private String lastName = null;
		private Account account = null;
		private String portalName = "";
		private boolean portalActive = false;
		private String portalPassword = "";
		
		public ContactBuilder(String lastName) { this.lastName = lastName;	}
		
		public ContactBuilder withAccount(Account account) { this.account = account; return this; }
		public ContactBuilder withPortalName(String portalName) { this.portalName = portalName; return this; }
		public ContactBuilder withPortalActive(boolean portalActive) { this.portalActive = portalActive; return this; }
		public ContactBuilder withPortalPassword(String portalPassword) { this.portalPassword = portalPassword; return this; }
		public Contact build() { return new Contact(this); }
	}
}

package com.sugarcrm.sugar.modules;

import com.sugarcrm.sugar.Sugar;
import com.sugarcrm.voodoo.automation.control.VHook.Strategy;

/**
 * @author Conrad Warmbold
 *
 */
public class ContactsModule {
	
	private final Sugar sugar;
	
	public ContactsModule(Sugar sugar) { this.sugar = sugar; }
	
	public void createContact(ContactRecord contact) throws Exception {
		sugar.i.getControl(Strategy.ID, "moduleTab_AllContacts").hover();
		sugar.i.getControl(Strategy.ID, "CreateContactAll").click();
		sugar.i.getControl(Strategy.ID, "picture_label").hover();
		sugar.i.getControl(Strategy.ID, "last_name").sendString(contact.lastName);
		sugar.i.getControl(Strategy.ID, "SAVE_HEADER").click();
//		sugar.i.getControl(Strategy.ID, "edit_button").halt(4);
//		sugar.i.getControl(Strategy.ID, "moduleTab_AllHome").click();
	}
	
	public void deleteAllContacts() throws Exception {
		sugar.i.getControl(Strategy.ID, "moduleTab_AllContacts").click();
		sugar.i.getControl(Strategy.ID, "massall_top").click();
		sugar.i.getControl(Strategy.ID, "delete_listview_top").click();
		sugar.i.acceptDialog();
		sugar.i.getControl(Strategy.ID, "moduleTab_AllHome").click();
	}
	
	public void editContact(ContactRecord oldContact, ContactRecord newContact) throws Exception {
		this.searchContacts(oldContact.lastName);
		sugar.i.getControl(Strategy.PLINK, oldContact.lastName).click();
		sugar.i.getControl(Strategy.ID, "edit_button").click();
		sugar.i.getControl(Strategy.ID, "last_name").sendString(newContact.lastName);
		sugar.i.getControl(Strategy.ID, "SAVE_HEADER").click();
//		sugar.i.getControl(Strategy.ID, "edit_button").halt(4);
//		sugar.i.getControl(Strategy.ID, "moduleTab_AllHome").click();
	}
	
	public void searchContacts(String search) throws Exception {
		sugar.i.getControl(Strategy.ID, "moduleTab_AllContacts").click();
		sugar.i.getControl(Strategy.ID, "name_basic").sendString(search);
		sugar.i.getControl(Strategy.ID, "search_form_submit").click();
	}
	
	public static class ContactRecord {
		
		public String lastName = null;
		
		public ContactRecord(String lastName) {
			this.lastName = lastName;
		}
	}
}
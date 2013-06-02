package com.sugarcrm.sugar.modules;

import com.sugarcrm.sugar.Sugar;
import com.sugarcrm.voodoo.automation.control.VHook.Strategy;

/**
 * @author Conrad Warmbold
 *
 */
public class NotesModule {
	
	private final Sugar sugar;
	
	public NotesModule(Sugar sugar) { this.sugar = sugar; }
	
	public void createNote(NoteRecord note) throws Exception {
		sugar.i.getControl(Strategy.ID, "moduleTabExtraMenuAll").click();
		sugar.i.getControl(Strategy.ID, "moduleMenuOverFlowMoreAll").click();
		sugar.i.getControl(Strategy.PLINK, "Notes").scroll();
		sugar.i.getControl(Strategy.PLINK, "Notes").hover();
		sugar.i.getControl(Strategy.PLINK, "Create Note").click();
		sugar.i.getControl(Strategy.ID, "parent_name_label").hover();
		sugar.i.getControl(Strategy.ID, "name").sendString(note.subject);
		sugar.i.getControl(Strategy.ID, "SAVE_HEADER").click();
//		sugar.i.getControl(Strategy.ID, "edit_button").halt(4);
//		sugar.i.getControl(Strategy.ID, "moduleTab_AllHome").click();
	}
	
	public void deleteAllNotes() throws Exception {
		sugar.i.getControl(Strategy.ID, "moduleTabExtraMenuAll").click();
		sugar.i.getControl(Strategy.ID, "moduleMenuOverFlowMoreAll").click();
		sugar.i.getControl(Strategy.PLINK, "Notes").click();
		sugar.i.getControl(Strategy.ID, "massall_top").click();
		sugar.i.getControl(Strategy.ID, "delete_listview_top").click();
		sugar.i.acceptDialog();
		sugar.i.getControl(Strategy.ID, "moduleTab_AllHome").click();
	}
	
	public void editNote(NoteRecord oldNote, NoteRecord newNote) throws Exception {
		this.searchNotes(oldNote.subject);
		sugar.i.getControl(Strategy.PLINK, oldNote.subject).click();
		sugar.i.getControl(Strategy.ID, "edit_button").click();
		sugar.i.getControl(Strategy.ID, "name").sendString(newNote.subject);
		sugar.i.getControl(Strategy.ID, "SAVE_HEADER").click();
//		sugar.i.getControl(Strategy.ID, "edit_button").halt(4);
//		sugar.i.getControl(Strategy.ID, "moduleTab_AllHome").click();
	}
	
	public void searchNotes(String search) throws Exception {
		sugar.i.getControl(Strategy.ID, "moduleTabExtraMenuAll").click();
		sugar.i.getControl(Strategy.ID, "moduleMenuOverFlowMoreAll").click();
		sugar.i.getControl(Strategy.PLINK, "Notes").scroll();
		sugar.i.getControl(Strategy.PLINK, "Notes").click();
		sugar.i.getControl(Strategy.ID, "name_basic").sendString(search);
		sugar.i.getControl(Strategy.ID, "search_form_submit").click();
	}
	
	public static class NoteRecord {
		
		public String subject = null;

		public NoteRecord(String subject) {
			this.subject = subject;
		}
	}
}
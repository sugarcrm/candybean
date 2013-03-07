package com.sugarcrm.sugar.modules;

import java.io.File;

import com.sugarcrm.sugar.Sugar;
import com.sugarcrm.voodoo.automation.control.VHook.Strategy;

/**
 * @author Conrad Warmbold
 *
 */
public class DocumentsModule {
	
	private final Sugar sugar;
	
	public DocumentsModule(Sugar sugar) { this.sugar = sugar; }
	
	public void createDocument(DocumentRecord document) throws Exception {
		sugar.i.getControl(Strategy.ID, "moduleTabExtraMenuAll").click();
		sugar.i.getControl(Strategy.PLINK, "Documents").hover();
		sugar.i.getControl(Strategy.PLINK, "Create Document").click();
		sugar.i.getControl(Strategy.ID, "status_id_label").hover();
		throw new Exception("createDocument not yet fully implemented.");
		// Unhide the file name web element --or--...
//		sugar.i.interact("Check if field is visible...");
//		WebElement filenameElement = sugar.i.wd.findElement(By.id("filename"));
//		((JavascriptExecutor)sugar.i.wd).executeScript("arguments[0].style.visibility = 'visible';", filenameElement);
//		sugar.i.interact("Check if field is visible...");
		// ... try this: http://qtp-help.blogspot.com/2009/08/selenium-handling-dialog-box-in-mac.html...
		//sugar.i.getControl(Strategy.ID, "filename_file").click();
//		sugar.i.getControl(Strategy.ID, "filename_file").sendString(document.file.getAbsolutePath());
//		sugar.i.getControl(Strategy.ID, "document_name").sendString(document.documentName);
//		sugar.i.getControl(Strategy.ID, "SAVE_HEADER").click();
//		sugar.i.getControl(Strategy.ID, "edit_button").halt(4);
//		sugar.i.getControl(Strategy.ID, "moduleTab_AllHome").click();
	}
	
	public void deleteAllDocuments() throws Exception {
		sugar.i.getControl(Strategy.ID, "moduleTabExtraMenuAll").click();
		sugar.i.getControl(Strategy.PLINK, "Documents").click();
		sugar.i.getControl(Strategy.ID, "massall_top").click();
		sugar.i.getControl(Strategy.ID, "delete_listview_top").click();
		sugar.i.acceptDialog();
		sugar.i.getControl(Strategy.ID, "moduleTab_AllHome").click();
	}
	
	public void editDocument(DocumentRecord oldDocument, DocumentRecord newDocument) throws Exception {
		this.searchDocuments(oldDocument.documentName);
		sugar.i.getControl(Strategy.PLINK, oldDocument.documentName).click();
		sugar.i.getControl(Strategy.ID, "edit_button").click();
		throw new Exception("editDocument not yet fully implemented.");
//		sugar.i.getControl(Strategy.ID, "name").sendString(newAccount.name);
//		sugar.i.getControl(Strategy.ID, "SAVE_HEADER").click();
//		sugar.i.getControl(Strategy.ID, "edit_button").halt(4);
//		sugar.i.getControl(Strategy.ID, "moduleTab_AllHome").click();
	}
	
	public void searchDocuments(String search) throws Exception {
		sugar.i.getControl(Strategy.ID, "moduleTabExtraMenuAll").click();
		sugar.i.getControl(Strategy.PLINK, "Documents").click();
		sugar.i.getControl(Strategy.ID, "name_basic").sendString(search);
		sugar.i.getControl(Strategy.ID, "search_form_submit").click();
	}
	
	public static class DocumentRecord {
		
		public File file = null;
		public String documentName = null;

		public DocumentRecord(File file, String documentName) {
			this.file = file;
			this.documentName = documentName;
		}
	}
}
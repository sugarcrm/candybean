package com.sugarcrm.sugar.modules;

import com.sugarcrm.sugar.Sugar;
import com.sugarcrm.voodoo.automation.control.VHook.Strategy;
import com.sugarcrm.voodoo.utilities.Utils;

/**
 * @author Conrad Warmbold
 *
 */
public class KnowledgeBaseModule {
	
	private final Sugar sugar;
	
	public KnowledgeBaseModule(Sugar sugar) { this.sugar = sugar; }
	
	public void createArticle(ArticleRecord article) throws Exception {
		String sugarURL = Utils.getCascadingPropertyValue(sugar.props, "http://localhost/ent670/", "env.base_url");
		sugar.i.go(sugarURL + "/index.php?module=KBDocuments&action=EditView");
		sugar.i.getControl(Strategy.ID, "kbdocument_name").sendString(article.title);
		sugar.i.getControl(Strategy.ID, "body_html_code").click();
		sugar.i.focusWindow(1);
		sugar.i.getControl(Strategy.ID, "htmlSource").sendString(article.body);		
		sugar.i.getControl(Strategy.ID, "insert").click();
		sugar.i.focusWindow(0);
		sugar.i.getControl(Strategy.ID, "btn_save").click();
//		sugar.i.getControl(Strategy.ID, "edit_button").halt(4);
//		sugar.i.getControl(Strategy.ID, "moduleTab_AllHome").click();
	}
	
	public void deleteAllArticles() throws Exception {
		String sugarURL = Utils.getCascadingPropertyValue(sugar.props, "http://localhost/ent670/", "env.base_url");
		sugar.i.go(sugarURL + "/index.php?module=KBDocuments&action=ListView");
		sugar.i.getControl(Strategy.ID, "massall_top").click();
		sugar.i.getControl(Strategy.ID, "delete_listview_top").click();
		sugar.i.acceptDialog();
		sugar.i.getControl(Strategy.ID, "moduleTab_AllHome").click();
	}
	
	public void editArticle(ArticleRecord oldArticle, ArticleRecord newArticle) throws Exception {
		this.searchArticles(oldArticle.title);
		sugar.i.getControl(Strategy.PLINK, oldArticle.title).click();
		sugar.i.getControl(Strategy.ID, "edit_button").click();
		sugar.i.getControl(Strategy.ID, "kbdocument_name").sendString(newArticle.title);
		sugar.i.getControl(Strategy.ID, "body_html_code").click();
		sugar.i.focusWindow(1);
		sugar.i.getControl(Strategy.ID, "htmlSource").sendString(newArticle.body);		
		sugar.i.getControl(Strategy.ID, "insert").click();
		sugar.i.focusWindow(0);
		sugar.i.getControl(Strategy.ID, "btn_save").click();
//		sugar.i.getControl(Strategy.ID, "edit_button").halt(4);
//		sugar.i.getControl(Strategy.ID, "moduleTab_AllHome").click();
	}
	
	public void searchArticles(String search) throws Exception {
		String sugarURL = Utils.getCascadingPropertyValue(sugar.props, "http://localhost/ent670/", "env.base_url");
		sugar.i.go(sugarURL + "/index.php?module=KBDocuments&action=ListView");
		sugar.i.getControl(Strategy.ID, "name_basic").sendString(search);
		sugar.i.getControl(Strategy.ID, "search_form_submit").click();
	}
	
	public static class ArticleRecord {
		
		public String title = null;
		public String body = null;

		public ArticleRecord(String title, String body) {
			this.title = title;
			this.body = body;
		}
	}
}
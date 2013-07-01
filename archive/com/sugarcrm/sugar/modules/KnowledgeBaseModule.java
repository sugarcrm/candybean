/**
 * ====
 *     Candybean is a next generation automation and testing framework suite.
 *     It is a collection of components that foster test automation, execution
 *     configuration, data abstraction, results illustration, tag-based execution,
 *     top-down and bottom-up batches, mobile variants, test translation across
 *     languages, plain-language testing, and web service testing.
 *     Copyright (C) 2013 <candybean@sugarcrm.com>
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * ====
 *
 * Candybean is a next generation automation and testing framework suite.
 * It is a collection of components that foster test automation, execution
 * configuration, data abstraction, results illustration, tag-based execution,
 * top-down and bottom-up batches, mobile variants, test translation across
 * languages, plain-language testing, and web service testing.
 * Copyright (C) 2013 SugarCRM, Inc. <candybean@sugarcrm.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.sugarcrm.sugar.modules;

import com.sugarcrm.sugar.Sugar;
import com.sugarcrm.voodoo.automation.control.VHook.Strategy;

/**
 * @author Conrad Warmbold
 *
 */
public class KnowledgeBaseModule {
	
	private final Sugar sugar;
	
	public KnowledgeBaseModule(Sugar sugar) { this.sugar = sugar; }
	
	public void createArticle(ArticleRecord article) throws Exception {
		String sugarURL = sugar.config.getProperty("env.base_url", "http://localhost/ent670/");
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
		String sugarURL = sugar.config.getProperty("env.base_url", "http://localhost/ent670/");
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
		String sugarURL = sugar.config.getProperty("env.base_url", "http://localhost/ent670/");
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
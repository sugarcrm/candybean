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
package com.sugarcrm.sugar;

import com.sugarcrm.sugar.modules.AccountsModule;
import com.sugarcrm.sugar.modules.BugTrackerModule;
import com.sugarcrm.sugar.modules.CalendarModule;
import com.sugarcrm.sugar.modules.CallsModule;
import com.sugarcrm.sugar.modules.CampaignsModule;
import com.sugarcrm.sugar.modules.CasesModule;
import com.sugarcrm.sugar.modules.ContactsModule;
import com.sugarcrm.sugar.modules.ContractsModule;
import com.sugarcrm.sugar.modules.CurrenciesModule;
import com.sugarcrm.sugar.modules.DocumentsModule;
import com.sugarcrm.sugar.modules.EmailsModule;
import com.sugarcrm.sugar.modules.KnowledgeBaseModule;
import com.sugarcrm.sugar.modules.LeadsModule;
import com.sugarcrm.sugar.modules.ManufacturersModule;
import com.sugarcrm.sugar.modules.MeetingsModule;
import com.sugarcrm.sugar.modules.NotesModule;
import com.sugarcrm.sugar.modules.OpportunitiesModule;
import com.sugarcrm.sugar.modules.ProductsModule;
import com.sugarcrm.sugar.modules.ProjectsModule;
import com.sugarcrm.sugar.modules.QuotesModule;
import com.sugarcrm.sugar.modules.ReportsModule;
import com.sugarcrm.sugar.modules.RolesModule;
import com.sugarcrm.sugar.modules.TargetListsModule;
import com.sugarcrm.sugar.modules.TargetsModule;
import com.sugarcrm.sugar.modules.TasksModule;
import com.sugarcrm.sugar.modules.UsersModule;

/**
 * @author Conrad Warmbold
 *
 */
public class Modules {
	
	public AccountsModule accounts;
	public BugTrackerModule bugTracker;
	public CalendarModule calendar;
	public CallsModule calls;
	public CampaignsModule campaigns;
	public CasesModule cases;
	public ContactsModule contacts;
	public ContractsModule contracts;
	public CurrenciesModule currencies;
	public DocumentsModule documents;
	public EmailsModule emails;
	public KnowledgeBaseModule knowledgeBase;
	public LeadsModule leads;
	public ManufacturersModule manufacturers;
	public MeetingsModule meetings;
	public NotesModule notes;
	public OpportunitiesModule opportunities;
	public ProductsModule products;
	public ProjectsModule projects;
	public QuotesModule quotes;
	public ReportsModule reports;
	public RolesModule roles;
	public TargetListsModule targetLists;
	public TargetsModule targets;
	public TasksModule tasks;
	public UsersModule users;

	public Modules(Sugar sugar) { 
		accounts = new AccountsModule(sugar);
		bugTracker = new BugTrackerModule(sugar);
		calendar = new CalendarModule(sugar);
		calls = new CallsModule(sugar);
		campaigns = new CampaignsModule(sugar);
		cases = new CasesModule(sugar);
		contacts = new ContactsModule(sugar);
		contracts = new ContractsModule(sugar);
		currencies = new CurrenciesModule(sugar);
		documents = new DocumentsModule(sugar);
		emails = new EmailsModule(sugar);
		knowledgeBase = new KnowledgeBaseModule(sugar);
		leads = new LeadsModule(sugar);
		manufacturers = new ManufacturersModule(sugar);
		meetings = new MeetingsModule(sugar);
		notes = new NotesModule(sugar);
		opportunities = new OpportunitiesModule(sugar);
		products = new ProductsModule(sugar);
		projects = new ProjectsModule(sugar);
		quotes = new QuotesModule(sugar);
		reports = new ReportsModule(sugar);
		roles = new RolesModule(sugar);
		targetLists = new TargetListsModule(sugar);
		targets = new TargetsModule(sugar);
		tasks = new TasksModule(sugar);
		users = new UsersModule(sugar);	
	}
}
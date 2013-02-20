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
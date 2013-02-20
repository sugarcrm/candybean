package com.sugarcrm.sugar.system;

import java.io.File;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.sugarcrm.sugar.system.SugarTest;
import com.sugarcrm.sugar.modules.AccountsModule.AccountRecord;
import com.sugarcrm.sugar.modules.BugTrackerModule.BugRecord;
import com.sugarcrm.sugar.modules.CallsModule.CallRecord;
import com.sugarcrm.sugar.modules.CampaignsModule.CampaignRecord;
import com.sugarcrm.sugar.modules.CasesModule.CaseRecord;
import com.sugarcrm.sugar.modules.ContactsModule.ContactRecord;
import com.sugarcrm.sugar.modules.ContractsModule.ContractRecord;
import com.sugarcrm.sugar.modules.CurrenciesModule.CurrencyRecord;
import com.sugarcrm.sugar.modules.DocumentsModule.DocumentRecord;
import com.sugarcrm.sugar.modules.EmailsModule.EmailTemplateRecord;
import com.sugarcrm.sugar.modules.KnowledgeBaseModule.ArticleRecord;
import com.sugarcrm.sugar.modules.LeadsModule.LeadRecord;
import com.sugarcrm.sugar.modules.ManufacturersModule.ManufacturerRecord;
import com.sugarcrm.sugar.modules.MeetingsModule.MeetingRecord;
import com.sugarcrm.sugar.modules.NotesModule.NoteRecord;
import com.sugarcrm.sugar.modules.OpportunitiesModule.OpportunityRecord;
import com.sugarcrm.sugar.modules.ProductsModule.ProductRecord;
import com.sugarcrm.sugar.modules.ProjectsModule.ProjectRecord;
import com.sugarcrm.sugar.modules.QuotesModule.QuoteRecord;
import com.sugarcrm.sugar.modules.ReportsModule.ReportRecord;
import com.sugarcrm.sugar.modules.RolesModule.RoleRecord;
import com.sugarcrm.sugar.modules.TargetListsModule.TargetListRecord;
import com.sugarcrm.sugar.modules.TargetsModule.TargetRecord;
import com.sugarcrm.sugar.modules.TasksModule.TaskRecord;

public class CRUD extends SugarTest {
	
	private static final String curWorkDir = System.getProperty("user.dir");
	private static final String relPropsPath = curWorkDir + File.separator + "src" + File.separator + "test" + File.separator + "resources";
	private static final String documentFilePath = relPropsPath + File.separator + "sugar.properties";
	
	@BeforeClass
	public static void first() throws Exception {
		SugarTest.first();
		sugar.login(sugar.admin.username, sugar.admin.password1);
	}
	
	@Before
	public void setup() throws Exception { super.setup(); }

//	@Ignore
	@Test
	public void accounts() throws Exception {
		AccountRecord account1 = new AccountRecord("accountName1");
		AccountRecord account2 = new AccountRecord("accountName2");
		sugar.modules.accounts.createAccount(account1);
		sugar.modules.accounts.editAccount(account1, account2);
		sugar.modules.accounts.searchAccounts(account2.name);
		sugar.modules.accounts.deleteAllAccounts();
	}
	
	@Ignore
	@Test
	public void bugs() throws Exception {
		BugRecord bug1 = new BugRecord("bugSubject1");
		BugRecord bug2 = new BugRecord("bugSubject2");
		sugar.modules.bugTracker.reportBug(bug1);
		sugar.modules.bugTracker.editBug(bug1, bug2);
		sugar.modules.bugTracker.searchBugTracker(bug2.subject);
		sugar.modules.bugTracker.deleteAllBugs();
	}

	@Ignore
	@Test
	public void calls() throws Exception {
		CallRecord call1 = new CallRecord("callSubject1");
		CallRecord call2 = new CallRecord("callSubject2");
		sugar.modules.calls.scheduleCall(call1);
		sugar.modules.calls.editCall(call1, call2);
		sugar.modules.calls.searchCalls(call2.subject);
		sugar.modules.calls.deleteAllCalls();
	}
	
	@Ignore
	@Test
	public void campaigns() throws Exception {
		CampaignRecord campaign1 = new CampaignRecord("campaignName1", "Planning", "Email", "12/12/2015");
		CampaignRecord campaign2 = new CampaignRecord("campaignName2", "Planning", "Email", "1/1/2016");
		sugar.modules.campaigns.createCampaign(campaign1);
		sugar.modules.campaigns.editCampaign(campaign1, campaign2);
		sugar.modules.campaigns.searchCampaigns(campaign2.name);
		sugar.modules.campaigns.deleteAllCampaigns();
	}

	@Ignore
	@Test
	public void cases() throws Exception {
		AccountRecord account = new AccountRecord("accountName1");
		sugar.modules.accounts.createAccount(account);
		CaseRecord caseRecord1 = new CaseRecord("caseSubject1", account);
		CaseRecord caseRecord2 = new CaseRecord("caseSubject2", account);
		sugar.modules.cases.createCase(caseRecord1);
		sugar.modules.cases.editCase(caseRecord1, caseRecord2);
		sugar.modules.cases.searchCases(caseRecord2.subject);
		sugar.modules.cases.deleteAllCases();
	}

	@Ignore
	@Test
	public void contacts() throws Exception {
		ContactRecord contact1 = new ContactRecord("lastName1");
		ContactRecord contact2 = new ContactRecord("lastName2");
		sugar.modules.contacts.createContact(contact1);
		sugar.modules.contacts.editContact(contact1, contact2);
		sugar.modules.contacts.searchContacts(contact2.lastName);
		sugar.modules.contacts.deleteAllContacts();
	}
	
	@Ignore
	@Test
	public void contracts() throws Exception {
		AccountRecord account = new AccountRecord("accountName1");
		sugar.modules.accounts.createAccount(account);
		ContractRecord contract1 = new ContractRecord("contractName1", "In Progress", account);
		ContractRecord contract2 = new ContractRecord("contractName2", "In Progress", account);
		sugar.modules.contracts.createContract(contract1);
		sugar.modules.contracts.editContract(contract1, contract2);
		sugar.modules.contracts.searchContracts(contract2.name);
		sugar.modules.contracts.deleteAllContracts();
	}
	
	@Ignore
	@Test
	public void currencies() throws Exception {
		CurrencyRecord currency1 = new CurrencyRecord("currency1", "0.88", "#");
		CurrencyRecord currency2 = new CurrencyRecord("currency2", "0.88", "&");
		sugar.modules.currencies.createCurrency(currency1);
		sugar.modules.currencies.editCurrency(currency1, currency2);
//		sugar.modules.currencies.searchCurrency(currency2);
	}
	
	@Ignore
	@Test
	public void documents() throws Exception {
		DocumentRecord document1 = new DocumentRecord(new File(documentFilePath), "documentName1");
		DocumentRecord document2 = new DocumentRecord(new File(documentFilePath), "documentName2");
		sugar.modules.documents.createDocument(document1);
		sugar.modules.documents.editDocument(document1, document2);
		sugar.modules.documents.searchDocuments(document2.documentName);
		sugar.modules.documents.deleteAllDocuments();
	}
	
	@Ignore
	@Test
	public void emails() throws Exception {
		EmailTemplateRecord emailTemplate1 = new EmailTemplateRecord("emailTemplate1");
		EmailTemplateRecord emailTemplate2 = new EmailTemplateRecord("emailTemplate2");
		sugar.modules.emails.createEmailTemplate(emailTemplate1);
		sugar.modules.emails.editEmailTemplate(emailTemplate1, emailTemplate2);
		sugar.modules.emails.searchEmailTemplates(emailTemplate2.name);
		sugar.modules.emails.deleteAllEmailTemplates();
	}
	
	@Ignore
	@Test
	public void employees() throws Exception {
//		EmployeeRecord employee = new EmployeeRecord();
//		sugar.modules.employees.createEmployee(employee);
	}
	
	@Ignore
	@Test
	public void knowledgeBase() throws Exception {
		ArticleRecord article1 = new ArticleRecord("articleTitle1", "articleBody1");
		ArticleRecord article2 = new ArticleRecord("articleTitle2", "articleBody2");
		sugar.modules.knowledgeBase.createArticle(article1);
		sugar.modules.knowledgeBase.editArticle(article1, article2);
		sugar.modules.knowledgeBase.searchArticles(article2.title);
		sugar.modules.knowledgeBase.deleteAllArticles();
	}
	
	@Ignore
	@Test
	public void leads() throws Exception {
		LeadRecord lead1 = new LeadRecord("leadLastName1");
		LeadRecord lead2 = new LeadRecord("leadLastName2");
		sugar.modules.leads.createLead(lead1);
		sugar.modules.leads.editLead(lead1, lead2);
		sugar.modules.leads.searchLeads(lead2.lastName);
		sugar.modules.leads.deleteAllLeads();
	}
	
	@Ignore
	@Test
	public void manufacturers() throws Exception {
		ManufacturerRecord manufacturer1 = new ManufacturerRecord("manufacturerName1");
		ManufacturerRecord manufacturer2 = new ManufacturerRecord("manufacturerName2");
		sugar.modules.manufacturers.createManufacturer(manufacturer1);
		sugar.modules.manufacturers.editManufacturer(manufacturer1, manufacturer2);
//		sugar.modules.manufacturers.searchManufacturers(manufacturer2.name);
		sugar.modules.manufacturers.deleteAllManufacturers();
	}
	
	@Ignore
	@Test
	public void meetings() throws Exception {
		MeetingRecord meeting1 = new MeetingRecord("meetingSubject1");
		MeetingRecord meeting2 = new MeetingRecord("meetingSubject2");
		sugar.modules.meetings.scheduleMeeting(meeting1);
		sugar.modules.meetings.editMeeting(meeting1, meeting2);
		sugar.modules.meetings.searchMeetings(meeting2.subject);
		sugar.modules.meetings.deleteAllMeetings();
	}
	
	@Ignore
	@Test
	public void notes() throws Exception {
		NoteRecord note1 = new NoteRecord("noteSubject1");
		NoteRecord note2 = new NoteRecord("noteSubject2");
		sugar.modules.notes.createNote(note1);
		sugar.modules.notes.editNote(note1, note2);
		sugar.modules.notes.searchNotes(note2.subject);
		sugar.modules.notes.deleteAllNotes();
	}
	
	@Ignore
	@Test
	public void opportunities() throws Exception {
		AccountRecord account = new AccountRecord("accountName1");
		sugar.modules.accounts.createAccount(account);
		OpportunityRecord opportunity1 = new OpportunityRecord("name1", account, "12/12/2015", null);
		OpportunityRecord opportunity2 = new OpportunityRecord("name2", account, "12/12/2015", null);
		sugar.modules.opportunities.createOpportunity(opportunity1);
		sugar.modules.opportunities.editOpportunity(opportunity1, opportunity2);
		sugar.modules.opportunities.searchOpportunities(opportunity2.name);
		sugar.modules.opportunities.deleteAllOpportunities();
	}
	
	@Ignore
	@Test
	public void products() throws Exception {
		ProductRecord product1 = new ProductRecord("productName1");
		ProductRecord product2 = new ProductRecord("productName2");
		sugar.modules.products.createProduct(product1);
		sugar.modules.products.editProduct(product1, product2);
		sugar.modules.products.searchProducts(product2.name);
		sugar.modules.products.deleteAllProducts();
	}
	
	@Ignore
	@Test
	public void projects() throws Exception {
		ProjectRecord project1 = new ProjectRecord("projectName1", "12/12/2014", "12/12/2015");
		ProjectRecord project2 = new ProjectRecord("projectName2", "1/1/2015", "1/1/2016");
		sugar.modules.projects.createProject(project1);
		sugar.modules.projects.editProject(project1, project2);
		sugar.modules.projects.searchProjects(project2.name);
		sugar.modules.projects.deleteAllProjects();
	}
	
	@Ignore
	@Test
	public void quotes() throws Exception {
		AccountRecord billingAccount = new AccountRecord("account1");
		sugar.modules.accounts.createAccount(billingAccount);
		QuoteRecord quote1 = new QuoteRecord("quoteSubject1", "12/12/2015", billingAccount);
		QuoteRecord quote2 = new QuoteRecord("quoteSubject2", "1/1/2016", billingAccount);
		sugar.modules.quotes.createQuote(quote1);
		sugar.modules.quotes.editQuote(quote1, quote2);
		sugar.modules.quotes.searchQuotes(quote2.subject);
		sugar.modules.quotes.deleteAllQuotes();
	}
	
	@Ignore
	@Test
	public void reports() throws Exception {
		ReportRecord report1 = new ReportRecord("reportTitle1");
		ReportRecord report2 = new ReportRecord("reportTitle2");
		sugar.modules.reports.createReport(report1);
		sugar.modules.reports.editReport(report1, report2);
		sugar.modules.reports.searchReports(report2.title);
		sugar.modules.reports.deleteAllReports();
	}
	
	@Ignore
	@Test
	public void roles() throws Exception {
		RoleRecord role1 = new RoleRecord("roleName1");
		RoleRecord role2 = new RoleRecord("roleName2");
		sugar.modules.roles.createRole(role1);
		sugar.modules.roles.editRole(role1, role2);
		sugar.modules.roles.searchRoles(role2.name);
//		sugar.modules.roles.deleteAllRoles();
	}
	
	@Ignore
	@Test
	public void targetLists() throws Exception {
		TargetListRecord targetList1 = new TargetListRecord("targetListName1");
		TargetListRecord targetList2 = new TargetListRecord("targetListName2");
		sugar.modules.targetLists.createTargetList(targetList1);
		sugar.modules.targetLists.editTargetList(targetList1, targetList2);
		sugar.modules.targetLists.searchTargetLists(targetList2.name);
		sugar.modules.targetLists.deleteAllTargetLists();
	}
	
	@Ignore
	@Test
	public void targets() throws Exception {
		TargetRecord target1 = new TargetRecord("lastName1");
		TargetRecord target2 = new TargetRecord("lastName2");
		sugar.modules.targets.createTarget(target1);
		sugar.modules.targets.editTarget(target1, target2);
		sugar.modules.targets.searchTargets(target2.lastName);
		sugar.modules.targets.deleteAllTargets();
	}
	
	@Ignore
	@Test
	public void tasks() throws Exception {
		TaskRecord task1 = new TaskRecord("taskSubject1");
		TaskRecord task2 = new TaskRecord("taskSubject2");
		sugar.modules.tasks.createTask(task1);
		sugar.modules.tasks.editTask(task1, task2);
		sugar.modules.tasks.searchTasks(task2.subject);
		sugar.modules.tasks.deleteAllTasks();
	}

	@After
	public void cleanup() throws Exception { super.cleanup(); }
	
	@AfterClass
	public static void last() throws Exception {
		sugar.logout();
		SugarTest.last();
	}
}

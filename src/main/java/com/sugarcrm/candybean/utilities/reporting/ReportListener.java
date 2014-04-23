package com.sugarcrm.candybean.utilities.reporting;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.Unmarshaller;
import org.apache.maven.plugins.surefire.report.ReportTestCase;
import org.apache.maven.plugins.surefire.report.ReportTestSuite;
import org.apache.maven.plugins.surefire.report.SurefireReportParser;
import org.apache.maven.reporting.MavenReportException;
import org.testng.IReporter;
import org.testng.ISuite;
import org.testng.xml.XmlSuite;
import com.sugarcrm.candybean.automation.Candybean;
import com.sugarcrm.candybean.configuration.Configuration;
import com.sugarcrm.candybean.exceptions.CandybeanException;
import com.sugarcrm.candybean.utilities.Utils;

public class ReportListener implements IReporter{
	
	Logger logger;
	
	private static final String SUREFIRE_RESULTS_DIRECTORY = "./target/surefire-reports/junitreports";
	
	private static final String CANDYBEAN_REPORT_PATH = "./target/candybean-reports/reports/CandybeanTestResults.html";

	private static final String TEST_TEMPLATE_PATH = "./resources/html/testTemplate.html";

	private static final String PACKAGE_TEMPLATE_PATH = "./resources/html/packageTemplate.html";

	private static final String TEST_RESULTS_TEMPLATE_PATH = "./resources/html/testResultsTemplate.html";

	private static final String PACKAGE_TABLE_TEMPLATE_PATH = "./resources/html/packageTableTemplate.html";

	private static final String DATA_TABLE_INIT_TEMPLATE_PATH = "./resources/html/dataTableInitTemplate.html";
	
	private static final String FAILED_TEST_RESULTS_XML = "./target/candybean-reports/results/failedTestResults.xml";
	
	private JAXBContext context;
	
	private File xmlFile;
	
	private Configuration config;

	@Override
	public void generateReport(List<XmlSuite> arg0, List<ISuite> arg1,
			String arg2) {
		try {
			config = Candybean.getInstance().config;
			logger = Logger.getLogger(Candybean.class.getSimpleName());
			logger.info("GENERATING REPORT NOW!");
			this.context = JAXBContext.newInstance(FailedTests.class);
			String candybeanConfigStr = System.getProperty(Candybean.CONFIG_KEY, Candybean.DEFAULT_CONFIG_FILE);
			this.config = new Configuration(new File(candybeanConfigStr));
			this.xmlFile = Utils.createFile(config.getValue("testResultsXMLPath", FAILED_TEST_RESULTS_XML), false);
			generateTestResultsReport();
		} catch (Exception e) {
			logger = Logger.getLogger(ReportListener.class.getSimpleName());
			logger.severe("Unable to get candybean instance");
		}
	}
	
	/**
	 * Generates the custom candybean report for all test results
	 * @throws MavenReportException
	 * @throws IOException
	 * @throws JAXBException 
	 */
	private void generateTestResultsReport() throws MavenReportException, IOException, JAXBException {
		File reportsDirectory = new File(config.getValue("surefireResultsDirectory", SUREFIRE_RESULTS_DIRECTORY));
		if(reportsDirectory.exists()){
			logger.info("TEST RESULTS DIRECTORY EXISTS!");
			Unmarshaller unmarshaller = context.createUnmarshaller();
			FailedTests failedTestList;
			try {
				failedTestList = (FailedTests) unmarshaller.unmarshal(xmlFile);
			} catch (UnmarshalException e) {
				// The file doesn't contain any previous results, so we will start
				// with a clean file.
				failedTestList = new FailedTests();
			}
			List<File> reportsDirectories = new ArrayList<File>();
			reportsDirectories.add(reportsDirectory);
			SurefireReportParser report = new SurefireReportParser( reportsDirectories, Locale.ENGLISH );
			List<ReportTestSuite> suites = report.parseXMLReportFiles();
			Map<String, String> summary = report.getSummary(suites);
			Map<String, List<ReportTestSuite>> packages = report.getSuitesGroupByPackage(suites);
			String baseTemplate = Utils.readFile(TEST_RESULTS_TEMPLATE_PATH, Charset.defaultCharset());
			
			baseTemplate = baseTemplate.replace("${title}", config.getValue("testResultsReport.title", ""));
			baseTemplate = baseTemplate.replace("${summary.tests}", summary.get("totalTests"));
			baseTemplate = baseTemplate.replace("${summary.errors}", summary.get("totalErrors"));
			baseTemplate = baseTemplate.replace("${summary.failures}", summary.get("totalFailures"));
			baseTemplate = baseTemplate.replace("${summary.skipped}", summary.get("totalSkipped"));
			baseTemplate = baseTemplate.replace("${summary.success}", summary.get("totalPercentage")+"%");
			baseTemplate = baseTemplate.replace("${summary.time}",  Utils.calculateTime(Float.parseFloat(summary.get("totalElapsedTime"))));
			baseTemplate = baseTemplate.replace("${testsPerPage}", config.getValue("testResultsReport.testsPerPage"));
			StringBuilder packageMarkup = new StringBuilder();
			StringBuilder packageTableMarkup = new StringBuilder();
			StringBuilder summaryTests = new StringBuilder();
			StringBuilder tableInits = new StringBuilder();
			String packageTemplate = Utils.readFile(PACKAGE_TEMPLATE_PATH, Charset.defaultCharset());
			String packageTableTemplate = Utils.readFile(PACKAGE_TABLE_TEMPLATE_PATH, Charset.defaultCharset());
			String dataTableInitTempalte = Utils.readFile(DATA_TABLE_INIT_TEMPLATE_PATH, Charset.defaultCharset());
			String entryTemplate = Utils.readFile("./resources/html/videoEntryTemplate.html", Charset.defaultCharset());
			String entryFailTemplate = Utils.readFile("./resources/html/failedEntryTemplate.html", Charset.defaultCharset());
			StringBuilder completeClassTableMarkup = new StringBuilder();
			StringBuilder completeClassMarkup = new StringBuilder();
			StringBuilder completeFailedTestPillsMarkup = new StringBuilder();
			StringBuilder completeFailedTestContentMarkup = new StringBuilder();
			for(String pkg: packages.keySet()){
				String pkgTemplate = packageTemplate;
				String pkgTableTemplate = packageTableTemplate;
				String dataTblInitTempalte = dataTableInitTempalte;
				List<ReportTestSuite> packageSuites = packages.get(pkg);
				Map<String, String> summaryForPackage = report.getSummary(packageSuites);
				StringBuilder packageTests = new StringBuilder();
				
				dataTblInitTempalte = dataTblInitTempalte.replace("${tableId}", pkg.replace(".", ""));
				dataTblInitTempalte = dataTblInitTempalte.replace("${testsPerPage}", config.getValue("testResultsReport.testsPerPage"));
				
				pkgTableTemplate = pkgTableTemplate.replace("${packageId}", pkg.replace(".", ""));
				pkgTableTemplate = pkgTableTemplate.replace("${package.tests}", summaryForPackage.get("totalTests"));
				pkgTableTemplate = pkgTableTemplate.replace("${package.errors}", summaryForPackage.get("totalErrors"));
				pkgTableTemplate = pkgTableTemplate.replace("${package.failures}", summaryForPackage.get("totalFailures"));
				pkgTableTemplate = pkgTableTemplate.replace("${package.skipped}", summaryForPackage.get("totalSkipped"));
				pkgTableTemplate = pkgTableTemplate.replace("${package.success}", summaryForPackage.get("totalPercentage")+"%");
				pkgTableTemplate = pkgTableTemplate.replace("${package.time}", Utils.calculateTime(Float.parseFloat((summaryForPackage.get("totalElapsedTime")))));
				
				pkgTemplate = pkgTemplate.replace("${packageId}", pkg.replace(".", ""));
				pkgTemplate = pkgTemplate.replace("${package.name}", pkg);

				StringBuilder classTableInitMarkup = new StringBuilder();
				String classTemplate = Utils.readFile(PACKAGE_TEMPLATE_PATH, Charset.defaultCharset());
				String classTableTemplate = Utils.readFile(PACKAGE_TABLE_TEMPLATE_PATH, Charset.defaultCharset());
				String classDataTableInitTempalte = Utils.readFile(DATA_TABLE_INIT_TEMPLATE_PATH, Charset.defaultCharset());
				
				for(ReportTestSuite testSuite: packageSuites){
					String clsTemplate = classTemplate;
					String clsTableTemplate = classTableTemplate;
					String clsDataTblInitTempalte = classDataTableInitTempalte;
					int numberOfTests = testSuite.getNumberOfTests();
					int numberOfErrors = testSuite.getNumberOfErrors();
					int numberOfFailures = testSuite.getNumberOfFailures();
					int numberOfSkipped = testSuite.getNumberOfSkipped();
					
					clsDataTblInitTempalte = clsDataTblInitTempalte.replace("${tableId}", testSuite.getFullClassName().replace(".", ""));
					clsDataTblInitTempalte = clsDataTblInitTempalte.replace("${testsPerPage}", config.getValue("testResultsReport.testsPerPage"));
					
					clsTableTemplate = clsTableTemplate.replace("${packageId}", testSuite.getFullClassName().replace(".", ""));
					clsTableTemplate = clsTableTemplate.replace("${package.tests}", String.valueOf(testSuite.getNumberOfTests()));
					clsTableTemplate = clsTableTemplate.replace("${package.errors}", String.valueOf(testSuite.getNumberOfErrors()));
					clsTableTemplate = clsTableTemplate.replace("${package.failures}", String.valueOf(testSuite.getNumberOfFailures()));
					clsTableTemplate = clsTableTemplate.replace("${package.skipped}", String.valueOf(testSuite.getNumberOfSkipped()));
					clsTableTemplate = clsTableTemplate.replace("${package.success}", String.valueOf(report.computePercentage(numberOfTests, numberOfErrors, numberOfFailures, numberOfSkipped))+"%");
					clsTableTemplate = clsTableTemplate.replace("${package.time}", Utils.calculateTime(testSuite.getTimeElapsed()));
					
					clsTemplate = clsTemplate.replace("${packageId}", testSuite.getFullClassName().replace(".", ""));
					clsTemplate = clsTemplate.replace("${package.name}", testSuite.getName());
					
					StringBuilder testMarkup = new StringBuilder();
					String testTemplate = Utils.readFile(TEST_TEMPLATE_PATH, Charset.defaultCharset());
					String baseListItemTemplate = Utils.readFile(PACKAGE_TEMPLATE_PATH, Charset.defaultCharset());
					for(ReportTestCase testCase: testSuite.getTestCases()){
						String listItemTemplate = baseListItemTemplate;
						boolean testPassed = true;
						String tstTemplate = testTemplate;
						Map<String, Object> result = testCase.getFailure();
						if(result == null){
							tstTemplate = tstTemplate.replace("${test.result}", "Success");
							tstTemplate = tstTemplate.replace("${result}", "pass");
						}else if(result.get("type").equals("skipped")){
							tstTemplate = tstTemplate.replace("${test.result}", "Skipped");
							tstTemplate = tstTemplate.replace("${result}", "skip");
						}else{
							tstTemplate = tstTemplate.replace("${test.result}", "Failed!");
							tstTemplate = tstTemplate.replace("${result}", "fail");
							testPassed = false;
						}
						tstTemplate = tstTemplate.replace("${test.name}", testCase.getName());
						tstTemplate = tstTemplate.replace("${test.class}", testCase.getClassName());
						tstTemplate = tstTemplate.replace("${test.package}", testCase.getFullClassName().replace(testCase.getClassName(), ""));
						float time = testCase.getTime();
						if(time == 0){
							tstTemplate = tstTemplate.replace("${test.runtime}", "Did not run");
						}else{
							tstTemplate = tstTemplate.replace("${test.runtime}", Utils.calculateTime(testSuite.getTimeElapsed()));
						}
						testMarkup.append(tstTemplate);
						listItemTemplate = listItemTemplate.replace("${packageId}", "failed" + testCase.getName() + testCase.getClassName());
						listItemTemplate = listItemTemplate.replace("${package.name}", testCase.getFullName());
						if(!testPassed){
							if(failedTestList.getFailures().containsKey(testCase.getName() + "(" + testCase.getFullClassName() + ")")) {
								String entryVideoTemplate = entryTemplate;
								TestFailure videoInformation = failedTestList.getFailures().get(testCase.getName() + "(" + testCase.getFullClassName() + ")");
								entryVideoTemplate = entryVideoTemplate.replace("${failure.header}", videoInformation.getPathToVideo());
								entryVideoTemplate = entryVideoTemplate.replace("${failure.pathToVideo}", videoInformation.getPathToVideo());
								entryVideoTemplate = entryVideoTemplate.replace("${failure.stacktrace}", videoInformation.getTrace());
								entryVideoTemplate = entryVideoTemplate.replace("${packageId}", "failed"+testCase.getName()+testCase.getClassName());
								completeFailedTestContentMarkup.append(entryVideoTemplate);
							} else {
								//Video for this failed test was not recorded
								String entryFailureTemplate = entryFailTemplate;
								entryFailureTemplate = entryFailureTemplate.replace("${packageId}", "failed"+testCase.getName()+testCase.getClassName());
								entryFailureTemplate = entryFailureTemplate.replace("${failure.exception}", 
										testCase.getFailure().get("type") == null?"Not Specified":testCase.getFailure().get("type").toString());
								entryFailureTemplate = entryFailureTemplate.replace("${failure.message}", 
										testCase.getFailure().get("message") == null?"Not Specified":testCase.getFailure().get("message").toString());
								entryFailureTemplate = entryFailureTemplate.replace("${failure.detail}",
										testCase.getFailure().get("detail") == null?"Not Specified":testCase.getFailure().get("detail").toString());
								completeFailedTestContentMarkup.append(entryFailureTemplate);
							}
							
							completeFailedTestPillsMarkup.append(listItemTemplate);
						}
					}
					
					summaryTests.append(testMarkup.toString());
					packageTests.append(testMarkup.toString());
					
					clsTableTemplate = clsTableTemplate.replace("${packageTests}", testMarkup.toString());
					completeClassTableMarkup.append(clsTableTemplate.toString());
					
					//classMarkup.append(clsTemplate);
					completeClassMarkup.append(clsTemplate);
					classTableInitMarkup.append(clsDataTblInitTempalte);
				}
				pkgTableTemplate = pkgTableTemplate.replace("${packageTests}", packageTests.toString());
				packageTableMarkup.append(pkgTableTemplate);
				packageMarkup.append(pkgTemplate);
				tableInits.append(dataTblInitTempalte);
				tableInits.append(classTableInitMarkup.toString());
			
			}

			baseTemplate = baseTemplate.replace("${summaryTests}", summaryTests.toString());
			
			baseTemplate = baseTemplate.replace("${packageList}", packageMarkup.toString());
			baseTemplate = baseTemplate.replace("${packageTableList}", packageTableMarkup.toString());
			
			baseTemplate = baseTemplate.replace("${classList}", completeClassMarkup.toString());
			baseTemplate = baseTemplate.replace("${classTableList}", completeClassTableMarkup.toString());
			
			baseTemplate = baseTemplate.replace("${failedTestList}", completeFailedTestPillsMarkup.toString());
			baseTemplate = baseTemplate.replace("${failedTestInfoList}", completeFailedTestContentMarkup.toString());
			
			baseTemplate = baseTemplate.replace("${table.init}", tableInits.toString());
			FileWriter fstream = new FileWriter(Utils.createFile(config.getValue(
					"testResultsReportPath", CANDYBEAN_REPORT_PATH), true));
			BufferedWriter out = new BufferedWriter(fstream);
			out.write(baseTemplate);
			out.close();
		}else{
			logger.warning("No surefire XML reports found in the surefire results directory, skipping html report generation");
			return;
		}
	}
	
}


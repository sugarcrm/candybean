package com.sugarcrm.candybean.runner;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.Unmarshaller;

import com.sugarcrm.candybean.exceptions.CandybeanException;
import org.apache.maven.plugins.surefire.report.ReportTestCase;
import org.apache.maven.plugins.surefire.report.ReportTestSuite;
import org.apache.maven.plugins.surefire.report.SurefireReportParser;
import org.apache.maven.reporting.MavenReportException;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import com.sugarcrm.candybean.automation.Candybean;
import com.sugarcrm.candybean.configuration.Configuration;
import com.sugarcrm.candybean.utilities.CandybeanLogger;
import com.sugarcrm.candybean.utilities.SpecializedScreenRecorder;
import com.sugarcrm.candybean.utilities.Utils;
import com.sugarcrm.candybean.utilities.reporting.FailedTests;
import com.sugarcrm.candybean.utilities.reporting.TestFailure;

/**
 * A custom {@link RunListener} which includes callback routines for when any
 * {@link Test} annotated with {@link Record} is started. The listener will
 * record any failing tests.
 * 
 * @author Shehryar Farooq
 */
public class TestRecorder extends RunListener {

	/*
	 * A custom {@link ScreenRecorder} used to capture a video of the screen.
	 * The {@link SpecializedScreenRecorder} can be used to configure specific
	 * details of the recorded files such as location and name.
	 */
	private SpecializedScreenRecorder screenRecorder;
	
	/*
	 * Singleton TestRecorder instance
	 */
	private static TestRecorder testRecorder;

	/*
	 * Failed state of the current test
	 */
	private boolean testFailed = false;
	
	/*
	 * Recording state
	 */
	private boolean recordInProgress = false;

	/*
	 * Logger of the current test class
	 */
	private Logger logger;

	/*
	 * The last failed test
	 */
	private Failure failure;

	/*
	 * The JAXBContext entry point for marshaling TestFailure objects to XML
	 */
	private JAXBContext context;

	/*
	 * File containing the details of the failed tests
	 */
	private File xmlFile;

	/*
	 * Candybean configuration
	 */
	private Configuration config;

	/*
	 * The default file location to store test results in XML
	 */
	private static final String FAILED_TEST_RESULTS_XML = "./target/candybean-reports/results/failedTestResults.xml";

	/*
	 * The default file location of the surefire results directory
	 */
	private static final String SUREFIRE_RESULTS_DIRECTORY = "./target/surefire-reports";
	
	/*
	 * The default path of where the candybean test results report will be generated
	 */
	private static final String CANDYBEAN_REPORT_PATH = "./target/candybean-reports/reports/CandybeanTestResults.html";

	private static final String TEST_TEMPLATE_PATH = "./resources/html/testTemplate.html";

	private static final String PACKAGE_TEMPLATE_PATH = "./resources/html/packageTemplate.html";

	private static final String TEST_RESULTS_TEMPLATE_PATH = "./resources/html/testResultsTemplate.html";

	private static final String PACKAGE_TABLE_TEMPLATE_PATH = "./resources/html/packageTableTemplate.html";

	private static final String DATA_TABLE_INIT_TEMPLATE_PATH = "./resources/html/dataTableInitTemplate.html";

	private TestRecorder() throws SecurityException, IOException, JAXBException, CandybeanException {
		super();
		logger = Logger.getLogger(Candybean.class.getSimpleName());
		this.context = JAXBContext.newInstance(FailedTests.class);
		String candybeanConfigStr = System.getProperty(Candybean.CONFIG_KEY, Candybean.getDefaultConfigFile());
		this.config = new Configuration(new File(candybeanConfigStr));
		this.xmlFile = createFile(config.getValue("testResultsXMLPath", FAILED_TEST_RESULTS_XML), false);
	}
	
	public static TestRecorder getInstance() throws SecurityException, IOException, JAXBException, CandybeanException {
		if(testRecorder == null) {
			testRecorder = new TestRecorder();
		}
		return testRecorder;
	}
	
	@Override
	public void testStarted(Description description) throws Exception {
		Record record = description.getAnnotation(Record.class);
		this.testFailed = false;
		// Check to see if this test is annotated with Record
		if (record != null) {
			if(!recordInProgress){
				logger.info("Recording started: " + description.getClassName()
						+ "." + description.getMethodName());
				// Start the recording
				startRecording(description.getClassName() + "-"
						+ description.getMethodName());
				recordInProgress = true;
			}else{
				logger.info("Record already in progress");
			}
		}
	}

	@Override
	public void testFinished(Description description) throws Exception {
		Record record = description.getAnnotation(Record.class);
		if (record != null && recordInProgress) {
			Duration duration = record.duration();
			logger.info("Recording ended: " + description.getClassName() + "."
					+ description.getMethodName());
			// Stop the recording
			stopRecording();
			recordInProgress = false;
			// Last recorded test
			List<File> recordedTests = this.screenRecorder
					.getCreatedMovieFiles();
			File createdVideoFile = recordedTests.get(recordedTests.size() - 1);
			// Create failure object containing all the details for this failed
			// test
			if (testFailed) {
				TestFailure failedTest = new TestFailure();
				Unmarshaller unmarshaller = context.createUnmarshaller();
				FailedTests failedTests;
				try {
					failedTests = (FailedTests) unmarshaller.unmarshal(xmlFile);
				} catch (UnmarshalException e) {
					// The file doesn't contain any previous results, so we will
					// start with a clean file.
					failedTests = new FailedTests();
				}
				failedTest.setClassName(description.getClassName());
				failedTest.setMethodName(description.getMethodName());
				failedTest.setPathToVideo(createdVideoFile.getCanonicalPath());
				failedTest.setTestHeader(failure.getTestHeader());
				failedTest.setTrace(failure.getTrace());
				failedTest.setFailMessage(failure.getMessage());
				if(!failedTests.getFailures().containsKey(failedTest.getTestHeader())){
					failedTests.getFailures().put(failedTest.getTestHeader(),failedTest);
				}
				Marshaller marshal = context.createMarshaller();
				marshal.marshal(failedTests, xmlFile);
			}
			// If the test failed, and user configured to record final moments,
			// cut the recording
			if (duration.equals(Duration.FINAL)
					|| (duration.equals(Duration.FINAL_FAILED) && testFailed)) {
				logger.info("TODO: Cut the recording to its final few seconds");
				// TODO: Cut the recording to final moments
			} else if (duration.equals(Duration.FINAL_FAILED) && !testFailed) {
				logger.info("Test passed, but we are only recording failed tests, deleting recording");
				// If the test didn't fail and we are only recording final
				// moments of a failed test, delete the recording.
				if (recordedTests.size() > 0) {
					createdVideoFile.delete();
				}
			}
		}
	}

	@Override
	public void testFailure(Failure failure) throws Exception {
		this.testFailed = true;
		this.failure = failure;
		super.testFailure(failure);
	}

	/**
	 * Starts a recording of the screen
	 * 
	 * @param testFileName
	 *            The name of the video file to create
	 * @throws Exception
	 */
	private void startRecording(String testFileName) throws Exception {
		GraphicsConfiguration gc = GraphicsEnvironment
				.getLocalGraphicsEnvironment().getDefaultScreenDevice()
				.getDefaultConfiguration();
		this.screenRecorder = new SpecializedScreenRecorder(gc, testFileName,
				config);
		this.screenRecorder.start();

	}

	/**
	 * Stops the current recording
	 * 
	 * @throws Exception
	 */
	private void stopRecording() throws Exception {
		this.screenRecorder.stop();
	}

	@Override
	public void testRunFinished(Result result) throws Exception {
		generateTestResultsReport();
		super.testRunFinished(result);
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
			String baseTemplate = readFile(TEST_RESULTS_TEMPLATE_PATH, Charset.defaultCharset());
			
			baseTemplate = baseTemplate.replace("${title}", config.getValue("testResultsReport.title", ""));
			baseTemplate = baseTemplate.replace("${summary.tests}", summary.get("totalTests"));
			baseTemplate = baseTemplate.replace("${summary.errors}", summary.get("totalErrors"));
			baseTemplate = baseTemplate.replace("${summary.failures}", summary.get("totalFailures"));
			baseTemplate = baseTemplate.replace("${summary.skipped}", summary.get("totalSkipped"));
			baseTemplate = baseTemplate.replace("${summary.success}", summary.get("totalPercentage")+"%");
			baseTemplate = baseTemplate.replace("${summary.time}",  Utils.calculateTime(Float.parseFloat(summary.get("totalElapsedTime").replace(",",""))));
			baseTemplate = baseTemplate.replace("${testsPerPage}", config.getValue("testResultsReport.testsPerPage"));
			StringBuilder packageMarkup = new StringBuilder();
			StringBuilder packageTableMarkup = new StringBuilder();
			StringBuilder summaryTests = new StringBuilder();
			StringBuilder tableInits = new StringBuilder();
			String packageTemplate = readFile(PACKAGE_TEMPLATE_PATH, Charset.defaultCharset());
			String packageTableTemplate = readFile(PACKAGE_TABLE_TEMPLATE_PATH, Charset.defaultCharset());
			String dataTableInitTempalte = readFile(DATA_TABLE_INIT_TEMPLATE_PATH, Charset.defaultCharset());
			String entryTemplate = readFile("./resources/html/videoEntryTemplate.html", Charset.defaultCharset());
			String entryFailTemplate = readFile("./resources/html/failedEntryTemplate.html", Charset.defaultCharset());
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
				pkgTableTemplate = pkgTableTemplate.replace("${package.time}", Utils.calculateTime(Float.parseFloat(summaryForPackage.get("totalElapsedTime").replace(",",""))));
				
				pkgTemplate = pkgTemplate.replace("${packageId}", pkg.replace(".", ""));
				pkgTemplate = pkgTemplate.replace("${package.name}", pkg);

				StringBuilder classTableInitMarkup = new StringBuilder();
				String classTemplate = readFile(PACKAGE_TEMPLATE_PATH, Charset.defaultCharset());
				String classTableTemplate = readFile(PACKAGE_TABLE_TEMPLATE_PATH, Charset.defaultCharset());
				String classDataTableInitTempalte = readFile(DATA_TABLE_INIT_TEMPLATE_PATH, Charset.defaultCharset());
				
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
					String testTemplate = readFile(TEST_TEMPLATE_PATH, Charset.defaultCharset());
					String baseListItemTemplate = readFile(PACKAGE_TEMPLATE_PATH, Charset.defaultCharset());
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
			FileWriter fstream = new FileWriter(createFile(config.getValue(
					"testResultsReportPath", CANDYBEAN_REPORT_PATH), true));
			BufferedWriter out = new BufferedWriter(fstream);
			out.write(baseTemplate);
			out.close();
		}else{
			logger.warning("No surefire XML reports found in the surefire results directory, skipping html report generation");
			return;
		}
	}
	
	/**
	 * Reads the contents of a file
	 * @param path Path to file
	 * @param encoding Encoding of the file
	 * @return The contents of the file
	 * @throws IOException
	 */
	private String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return encoding.decode(ByteBuffer.wrap(encoded)).toString();
	}
	
	/**
	 * Creates a file at the given path
	 * @param path
	 * @return
	 * @throws IOException
	 */
	private File createFile(String path, boolean replace) throws IOException{
		File f = new File(path);
		if (!f.getParentFile().exists()) {
			f.getParentFile().mkdirs();
		}
		if (f.exists()) {
			if (replace) {
				f.delete();
				f.createNewFile();
			}
		} else {
			f.createNewFile();
		}
		return f;
	}
	
}


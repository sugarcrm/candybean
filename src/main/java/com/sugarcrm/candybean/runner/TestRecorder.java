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
import org.apache.maven.plugins.surefire.report.ReportTestCase;
import org.apache.maven.plugins.surefire.report.ReportTestSuite;
import org.apache.maven.plugins.surefire.report.SurefireReportParser;
import org.apache.maven.reporting.MavenReportException;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.monte.screenrecorder.ScreenRecorder;
import com.google.common.html.HtmlEscapers;
import com.sugarcrm.candybean.automation.Candybean;
import com.sugarcrm.candybean.configuration.Configuration;
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

	/**
	 * A custom {@link ScreenRecorder} used to capture a video of the screen.
	 * The {@link SpecializedScreenRecorder} can be used to configure specific
	 * details of the recorded files such as location and name.
	 */
	private SpecializedScreenRecorder screenRecorder;

	/**
	 * Failed state of the current test
	 */
	private boolean testFailed = false;

	/**
	 * Logger of the current test class
	 */
	private Logger logger;

	/**
	 * The last failed test
	 */
	private Failure failure;

	/**
	 * The JAXBContext entry point for marshaling TestFailure objects to XML
	 */
	private JAXBContext context;

	/**
	 * File containing the details of the failed tests
	 */
	private File xmlFile;

	/**
	 * Candybean configuration
	 */
	private Configuration config;

	/**
	 * The default file location to store test results in XML
	 */
	private static final String FAILED_TEST_RESULTS_XML = "./log/failedTestResults.xml";

	/**
	 * The default file location to create the html report of the failed test
	 * results
	 */
	private static final String FAILED_RECORDING_REPORT_HTML = "./log/FailedRecordingReport.html";

	/**
	 * The default file location of the surefire results directory
	 */
	private static final String SUREFIRE_RESULTS_DIRECTORY = "./target/surefire-reports";
	
	/**
	 * The default path of where the candybean test results report will be generated
	 */
	private static final String CANDYBEAN_REPORT_PATH = "./log/candybeanTestResults.html";

	public TestRecorder() throws SecurityException, IOException, JAXBException {
		super();
		context = JAXBContext.newInstance(FailedTests.class);
		String candybeanConfigStr = System
				.getProperty(Candybean.CONFIG_KEY);
		if (candybeanConfigStr == null)
			candybeanConfigStr = Candybean.CONFIG_DIR.getCanonicalPath()
					+ File.separator + Candybean.CONFIG_FILE_NAME;
		config = new Configuration(new File(
				Utils.adjustPath(candybeanConfigStr)));
		xmlFile = new File(config.getValue("testResultsXMLPath",
				FAILED_TEST_RESULTS_XML));
		xmlFile.delete();
		xmlFile.createNewFile();
	}

	@Override
	public void testStarted(Description description) throws Exception {
		Record record = description.getAnnotation(Record.class);
		logger = Logger.getLogger(description.getTestClass().getSimpleName());
		this.testFailed = false;
		// Check to see if this test is annotated with Record
		if (record != null) {
			logger.info("Recording started: " + description.getClassName()
					+ "." + description.getMethodName());
			// Start the recording
			startRecording(description.getClassName() + "-"
					+ description.getMethodName());
		}
	}

	@Override
	public void testFinished(Description description) throws Exception {
		Record record = description.getAnnotation(Record.class);
		if (record != null) {
			Duration duration = record.duration();
			logger.info("Recording ended: " + description.getClassName() + "."
					+ description.getMethodName());
			// Stop the recording
			stopRecording();
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
				failedTests.getFailures().add(failedTest);
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
		generateRecordingsReport();
		generateTestResultsReport();
		super.testRunFinished(result);
	}

	/**
	 * Generates the custom candybean report for all test results
	 * @throws MavenReportException
	 * @throws IOException
	 */
	private void generateTestResultsReport() throws MavenReportException, IOException {
		File reportsDirectory = new File(config.getValue("surefireResultsDirectory",SUREFIRE_RESULTS_DIRECTORY));
		if(reportsDirectory.exists()){
			List<File> reportsDirectories = new ArrayList<File>();
			reportsDirectories.add(reportsDirectory);
			SurefireReportParser report = new SurefireReportParser( reportsDirectories, Locale.ENGLISH );
			List<ReportTestSuite> suites = report.parseXMLReportFiles();
			Map<String, String> summary = report.getSummary(suites);
			Map<String, List<ReportTestSuite>> packages = report.getSuitesGroupByPackage(suites);
			String baseTemplate = readFile("./resources/static/html/testResultsTemplate.html", Charset.defaultCharset());
			baseTemplate = baseTemplate.replace("${summary.tests}", summary.get("totalTests"));
			baseTemplate = baseTemplate.replace("${summary.errors}", summary.get("totalErrors"));
			baseTemplate = baseTemplate.replace("${summary.failures}", summary.get("totalFailures"));
			baseTemplate = baseTemplate.replace("${summary.skipped}", summary.get("totalSkipped"));
			baseTemplate = baseTemplate.replace("${summary.success}", summary.get("totalPercentage"));
			baseTemplate = baseTemplate.replace("${summary.time}", summary.get("totalElapsedTime"));
			StringBuilder packageMarkup = new StringBuilder();
			String packageTemplate = readFile("./resources/static/html/packageTemplate.html", Charset.defaultCharset());
			for(String pkg: packages.keySet()){
				String pkgTemplate = packageTemplate;
				List<ReportTestSuite> packageSuites = packages.get(pkg);
				Map<String, String> summaryForPackage = report.getSummary(packageSuites);
				pkgTemplate = pkgTemplate.replace("${packageCollapseId}", pkg.replace(".", ""));
				pkgTemplate = pkgTemplate.replace("${package}", pkg);
				pkgTemplate = pkgTemplate.replace("${package.tests}", summaryForPackage.get("totalTests"));
				pkgTemplate = pkgTemplate.replace("${package.errors}", summaryForPackage.get("totalErrors"));
				pkgTemplate = pkgTemplate.replace("${package.failures}", summaryForPackage.get("totalFailures"));
				pkgTemplate = pkgTemplate.replace("${package.skipped}", summaryForPackage.get("totalSkipped"));
				pkgTemplate = pkgTemplate.replace("${package.success}", summaryForPackage.get("totalPercentage"));
				pkgTemplate = pkgTemplate.replace("${package.time}", summaryForPackage.get("totalElapsedTime"));
				StringBuilder classMarkup = new StringBuilder();
				String classTemplate = readFile("./resources/static/html/classTemplate.html", Charset.defaultCharset());
				for(ReportTestSuite testSuite: packageSuites){
					String clsTemplate = classTemplate;
					String className = testSuite.getName();
					int numberOfTests = testSuite.getNumberOfTests();
					int numberOfErrors = testSuite.getNumberOfErrors();
					int numberOfFailures = testSuite.getNumberOfFailures();
					int numberOfSkipped = testSuite.getNumberOfSkipped();
					clsTemplate = clsTemplate.replace("${classCollapseId}", className);
					clsTemplate = clsTemplate.replace("${class}", className);
					clsTemplate = clsTemplate.replace("${class.tests}", String.valueOf(testSuite.getNumberOfTests()));
					clsTemplate = clsTemplate.replace("${class.errors}", String.valueOf(testSuite.getNumberOfErrors()));
					clsTemplate = clsTemplate.replace("${class.failures}", String.valueOf(testSuite.getNumberOfFailures()));
					clsTemplate = clsTemplate.replace("${class.skipped}", String.valueOf(testSuite.getNumberOfSkipped()));
					clsTemplate = clsTemplate.replace("${class.success}", String.valueOf(report.computePercentage(numberOfTests, numberOfErrors, numberOfFailures, numberOfSkipped)));
					clsTemplate = clsTemplate.replace("${class.time}", String.valueOf(testSuite.getTimeElapsed()));
					StringBuilder testMarkup = new StringBuilder();
					String testTemplate = readFile("./resources/static/html/testTemplate.html", Charset.defaultCharset());
					for(ReportTestCase testCase: testSuite.getTestCases()){
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
						}
						tstTemplate = tstTemplate.replace("${test.name}", testCase.getName());
						float time = testCase.getTime();
						if(time == 0){
							tstTemplate = tstTemplate.replace("${test.runtime}", "Did not run");
						}else{
							tstTemplate = tstTemplate.replace("${test.runtime}", String.valueOf(testCase.getTime()));
						}
						testMarkup.append(tstTemplate);
					}
					clsTemplate = clsTemplate.replace("${testMarkup}", testMarkup.toString());
					classMarkup.append(clsTemplate);
				}
				pkgTemplate = pkgTemplate.replace("${classMarkup}", classMarkup.toString());
				packageMarkup.append(pkgTemplate);
			}
			baseTemplate = baseTemplate.replace("${packageMarkup}", packageMarkup.toString());
			FileWriter fstream = new FileWriter(config.getValue(
					"testResultsReportPath", CANDYBEAN_REPORT_PATH));
			BufferedWriter out = new BufferedWriter(fstream);
			out.write(baseTemplate);
			out.close();
		}else{
			logger.warning("No surefire XML reports found in the surefire results directory, skipping html report generation");
			return;
		}
	}

	/**
	 * Generates candybean report of all failing test with recordings
	 * @throws JAXBException
	 * @throws IOException
	 */
	private void generateRecordingsReport() throws JAXBException, IOException {
		StringBuilder builder = new StringBuilder();
		builder.append("<h1>Failed Test Recordings</h1>");
		builder.append("<table>");
		Unmarshaller unmarshaller = context.createUnmarshaller();
		FailedTests failedTests;
		try {
			failedTests = (FailedTests) unmarshaller.unmarshal(xmlFile);
		} catch (UnmarshalException e) {
			// The file doesn't contain any previous results, so we will start
			// with a clean file.
			failedTests = new FailedTests();
		}

		for (TestFailure failure : failedTests.getFailures()) {
			builder.append("<tr>");
			builder.append("<td style='border: 1px solid black;'>");
			builder.append("<b>" + failure.getTestHeader() + "</b>");
			builder.append("<br>");
			builder.append("<br>");
			builder.append("<div style='color:red;'>"
					+ HtmlEscapers.htmlEscaper().escape(failure.getTrace())
					+ "</div>");
			builder.append("</td>");
			builder.append("<td style='border: 1px solid black;'>");
			builder.append("<object classid='clsid:02BF25D5-8C17-4B23-BC80-D3488ABDDC6B' codebase='http://www.apple.com/qtactivex/qtplugin.cab'"
					+ "><param name='src' value='"
					+ failure.getPathToVideo()
					+ "'><param name='autoplay' value='true'><param name='type' value='video/quicktime'>"
					+ "<embed src='"
					+ failure.getPathToVideo()
					+ "' autoplay='false' scale='aspect' type='video/quicktime' pluginspage='http://www.apple.com/quicktime/download/'></embed></object>");
			builder.append("<br>");
			builder.append("<a href='file:///" + failure.getPathToVideo()
					+ "'>Link to recording</a>");
			builder.append("</td>");
			builder.append("</tr>");
		}
		builder.append("</table>");
		FileWriter fstream = new FileWriter(config.getValue(
				"testResultsHtmlPath", FAILED_RECORDING_REPORT_HTML));
		BufferedWriter out = new BufferedWriter(fstream);
		out.write(builder.toString());
		out.close();
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
}


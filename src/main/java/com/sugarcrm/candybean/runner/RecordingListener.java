package com.sugarcrm.candybean.runner;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.util.List;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import com.sugarcrm.candybean.automation.Candybean;
import com.sugarcrm.candybean.configuration.Configuration;
import com.sugarcrm.candybean.utilities.SpecializedScreenRecorder;
import com.sugarcrm.candybean.utilities.Utils;
import com.sugarcrm.candybean.utilities.reporting.FailedTests;
import com.sugarcrm.candybean.utilities.reporting.TestFailure;

public class RecordingListener implements ITestListener {
	
	/*
	 * A custom {@link ScreenRecorder} used to capture a video of the screen.
	 * The {@link SpecializedScreenRecorder} can be used to configure specific
	 * details of the recorded files such as location and name.
	 */
	private SpecializedScreenRecorder screenRecorder;

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
	 * 
	 */
	private boolean recordInProgress;

	/*
	 * 
	 */
	private Logger logger;

	/*
	 * The default file location to store test results in XML
	 */
	private static final String FAILED_TEST_RESULTS_XML = "./target/candybean-reports/results/failedTestResults.xml";

	@Override
	public void onStart(ITestContext ctx) {
		try {
			this.logger = Logger.getLogger(Candybean.getInstance().getClass().getSimpleName());
			this.context = JAXBContext.newInstance(FailedTests.class);
			String candybeanConfigStr = System.getProperty(Candybean.CONFIG_KEY, Candybean.DEFAULT_CONFIG_FILE);
			this.config = new Configuration(new File(candybeanConfigStr));
			this.xmlFile = Utils.createFile(config.getValue("testResultsXMLPath", FAILED_TEST_RESULTS_XML), false);
		} catch (Exception e) {
			logger.severe(e.getMessage());
		}
	}

	@Override
	public void onTestFailure(ITestResult result) {
		try {
			testFinished(result);
		} catch (Exception e) {
			logger.severe(e.getMessage());
		}
	}
	

	@Override
	public void onTestSuccess(ITestResult result) {
		try {
			testFinished(result);
		} catch (Exception e) {
			logger.severe(e.getMessage());
		}
	}
	
	public void testFinished(ITestResult result) throws Exception {
		Record record = result.getMethod().getConstructorOrMethod().getMethod().getAnnotation(Record.class);
		String className = result.getTestClass().getName();
		String methodName = result.getMethod().getMethodName();
		boolean testFailed = result.getStatus() == ITestResult.FAILURE;
		if (record != null && recordInProgress) {
			Duration duration = record.duration();
			logger.info("Recording ended: " + className + "."
					+ methodName);
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
				} catch (Exception e) {
					// The file doesn't contain any previous results, so we will
					// start with a clean file.
					failedTests = new FailedTests();
				}
				Throwable failure = result.getThrowable();
				failedTest.setClassName(className);
				failedTest.setMethodName(methodName);
				failedTest.setPathToVideo(createdVideoFile.getCanonicalPath());
				failedTest.setTestHeader(className+"."+methodName);
				failedTest.setTrace(failure.getStackTrace().toString());
				failedTest.setFailMessage(failure.getMessage());
				failedTests.getFailures().put(failedTest.getTestHeader(),failedTest);
				Marshaller marshaller = context.createMarshaller();
				marshaller.marshal(failedTests, xmlFile);
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
	public void onTestStart(ITestResult ctx) {
		Record record = ctx.getMethod().getConstructorOrMethod().getMethod().getAnnotation(Record.class);
		// Check to see if this test is annotated with Record
		if (record != null) {
			String className = ctx.getTestClass().getName();
			String methodName = ctx.getMethod().getMethodName();
			if(!recordInProgress){
				logger.info("Recording started: " + className+ "." + methodName);
				// Start the recording
				try {
					startRecording(className + "-" + methodName);
				} catch (Exception e) {
					logger.severe("Unable to start recording");
					logger.severe(e.getMessage());
				}
				recordInProgress = true;
			}else{
				logger.info("Record already in progress");
			}
		}
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
	public void onTestSkipped(ITestResult arg0) {
		// This use of this callback is not required for test recording listener
	}

	@Override
	public void onFinish(ITestContext ctx) {
		// This use of this callback is not required for test recording listener
	}
	
	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult arg0) {
		// This use of this callback is not required for test recording listener
	}
	
}


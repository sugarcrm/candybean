package com.sugarcrm.candybean.runner;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.util.List;
import java.util.logging.Logger;

import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.monte.screenrecorder.ScreenRecorder;

import com.sugarcrm.candybean.automation.Candybean;
import com.sugarcrm.candybean.configuration.Configuration;
import com.sugarcrm.candybean.utilities.SpecializedScreenRecorder;
import com.sugarcrm.candybean.utilities.Utils;

/**
 * A custom {@link RunListener} which includes callback routines for when any {@link Test} annotated
 * with {@link Record} is started. The listener will record any failing tests.
 * 
 * @author Shehryar Farooq
 */
public class TestRecorder extends RunListener {

	/**
	 * A custom {@link ScreenRecorder} used to capture a video of the screen. The {@link SpecializedScreenRecorder} can
	 * be used to configure specific details of the recorded files such as location and name.
	 */
	private SpecializedScreenRecorder screenRecorder;
	
	/**
	 * Failed state of the current test
	 */
	private boolean testFailed = false;

	private Logger logger;


	@Override
	public void testStarted(Description description) throws Exception {
		Record record = description.getAnnotation(Record.class);
		logger = Logger.getLogger(description.getTestClass().getSimpleName());
		this.testFailed = false;
		// Check to see if this test is annotated with Record
		if (record != null) {
			logger.info("Recording started: "
					+ description.getClassName() + "." + description.getMethodName());
			// Start the recording
			startRecording(description.getClassName()+"-"+description.getMethodName());
		}
	}

	@Override
	public void testFinished(Description description) throws Exception {
		Record record = description.getAnnotation(Record.class);
		if (record != null) {
			Duration duration = record.duration();
			logger.info("Recording ended: "
					+ description.getClassName() + "." + description.getMethodName());
			// Stop the recording
			stopRecording();
			// If the test failed, and user configured to record final moments, cut the recording
			if(duration.equals(Duration.FINAL) ||
					(duration.equals(Duration.FINAL_FAILED) && testFailed)){
				logger.info("TODO: Cut the recording to its final few seconds");
				//TODO: Cut the recording to final moments
			}else if(duration.equals(Duration.FINAL_FAILED) && !testFailed){
				logger.info("Test passed, but we are only recording failed tests, deleting recording");
				// If the test didn't fail and we are only recording final moments of a failed test, delete the recording.
				List<File> recordedTests = this.screenRecorder.getCreatedMovieFiles();
				if(recordedTests.size() > 0) {
					File createdVideoFile = recordedTests.get(recordedTests.size()-1);
					createdVideoFile.delete();
				}
			}
		}
	}
	
	@Override
	public void testFailure(Failure failure) throws Exception {
		this.testFailed = true;
		super.testFailure(failure);
	}

	/**
	 * Starts a recording of the screen
	 * @param testFileName The name of the video file to create
	 * @throws Exception
	 */
	private void startRecording(String testFileName) throws Exception {
		GraphicsConfiguration gc = GraphicsEnvironment
				.getLocalGraphicsEnvironment().getDefaultScreenDevice()
				.getDefaultConfiguration();

		String candybeanConfigStr = System.getProperty(Candybean.CONFIG_KEY);
		if (candybeanConfigStr == null)
			candybeanConfigStr = Candybean.CONFIG_DIR.getCanonicalPath() + File.separator
					+ Candybean.CONFIG_FILE_NAME;
		Configuration config = new Configuration(new File(Utils.adjustPath(candybeanConfigStr)));
		this.screenRecorder = new SpecializedScreenRecorder(gc, testFileName, config);
		this.screenRecorder.start();

	}

	/**
	 * Stops the current recording
	 * @throws Exception
	 */
	private void stopRecording() throws Exception {
		this.screenRecorder.stop();
	}

}

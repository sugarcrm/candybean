package com.sugarcrm.candybean.runner;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.util.List;
import java.util.logging.Logger;

import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

import com.sugarcrm.candybean.utilities.SpecializedScreenRecorder;

public class TestRecorder extends RunListener {

	private SpecializedScreenRecorder screenRecorder;
	
	private boolean testFailed = false;
	
	private Logger logger;

	@Override
	public void testStarted(Description description) throws Exception {
		Record record = description.getAnnotation(Record.class);
		logger = Logger.getLogger(description.getTestClass().getSimpleName());
		this.testFailed = false;
		if (record != null) {
			logger.info("Recording started: "
					+ description.getClassName() + "." + description.getMethodName());
			startRecording(description.getClassName()+"-"+description.getMethodName());
		}
	}

	@Override
	public void testFinished(Description description) throws Exception {
		Record record = description.getAnnotation(Record.class);
		if (record != null) {
			logger.info("Recording ended: "
					+ description.getClassName() + "." + description.getMethodName());
			stopRecording();
			if (!testFailed) {
				List<File> recordedTests = this.screenRecorder.getCreatedMovieFiles();
				File createdVideoFile = recordedTests.get(recordedTests.size()-1);
				createdVideoFile.delete();
			}
		}
	}
	
	@Override
	public void testFailure(Failure failure) throws Exception {
		this.testFailed = true;
		super.testFailure(failure);
	}

	private void startRecording(String testFileName) throws Exception {
		GraphicsConfiguration gc = GraphicsEnvironment
				.getLocalGraphicsEnvironment().getDefaultScreenDevice()
				.getDefaultConfiguration();

		this.screenRecorder = new SpecializedScreenRecorder(gc, testFileName);
		this.screenRecorder.start();

	}

	private void stopRecording() throws Exception {
		this.screenRecorder.stop();
	}

}

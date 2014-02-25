package com.sugarcrm.candybean.configuration;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.junit.Before;
import org.junit.Test;

import com.sugarcrm.candybean.test.AbstractTest;

/**
 * Logger unit test that checks to see if the logging configuration is always reading from the default configuration file.
 * @author Shehryar Farooq
 */
public class LoggerUnitTest extends AbstractTest{
	
public LoggerUnitTest() throws Exception {
		super();
	}

private Logger logger;

	/**
	 * A system test for the default configured logger.
	 * 
	 * @throws IOException
	 * @throws SecurityException
	 */
	@Test
	public void defaultConfiguredLogger() throws SecurityException, IOException {
		// We remove the system property that tells the logger where to
		// configure the logger from.
		System.clearProperty("java.util.logging.config.file");
		LogManager.getLogManager().reset();
		logger = Logger.getLogger("DefaultLogger");
		// The default configured logger should have no file handlers, so we
		// check to see if that is the case.
		assertEquals(logger.getHandlers().length, 0);
		File file = new File("./log/" + LoggerUnitTest.class.getSimpleName()+".log");
		file.delete();
		assertTrue(!file.exists());
	}

	/**
	 * A system test for a logger configured using the candybean logger
	 * configuration.
	 * 
	 * @throws Exception
	 */
	@Test
	public void cbConfiguredLogger() throws Exception {
		// Load the initial properties from the candybean config file
        Properties initialProperties = new Properties();
		initialProperties.load(new FileInputStream("./config/candybean.config"));
		// Change the Formatter for the FileHandler to XMLFormatter
		initialProperties.setProperty("java.util.logging.FileHandler.formatter", "java.util.logging.XMLFormatter");
		initialProperties.setProperty("java.util.logging.ConsoleHandler.formatter", "java.util.logging.XMLFormatter");
		// Create a new config file to copy the properties to
		File firstTestConfig = new File("./config/candybean1.config");
		firstTestConfig.createNewFile();
		// Write the new properties to the first test config file
		initialProperties.store(new FileOutputStream(firstTestConfig), null);
		// Update the system property that specifies where to load the logging configuration from.
		System.setProperty("java.util.logging.config.file", "./config/candybean1.config");
		LogManager.getLogManager().readConfiguration();
		logger = Logger.getLogger(this.getClass().getSimpleName());
		File firstLogFile = new File("./log/FirstLogTest.log");
		FileHandler firstFileHanler = new FileHandler("./log/FirstLogTest.log");
		logger.addHandler(firstFileHanler);
		logger.info("First logged message configured using candybean configuration file");
		assertTrue(firstLogFile.exists());
		assertEquals(getLinesInLogFile(firstLogFile), 14);
		// Change the Formatter for the FileHandler to SimpleFormatter
		initialProperties.setProperty("java.util.logging.FileHandler.formatter", "java.util.logging.SimpleFormatter");
		initialProperties.setProperty("java.util.logging.ConsoleHandler.formatter", "java.util.logging.SimpleFormatter");
		// Create a second config file to copy the properties to
		File secondTestConfig = new File("./config/candybean2.config");
		secondTestConfig.createNewFile();
		// Write the new properties to the first test config file
		initialProperties.store(new FileOutputStream(secondTestConfig), null);
		// Update the system property that specifies where to load the logging configuration from.
		System.setProperty("java.util.logging.config.file", "./config/candybean2.config");
		LogManager.getLogManager().readConfiguration();
		logger = Logger.getLogger(this.getClass().getSimpleName());
		File secondLogFile = new File("./log/SecondLogTest.log");
		FileHandler secondFileHandler = new FileHandler("./log/SecondLogTest.log");
		logger.addHandler(secondFileHandler);
		
		logger.info("Second logged message configured using different candybean configuration file");
		assertTrue(secondLogFile.exists());
		assertTrue(getLinesInLogFile(secondLogFile) < 13);
		
		// Reset the logging config file path to the default and re-read the configuration
		System.setProperty("java.util.logging.config.file", "./config/candybean.config");
		LogManager logManager = LogManager.getLogManager();
		logManager.readConfiguration();
		// Delete all created configuration and log files
		firstTestConfig.delete();
		firstLogFile.delete();
		secondTestConfig.delete();
		secondLogFile.delete();
	}

	/**
	 * Reads a file to see how many lines it contains.
	 * 
	 * @param file
	 *            The file to read
	 * @return The number of lines in the file
	 * @throws IOException
	 */
	private int getLinesInLogFile(File file) throws IOException {
		int counter = 0;
		BufferedReader logReader = new BufferedReader(new FileReader(file));
		@SuppressWarnings("unused")
		String line;
		while ((line = logReader.readLine()) != null)
			counter++;
		logReader.close();
		return counter;
	}

	@Override
	@Before
	public void instantiateInterface() throws Exception {
	}

}

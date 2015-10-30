package com.sugarcrm.candybean.configuration;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.junit.BeforeClass;
import org.junit.Test;

import com.sugarcrm.candybean.automation.Candybean;
import com.sugarcrm.candybean.utilities.Utils;

/**
 * Logger unit test that checks to see if the logging configuration is always reading from the default configuration file.
 * 
 * @author Shehryar Farooq
 */
public class LoggerUnitTest {
	
	private static Candybean candybean;
	private Logger logger = Logger.getLogger(LoggerUnitTest.class.getSimpleName());
	
	@BeforeClass
	public static void setUp() throws Exception {
		String candybeanConfigStr = System.getProperty(Candybean.CONFIG_KEY, Candybean.getDefaultConfigFile());
		Configuration candybeanConfig = new Configuration(new File(Utils.adjustPath(candybeanConfigStr)));
		candybean = Candybean.getInstance(candybeanConfig);
	}

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
		this.logger = Logger.getLogger("SomeLogger");
		// The logger should have no file handlers, so we
		// check to see if that is the case.
		assertEquals(this.logger.getHandlers().length, 0);
		File file = new File("./log/" + LoggerUnitTest.class.getSimpleName() + ".log");
		file.deleteOnExit();
	}

	/**
	 * A system test for a logger configured using the candybean logger
	 * configuration.
	 * 
	 * @throws Exception
	 */
	@Test
	public void cbConfiguredLogger() throws Exception {
		String name1 = this.getClass().getSimpleName() + "1";
		String name2 = this.getClass().getSimpleName() + "2";
		String config1Path = Candybean.ROOT_DIR + File.separator + name1 + ".config";
		String config2Path = Candybean.ROOT_DIR + File.separator + name2 + ".config";
		String log1Path = Candybean.ROOT_DIR + File.separator + "log" + File.separator + name1 + ".log";
		String log2Path = Candybean.ROOT_DIR + File.separator + "log" + File.separator + name2 + ".log";
		
		// Load the initial properties from the candybean config file
        Properties initialProperties = candybean.config.getPropertiesCopy();
		
		// Change the FileHandler Formatter to XMLFormatter
		initialProperties.setProperty("java.util.logging.FileHandler.formatter", "java.util.logging.XMLFormatter");
		initialProperties.setProperty("java.util.logging.ConsoleHandler.formatter", "java.util.logging.XMLFormatter");
		
		// Create a new config file and write props to that file
		File config1File = new File(config1Path);
		config1File.createNewFile();
		initialProperties.store(new FileOutputStream(config1File), null);

		// Update the system property that specifies where to load the logging configuration from.
		System.setProperty("java.util.logging.config.file", config1Path);
		LogManager.getLogManager().readConfiguration();
		logger = Logger.getLogger(this.getClass().getSimpleName());
		
		// Log to file and verify text
		File log1File = new File(log1Path);
		FileHandler firstFileHandler = new FileHandler(log1Path);
		logger.addHandler(firstFileHandler);
		logger.info("First logged message configured using candybean configuration file");
		assertTrue(log1File.exists());
		assertEquals(getLinesInLogFile(log1File), 14);
		
		// Change the FileHandler Formatter to SimpleFormatter
		initialProperties.setProperty("java.util.logging.FileHandler.formatter", "java.util.logging.SimpleFormatter");
		initialProperties.setProperty("java.util.logging.ConsoleHandler.formatter", "java.util.logging.SimpleFormatter");
		
		// Create a second config file and write props to that file
		File config2File = new File(config2Path);
		config2File.createNewFile();
		initialProperties.store(new FileOutputStream(config2File), null);

		// Update the system property that specifies where to load the logging configuration from.
		System.setProperty("java.util.logging.config.file", config2Path);
		LogManager.getLogManager().readConfiguration();
		logger = Logger.getLogger(this.getClass().getSimpleName());
		
		// Log to file and verify text
		File log2File = new File(log2Path);
		FileHandler secondFileHandler = new FileHandler(log2Path);
		logger.addHandler(secondFileHandler);
		logger.info("Second logged message configured using different candybean configuration file");
		assertTrue(log2File.exists());
		assertTrue(getLinesInLogFile(log2File) < 13);
		
		// Reset the logging config file path to the default and re-read the configuration
		System.setProperty("java.util.logging.config.file", candybean.config.configFile.getCanonicalPath());
		LogManager logManager = LogManager.getLogManager();
		logManager.readConfiguration();
		
		// Delete all created configuration and log files
		config1File.delete();
		log1File.delete();
		config2File.delete();
		log2File.delete();
	}

	/**
	 * Reads a file to see how many lines it contains.
	 * 
	 * @param file	The file to read
	 * @return 		The number of lines in the file
	 * @throws 		IOException
	 */
	private int getLinesInLogFile(File file) throws IOException {
		int counter = 0;
		BufferedReader logReader = new BufferedReader(new FileReader(file));
		@SuppressWarnings("unused")
		String line;
		while ((line = logReader.readLine()) != null) {
			counter++;
		}
		logReader.close();
		return counter;
	}
}

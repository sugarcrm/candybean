package com.sugarcrm.candybean.configuration;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.junit.Test;
import com.sugarcrm.candybean.test.AbstractTest;

public class LoggerConfigurationTest extends AbstractTest{


	/**
	 * A system test for the default configured logger.
	 * @throws IOException 
	 * @throws SecurityException 
	 */
	@Test
	public void defaultConfiguredLogger() throws SecurityException, IOException{
		// We remove the system property that tells the logger where to configure the logger from.
		System.clearProperty("java.util.logging.config.file");
		LogManager.getLogManager().reset();
		logger = Logger.getLogger("DefaultLogger");
		// The default configured logger should have no file handlers, so we check to see if that is the case.
		assertEquals(logger.getHandlers().length,0);
		File file = new File("./log/LoggerConfigurationTest.log");
		file.delete();
		assertTrue(!file.exists());
	}
	



	/**
	 * A system test for a logger configured using the candybean logger configuration.
	 * @throws Exception 
	 */
	@Test
	public void cbConfiguredLogger() throws Exception{
		assertNotSame(0,logger.getHandlers().length);
		logger.info("First logged message configured using candybean configuration file");
		File logFile = new File("./log/"+this.getClass().getSimpleName()+".log");
		assertTrue(logFile.exists());
		// The candybean configuration pecifies the logger to log in one line, so we check to see if this is the case.
		assertEquals(getLinesInLogFile(logFile),1);
		logFile.delete();
	}
	

	/**
	 * Reads a file to see how many lines it contains.
	 * @param file The file to read
	 * @return The number of lines in the file
	 * @throws IOException 
	 */
	private int getLinesInLogFile(File file) throws IOException {
		int counter = 0;
		BufferedReader logReader = new BufferedReader(new FileReader(file));
		@SuppressWarnings("unused")
		String line;
		while((line = logReader.readLine() ) != null )
			counter++;
		logReader.close();
		return counter;
	}
	
}
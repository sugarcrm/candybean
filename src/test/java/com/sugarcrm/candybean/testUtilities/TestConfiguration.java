package com.sugarcrm.candybean.testUtilities;

import com.sugarcrm.candybean.automation.Candybean;
import com.sugarcrm.candybean.configuration.Configuration;
import com.sugarcrm.candybean.exceptions.CandybeanException;

import java.io.File;
import java.io.IOException;

/**
 * A Utility class to return the test related candybean configuration files
 * @author Eric Tam <etam@sugarcrm.com>
 */
public class TestConfiguration {
	public static Configuration getTestConfiguration(String configFileName) throws IOException, CandybeanException {
		String relativeTestResourcesPath = "src/test/resources/" + configFileName;
		return new Configuration(new File(Candybean.ROOT_DIR, relativeTestResourcesPath));
	}
}

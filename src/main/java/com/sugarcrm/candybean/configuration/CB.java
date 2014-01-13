/**
 * Candybean is a next generation automation and testing framework suite.
 * It is a collection of components that foster test automation, execution
 * configuration, data abstraction, results illustration, tag-based execution,
 * top-down and bottom-up batches, mobile variants, test translation across
 * languages, plain-language testing, and web service testing.
 * Copyright (C) 2013 SugarCRM, Inc. <candybean@sugarcrm.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.sugarcrm.candybean.configuration;

import java.io.File;
import java.io.IOException;

import com.sugarcrm.candybean.automation.Candybean;
import com.sugarcrm.candybean.automation.VInterface;
import com.sugarcrm.candybean.utilities.Utils;

public class CB {

	/**
	 * The root directory of candy bean
	 */
	public static File ROOT_DIR = new File(System.getProperty("user.dir") + File.separator);
	
	/**
	 * The default configuration directory 
	 */
	public static File CONFIG_DIR = new File(System.getProperty("user.dir") + File.separator + "config" + File.separator);
	
	/**
	 * @return The complete path to the candybean configuration file in this JRE
	 * @throws IOException
	 */
	public static String getConfugrationFilePath() throws IOException{
		return CONFIG_DIR.getCanonicalPath() + File.separator + Candybean.CONFIG_FILE_NAME;
	}
	
	/**
	 * Build a VInterface based on default configuration.
	 * @return The VInterface
	 * @throws Exception If default configuration files do not exist.
	 */
	public static VInterface buildInterface() throws Exception{
		
		VInterface iface;
		Candybean candybean;
		String candybeanConfigStr = System.getProperty(Candybean.CONFIG_SYSTEM_PROPERTY);
		if (candybeanConfigStr == null) 
			candybeanConfigStr = CB.CONFIG_DIR.getCanonicalPath() + File.separator + Candybean.CONFIG_FILE_NAME;
		Configuration candybeanConfig = new Configuration(new File(Utils.adjustPath(candybeanConfigStr)));
		candybean = Candybean.getInstance(candybeanConfig);
		iface = candybean.getInterface();
		return iface;
	}
	
}

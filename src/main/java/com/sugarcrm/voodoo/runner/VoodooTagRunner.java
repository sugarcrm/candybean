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
package com.sugarcrm.voodoo.runner;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;

public class VoodooTagRunner extends BlockJUnit4ClassRunner {
	private final String MAC_OS = "MAC";
	private final String WINDOWS_OS = "WINDOWS";
	private final String SOLARIS_OS = "SOLARIS";
	private final String UNIX_OS = "UNIX";

	public VoodooTagRunner(Class<?> klass) throws InitializationError {
		super(klass);
	}

	/**
	 * Extending computeTestMethods to support VoodooTag Annotation
	 * @author Wilson Li
	 */
	@Override
	protected List<FrameworkMethod> computeTestMethods() {
		// Get a List of methods that needs to be tested
		final List<FrameworkMethod> testMethods = getTestClass().getAnnotatedMethods(Test.class);
		// If there are not @Tests then just return an empty List
		if (testMethods == null || testMethods.size() == 0) return testMethods;
		// Checking through all the test methods for platform annotation
		final List<FrameworkMethod> toTestMethods = new ArrayList<FrameworkMethod>(testMethods.size());
		for (final FrameworkMethod method : testMethods) {
			final VoodooTag voodooAnnotation = method.getAnnotation(VoodooTag.class);
			if (voodooAnnotation != null) {
				// Add to toTestMethods list only if indicated OS is compatible with running running OS
				if (isPlatformCompatible(voodooAnnotation.OS())) toTestMethods.add(method);
			} else {
				// Add to toTestMethods list even if method does not contain VoodooTag
				toTestMethods.add(method);
			}
		}
		return toTestMethods;
	}

	/**
	 * Convert the platform(s) provided (String) to a List type
	 * 
	 * @author Wilson Li
	 * @param stringPlatforms
	 * @return
	 */
	private List<String> convertToListofPlatforms(String stringPlatforms) {
		String[] arrayPlatforms = stringPlatforms.split(" ");
		List<String> listOfPlatforms = new ArrayList<String>(arrayPlatforms.length);
		for (String platformName : arrayPlatforms) {
			if (platformNameCheck(platformName.trim())) listOfPlatforms.add(platformName);
			else { 
				System.out.println("[VoodooTagRunner ERROR]: Given unmatched platform name: " + platformName);
				System.exit(-1);
			}
		}
		return listOfPlatforms;
	}

	/**
	 * This is to check if the current running platform matches with the ones
	 * provided under VoodooTag -> OS. If so it will return true and the JUnit test will 
	 * execute, else the test will not be run
	 * 
	 * @author Wilson Li
	 * @param platforms
	 * @return
	 */
	private boolean isPlatformCompatible(String platforms) {
		if (platforms.equals("")) return true; // Default Case
		List<String> listOfPlatforms = convertToListofPlatforms(platforms);
		String myRunningOS = System.getProperty("os.name").toLowerCase().substring(0,3);
		myRunningOS = replaceOSNameWithConstant(myRunningOS);
		if (listOfPlatforms.contains(myRunningOS)) return true;
		return false;
	}

	/**
	 * Check if the values given for the OS() parameter under the VoodooTags matches the
	 * constants provided. This is to avoid typing issues
	 * 
	 * @author Wilson Li
	 * @param platformName
	 * @return
	 */
	private boolean platformNameCheck(String platformName) {
		if (platformName.equals(WINDOWS_OS) 
				|| platformName.equals(MAC_OS) 
				|| platformName.equals(SOLARIS_OS)
				|| platformName.equals(UNIX_OS)) {
			return true;
		}
		return false;
	}

	/**
	 * Replaces the given Operating System's name with our defined constants
	 *  
	 * @author Wilson Li
	 * @param myRunningOS
	 * @return
	 */
	private String replaceOSNameWithConstant(String myRunningOS) {
		switch(myRunningOS) {
		case "win": return WINDOWS_OS;
		case "mac": return MAC_OS;
		case "sun": return SOLARIS_OS;
		default: return UNIX_OS; //	case for 'nix', 'nux' and 'aix'
		}
	}
}

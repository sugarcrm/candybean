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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import org.junit.Test;

/**
 * VTestRunner is a test runner. It is a command line utility for running tests.
 * The test scripts should have been compiled prior to running VTestRunner.
 * 
 * @author Soon Han
 * @author Jon duSaint
 */

public class VTestRunner {
	static final String CLASSFILE_EXTENSION = "class";
	HashMap<String, Pair> beforeClassMethods;
	HashMap<String, Pair> afterClassMethods;
	// Assume annotations appear in current class plus at most one level up.
	HashMap<String, Pair> beforeClassSuperMethods;
	HashMap<String, Pair> afterClassSuperMethods;
	HashMap<String, Pair> beforeMethods;
	HashMap<String, Pair> afterMethods;
	HashMap<String, Pair> testMethods;
	static CL cl = new CL();

	/**
	 * Name: main
	 * 
	 * The input can be a mix of test classes and directories. The items are
	 * space separated. When a directory is given, the files contained therein
	 * are run. An example input is one consisting of a test class and a test
	 * directory, assuming VTestRunner is run from the VoodooGrimoire directory:
	 * "target/test-class/accounts/Accounts_0001 target/test-class/Quotes" The
	 * extension, "class", may be omitted in the input.
	 */
	public static void main(String[] args) {

		ClassLoader.getSystemClassLoader().setDefaultAssertionStatus(true);
		VTestRunner runner = new VTestRunner();
		runner.go(args);
	}

	/**
	 * Start processing the input files.
	 * 
	 * @param args
	 *            the input from the command line (eg. <test class>
	 *            <scripts/directories>)
	 * @return void
	 */
	private void go(String[] args) {
		if (args.length == 0) {
			processDirectory(new File("."));
		} else {
			for (String arg : args) {
				File fileArg = new File(arg);
				if (fileArg.isDirectory()) {
					processDirectory(fileArg);
				} else {
					if (fileArg.exists()
							&& !arg.endsWith("." + CLASSFILE_EXTENSION)) {
						error(arg + " is not a class file");
					} else if (!fileArg.exists()
							&& (new File(arg + "." + CLASSFILE_EXTENSION))
									.exists()) {

						arg += "." + CLASSFILE_EXTENSION;
						try {
							processFile(new File(arg).getCanonicalFile());
						} catch (IOException e) {
							error(e, arg + " is not a valid file");
						}
					} else {
						error(arg + " does not exist and is not a class file");
					}
				}
			}
		}
	}

	/**
	 * Process the given file or directory
	 * 
	 * @param root
	 *            the {@link File} of the directory to process
	 * @return void
	 */
	private void processDirectory(File root) {
		ClassFilesFinder filesFinder = new ClassFilesFinder();

		try {
			Files.walkFileTree(root.toPath(), filesFinder);
		} catch (IOException e) {
			error(e, "Failed in walking directory tree");
		}

		try {
			List<File> files = filesFinder.getFiles();

			for (File file : files) {
				processFile(file.getCanonicalFile());
			}
		} catch (IOException e) {
			error(e, "Failed to getCanonicalFile");
		}
	}

	/**
	 * Processing a regular file passed in as a {@link File}. Each method is
	 * examined for the annotations "Before, After, Test, etc."
	 * 
	 * @param cFile
	 *            a class file
	 * @return void
	 */
	private void processFile(File cFile) {

		beforeClassMethods = new HashMap<String, Pair>();
		afterClassMethods = new HashMap<String, Pair>();
		beforeClassSuperMethods = new HashMap<String, Pair>();
		afterClassSuperMethods = new HashMap<String, Pair>();
		beforeMethods = new HashMap<String, Pair>();
		afterMethods = new HashMap<String, Pair>();
		testMethods = new HashMap<String, Pair>();

		loadCheck(cFile);
		runMethods();
	}

	/**
	 * load class and provide some checks on input validity.
	 * 
	 * @param cFile
	 *            a class file
	 * @return void
	 */
	private void loadCheck(File cFile) {

		Class<?> testClass = loadClass(cFile);

		addMethods(testClass);
		addSuperClassMethods(testClass);

		String cName = testClass.getCanonicalName();

		if (!cName.contains(".")) {
			log(cName + " is not a packaged class. Skipped it.");
			return; // want packaged classes only
		}
	}

	/**
	 * Run all the (BeforeClass, AfterClass, Before, After, etc.) annotated
	 * methods
	 * 
	 * @param None
	 * @return void
	 */
	private void runMethods() {
		for (String mName : beforeClassMethods.keySet()) {
			// Run methods in super class which were not overridden
			for (String mNameSuper : beforeClassSuperMethods.keySet()) {
				if (!beforeClassMethods.containsKey(mNameSuper)) {
					runTestMethod(beforeClassSuperMethods.get(mNameSuper).cls,
							beforeClassSuperMethods.get(mNameSuper).method);
				}
			}

			runTestMethod(beforeClassMethods.get(mName).cls,
					beforeClassMethods.get(mName).method);

		}

		for (String mName : testMethods.keySet()) {
			// Run the Before methods
			for (String mNameBefore : beforeMethods.keySet()) {
				runTestMethod(beforeMethods.get(mNameBefore).cls,
						beforeMethods.get(mNameBefore).method);
			}

			runTestMethod(testMethods.get(mName).cls,
					testMethods.get(mName).method);

			// Run the after methods
			for (String mNameAfter : afterMethods.keySet()) {
				runTestMethod(afterMethods.get(mNameAfter).cls,
						afterMethods.get(mNameAfter).method);
			}
		}

		// TODO. Need work to allow for super class deeper than 1 level
		for (String mName : afterClassMethods.keySet()) {
			// Run methods in super class which are not overridden
			for (String mNameSuper : afterClassSuperMethods.keySet()) {
				if (!afterClassMethods.containsKey(mNameSuper)) {
					runTestMethod(afterClassSuperMethods.get(mNameSuper).cls,
							afterClassSuperMethods.get(mNameSuper).method);
				}
			}

			runTestMethod(afterClassMethods.get(mName).cls,
					afterClassMethods.get(mName).method);
		}
	}

	/**
	 * Add methods to hashmaps for methods that are BeforeClass, AfterClass,
	 * Before, After, Test. The hashmaps are global.
	 * 
	 * @param testClass
	 * @return void
	 */
	private void addMethods(Class<?> testClass) {
		for (Method m : testClass.getDeclaredMethods()) {
			// Not sure why the STATIC check doesn't work anymore. Remove check
			if (m.getAnnotation(BeforeClass.class) != null) {
				log("addMethods(): added " + m.getName() + " to beforeClassMethods");
				beforeClassMethods.put(testClass.getCanonicalName(), new Pair(
						testClass, m));
			} else if (m.getAnnotation(AfterClass.class) != null) {
				log("addMethods(): added " + m.getName() + " to afterClassMethods");
				afterClassMethods.put(testClass.getCanonicalName(), new Pair(
						testClass, m));
			} else if (m.getAnnotation(Before.class) != null
					&& m.getReturnType().equals(void.class)) {
				log("addMethods(): added " + m.getName() + " to beforeMethods");
				beforeMethods.put(testClass.getCanonicalName(), new Pair(
						testClass, m));
			} else if (m.getAnnotation(After.class) != null
					&& m.getReturnType().equals(void.class)) {
				log("addMethods(): added " + m.getName() + " to afterMethods");
				afterMethods.put(testClass.getCanonicalName(), new Pair(
						testClass, m));
			} else if (m.getAnnotation(Test.class) != null
					&& m.getReturnType().equals(void.class)) {
				log("addMethods(): added " + m.getName() + " to testMethods");
				testMethods.put(testClass.getCanonicalName(), new Pair(
						testClass, m));
			}
		}
	}

	/**
	 * Add methods to hashmaps for BeforeClassa and AfterClass that are present
	 * in immediate superclass. The hashmaps are global.
	 * 
	 * @param testClass
	 * @return void
	 */
	private void addSuperClassMethods(Class<?> testClass) {
		for (Method m : testClass.getSuperclass().getDeclaredMethods()) {
			if (m.getAnnotation(BeforeClass.class) != null) {
				log("addSuperClassMethods(): added " + m.getName() + " to beforeClassSuperMethods");
				beforeClassSuperMethods.put(testClass.getCanonicalName(),
						new Pair(testClass, m));
			} else if (m.getAnnotation(AfterClass.class) != null) {
				log("addSuperClassMethods(): added " + m.getName()
						+ " to afterClassSuperMethods");
				afterClassSuperMethods.put(testClass.getCanonicalName(),
						new Pair(testClass, m));
			}
		}
	}

	/**
	 * Load an example class from its class file.
	 * 
	 * @param f
	 *            class file
	 */

	private static Class<?> loadClass(File f) {

		Class<?> c = null;

		try {
			c = cl.loadClass(f);
		} catch (FileNotFoundException e) {
			error("Class file '" + f + "' not found: " + e);
		} catch (IOException e) {
			error("Error reading class file '" + f + "': " + e);
		}

		return c;
	}

	/**
	 * @param m
	 *            the method to run
	 * @return void
	 */
	private static void runTestMethod(Class<?> testClass, Method m) {

		Object testObject = createTestObject(testClass);

		try {
			log("runTestMethod(): " + m.getName());
			m.invoke(testObject);
		} catch (IllegalAccessException e) {
			error(e, "Unable to access " + testClass.getName());
		} catch (InvocationTargetException e) {
			Throwable cause = e.getCause();
			if (cause == null) {
				cause = e;
			}
			error(cause, "Exception during method invocation");
		}
	}

	/**
	 * @param testClass
	 *            a Class to create an Object for.
	 * @return Object
	 */
	private static Object createTestObject(Class<?> testClass) {
		Object instance = null;
		try {
			instance = testClass.newInstance();
		} catch (IllegalAccessException e) {
			error(e, "Unable to access " + testClass.getName());
		} catch (InstantiationException e) {
			error(e, "Failed to instantiate "
					+ com.sugarcrm.voodoo.test.Test.class.getName());
		}

		return instance;
	}

	private static void log(String m) {
		// TODO: Can Voodoo class make log() static? If possible, this would
		// allow
		// other components to use log() when they don't necessarily possess a
		// voodoo instance.
		System.out.println(m);
	}

	private static void error(String errm) {
		error((Throwable) null, errm);
	}

	private static void error(Throwable exc, String errm) {
		System.err.println(errm + ":");
		exc.printStackTrace(System.err);
	}

	/**
	 * Class loader used to obtain class name
	 * 
	 * </p>This class loader supports loading classes from class files without
	 * knowing the name of the class in advance.</p>
	 * 
	 * @author Jon duSaint
	 */

	private static class CL extends ClassLoader {
		public Class<?> loadClass(File f) throws FileNotFoundException,
				IOException {
			FileInputStream fin = new FileInputStream(f);
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();

			while (true) {
				int b = fin.read();
				if (b == -1) {
					break;
				}
				buffer.write(b);
			}

			try {
				fin.close();
			} catch (IOException e) {
				error(e, "Failed to close " + fin.getClass().getName());
			} // Unlikely and probably safe to ignore.

			byte bytes[] = buffer.toByteArray();

			return defineClass(null, bytes, 0, bytes.length);
		}
	}

	/**
	 * Container for a Class,Method pair.
	 * 
	 * <p>
	 * The method half of the pair is the entry point of the first method
	 * encountered among the methods having the same annotation.
	 * </p>
	 * 
	 * @author Jon duSaint
	 */

	private class Pair {

		/**
		 * The class field.
		 */

		public Class<?> cls;

		/**
		 * The method field.
		 */

		public Method method;

		/**
		 * Instantiate a Pair with a class and a method.
		 * 
		 * @param cls
		 *            the example class
		 * @param method
		 *            the example method
		 */

		Pair(Class<?> cls, Method method) {
			this.cls = cls;
			this.method = method;
		}
	}
}
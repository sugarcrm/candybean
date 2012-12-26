package com.sugarcrm.voodoo.runner;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.nio.file.Files;
import java.io.*;
import java.util.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * VTestRunner is a test runner. It is a standalone command line utility for running 
 * tests. The test scripts should have been compiled prior to running VTestRunner. 
 * 
 */
public class VTestRunner {
	static Class<?> testClass;
	static TestResults testResults = new TestResults(); // TODO. To remove
	static final String ext = "class";

	/**
	 * Name: main 
	 * 
	 * The input can be a mix of test classes and directories.
	 * The items are space separated. When a directory is given, the files 
	 * contained therein are run. 
	 * Example input consisting of a test class and a test directory, assuming 
	 * VTestRunner is run from the VoodooGrimoire directory:
	 * "target/test-class/accounts/Accounts_0001 target/test-class/Quotes"
	 * The extension, "class", may be omitted in the input. 
	 */
	public static void main(String[] args) throws Exception {

		ClassLoader.getSystemClassLoader().setDefaultAssertionStatus(true);
		new VTestRunner().go(args, ext);

		// TODO. Remove this TestResults() section.
		// It's not how Voodoo2 getting pass/fail.
		// Maybe useful just to retain the number of tests run
		long testsRun = testResults.getNumTestsRun();
		long failures = testResults.getNumFailures();
		List<String> failedTests = testResults.getFailedTests();

		if (failures == 0) {
			System.out.println("OK: " + testsRun + " test" + (testsRun > 1 ? "s" : "") + " (ALL PASSED)");
		} else {
			System.out.println(testsRun + " tests run)");
			System.out.println("\nNot OK: " + failures + " FAILURE"
					+ (failures > 1 ? "S" : ""));
			for (String failed : failedTests) {
				System.out.println("  " + failed);
			}
		}
	}

	/**
	 * Name: go 
	 * 
	 * @param 
	 * args : String[]. The input (test scripts/directories) from the command line. 
	 * ext : String. The extension of file name (class)
	 * @return void
	 */
	private void go(String[] args, String ext) {
		try {
			if (args.length == 0) {
				processDirectory(new File("."));
			} else {
				for (String arg : args) {
					//System.out.println("arg = " + arg);
					File fileArg = new File(arg);
					if (fileArg.isDirectory()) {
						System.out.println(arg + " is a directory");
						processDirectory(fileArg);
					} else {
						if (!arg.endsWith("." + ext)) // allows no ext input
							arg += "." + ext;
						process(new File(arg).getCanonicalFile());
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Name: processDirectory 
	 * Process the given file, possibly a directory.
	 * 
	 * @param 
	 * root : File. The path to a file or a directory 
	 * @return void
	 */
	private static void processDirectory(File root) throws IOException {
		FilesFinder filesFinder = new FilesFinder(ext);
		Files.walkFileTree(root.toPath(), filesFinder);
		List<File> files = filesFinder.getResult();

		for (File file : files) {
			process(file.getCanonicalFile());
		}
	}

	/**
	 * Name: process
	 * Detailed processing a non-directory file.
	 * Here, each method is examined for the annotations "Before, After, Test, etc." 
	 * The methods are processed accordingly in the order specified by the annotations.
	 * 
	 * @param 
	 * root : File. The path to a non-directory file
	 * @return void
	 */
	private static void process(File cFile) {
		try {
			//System.out.println("VTestRunner: process(): "
					//+ cFile.getCanonicalPath());

			String cName = ClassNameGetter.get(cFile.getCanonicalPath());
			// System.out.println("process(): cName = " + cName);
			if (!cName.contains("."))
				return; // want packaged classes only
			testClass = Class.forName(cName);

			//System.out.println("process(): testClass.getName() = "
			//		+ testClass.getName());

		} catch (Exception e) {
			e.printStackTrace();
		}

		TestMethods testMethods = new TestMethods();

		// Assume annotations appear in current class plus at most one level up.
		// To relax constraint later
		Method beforeClass = null;
		Method afterClass = null;
		Method beforeClassSuper = null;
		Method afterClassSuper = null;
		Method before = null;
		Method after = null;

		beforeClass = isClassMethodPresent(beforeClass, testClass,
				BeforeClass.class); // org.junit.BeforeClass
		afterClass = isClassMethodPresent(afterClass, testClass,
				AfterClass.class);

		beforeClassSuper = isClassMethodPresent(beforeClassSuper,
				testClass.getSuperclass(), BeforeClass.class);
		afterClassSuper = isClassMethodPresent(afterClassSuper,
				testClass.getSuperclass(), AfterClass.class);

		before = isBeforeAfterMethodPresent(before, testClass, Before.class);
		after = isBeforeAfterMethodPresent(after, testClass, After.class);

		// TODO. Need work to allow for super class deeper than 1 level
		if (beforeClass != null) {
			runRecurMethod(beforeClass);
		} else {
			runRecurMethod(beforeClassSuper);
		}

		for (Method m : testClass.getDeclaredMethods()) {
			//System.out.println("testClass.getName() = " + testClass.getName()
			//		+ "  m.getName() = " + m.getName());
			testMethods.addTestMethod(m);
		}

		//System.out.println("testMethods.size() = " + testMethods.size());

		for (Method m : testMethods) {
			//System.out.println("Test method = " + m.getName());

			try {
				boolean success = false;

				runRecurMethod(before);

				success = runTestMethod(m);
				testResults.incrementTestRuns();
				testResults.recordResult(m, success);

				runRecurMethod(after);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// TODO. Need work to allow for super class deeper than 1 level
		if (afterClass != null) {
			runRecurMethod(afterClass);
		} else {
			runRecurMethod(afterClassSuper);
		}
	}

	/**
	 * Name: runTestMethod 
	 * Run the method carrying the "Test" annotation. 
	 * A test object is created for the purpose.
	 * 
	 * @param 
	 * m : Method 
	 * @return boolean
	 */
	private static boolean runTestMethod(Method m) throws Exception {
		boolean success = false;
		Object testObject = createTestObject();

		try {
			if (m.getReturnType().equals(boolean.class))
				success = (boolean) m.invoke(testObject);
			else {
				m.invoke(testObject);
				success = true; // if no failed assertion
			}
		} catch (InvocationTargetException e) {
			System.out.println(e.getCause());
		}

		return success;
	}

	/**
	 * Name: runRecurMethod 
	 * Run the method carrying the "Before/After" annotation. 
	 * A test object is created for the purpose.
	 * 
	 * @param 
	 * target : Method 
	 * @return void
	 */
	private static void runRecurMethod(Method target) {
		try {
			if (target != null) {
				Object testObject = createTestObject();
				target.invoke(testObject);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Name: isBeforeAfterMethodPresent 
	 * Check if "Before/After" method is present 
	 * The "Before/After" method is returned if found.
	 * 
	 * @param 
	 * classMethod : Method 
	 * @return Method 
	 */
	private static Method isBeforeAfterMethodPresent(Method classMethod,
			Class<?> testClass, Class<?> clazz) {
		for (Method m : testClass.getDeclaredMethods()) {
			if (classMethod == null) {
				classMethod = checkBeforeAfterMethod(m, clazz);
				// Only one before and/or one after method
				if (classMethod != null) {
					break; // found the before or after method, bail out. There
							// is only one before and/or one after method
				}
			}
		}

		return classMethod;
	}

	@SuppressWarnings("unchecked")
	private static Method checkBeforeAfterMethod(Method m, Class<?> clazz) {
		if (m.getAnnotation((Class<org.junit.Before>) clazz) == null
				&& m.getAnnotation((Class<org.junit.After>) clazz) == null) {
			return null;
		}
		m.setAccessible(true);
		return m;
	}

	/**
	 * Name: isClassMethodPresent 
	 * Check if "Before/After" method is present 
	 * The "BeforeClass/AfterClass" method is returned if found.
	 * 
	 * @param 
	 * classMethod : Method 
	 * @return Method 
	 */
	private static Method isClassMethodPresent(Method classMethod,
			Class<?> testClass, Class<?> clazz) {
		for (Method m : testClass.getDeclaredMethods()) {
			if (classMethod == null) {
				classMethod = checkBeforeAfterClassMethod(m, clazz);
				// There is only one such Class method, either in the class
				// or its super class, but not both. Valid for current Voodoo2.

				if (classMethod != null) {

					//System.out.println("isClassMethodPresent(): " + m.getName()
					//		+ " is a class method");

					break; // found the Class method, bail out
				}
			}
		}

		return classMethod;
	}

	@SuppressWarnings("unchecked")
	private static Method checkBeforeAfterClassMethod(Method m, Class<?> clazz) {
		if (m.getAnnotation((Class<org.junit.After>) clazz) == null
				&& m.getAnnotation((Class<org.junit.After>) clazz) == null) {

			//System.out
				//	.println("checkBeforeAfterClassMethod(): m.getAnnotation(Class<atunit.BeforeClass>) &&  null m.getAnnotation(Class<atunit.AfterClass>) are null");

			return null;
		}

		checkMethodStatic(m);
		//System.out.println("checkBeforeAfterClassMethod(): " + m.getName());

		m.setAccessible(true);
		return m;
	}

	/**
	 * Name: isMethodStatic
	 * Check if the given method is static. 
	 * The "BeforeClass/AfterClass" method must be static 
	 * 
	 * @param 
	 * m : Method 
	 * @return void 
	 */
	private static void checkMethodStatic(Method m) {
		if ((m.getModifiers() & java.lang.reflect.Modifier.STATIC) < 1) {
			throw new RuntimeException("checkMethodStatic(): Class method "
					+ m.getName() + " must be static.");
		}
	}

	/**
	 * Name: checkReturnType 
	 * Check if the given method is static. 
	 * The "BeforeClass/AfterClass" method must be static 
	 * 
	 * @param 
	 * m : Method 
	 * @return void 
	 */
	private static void checkReturnType(Method m) {
		if (!(m.getReturnType().equals(boolean.class) || m.getReturnType()
				.equals(void.class))) {
			throw new RuntimeException("@Test method"
					+ " must return boolean or void");
		}
	}

	/**
	 * Name: createTestObject 
	 * Create an object for the purpose of running the method
	 * 
	 * @param  None
	 * @return Object 
	 */
	private static Object createTestObject() {
		Object instance = null;
		try {
			//System.out.println("createTestObject(): testClass.getName() = "
			//		+ testClass.getName());
			instance = testClass.newInstance();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return instance;
	}

	/**
	 * Class name: TestMethods 
	 * A container class for "Test" methods. 
	 * 
	 */
	@SuppressWarnings("serial")
	private static class TestMethods extends ArrayList<Method> {

		void addTestMethod(Method m) {
			//System.out.println("addTestMethod(): " + m.getName());
			for (Annotation a : m.getAnnotations()) {
				//System.out.println("a.getClass().getName() = "
				//		+ a.annotationType().getName());
			}

			if (m.getAnnotation(Test.class) != null) {
				checkReturnType(m);
				m.setAccessible(true);

				//System.out.println("addTestMethod(): " + m.getName()
				//		+ " added to TestMethods");

				add(m);
			}
		}
	}

	/**
	 * Class name: TestResult
	 * A class for keeping track of test results. 
	 * This class is mainly for developments/debuggging.
	 * It will be removed when the code is stable.
	 * 
	 */
	private static class TestResults {
		static List<String> failedTests = new ArrayList<String>();
		static long testsRun = 0;
		static long failures = 0;

		public void incrementTestRuns() {
			testsRun++;
		}

		public void recordResult(Method m, boolean success) {
			System.out.println(success ? "" : "(failed)");
			if (!success) {
				failures++;
				failedTests.add(testClass.getName() + ": " + m.getName());
			}
		}

		public long getNumTestsRun() {
			return testsRun;
		}

		public long getNumFailures() {
			return failures;
		}

		public List<String> getFailedTests() {
			return failedTests;
		}
	}
}
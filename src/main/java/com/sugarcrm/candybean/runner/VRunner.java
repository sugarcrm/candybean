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
package com.sugarcrm.candybean.runner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.logging.Logger;
import javax.xml.bind.JAXBException;
import org.junit.Test;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerScheduler;
import com.sugarcrm.candybean.automation.Candybean;
import com.sugarcrm.candybean.exceptions.CandybeanException;
import com.sugarcrm.candybean.utilities.CandybeanLogger;

/**
 * Custom JUnit test runner class. When a test is annotated to use this runner,
 * the runner will look at the defined and specified logic to determine if a method
 * will execute.
 * 
 * If a test is listed by SimpleClassName.TestName in a given 'block list' file, that
 * test is not run.  This feature provides extra control for not executing tests that
 * perhaps are known to fail or currently under development.  The block list file path 
 * is specified using the system variable "blocklist" (either camel or lower case).
 *
 */
public class VRunner extends BlockJUnit4ClassRunner {
	
	public static final String BLOCKLIST_PATH_KEY = "blocklist";
	public static final String BLOCKLIST_COMMENT = "#";
	private static int threadCounter = 0;
	private static Logger logger;
	private static Candybean candybean;
	
	public VRunner(Class<?> klass) throws InitializationError, SecurityException, IOException {
		super(klass);
		try {
			candybean = Candybean.getInstance();
			boolean parallelTests = Boolean.parseBoolean(candybean.config.getValue("parallel.enabled","false"));
			if (parallelTests) {
				setScheduler(new NonBlockingAsynchronousRunner(Integer.parseInt(Candybean.getInstance().config.getValue("parallel.threads", "4"))));
			}
		} catch (CandybeanException e1) {
			logger = Logger.getLogger(VRunner.class.getSimpleName());
			logger.severe("Unable to instantiate candybean.");
		}

	}

	// TODO add validation/error checking, flexibility in provided class/method name, etc.
	@Override
	protected List<FrameworkMethod> computeTestMethods() {
		/*
		 * We must always use the candybean configured logger, so we attempt 
		 * to instantiate candybean to retrieve its named logger first
		 */
		try {
			logger = Logger.getLogger(Candybean.getInstance().getClass().getSimpleName());
			((CandybeanLogger)logger).removeAllHandlers();
		} catch (CandybeanException e) {
			logger = Logger.getLogger(VRunner.class.getSimpleName());
		}
		List<FrameworkMethod> testMethods = getTestClass().getAnnotatedMethods(Test.class);
        if (testMethods == null || testMethods.size() == 0) {
        	return testMethods;
        }
        final List<FrameworkMethod> finalTestMethods = new ArrayList<FrameworkMethod>(testMethods.size());
		try {
			String blockListPathValue = System.getProperty(VRunner.BLOCKLIST_PATH_KEY);
			if (blockListPathValue != null) {
				logger.info("Blocklist enabled via system variable: " + VRunner.BLOCKLIST_PATH_KEY + ":" + blockListPathValue);
				testMethods = this.removeBlockedTests(testMethods); // scrub tests for blocked tests
			}
			for (final FrameworkMethod method : testMethods) {
				logger.finer("method: " + method.getName());
				final VTag vTag = method.getAnnotation(VTag.class);
				if (vTag != null) {
					if (vTag.tags().length != 0) {
						if (!vTag.tagLogicClass().isEmpty() && !vTag.tagLogicMethod().isEmpty()) {
							for (String tag : vTag.tags()) {
								logger.finer("method:" + method.getName() + ", tag:" + tag + ", logic class:" + vTag.tagLogicClass() + ", logic method:" + vTag.tagLogicMethod());
								Class<?> c = Class.forName(vTag.tagLogicClass());
								Method m = c.getDeclaredMethod(vTag.tagLogicMethod(), tag.getClass());
								if ((boolean) m.invoke(null, (Object) tag)) {
									logger.info("Adding test to execution list -- tag logic succeeds: " + method.getName());
									finalTestMethods.add(method);
								}
							}
						} else {
							for (String tag : vTag.tags()) {
								if (Boolean.parseBoolean(System.getProperty(tag))) {
									logger.info("Adding test to execution list -- sysvar tag true: " + method.getName());
									finalTestMethods.add(method);
								}
							}
						}
					} else {
						logger.info("Adding test to execution list -- empty tags: " + method.getName());
						finalTestMethods.add(method);
					}
				} else {
					logger.info("Adding test to execution list -- no tags: " + method.getName());
					finalTestMethods.add(method);
				}
			}
		} catch (Exception e) {
			logger.severe(e.getMessage());
		}
		return finalTestMethods;
	}
	
	private List<FrameworkMethod> removeBlockedTests(List<FrameworkMethod> tests) throws FileNotFoundException, IOException {
		Set<String> blockListTests = this.getBlockedTestNames();
		logger.info("Blocked tests: ");
		Iterator<String> iter = blockListTests.iterator();
		while (iter.hasNext()) {
			logger.info("    " + iter.next());
		}
		List<FrameworkMethod> removeTests = new ArrayList<FrameworkMethod>();
		for (FrameworkMethod test : tests) {
			String testName = test.getMethod().getDeclaringClass().getSimpleName() + "." + test.getMethod().getName();
			logger.info("Testing for: " + testName);
			if (blockListTests.contains(testName)) {
				logger.info("Blocklist test positive.  Found.");
				removeTests.add(test); // add to separate list to avoid concurrent modification
			} else {
				logger.info("Blocklist test negative.  Not found.");
			}
		}
		for (FrameworkMethod removeTest : removeTests) {
			logger.info("Removing blocked test from execution: " + removeTest.getName());
			tests.remove(removeTest);
		}
		return tests;
	}
	
	private Set<String> getBlockedTestNames() throws FileNotFoundException, IOException {
		try{
			Set<String> blockedTestNames = new HashSet<String>();
			String blockListPath = System.getProperty(VRunner.BLOCKLIST_PATH_KEY, Candybean.ROOT_DIR + File.separator + "blocklist.txt");
			BufferedReader fileReader = new BufferedReader(new FileReader(blockListPath));
			String blockLine = fileReader.readLine().trim();
			while (blockLine != null) {
				int commentIndex = blockLine.indexOf(VRunner.BLOCKLIST_COMMENT);
				while (commentIndex >= 0) {
					String newBlockLine = blockLine.substring(0, commentIndex);
					logger.info("Removing comments from blockLine '" + blockLine + "' to '" + newBlockLine + "'");
					blockLine = newBlockLine.trim(); 
					commentIndex = blockLine.indexOf(VRunner.BLOCKLIST_COMMENT);
				}
				if (!blockLine.equals("")) blockedTestNames.add(blockLine);
				blockLine = fileReader.readLine();
			}
			fileReader.close();
			return blockedTestNames;
		} catch (FileNotFoundException fnfe) {
			throw new FileNotFoundException("The given blocklist file was not found; ensure path is correct for the system variable: " + BLOCKLIST_PATH_KEY);
		}
	}
	
	/**
	 * Adds a {@link TestRecorder} listener to the JUnit {@link RunNotifier} which
	 * listens to a failing state of a test annotated with {@link Record}. 
	 */
    @Override
    public void run(final RunNotifier notifier) {
    	try {
			notifier.addFirstListener(TestRecorder.getInstance());
		} catch (SecurityException e) {
			logger.info("Unable to instantiate test recorder");
		} catch (IOException e) {
			logger.info("Unable to instantiate test recorder");
		} catch (JAXBException e) {
			logger.info("Unable to instantiate test recorder");
		} catch (CandybeanException e) {
			logger.info("Unable to instantiate test recorder");
		}
    	super.run(notifier);
    }
    
    private static class NonBlockingAsynchronousRunner implements RunnerScheduler {
        private final List<Future<Object>> futures = Collections.synchronizedList(new ArrayList<Future<Object>>());
        private final ExecutorService fService;
        public NonBlockingAsynchronousRunner(int threads) {
            fService = Executors.newFixedThreadPool(threads, new CandybeanThreadFactory(threads));
        }

        public void schedule(final Runnable childStatement) {
            final Callable<Object> objectCallable = new Callable<Object>() {
                public Object call() throws Exception {
                	
                    childStatement.run();
                    return null;
                }
            };
            futures.add(fService.submit(objectCallable));
        }


        public void finished() {
            waitForCompletion();
        }

        public void waitForCompletion() {
            for (Future<Object> each : futures)
                try {
                    each.get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
        }
    }
    
	private static class CandybeanThreadFactory implements ThreadFactory {
		
		private int numOfThreads = 4;
		
		public CandybeanThreadFactory(int numOfThreads) {
			super();
			this.numOfThreads = numOfThreads;
		}

		public Thread newThread(Runnable r) {
			if(threadCounter == numOfThreads){
				threadCounter = 0;
			}
			return new Thread(r, candybean.config.getValue("parallel.threadNamePattern","")+threadCounter++);
		}
	}
}

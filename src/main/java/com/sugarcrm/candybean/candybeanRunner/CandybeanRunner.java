package com.sugarcrm.candybean.candybeanRunner;

import com.sugarcrm.candybean.automation.Candybean;
import com.sugarcrm.candybean.exceptions.CandybeanException;
import org.junit.Ignore;
import org.junit.internal.AssumptionViolatedException;
import org.junit.internal.runners.model.EachTestNotifier;
import org.junit.internal.runners.model.ReflectiveCallable;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.junit.runner.notification.StoppedByUserException;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

import javax.swing.plaf.nimbus.State;
import java.lang.reflect.Method;
import java.util.concurrent.*;

/**
 * CandybeanRunner is a test runner designed to give more control over the test runs.
 * It provides features that while currently available in JUnit, are implemented in a
 * slightly different manner.
 * <p>
 * To use the runner, annotate your tests with @RunWith(CandybeanRunner.class)
 * <p>
 * <b>Test Retries</b>
 * Set the value "runner.retryCount = x" in candybean.config where x is the maximum
 * number of times to retry a test if the first run fails (default 0). If any of the
 * runs pass, the test is marked as a pass. This differs from setting
 * -DrerunFailingTestsCount as that will mark tests as a flake, which does not play
 * well with external reporting tools such as Allure. Allure currently has a PR in
 * to fix that in which retries may become unnecessary.
 * <a href=https://github.com/allure-framework/allure-core/issues/611>Link</a>
 * <p>
 * <b>Test Timeouts</b>
 * Set the value "runner.timeout = x"  in candybean.config where x is the maximum time in
 * milliseconds that a test (including its @Before and @After methods) should run before
 * being killed. This setting allows a \@Test('timeout=xxx') like functionality to be set
 * on a class level rather than on a per test level i.e. if the test fails, its \@After
 * method will still be ran, unlike setting a timeout @Rule which will hard kill tests
 * without running \@After.
 * Additionally, the timeout applies to the \@Before, \@Test, and \@After methods together,
 * <p>
 * The precise order of actions which are run is @Before -> @Test -> @After. If the timeout
 * occurs during any of the methods, the test will be killed, and the runner will attempt to
 * run @Before and @After to clean up the environment, depending on candybean.config. If
 * the cleaning up fails, the runner will simply continue on, notifying that the test failed.
 * <p>
 * <b>Additional Settings</b>
 * runner.rerunIfTimedOut     = [true|false] If false, do not rerun if a test fails due to timeout
 * runner.cleanupTimeout      = xxx The max timeout in milliseconds the runner will attempt to
 * clean up after timing out (Defaults to runner.timeout)
 * runner.runBeforeIfTimedOut = [true|false] If true, run the @Before method on timeout
 * runner.runAfterIfTimedOut  = [true|false] If true, run the @After method on timeout
 *
 * Since I expect that those editing this file don't want to have to go read the docs on
 * how to extend JUnit runners, I've attempted to keep the documentation to a level that
 * anyone can figure out what's going on without reading the docs (even though you should
 * anyways).
 */
public class CandybeanRunner extends BlockJUnit4ClassRunner {

	protected final Candybean candybean = Candybean.getInstance();
	protected final int retryCount = Integer.parseInt(candybean.config.getValue("runner.retryCount", "0"));
	protected final int timeout = Integer.parseInt(candybean.config.getValue("runner.timeout", "0"));
	protected final int cleanupTimeout = Integer.parseInt(
			candybean.config.getValue("runner.cleanupTimeout", String.valueOf(timeout)));
	protected final boolean rerunIfTimedOut = Boolean.parseBoolean(candybean.config.getValue("runner.rerunIfTimedOut", "false"));
	protected final boolean runBeforeIfTimedOut = Boolean.parseBoolean(candybean.config.getValue("runner.runBeforeIfTimedOut", "true"));
	protected final boolean runAfterIfTimedOut = Boolean.parseBoolean(candybean.config.getValue("runner.runAfterIfTimedOut", "true"));
	final boolean INTERRUPT = true;

	/**
	 * Creates a BlockJUnit4ClassRunner to run klass
	 *
	 * @param klass The class to initialize
	 * @throws InitializationError
	 */
	public CandybeanRunner(Class<?> klass) throws InitializationError, CandybeanException {
		super(klass);
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	public void run(final RunNotifier notifier) {
		/* Construct statement that when evaluated:
		 *  Runs @BeforeClass
		 *  Runs All children tests with runChild
		 *  Runs @AfterClass
		 *
		 * If the test fails, it notifies the test runner
		 *
		 * This method is called on each test class. Evaluating
		 * the statement then runs runChild on each test within
		 * the class.
		 */
		final Statement statement = classBlock(notifier);
		try {
			statement.evaluate();
		} catch (StoppedByUserException e) {
			throw e;
		} catch (Throwable e) {
			notifier.fireTestFailure(new Failure(getDescription(), e));
		}
	}

	/**
	 * Runs each test using retries and timeouts. The general idea of this
	 * method is that it constructs a Callable that when run, runs the test. It
	 * attempts to retrieve the results of the callable after a certain timeout.
	 * If the test wasn't ready, or the test failed, it reruns the callable if
	 * applicable up to some specified number of times.
	 *
	 * @param method The test method to run
	 * @param notifier The test notifier to update
	 */
	@Override
	protected void runChild(final FrameworkMethod method, RunNotifier notifier) {
		final Description description = describeChild(method);

		// Check the annotations, it the test is marked ignore, ignore it.
		// If you wanted add a check for custom annotations, perhaps you
		// wanted an @DoNotReRun annotation, you would check for it here
		if (method.getAnnotation(Ignore.class) != null) {
			notifier.fireTestIgnored(description);
			return;
		}

		// Get a statement representing a single test
		final Statement statement = methodBlock(method);

		// Construct a callable to run a single test
		final Callable<Void> runTest = new Callable<Void>() {
			public Void call() throws Exception {
				try { // this code throws a Throwable...
					statement.evaluate();
				} catch (Error | Exception e) { // ...if it's an Error or Exception, that's fine...
					throw e;
				} catch (Throwable e) { // ...if not, wrap it in an Exception before throwing.
					throw new Exception(e);
				}
				return null;
			}
		};
		final EachTestNotifier eachTestNotifier = new EachTestNotifier(notifier, description);
		eachTestNotifier.fireTestStarted();

		TestResult result;
		int attempts = 0;
		do {
			result = attemptTest(description, runTest, method);
			if (result.failed() && attempts != retryCount){
				candybean.getLogger().warning("Caught \"" + result.getThrowable()
						+ "\" while running " + description
						+ ", but have not yet exceeded retries. Retrying test...");
			}
		}
		while (result.failed()
				&& attempts++ < retryCount
				&& !(result.getThrowable() instanceof TimeoutException && !rerunIfTimedOut));

		if (result.failed()) {
			eachTestNotifier.addFailure(result.getThrowable());
		}
		eachTestNotifier.fireTestFinished();
	}

	/**
	 * Attempt to run the test, and handle the exceptions that a test can throw
	 *
	 * @param description The test Description
	 * @param runTest The callable that runs the test
	 * @param method The FrameworkMethod of the test to run
	 * @return A TestResult representing the result
	 */
	protected TestResult attemptTest(Description description, Callable runTest, FrameworkMethod method) {
		final ExecutorService executorService = Executors.newCachedThreadPool();
		Future task = executorService.submit(runTest);
		try {
			// Create task to run the test, and attempt to retrieve it within
			// the time limit, else throw a timeout exception
			task.get(timeout, TimeUnit.MILLISECONDS);
		} catch (TimeoutException te) {
			candybean.log.severe(description + " exceeded allocated runtime of " + timeout + "ms, killing now");

			// Interrupt the task and shutdown the task executor
			task.cancel(INTERRUPT);
			executorService.shutdownNow();
			// If we catch a timeout exception, attempt to run @Before and @After, if enabled
			if (runAfterIfTimedOut || runBeforeIfTimedOut) {
				cleanUpState(method);
			}
			return TestResult.Fail(new TimeoutException(description + " exceeded allocated runtime of " + timeout + "ms"));
		} catch (ExecutionException e) {
			// If the test failed, unwrap the ExecutionException
			executorService.shutdownNow();
			return TestResult.Fail(e.getCause());
		} catch (AssumptionViolatedException | InterruptedException t) {
			executorService.shutdownNow();
			return TestResult.Fail(t);
		}
		return TestResult.Success();
	}

	/**
	 * Attempts to cleanup the testing environment using the Afters and Befores of method
	 *
	 * @param method The method to use to cleanup state
	 */
	protected void cleanUpState(final FrameworkMethod method) {
		Future cleanupTask = null;
		final ExecutorService executorService = Executors.newCachedThreadPool();
		try {
			// Construct a new empty test
			final Object test = new ReflectiveCallable() {
				@Override
				protected Object runReflectiveCall() throws Throwable {
					return createTest();
				}
			}.run();
			// Attach the befores and afters to the empty test
			final Statement emptyStatement = new Statement() {
				@Override
				public void evaluate() throws Throwable {}
			};
			final Callable<Void> runAfter = new Callable<Void>() {
				public Void call() throws Exception {
					try {
						/*
						 * In order to run the befores and/or the afters without
						 * running the actual test, we construct an empty Statement
						 * and add the before/afters to it
						 */
						if (runBeforeIfTimedOut && runAfterIfTimedOut) {
							withBefores(method, test,
									withAfters(method, test, emptyStatement)).evaluate();
						} else if (runBeforeIfTimedOut) {
							withBefores(method, test, emptyStatement).evaluate();
						} else {
							withAfters(method, test, emptyStatement).evaluate();
						}
					} catch (Error | Exception e) {
						throw e;
					} catch (Throwable t) {
						throw new Exception(t);
					}
					return null;
				}
			};
			cleanupTask = executorService.submit(runAfter);
			cleanupTask.get(cleanupTimeout, TimeUnit.MILLISECONDS);
		} catch (ExecutionException e) {
			// If cleaning up failed, log a warning, but continue on, we use the original error to
			// notify the test runner, not this one
			candybean.log.warning("Cleaning up" + getDescription() + "failed with " + e.getCause());
		} catch (TimeoutException e) {
			// Similar to if cleaning up failed, we alert if the clean up timed out, but continue on
			// after killing the cleanup task
			candybean.log.warning(getDescription() + "' @After method exceeded allocated runtime of "
					+ cleanupTimeout + "ms, killing now");
			if (cleanupTask != null) {
				cleanupTask.cancel(INTERRUPT);
			}
		} catch (Throwable t) {
			candybean.log.warning("Cleaning up" + getDescription() + "failed with " + t);
		} finally {
			executorService.shutdownNow();
		}
	}

	/**
	 * Inner class to represent a test result. A test result
	 * contains the result of the test, passed: true, or failed: false.
	 * If the result is failed, it also contains the throwable the test
	 * created.
	 */
	protected static class TestResult {
		private boolean result;
		private Throwable throwable;

		private TestResult(boolean result, Throwable throwable) {
			this.result = result;
			this.throwable = throwable;
		}

		/**
		 * Create an instance of a passing TestResult
		 * @return A passing TestResult
		 */
		public static TestResult Success() {
			return new TestResult(true, null);
		}

		/**
		 * Create an instance of a failing TestResult
		 * @param t The throwable the test threw
		 * @return A failing TestResult
		 */
		public static TestResult Fail(Throwable t) {
			return new TestResult(false, t);
		}

		/**
		 * Get the result of the test
		 * @return true if the test passed
		 */
		public boolean passed() {return result;}
		/**
		 * Get the result of the test
		 * @return true if the test failed
		 */
		public boolean failed() {return !result;}

		/**
		 * The failure reason of the test
		 * @return The throwable created by the test
		 */
		public Throwable getThrowable() {
			if (result) {
				throw new RuntimeException("Cannot access throwable of passed test");
			}
			return throwable;
		}
	}
}

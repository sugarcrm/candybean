package com.sugarcrm.candybean.candybeanRunner;

import com.sugarcrm.candybean.automation.Candybean;
import com.sugarcrm.candybean.exceptions.CandybeanException;
import org.junit.Ignore;
import org.junit.internal.AssumptionViolatedException;
import org.junit.internal.runners.model.EachTestNotifier;
import org.junit.internal.runners.model.ReflectiveCallable;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.junit.runner.notification.StoppedByUserException;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

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
	 * @throws CandybeanException
	 */
	public CandybeanRunner(Class<?> klass) throws InitializationError, CandybeanException {
		super(klass);
	}

	@Override
	/**
	 * @{inheritDoc}
	 */
	public void run(final RunNotifier notifier) {
		/* Construct statement that when evaluated:
		 *  Runs @BeforeClass
		 *  Runs All children tests with runChild
		 *  Runs @AfterClass
		 *
		 *  If the test fails, it notifies the test runner
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

	@Override
	protected void runChild(final FrameworkMethod method, RunNotifier notifier) {
		final Description description = describeChild(method);

		if (method.getAnnotation(Ignore.class) != null) {
			notifier.fireTestIgnored(description);
			return;
		}

		final EachTestNotifier eachTestNotifier = new EachTestNotifier(notifier, description);
		final Statement statement = methodBlock(method);

		// Construct a callable to run a single test
		final Callable<Void> runTest = new Callable<Void>() {
			public Void call() throws Exception {
				try {
					statement.evaluate();
				} catch (Error | Exception e) {
					throw e;
				} catch (Throwable e) {
					throw new Exception(e);
				}
				return null;
			}
		};
		eachTestNotifier.fireTestStarted();

		Throwable throwable;
		int attempts = 0;
		do {
			throwable = null;
			final ExecutorService executorService = Executors.newCachedThreadPool();
			Future task = executorService.submit(runTest);
			try {
				// Create task to run the test, and attempt to retrieve it within
				// the time limit, else throw a timeout exception
				task.get(timeout, TimeUnit.MILLISECONDS);
			} catch (TimeoutException te) {
				throwable = new TimeoutException(description + " exceeded allocated runtime of " + timeout + "ms");
				candybean.log.severe(description + " exceeded allocated runtime of " + timeout + "ms, killing now");

				// Interupt the task and shutdown the task executor
				task.cancel(INTERRUPT);
				executorService.shutdownNow();
				// If we catch a timeout exception, attempt to run @Before and @After, if enabled
				if (runAfterIfTimedOut || runBeforeIfTimedOut) {
					cleanUpState(method);
				}
				if (!rerunIfTimedOut) {
					break;
				}
			} catch (ExecutionException e) {
				// If the test failed, unwrap the ExecutionException
				throwable = e.getCause();
			} catch (AssumptionViolatedException | InterruptedException t) {
				throwable = t;
			}
			if (throwable != null && attempts != retryCount) {
				candybean.getLogger().warning("Caught \"" + throwable +
						"\" while running " + description + ", but have not yet exceeded retries. Retrying test...");
			}
		} while (throwable != null && attempts++ < retryCount);
		if (throwable != null) {
			eachTestNotifier.addFailure(throwable);
		}
		eachTestNotifier.fireTestFinished();
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
			final Callable<Void> runAfter = new Callable<Void>() {
				public Void call() throws Exception {
					try {
						if (runBeforeIfTimedOut && runAfterIfTimedOut) {
							withBefores(method, test,
									withAfters(method, test, new Statement() {
										@Override
										public void evaluate() throws Throwable {
										}
									})).evaluate();
						} else if (runBeforeIfTimedOut) {
							withBefores(method, test, new Statement() {
								@Override
								public void evaluate() throws Throwable {
								}
							}).evaluate();
						} else {
							withAfters(method, test, new Statement() {
								@Override
								public void evaluate() throws Throwable {
								}
							}).evaluate();
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
}

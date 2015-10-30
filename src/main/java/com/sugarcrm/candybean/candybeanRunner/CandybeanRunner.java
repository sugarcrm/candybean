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
 *
 * To use the runner, annotate your tests with @RunWith(CandybeanRunner.class)
 *
 * <b>Test Retries</b>
 * Set the value "runner.retryCount = x" in candybean.config where x is the maximum
 * number of times to retry a test if the first run fails (default 0). If any of the
 * runs pass, the test is marked as a pass. This differs from setting
 * -DrerunFailingTestsCount as that will mark tests as a flake, which does not play
 * well with external reporting tools such as Allure. Allure currently has a PR in
 * to fix that in which retries may become unnecessary.
 * <a href=https://github.com/allure-framework/allure-core/issues/611>Link</a>
 *
 * <b>Test Timeouts</b>
 * Set the value "runner.timeout = x"  in candybean.conig where x is the maximum time in
 * milliseconds that a test (including its @Before and @After methods) should run before
 * being killed. This setting allows a \@Test('timeout=xxx') like functionality to be set
 * on a class level rather than on a per test level i.e. if test fails, its \@After
 * method will still be ran, unlike setting a timeout @Rule which will hard kill tests
 * without running \@After.
 * Additionally, the timeout applies to the \@Before, \@Test, and \@After methods together,
 *
 * The precise order of actions which are run is @Before -> @Test -> @After. If the timeout
 * occurs during any of the methods, the test will be killed, and the runner will attempt to
 * run @After, even if @After had already begun. If the second @After fails, the runner will
 * simply continue on, notifying that the test failed in @After.
 *
 * <b>Additional Settings</b>
 * runner.rerunIfTimedOut = [true|false] If false, do not rerun if a test fails due to timeout
 * runner.afterTimeout = xxx The max timeout @After will run for. (Defaults to runner.timeout)
 * runner.afterIfTimedOut = xxx The max timeout @After will run for. (Defaults to runner.timeout)
 *
 */
public class CandybeanRunner extends BlockJUnit4ClassRunner {

	protected final Candybean candybean = Candybean.getInstance();
	private final int retryCount = Integer.parseInt(candybean.config.getValue("runner.retryCount", "0"));
	private final int timeout = Integer.parseInt(candybean.config.getValue("runner.timeout", "0"));
	private final int afterTimeout= Integer.parseInt(
			candybean.config.getValue("runner.afterTimeout", String.valueOf(timeout)));
	private final boolean rerunIfTimedOut = Boolean.parseBoolean(candybean.config.getValue("runner.rerunIfTimedOut", "false"));
	private final boolean runBeforeIfTimedOut = Boolean.parseBoolean(candybean.config.getValue("runner.runBeforeIfTimedOut", "true"));
	private final boolean runAfterIfTimedOut = Boolean.parseBoolean(candybean.config.getValue("runner.runAfterIfTimedOut", "true"));

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
		final boolean INTERRUPT = true;
		final Description description = describeChild(method);

		if (method.getAnnotation(Ignore.class) != null) {
			notifier.fireTestIgnored(description);
			return;
		}

		final EachTestNotifier eachTestNotifier = new EachTestNotifier(notifier, description);
		final Statement statement = methodBlock(method);

		final Callable<Void> runTest = new Callable<Void>() {
			public Void call() throws Exception {
				try {
					statement.evaluate();
				} catch (Error|Exception e) {
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
			throwable= null;
			final ExecutorService executorService = Executors.newCachedThreadPool();
			Future task = executorService.submit(runTest);
			try {
				// Create task to run the test, and attempt to retrieve it within
				// the time limit
				task.get(timeout, TimeUnit.MILLISECONDS);
			} catch (TimeoutException te) {
				throwable = new TimeoutException(description + " exceeded allocated runtime of " + timeout + "ms");
				candybean.log.severe( description + " exceeded allocated runtime of " + timeout + "ms, killing now");
				task.cancel(INTERRUPT);
				executorService.shutdownNow();
				// If we catch a timeout exception, attempt to run @After, if the enabled, regardless of state
				if (runAfterIfTimedOut || runBeforeIfTimedOut) {
					try {
						final ExecutorService innerExecutorService = Executors.newCachedThreadPool();
						final Object test = new ReflectiveCallable() {
							@Override
							protected Object runReflectiveCall() throws Throwable {
								return createTest();
							}
						}.run();
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
									}
									else if (runBeforeIfTimedOut) {
										withBefores(method, test, new Statement() {
											@Override
											public void evaluate() throws Throwable {
											}
										}).evaluate();
									}
									else if (runAfterIfTimedOut) {
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
						Future afterTask = innerExecutorService.submit(runAfter);
						try {
							afterTask.get(afterTimeout, TimeUnit.MILLISECONDS);
						} catch (ExecutionException e) {
							afterTask.cancel(INTERRUPT);
							continue;
						} catch (TimeoutException e) {
							candybean.log.severe(getDescription() + "' @After method exceeded allocated runtime of "
									+ afterTimeout + "ms, killing now");
							afterTask.cancel(INTERRUPT);
							continue;
						} finally {
							innerExecutorService.shutdownNow();
						}
					} catch (Throwable t) {
						throwable = new Exception(t);
					}
				}
				if (!rerunIfTimedOut) {
					break;
				}
			} catch (ExecutionException e) {
				throwable = e.getCause();
			}
			catch (AssumptionViolatedException|InterruptedException t) {
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
}

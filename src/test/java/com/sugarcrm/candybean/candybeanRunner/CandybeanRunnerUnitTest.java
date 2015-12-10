package com.sugarcrm.candybean.candybeanRunner;

import com.sugarcrm.candybean.exceptions.CandybeanException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runners.model.InitializationError;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

public class CandybeanRunnerUnitTest extends CandybeanRunner {
	public CandybeanRunnerUnitTest() throws InitializationError, CandybeanException {
		super(CandybeanRunnerUnitTest.class);
	}

	private class CallStub {
		public boolean called = false;
		public boolean completed = false;
		public void call() {
			called = completed = true;
		}
		public void callFail() throws ExecutionException {
			called = completed = true;
			Throwable throwable = new Throwable("Test Failed");
			throw new ExecutionException(throwable);
		}
		public void callInteruptedFail() throws InterruptedException {
			called = completed = true;
			throw new InterruptedException("Test Interrupted");
		}
		public void callDelay() throws InterruptedException {
			called = true;
			Thread.sleep(2000);
			completed = true;
			throw new InterruptedException("Test Interrupted");
		}
	}

	@Test
	public void TestResultTest() {
		Assert.assertTrue(TestResult.Success().passed());
		Assert.assertFalse(TestResult.Success().failed());
		Assert.assertTrue(TestResult.Fail(new Throwable()).failed());
		Assert.assertFalse(TestResult.Fail(new Throwable()).passed());
		Throwable testThrowable = new Throwable("Test Throw");
		Assert.assertEquals(testThrowable, TestResult.Fail(testThrowable).getThrowable());
	}

	@Test
	public void attemptTestPassedTest() throws InitializationError {
		final CallStub stub = new CallStub();

		final Callable<Void> runTest = new Callable<Void>() {
			public Void call() throws Exception {
				stub.call();
				return null;
			}
		};
		TestResult result = attemptTest(null, runTest, null);
		Assert.assertTrue(stub.called);
		Assert.assertTrue(result.passed());
	}

	@Test
	public void attemptTestFailedTest() throws InitializationError {
		final CallStub stub = new CallStub();

		final Callable<Void> runTest = new Callable<Void>() {
			public Void call() throws Exception {
				stub.callFail();
				return null;
			}
		};
		TestResult result = attemptTest(null, runTest, null);
		Assert.assertTrue(stub.called);
		Assert.assertTrue(result.failed());
		Assert.assertEquals("java.lang.Throwable: Test Failed", result.getThrowable().getMessage());
	}

	@Test
	public void attemptTestInterruptedTest() throws InitializationError {
		final CallStub stub = new CallStub();

		final Callable<Void> runTest = new Callable<Void>() {
			public Void call() throws Exception {
				stub.callInteruptedFail();
				return null;
			}
		};
		TestResult result = attemptTest(null, runTest, null);
		Assert.assertTrue(stub.called);
		Assert.assertTrue(result.failed());
		Assert.assertEquals("Test Interrupted", result.getThrowable().getMessage());
	}

	@Test
	public void attemptTestTimeout() throws InitializationError {
		final CallStub stub = new CallStub();

		final Callable<Void> runTest = new Callable<Void>() {
			public Void call() throws Exception {
				stub.callDelay();
				return null;
			}
		};
		TestResult result = attemptTest(null, runTest, null);
		Assert.assertTrue(stub.called);
		Assert.assertFalse(stub.completed);
		Assert.assertTrue(result.failed());
		Assert.assertEquals("null exceeded allocated runtime of 1500ms", result.getThrowable().getMessage());
	}
}

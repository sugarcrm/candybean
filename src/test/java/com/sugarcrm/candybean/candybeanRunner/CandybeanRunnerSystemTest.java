package com.sugarcrm.candybean.candybeanRunner;

import org.apache.commons.io.FileUtils;
import org.junit.*;
import org.junit.runner.RunWith;

import java.io.*;

@RunWith(CandybeanRunner.class)
public class CandybeanRunnerSystemTest {
	static String filename = "./candybeanTest.txt";
	String testName;

	@BeforeClass
	static public void createFile() {
		File f = new File(filename);
		if (f.exists()) {
			if (!f.delete()) {
				Assert.fail("Could not delete external resource " + filename);
			}
		} else {
			try {
				if (!f.createNewFile()) {
					Assert.fail("Could not create external resource " + filename);
				}
			} catch (IOException e) {
				Assert.fail("Could not create external resource " + filename);
			}
		}
		try {
			FileUtils.writeStringToFile(f, "0");
		} catch (IOException e) {
			Assert.fail("Could not write to file: " + filename);
		}
	}

	@AfterClass
	static public void deleteFile() {
		File f = new File(filename);
		if (f.exists()) {
			if (!f.delete()) {
				Assert.fail("Could not delete external resource " + filename);
			}
		}
	}

	@Before
	public void before() throws InterruptedException {
		testName = getClass().getSimpleName();
		System.err.println("In Before of " + testName);
	}

	@After
	public void after() throws InterruptedException {
		System.err.println("In After of " + testName);
	}

	@Test
	public void testCountTest() {
		File f = new File(filename);
		try {
			String contents = FileUtils.readFileToString(f);
			Integer count = Integer.parseInt(contents);
			if (count++ < 1) {
				FileUtils.writeStringToFile(f, count.toString());
				Assert.fail("Have not run test enough times");
			}
		} catch (IOException e) {
			Assert.fail("Could not read from file: " + filename);
		}
	}

	@Test
	public void testTimeoutSuccess() throws InterruptedException {
		System.err.println("In Tests");
		Thread.sleep(500);
	}

	@Ignore("This test used to test timing out, it is expected to fail")
	@Test
	public void testTimeoutFail() throws InterruptedException {
		System.err.println("In Tests");
		Thread.sleep(2000);
	}

	// Make sure I didn't break the expected exception annotation
	@Test(expected = AssertionError.class)
	public void expectedFailTest() {
		Assert.assertTrue(false);
	}

}

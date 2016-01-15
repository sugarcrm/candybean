package com.sugarcrm.candybean.candybeanRunner;

import org.apache.commons.io.FileUtils;
import org.junit.*;
import org.junit.runner.RunWith;

import java.io.*;

/**
 * A series of test intended to test some of the expected function
 * Since there doesn't appear to be a good way to test a runner, these
 * tests certainly are not conclusive.
 */
@RunWith(CandybeanRunner.class)
public class CandybeanRunnerSystemTest {
	static String filename = "./candybeanTest.txt";

	@BeforeClass
	static public void createFile() {
		File f = new File(filename);
		if (f.exists()) { // delete the file if it exists.
			if (!f.delete()) {
				Assert.fail("Could not delete external resource " + filename);
			}
		}

		try { // create the file regardless of whether it was deleted previously.
			if (!f.createNewFile()) {
				Assert.fail("Could not create external resource " + filename);
			}
		} catch (IOException e) {
			Assert.fail("Could not create external resource " + filename);
		}

		try { // write to file.
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
	}

	@After
	public void after() throws InterruptedException {
	}

	/**
	 * Increments a number in a file until the test passes
	 * Note: Ensure that retries is set to at least 1
	 * when running this test
	 */
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
		Thread.sleep(500);
	}

	/**
	 * This test is intended to fail, however, since it is killed by the runner, not
	 * the test itself, it can't use an expected exception to run properly
	 *
	 * @throws InterruptedException
	 */
	@Ignore("This test is used to test timing out, it is expected to fail")
	@Test
	public void testTimeoutFail() throws InterruptedException {
		Thread.sleep(2000);
	}

	// Make sure I didn't break the expected exception annotation
	@Test(expected = AssertionError.class)
	public void expectedFailTest() {
		Assert.assertTrue(false);
	}

}

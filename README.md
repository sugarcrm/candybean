Candybean
=========
The Candybean project started while acknowledging that many engineers have developed their own automation frameworks from scratch, and because automation is often designed to support software testing, these frameworks have very similar structure and functionality.  That said, some frameworks have better features than others.  Candybean's goal is to gather these best-in-class features, approach automation abstractly, and provide a baseline _test-specific_ automation framework.  In doing so, we aim to establish an open-source, collaborative project that can become an industry standard on which to iterate further.

For an explicit listing of Candybean goals and features, see the [Features section](#features) below.

There are two audiences this documentation is meant to support: users and contributors (developers).  Both audiences can learn more about how to use the project by reading further, but project contributors can additionally benefit from visiting the [Developer Documentation](DEVELOPERS.md).

See our [wiki](https://github.com/sugarcrm/candybean/wiki/Candybean), [FAQ](https://github.com/sugarcrm/candybean/wiki/FAQ) or [Quickstart](#quickstart) sections for quick high-level information and installation.

<a name="features"></a>
Features
--------
Here is an unordered list of planned project features Candybean:
* Automation interface abstraction: the project is extensible across underlying automation libraries like Selenium and AutoIt
* HTML5 support: supports the latest elements & attributes and provides additional automation functionality for common elements
* Java-based testing: allows testers to write tests in Java and have the support of an IDE and easily-learned, ubiquitous language
* Mobile support: tests can be queued to run on both iOS and Android devices
* Customizable results/reporting: test results are parsed to output custom HTML with video
* Data-driven: test input is extensible to support a variety of formats (e.g. DB, XML, JSON)
* Script-style logging: makes debugging easier and fully configurable
* Tag-based execution: provides another grouping mechanism for batching & executing tests
* Parallel support: several options for supporting a batch of tests across available resources & platforms
* Fully configurable: a single configuration runs Candybean and changes environment values based on exection platform 
* Randomized testing: supports randomized/stress testing via a page object modeling-style convention 
* JavaDoc & API documentation: includes usage details, examples, and best practices
* Easy to install: as Maven dependency, forked source, JAR, or GUI installer
* Unit and system tested

Components
----------
Candybean is a collection of components that fosters test automation.  Below is a list of those components:

* Automation: contains functionality that automates application interaction
* Element: contains functionality that encapsulates and automates component/element; applications contains elements
* Configuration: an extension of the native Java Properties object with configuration-file-specific functionality
* Data Source: an abstracted data source object with iterative and key-value behavior for data-driven testing
* Examples: example automated test projects that highlight Candybean's features
* Model: an extension of the page object model, this module seeks to abstracts framework extending the notion of page object models for automated testing
* Results: an encapsulation of test result parsing and illustrative/presentation functionality
* Runner: an annotation-based orchestration object that executes identified methods for test execution
* Test: a utilities package with test-specific helper functionality
* Utilities: contains generic helper functionality
* Web Services: contains request-building and response-parsing functionality

<a name="quickstart"></a>
Quickstart
----------
Writing automated tests with Candybean is as quick as these handful of steps:

* [Install Candybean prequisites](#prereqs)
* Create a [Maven](http://maven.apache.org/) project; add Candybean to your new test project's [POM](https://maven.apache.org/pom.html) as a dependency: 
```
<dependency>
    <groupId>com.sugarcrm</groupId>
    <artifactId>candybean</artifactId>
    <version>1.1.1</version>
</dependency>
```
* [Write a test file](#tests)
* [Configure Candybean](#config) 
* Execute your test(s) using the [Maven Surefire Plugin](http://maven.apache.org/surefire/maven-surefire-plugin/)

<a name="prereqs"></a>
Installation prerequisites
--------------------------
* <a href="http://www.oracle.com/technetwork/java/javase/downloads/index.html">Java SE 7 JDK</a>
* <a href="http://maven.apache.org/download.html">Maven 3 (recommended build management)</a>

Once Maven is installed, it will detect and automatically install further Candybean prerequisites.

If not already familiar, review the basics of Maven to better understand dependency management and execution:
* [Maven in 5 minutes](http://maven.apache.org/guides/getting-started/maven-in-five-minutes.html)
* [Maven overview](http://www.tutorialspoint.com/maven/maven_overview.htm)

<a name="tests"></a>
Writing tests
-------------
Here's an example Java-JUnit test that extends AbstractTest (which instantiates 
a Candybean interface from the configuration file) and begins testing through 
the interface defined in the configuration.

The second Java-JUnit test has been enabled for recording, using the @Record annotation, a feature of Candybean
that will make a video recording of the test execution. This feature can be configured in the Candybean configuration file.

The VTag annotation on the second JUnit test showcases the ability to tag certain tests to be run only on specific platforms.

```
import com.sugarcrm.candybean;
import org.junit.AfterClass;
import org.junit.Test;
import com.sugarcrm.candybean.test.AbstractTest;

@RunWith(VRunner.class)
public class CandybeanTest extends AbstractTest {
	
	@Test
	public void backwardForwardRefreshTest() throws Exception {
		logger.log("Bringing up craigslist.com for an apartment search!");
		candybean.getInterface().start();
		candybean.getInterface().go("http://www.craigslist.com/");
		assertEquals("http://www.craigslist.org/about/sites", cb.getURL());
		... do other things
		... perform other assertions
		... perform other logging
		... use other candybean features		
	}
	
	@Test
	@Record(duration = Duration.FINAL_FAILED)
	@VTag(tags={"mac", "windows", "linux"}, tagLogicClass="com.sugarcrm.candybean.runner.VTagUnitTest", tagLogicMethod="processTags")
	public void recordingTest() throws Exception {
		String amazonUrl = "http://www.amazon.com/";
		iface.go(amazonUrl);
		assertEquals(amazonUrl, iface.getURL());	
		... do other things
		... perform other assertions
		... perform other logging
		... use other candybean features			
	}
	
	@AfterClass
	public static void last() throws Exception {
		candybean.getInterface().stop();
	}
}
```

Refer to [Candybean's API Documentation](http://sugarcrm.github.io/candybean/doc/index.html) for further feature usage.

<a name="config"></a>
Configuration
-------------
Candybean's git repo includes a git submodule at '/config' referencing SugarCRM's private repo.  Out 
of the box, Candybean will look for its configuration file in this directory, which should either be 
explicitly defined or overridden via command line or system variable.

The following key-value keys should be defined in a configuration file used to instantiate Candybean.
By default, Candybean will look for a <b>candybean.config</b> file located in the 'config' directory, but
a path can also be specified from the command line or a system variable 'candybean_config'.
```
# specifies the type of automation interface
automation.interface = chrome # chrome | firefox | ie | opera | android | ios  

# browser specific profiles and driver paths
browser.firefox_binary = {\
	"linux": "/path/to/firefox/binary/in/linux", \
	"mac": "/path/to/firefox/binary/on/mac", \
	"windows": "c:/path/to/firefox/binary/in/windows"}
browser.firefox_profile = default
browser.chrome_driver_path = {\
	"linux": "/path/to/chrome/driver/in/linux", \
	"mac": "/path/to/chrome/driver/on/mac", \
	"windows": "/path/to/chrome/driver/in/windows"}
browser.chrome_driver_log_path = /path/to/chromedriver/log
browser.ie_driver_path = /path/to/ie/driver
perf.page_load_timeout = /page/load/in/seconds
perf.implicit_wait_seconds = /passive/wait/in/seconds

# logger configuration
handlers = java.util.logging.FileHandler, java.util.logging.ConsoleHandler

# file logging
java.util.logging.FileHandler.limit = 50000
java.util.logging.FileHandler.count = 1
java.util.logging.FileHandler.formatter = java.util.logging.SimpleFormatter
java.util.logging.FileHandler.level = INFO

# logging format
java.util.logging.SimpleFormatter.format = [%1$tm-%1$td-%1$tY %1$tk:%1$tM:%1$tS:%1$tL] %2$s %4$s: %5$s %6$s %n

# Monte Media Library Recorder Settings
video.format=video/quicktime
video.format.frameRate=15
video.directory=./log
video.encoding=rle 
video.compression=Animation
maxFileSize=512000
maxRecordingTime=120000
```

Things we like
--------------
This is a list of additional tools and things we like for automation, testing, etc.

We like this stuff:
* [Eclipse](https://www.eclipse.org/)
* [UIAutomatorViewer](http://developer.android.com/tools/testing/testing_ui.html#uianalysis) for android devices. Useful UI analysis tool when writing mobile automation tests.

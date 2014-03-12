Candybean
=========
The Candybean project started while acknowledging that many engineers have developed their own automation frameworks from scratch, and because automation is often designed to support software testing, these frameworks have very similar structure and functionality.  That said, if you compare frameworks, you'll find features that are implemented more robustly in some versions than others.  Candybean's goal is to gather these best-in-class features, approach automation abstractly, and provide a baseline _test-specific_ automation framework.  In doing so, we aim to establish an open-source, collaborative project that can become an industry standard on which to iterate further.  For an explicit listing of Candybean goals and features, see the [Features section](#features) below.

There are two audiences this documentation is meant to support: users and potential developer-contributors.  Both audiences can learn more about how to use the project below, but potential developer-contributors can additionally benefit from visiting the [Developer Documentation] (DEVELOPERS.md).

See our [FAQ](#faq) or [Quickstart](#quickstart) sections for more high-level information.

Summary
-------
Candybean is a collection of components that fosters test automation, execution configuration, data abstraction, results illustration, tag-based execution, top-down and bottom-up batching, and mobile, plain-language, & web service testing.

<a name="features"></a>
Features
--------
Some project features planned for Candybean:
* HTML 5 support (Sugar 7): supports HTML 5 elements
* Support Java-based tests/calls: executes Java-based test scripts
* Mobile support: tests run on web or mobile
* Abstraction from dependent technologies:
** Supports multiple, independent reporting options (e.g. XML, HTML)
** Supports multiple automation frameworks
** Data-driven resource agnostic (e.g. DB, XML, JSON)
* Independent from project-specific parameters:
** Supports product objectification, thus platform-independent
** Product independent (no Sugar-specific references)
* Self-testing:
** Verify element hooks are valid before running tests
** Verify abstract element behavior (fields, menus, etc.)
** Unit and system tested
* Script-style logging: make debugging readable, easier and fully configurable
* Tag-based execution: define tag logic determining which methods execute
* Failure/error non-blocking: option to continue executing tests upon error/failure
* Resource consolidating: pre-execution resource scan for consolidated allocation
* Supports 'smart' waits (WIP definition)
* Supports video recording of tests
* Configurable with ease/overridable via CLI
* Randomized testing: Supports randomized/stress testing
* Open-source:
** JavaDoc/API with usage details, examples, best practices
** Externally-facing site with installation documentation
** Code examples
* Best practice, OOP-organized code for ease of maintenance
* Easy to install (GUI installer or minimal configuration)

Components
----------
This is a list of components that collectively fall under Candybean:
* Automation: contains functionality that automates application interaction
* Configuration: an extension of the native Java Properties object with configuration-file-specific functionality
* Data Source: an abstracted data source object with iterative and key-value behavior for data-driven testing
* Examples: example automated test projects that highlight Candybean's features
* Model: test target abstraction framework for automated testing
* Results: an encapsulation of test result parsing and illustrative/presentation functionality
* Runner: an annotation-based orchestration object that executes identified methods for test execution
* Test: a utilities package with test-specific helper functionality
* Utilities: contains generic helper functionality and reporting functionality
* Web Services: contains request-building and response-parsing functionality

API Documentation
-----------------
[Javadoc API Documentation](http://sugarcrm.github.io/candybean/doc/index.html)

Installation
------------
Install and configure the following dependencies:
* <a href="http://git-scm.com/downloads">Git (clone your fork)</a>
* <a href="http://www.oracle.com/technetwork/java/javase/downloads/index.html">Java SE 7 JDK</a>
* <a href="https://www.google.com/intl/en/chrome/browser/">Chrome (default browser)</a>
* <a href="http://maven.apache.org/download.html">Maven 3 (recommended build management)</a>

Configuration
-------------
Candybean's git repo includes a git submodule at '/config' referencing SugarCRM's private repo.  Out 
of the box, Candybean will look for its configuration file in this directory, which should either be 
explicitly defined or overridden via command line or system variable.

The following key-value keys should be defined in a configuration file used to instantiate Candybean.
By default, Candybean will look for a <b>candybean.config</b> file located in the 'config' directory, but
a path can also be specified from the command line or a system variable 'candybean_config'.
```
#specifies the type of autmation interface
automation.interface = chrome # chrome | firefox | ie | opera | android | ios  

#browser specific profiles and driver paths
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

#candybean logger configuration
handlers = java.util.logging.FileHandler, java.util.logging.ConsoleHandler

#file logging
java.util.logging.FileHandler.limit = 50000
java.util.logging.FileHandler.count = 1
java.util.logging.FileHandler.formatter = java.util.logging.SimpleFormatter
java.util.logging.FileHandler.level = INFO

#logging format
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

Writing tests
-------------
Candybean recommends the use of <a href="http://maven.apache.org/">Maven</a>!  So if you're not familiar 
with Maven already, <a href="http://www.tutorialspoint.com/maven/maven_overview.htm">try this link for an overview.</a>   

Here's an example Java-JUnit test that extends AbstractTest (which instantiates 
a Candybean interface from the configuration file) and begins testing through 
the interface defined in the configuration.

The second Java-JUnit test has been enabled for recording, using the @Record annotation, a feature of candybean
that will make a video recording of the test execution. This feature can be configured in the candybean configuration file.

The VTag annotation on the second JUnit test showcases the ability to tag certain tests to be run only on specific platforms

```
import com.sugarcrm.candybean;
import org.junit.AfterClass;
import org.junit.Test;
import com.sugarcrm.candybean.test.AbstractTest;

@RunWith(VRunner.class)
public class CandybeanTest extends AbstractTest{
	
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

Executing tests
---------------
Because Candybean recommends Maven, executing your tests is as simple as making sure they're 
located correctly in a standardized directory structure and/or naming them using certain 
conventions.  For more information, review the <a href="http://maven.apache.org/surefire/maven-surefire-plugin/">Maven Surefire Plugin</a>.

<a name="faq"></a>
Frequently asked questions
--------------------------
WIP 

<a name="quickstart"></a>
Quickstart
----------
WIP

Additional tools
----------------
We find the following tools useful for development with Candybean:
* <a href="http://developer.android.com/tools/testing/testing_ui.html#uianalysis">UIAutomatorViewer</a> for android devices. Useful UI analysis tool when writing mobile automation tests.

Core contributors
-----------------
* Conrad Warmbold (<a href="https://github.com/cradbold">@cradbold</a>)
* Soon Han (<a href="https://github.com/hans-sugarcrm">@hans-sugarcrm</a>)
* Larry Cao (<a href="https://github.com/sqwerl">@sqwerl</a>)
* Jason Lin (<a href="https://github.com/Raydians">@Raydians</a>)
* Wilson Li (<a href="https://github.com/wli-sugarcrm">@wli-sugarcrm</a>)

Candybean
=========
The Candybean project started while acknowledging that many engineers have developed their own automation 
frameworks from scratch, and because automation is often designed to support software testing, these 
frameworks have very similar structure and functionality.  That said, some frameworks have better 
features than others.  Candybean's goal is to gather these best-in-class features, approach automation 
abstractly, and provide a baseline _test-specific_ automation framework.  In doing so, we aim to 
establish an open-source, collaborative project that can become an industry standard on which to iterate 
further.

For an explicit listing of Candybean goals and features, see the [Features section](#features) below.

There are two audiences this documentation is meant to support: users and contributors (developers).  
Both audiences can learn more about how to use the project by reading further, but project contributors 
can additionally benefit from visiting the [Developers documentation](DEVELOPERS.md).

See our [wiki](https://github.com/sugarcrm/candybean/wiki/Candybean), [FAQ](https://github.com/sugarcrm/candybean/wiki/FAQ) or [Quickstart](#quickstart) sections for quick high-level information and installation.

<a name="features"></a>
Features
--------
Some project features planned for Candybean:
* HTML 5 support (Sugar 7): supports HTML 5 elements
* Support Java-based tests/calls: executes Java-based test scripts
* Abstraction from dependent technologies:
    * Supports multiple, independent reporting options (e.g. XML, HTML)
    * Supports multiple automation frameworks (de-prioritized)
    * Data-driven resource agnostic (e.g. DB, XML, JSON)
* Independent from project-specific parameters:
    * Supports product objectification, thus platform-independent
    * Product independent (no Sugar-specific references)
* Self-testing:
    * Verify element hooks are valid before running tests
    * Verify abstract element behavior (fields, menus, etc.)
    * Unit and system tested
* Script-style logging: make debugging readable, easier
* Failure/error non-blocking: option to continue executing tests upon error/failure
* Resource consolidating: pre-execution resource scan for consolidated allocation
* Supports 'smart' waits (WIP definition)
* Configurable with ease/overridable via CLI
* Randomized testing: Supports randomized/stress testing
* Open-source:
    * JavaDoc/API with usage details, examples, best practices
    * Externally-facing site/wiki with installation documentation
    * Code samples
* Best practice, OOP-organized code for ease of maintenance
* Easy to install (GUI installer or minimal configuration)

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
* [Configure Candybean](#config) 
* [Write your test(s)](#tests)
* [Execute your test(s)](#execute)

<a name="prereqs"></a>
Installation prerequisites
--------------------------
* <a href="http://www.oracle.com/technetwork/java/javase/downloads/index.html">Java SE 7 JDK</a>
* <a href="http://maven.apache.org/download.html">Maven 3 (recommended build management)</a>

Once Maven is installed, it will detect and automatically install further Candybean prerequisites.

If not already familiar, review the basics of Maven to better understand dependency management and execution:
* [Maven in 5 minutes](http://maven.apache.org/guides/getting-started/maven-in-five-minutes.html)
* [Maven overview](http://www.tutorialspoint.com/maven/maven_overview.htm)

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
browser.firefox.binary = {\
	"linux": "/path/to/firefox/binary/in/linux", \
	"mac": "/path/to/firefox/binary/on/mac", \
	"windows": "c:/path/to/firefox/binary/in/windows"}
browser.firefox.profile = default
browser.chrome.driver.path = {\
	"linux": "/path/to/chrome/driver/in/linux", \
	"mac": "/path/to/chrome/driver/on/mac", \
	"windows": "/path/to/chrome/driver/in/windows"}
browser.chrome.driver.log.path = /path/to/chromedriver/log
browser.ie.driver.path = /path/to/ie/driver
perf.page.load.timeout = /page/load/in/seconds
perf.implicit.wait.seconds = /passive/wait/in/seconds

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
Optionally, a candybean configuration file can be generated using the built-in configuration server.
To start the server, simply run "mvn -Pconfigure compile", and you may access the web-based configuration interface
at http://localhost:8080/cfg

When using the configuration server, candybean will look for a config file to load default values in to the configuration form.

<a name="tests"></a>
Writing tests
-------------
Here's an example Java-JUnit test that uses the candybean AutomationInterfaceBuilder and begins testing through 
the interface.

The second Java-JUnit test has been enabled for recording, using the @Record annotation, a feature of Candybean
that will make a video recording of the test execution. This feature can be configured in the Candybean configuration file.

The VTag annotation on the second JUnit test showcases the ability to tag certain tests to be run only on specific platforms.

When attempting to run tests in parallel, it is important that WebDriverInterface is not instantiated as a static variable, and it is not safe
to instantiate WebDriverInterface in the @BeforeClass annotated method, if one is included. 

```
import com.sugarcrm.candybean;
import org.junit.AfterClass;
import org.junit.Test;
import com.sugarcrm.candybean.test.AbstractTest;

@RunWith(VRunner.class)
public class CandybeanTest {
	
	private WebDriverInterface iface;
	
	//The Candybean logger automatically handles the creation of log files specific to test classes
	private Logger logger = Logger.getLogger(Candybean.class.getSimpleName());
	
	@Before
	public void setUp() throws CandybeanException {
		Candybean candybean = Candybean.getInstance();
		AutomationInterfaceBuilder builder = candybean.getAIB(WebDriverControlSystemTest.class);
		builder.setType(Type.CHROME);
		iface = builder.build();
		iface.start();
	}

	@After
	public void tearDown() throws CandybeanException {
		iface.stop();
	}
	
	@Test
	public void backwardForwardRefreshTest() throws Exception {
		logger.info("Bringing up craigslist.com for an apartment search!");
		iface.go("http://www.craigslist.com/");
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
	
}
```

To run tests in parallel, simply set the value of 'parallel.enabled' to true in the candybean configuration file.
The number of threads used for parallel testing can be specified by setting the key 'parallel.threads' in the configuration file.

Refer to [Candybean's API Documentation](http://sugarcrm.github.io/candybean/doc/index.html) for further feature usage.

<a name="execute"></a>
Executing your tests
--------------------
Generally speaking, tests can be/are executed using the [Maven Surefire Plugin](http://maven.apache.org/surefire/maven-surefire-plugin/).  So 
if your tests make use of the [Maven standard directory layout](https://maven.apache.org/guides/introduction/introduction-to-the-standard-directory-layout.html) and 
your tests are configured for either [JUnit](http://maven.apache.org/surefire/maven-surefire-plugin/examples/junit.html) or 
[TestNG](http://maven.apache.org/surefire/maven-surefire-plugin/examples/testng.html), the following command should trigger test execution:
```
> mvn clean test -Dcbconfig=./candybean.config
```
Note, though Candybean has default configuration settings, any practical use of the Candybean 
project will at least require some custom Candybean configuration, thus specifying the 
location of your Candybean configuration file is required.

Other things you can do:</br></br>
Specify a testcase and/or test for execution:
```
> mvn clean test -Dcbconfig=./candybean.config -Dtest=MyTestCase#MyTest
```
Specify a configured [maven profile](http://maven.apache.org/guides/introduction/introduction-to-profiles.html) with [surefire plugin inclusion/exclusion](http://maven.apache.org/surefire/maven-surefire-plugin/examples/inclusion-exclusion.html) for execution:
```
<project>
	...
	<profiles>
		<profile>
			<id>integration</id>
			<build>
				<plugins>
					<plugin>
						<groupId>org.apache.maven.plugins</groupId>
						<artifactId>maven-surefire-plugin</artifactId>
						<configuration>
							<includes>
								<include>**/*IntegrationTest.java</include>
							</includes>
							<excludes>
								<exclude>**/*UnitTest.java</exclude>
							</excludes>
						</configuration>
					</plugin>
				</plugins>
			</build>
		</profile>
	<profiles>
	...
</project>
```
```
> mvn clean test -Dcbconfig=./candybean.config -Pintegration
```
(TestNG) Specify [suite XML files from CLI](http://stackoverflow.com/questions/11397315/how-to-parametrize-in-maven-surefire-plugin-which-testng-suites-to-run/13829933#13829933):
```
> mvn clean test -Dcbconfig=./candybean.config -DsuiteFile=test1.xml,test2.xml
``` 

Things we like
--------------
This is a list of additional tools and things we like for automation, testing, etc.

We like this stuff:
* [Eclipse](https://www.eclipse.org/) - as standard an IDE as any; recently added git integration improvements
* [UIAutomatorViewer](http://developer.android.com/tools/testing/testing_ui.html#uianalysis) for android devices. Useful UI analysis tool when writing mobile automation tests.

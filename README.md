Candybean
=========
Candybean is SugarCRM's next generation automation and testing framework.  It is a collection of components that fosters test automation, execution configuration, data abstraction, results illustration, tag-based execution, top-down and bottom-up batches, mobile variants, plain-language testing, and web service testing.

Features
--------
Some project features planned for Candybean:
* HTML 5 support (Sugar 7): supports HTML 5 elements
* Support Java-based tests/calls: executes Java-based test scripts
* Abstraction from dependent technologies:
** Supports multiple, independent reporting options (e.g. XML, HTML)
** Supports multiple automation frameworks (de-prioritized)
** Data-driven resource agnostic (e.g. DB, XML, JSON)
* Independent from project-specific parameters:
** Supports product objectification, thus platform-independent
** Product independent (no Sugar-specific references)
* Self-testing:
** Verify element hooks are valid before running tests
** Verify abstract element behavior (fields, menus, etc.)
** Unit and system tested
* Script-style logging: make debugging readable, easier
* Failure/error non-blocking: option to continue executing tests upon error/failure
* Resource consolidating: pre-execution resource scan for consolidated allocation
* Supports 'smart' waits (WIP definition)
* Configurable with ease/overridable via CLI
* Randomized testing: Supports randomized/stress testing
* Open-source:
** JavaDoc/API with usage details, examples, best practices
** Externally-facing site/wiki with installation documentation
** Code samples
* Best practice, OOP-organized code for ease of maintenance
* Easy to install (GUI installer or minimal configuration)

Components
----------
This is a list of components that collectively fall under Candybean:
* Automation: contains functionality that automates application interaction
* Configuration: an extension of the native Java Properties object with configuration-file-specific functionality
* Data Source: an abstracted data source object with iterative and key-value behavior
* Results: an encapsulation of test result parsing and illustrative/presentation functionality
* Runner: an annotation-based orchestration object that executes identified code portions
* Test: a utilities package with test-specific helper functionality
* Translations: outputs given files with translated strings; can be used for i18n, etc.
* Utilities: contains generic helper functionality
* Web Services: contains REST request-building and response-parsing functionality
* Mobile: a cross platform mobile testing framework

Installation
------------
Install and configure the following dependencies:
* <a href="http://git-scm.com/downloads">Git (clone your fork)</a>
* <a href="http://www.oracle.com/technetwork/java/javase/downloads/index.html">Java SE 7 JDK</a>
* <a href="http://maven.apache.org/download.html">Maven 3</a>
* <a href="https://www.google.com/intl/en/chrome/browser/">Chrome (default browser)</a>

Configuration
-------------
The following key-value keys should be defined in a configuration file used to instantiate Candybean
```
automation.interface = chrome # chrome | firefox | ie | opera | android | ios  
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
```

Writing tests
------------
Here's an example Java-JUnit test that instantiates Candybean and begins testing through a Chrome browser:
```
import com.sugarcrm.candybean;

public class CandybeanTest {

	static Candybean cb;
	
	@BeforeClass
	public static void first() throws Exception {
		Configuration candybeanConfig = new Configuration(new File("path/to/candybean.config"));
		cb = Candybean.getInstance(candybeanConfig);
		cb.getInterface().start(Type.CHROME);
	}
	
	@Test
	public void backwardForwardRefreshTest() throws Exception {
		cb.log("Bringing up craigslist.com for an apartment search!");
		cb.getInterface().go("http://www.craigslist.com/");
		assertEquals("http://www.craigslist.com/", cb.getURL());
		... do other things
		... perform other assertions
		... perform other logging
		... use other candybean features		
	}
	
	@AfterClass
	public static void last() throws Exception {
		cb.getInterface().stop();
	}
}
```

Executing tests
--------------
At this point, because Candybean is Maven-based, executing simple maven commands will detect written tests and execute them for test results:
```
> mvn clean install
```
You can also see the included system tests to see test examples.  System tests can be executing via maven and the 'system' profile:
```
> mvn clean install -Psystem
```

Core contributors
-----------------
* Conrad Warmbold (<a href="https://github.com/cradbold">@cradbold</a>)
* Soon Han (<a href="https://github.com/hans-sugarcrm">@hans-sugarcrm</a>)
* Larry Cao (<a href="https://github.com/sqwerl">@sqwerl</a>)
* Jason Lin (<a href="https://github.com/Raydians">@Raydians</a>)
* Wilson Li (<a href="https://github.com/wli-sugarcrm">@wli-sugarcrm</a>)

Voodoo2
=======




***
## Component: Translations 
### Background: 
Files are used as a form of tests to run along an environment like Sugar. However, Sugar supports 
many languages and therefore it is required that the test files contain the appropriate language-specific
strings to compare with. This program will take-in a file and generate a new file where the attribute of an
'assertEquals' statement are replaced according to the language specified. 

### Usage: 
There are mainly two ways to run translations on a file or a directory containing multiple files.
**Method 1:** passing specific parameters to the Translate method as follows:
<pre><code> Translate(database, module(s), language, testfile); </pre></code>

where,
+ database - name of the database (ie, "translate\_6\_3") 
+ module - name of the module (ie, "Accounts") or path to a file containing a list of modules (ie, ../resources/module-list.txt) where each module is separated by a newline
+ language - desire translation language (ie, fr\_FR, es\_ES)
+ testfile - path to a single test file (ie, ../Accounts\_xxxx.java) or a path to a directory containing multiple test files (ie, ../MyTestFiles/)

*NOTE:* The output file path and database access information has been hardcoded (see below). So If you want to use another output 
path or another database, it is suggested to use **Method 2** or you can simple add them to your System's property with the following keys:
+ output path (translate.output) -  [hardcoded as]: will be stored in the execution-program path, pre-named as: ../translated\_Files\_[language]
+ database server(translate.server) - [hardcoded as]: 10.8.31.10
+ database username(translate.username) - [hardcoded as]: translator
+ database password(translate.password) - [hardcoded as]: Sugar123!

**Method 2:** passing a properties file path to the Translate method as follows: 
<pre><code> Translate("/resources/translate.properties"); </pre></code>

The properties files must contain the following keys for the program execution to succeed:
**Properties key/value format:** <pre><code> translate.key=value </pre></code>
+ translate.database - name of the database (ie, "translate\_6\_3") 
+ translate.module - name of the module (ie, "Accounts") or path to a file containing a list of modules (ie, ../resources/module-list.txt) where each module is separated by a newline
+ translate.language - desire translation language (ie, fr\_FR, es\_ES)
+ translate.testpath - path to a single test file (ie, ../Accounts\_xxxx.java) or a path to a directory containing multiple test files (ie, ../MyTestFiles/)
+ translate.output - output path/directory (ie, ../TestOutput)

+ translate.server - database server address (eg. 10.8.31.10)
+ translate.username - database server username (eg. translator)
+ translate.password - database server password (eg. Sugar123!)

### Supported Assert Statements:
The 'expectedValue' will be replaced with a language-specified string from the database 
+ assertEquals(expectedValue, actualValue); 
+ assertEquals(message, expectedValue, actualValue);

### TODO::
+ Use Centralized Database
+ Add assert robustness by adding more assert types or use a global wrapper assert method 

###Last-Minute Update 12-21-2012
+ Voodoo2 Translation will support .xml format tests (instead of only .java)

***

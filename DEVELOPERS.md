CANDYBEAN DEVELOPERS
====================
This documentation is meant for project contributing developers.  For more, general information on Candybean and how to get it configured and running quickly, see the project's [README](README.md). 

Setup
-----
1. See the <a href="README.md#prereqs">installation prerequisites for preliminary installs.</a>
2. Additionally, [install git](http://git-scm.com/book/en/Getting-Started-Installing-Git) and [fork & clone the Candybean project](https://help.github.com/articles/fork-a-repo) from the [Candybean Github page](https://github.com/sugarcrm/candybean). 
3. Make sure SugarCRM Legal has received a copy of your signed [Contribution Agreement](https://github.com/sugarcrm/candybean/raw/cb-186/SugarCRMCA-Candybean.doc)
4. At this point, you're ready to make code changes; to deliver them back to the project, [submit a pull request](https://help.github.com/articles/using-pull-requests). 

To file bugs or features requests, etc. against the project, [submit issues here](https://github.com/sugarcrm/candybean/issues?state=open).

Refer to our [wiki](https://github.com/sugarcrm/candybean/wiki/Candybean) for more information as well.

Project tests
-------------
Unit tests are encouraged for all functionality that has no third party dependencies.  These tests are executed by default in the Maven test phase and are delimited by the naming convention of "*UnitTest.java".

System tests are encouraged for all functionality that requires third party dependencies -- e.g. the use of Selenium or Chrome as a browser.  These tests are executed using the '-Psystem' Maven profile designation and are delimited by the naming convention of "*SystemTest.java".
 
Core contributors
-----------------
* Conrad Warmbold (<a href="https://github.com/cradbold">@cradbold</a>)
* Soon Han (<a href="https://github.com/hans-sugarcrm">@hans-sugarcrm</a>)
* Shehryar Farooq (<a href="https://github.com/Ownageful">@Ownageful</a>)
* Larry Cao (<a href="https://github.com/sqwerl">@sqwerl</a>)
* Jason Lin (<a href="https://github.com/Raydians">@Raydians</a>)
* Wilson Li (<a href="https://github.com/wli-sugarcrm">@wli-sugarcrm</a>)
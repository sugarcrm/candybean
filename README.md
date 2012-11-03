Voodoo2
=======





Translations
============
The command used to compile the java program:
navigate to the directory with the java file,
javac -classpath mysql-connector-java-5.1.18/mysql-connector-java-5.1.18-bin.jar:. translate.java

Make sure that the latest version mysql-connector is downloaded from: http://dev.mysql.com/downloads/connector/j/

There are mainly two ways to run the program, use the command:
1) By giving specific arguments when running the program, as follows:
Independent file: java -classpath mysql-connector-java-5.1.18/mysql-connector-java-5.1.18-bin.jar:. translate translate_6_3 Accounts jp_JP Accounts_0105.java
Within Voodoo2: Translate(translate_6_3, Accounts, jp_JP, Accounts_0105.java);

where,
translate - name of the program
translate_6_3 - the database that is going to be accessed (arg(0))
Accounts - the module the test is from (arg(1))
jp_JP - the language (arg(2))
Accounts_0105.java - the path to the test file (arg(3))


2) A properties file (/resources/translate.properties) must be setup and use the command:
Independent file: java -classpath mysql-connector-java-5.1.18/mysql-connector-java-5.1.18-bin.jar:. translate propPath
Within Voodoo2: Translate(propPath);

where, 
propPath - path to the properties file (e.g. ./src/main/resources/translate.properties)

Setting up properties file:
translate.database - the database that is going to access (eg. translate_6_3)
translate.module - 1) a single Module name or 2) a path to a file containing a list of modules (eg. 1. Accounts or 2. ./resources/module-list)
translate.language - the language (eg. es_ES)
translate.testpath - 1) a path to a single file or 2) a path to a directory with multiple test files (eg. 1. ./TestFiles/Accounts_1234.java or 2. ./TestFiles)
translate.output - an output directory (e.g. ./TestOutput)
translate.server - database server address (eg. 10.8.31.10)
translate.username - database server username (eg. translator)
translate.password - database server password (eg. Sugar123!)

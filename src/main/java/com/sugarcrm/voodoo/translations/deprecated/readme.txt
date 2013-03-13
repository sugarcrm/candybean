the command used to compile the java program:
navigate to the directory with the java file,
javac -classpath mysql-connector-java-5.1.18/mysql-connector-java-5.1.18-bin.jar:. translate.java

Make sure that the latest version mysql-connector is downloaded from: http://dev.mysql.com/downloads/connector/j/

to run the program, use the command:
java -classpath mysql-connector-java-5.1.18/mysql-connector-java-5.1.18-bin.jar:. translate translate_6_3 Accounts jp_JP Accounts_0105bk.xml

where,
translate - name of the program
translate_6_3 - the database that is going to be accessed (arg(0))
Accounts - the module the test is from (arg(1))
jp_JP - the language (arg(2))
Accounts_0105bk.xml - the path to the test file (arg(3))
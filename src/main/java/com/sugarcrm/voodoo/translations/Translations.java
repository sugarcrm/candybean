package com.sugarcrm.voodoo.translations;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.*;
import java.lang.System;

public class Translations {
  private static String currentWorkingPath = System.getProperty("user.dir");
  private static String database;
  private static String module;
  private static String language;
  private static String testPath;
  private static String outputDirPath;
  private static String[] listOfModules;
  private static Properties translateProp = new Properties();
  private static Connection con = null;

  /**
   * consume one argument representing a path to a properties file
   */
  public static void Translate(String propPath) throws Exception {
    try {
      translateProp.load((new FileInputStream(propPath)));

      database = getCascadingPropertyValue(translateProp, "null", "translate.database");
      module = getCascadingPropertyValue(translateProp, "null", "translate.module");
      language = getCascadingPropertyValue(translateProp, "null", "translate.language");
      testPath = getCascadingPropertyValue(translateProp, "null", "translate.testpath");
      outputDirPath = getCascadingPropertyValue(translateProp, "null", "translate.output") + "_" + language;
      // Putting module(s) in a List
      getModuleListIntoArray(module);

      // creating folder for output
      File outputFolder = new File(outputDirPath);
      if (!outputFolder.exists()) outputFolder.mkdir();

      recurseTestFolder(testPath);

    } catch (Exception e) {
      throw new Exception(e);
    }
  }

  /** 
   * first arg: database (translate_6_3, translate_6_2, translate_6_1)
   * second arg: module (Account, Quotes, Contacts, etc)
   * third arg: language (fr_FR, ja_JP, zh_CH, en_UK, etc)
   * forth arg: testPath (path to the test that needs to be converted)
   */
  public static void Translate(String db, String mod, String lang, String testP) throws Exception {
    try {
      //File translatePropertiesFilePath = new File(currentWorkingPath + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "translate.properties");
      //translateProp.load((new FileInputStream(translatePropertiesFilePath)));
      database = db;
      module = mod;
      language = lang;
      testPath = testP;
      outputDirPath = currentWorkingPath + File.separator + "translated_Files" + "_" + language;

      // Putting module(s) in a list
      getModuleListIntoArray(module);

      // creating folder for output
      File outputFolder = new File(outputDirPath);
      if (!outputFolder.exists()) outputFolder.mkdir();

      recurseTestFolder(testPath);

    } catch (Exception e) {
      throw new Exception(e);
    }
  }

  /**
   * @author wli
   *
   * A wrapper function that will run fileReaderWriter() once if the given testpath is a single testfile
   * or multiple times if it is a directory with multiple test files.
   *
   * @param input
   */
  private static void recurseTestFolder(String input) throws Exception {
    try {

      // Check if the input is a path to many test or just one test file
      File inputFile = new File (input);
      if (inputFile.isFile()) { // no need to recurse -- perform fileReaderWriter once
        fileReaderWriter(connect(con, database), module, language, input, outputDirPath + File.separator + inputFile.getName() + "_" + language);
      } else { //else it is a path that could contain many files - perform a check recursively
        File[] files = new File(input).listFiles();
        for(File file: files){
          // for each file: if the file is of type java (fileformat) and contains a module name from the ModuleList then perform translation
          if(file.isFile() && moduleExist(getFileModuleName(file.getName())) && file.getName().contains(".java")) {
            // perform translation
            fileReaderWriter(connect(con, database), getFileModuleName(file.getName()), language, file.getAbsolutePath(), outputDirPath + File.separator + file.getName() + "_" + language);
          }
          if(file.isDirectory()){
            recurseTestFolder(file.getAbsolutePath());
          }
        }
      }
    } catch (Exception e) {
      throw new Exception(e);
    }
  }

  /**
   * @param con
   * @param module
   * @param language
   * @param inputFile
   * @param outputFile
   * 
   * Scans the input file for a set of regex patters: assert, link text, {variable}, module_tab, extramenu, and (data) and
   * outputs to another xml file with the appropriate tanslations made. The converted file should be used with voodoo
   * 
   * If a {variable} or (data) is found, no queries will be performed
   * module_tab and extramenu are ids that change depending on the language that is chosen (this is a product bug that should be filed, if filed, needs to be removed) 
   */
  private static void fileReaderWriter(Connection con, String module, String language, String inputFile, String outputFile) throws Exception {
    // For Voodoo2 support (Assuming the test files are in Java format)
    boolean javaFile = false; 
    if (inputFile.contains(".java")) {
      javaFile = true;
    }

    try {
      File file = new File(inputFile);
      File convertedFile = new File(outputFile);

      Scanner fileScanner = new Scanner(file);
      Writer output = null;

      Pattern pattern_link = Pattern.compile("link text=\"(.*?)\"");
      Pattern pattern_assert = null;
      if (javaFile) { // Java file format (Voodoo2)
        pattern_assert = Pattern.compile("assertEquals\\((.*?)\\)");
      } else { // XML file format
        pattern_assert = Pattern.compile("assert=\"(.*?)\"");
      }

      //these two patterns can be removed once the product bug is fixed
      Pattern pattern_moduletab = Pattern.compile("id=\"moduleTab_(" + module + ")\"");
      Pattern pattern_menuextra_all = Pattern.compile("id=\"moduleTabExtraMenu(All)\"");

      //patterns used to disregard the asserts that are found with { and (
      Pattern pattern_variable = Pattern.compile("\\{");
      Pattern pattern_pageNumber = Pattern.compile("\\(");

      Matcher match_assert = null;
      Matcher match_link = null;

      Matcher match_moduletab = null;
      Matcher match_menuextra_all = null;

      Matcher match_variable = null;
      Matcher match_pageNumber = null;

      output = new BufferedWriter(new FileWriter(convertedFile));

      while(fileScanner.hasNextLine()){
        String line = fileScanner.nextLine();

        match_assert = pattern_assert.matcher(line);
        match_link = pattern_link.matcher(line);

        match_moduletab = pattern_moduletab.matcher(line);
        match_menuextra_all = pattern_menuextra_all.matcher(line);

        match_variable = pattern_variable.matcher(line);
        match_pageNumber = pattern_pageNumber.matcher(line);

        if (match_assert.find()){ // Assert replacement
          if (javaFile && match_variable.find()) { // java file format check (Voodoo2)
            System.out.println("Not finding translation for " + match_assert.group(1));
            output.write(line + "\r\n"); // XML file format check
          } else if(!javaFile && (match_variable.find() || match_pageNumber.find())){
            System.out.println("Not finding translation for " + match_assert.group(1));
            output.write(line + "\r\n");
          } else { // no strange characters, therefore proceed in translation
            if (javaFile) { // for java file format replacement
              String newLine = line.replace(assertSplit(match_assert.group(1)), databaseReplace(con, module, language, assertSplit(match_assert.group(1))));
              output.write(newLine + "\r\n");
            } else { // for XML file format
              String newLine = line.replace(match_assert.group(1), databaseReplace(con, module, language, match_assert.group(1)));
              output.write(newLine + "\r\n");
            }
          }

        } else if(match_link.find()) {
          if(match_variable.find() || match_pageNumber.find()){
            System.out.println("Not finding translation for " + match_link.group(1));
            output.write(line);
          }else {
            String newLine = line.replace(match_link.group(1), databaseReplace(con, module, language, match_link.group(1)));
            output.write(newLine);
          }
        } else if(match_moduletab.find()) {
          String newLine = line.replace(match_moduletab.group(1), databaseReplace(con, "SugarFeed", language, "All") + "_" + module);
          output.write(newLine);
        } else if(match_menuextra_all.find()) {
          String newLine = line.replace(match_menuextra_all.group(1), databaseReplace(con, "SugarFeed", language, match_menuextra_all.group(1)));
          output.write(newLine);
        } else {
          output.write(line+"\r\n");
        }
      }
      output.close();
      fileScanner.close();
    } catch (Exception e){
      throw new Exception(e.getMessage());
    }
    }

    /**
     * @param con
     * @param database
     * @return a mysql connection object that is used for database queries
     */
    @SuppressWarnings("finally")
      private static Connection connect(Connection con, String database) throws Exception {
        String serverName = getCascadingPropertyValue(translateProp, "10.8.31.10", "translate.serverName");
        String username = getCascadingPropertyValue(translateProp, "translator", "translate.username");
        String password = getCascadingPropertyValue(translateProp, "Sugar123!", "translate.password");
        System.out.println("connecting with: " + serverName + " " + username + " " + password);
        try {
          // Create a connection to the database
          String driverName = "com.mysql.jdbc.Driver"; // MySQL MM JDBC driver
          Class.forName(driverName);
          String url = "jdbc:mysql://" + serverName +  File.separator + database; // a JDBC url
          con = DriverManager.getConnection(url, username, password);
          System.out.println("Connection to database successfull!");
        } catch (Exception e){
          throw new Exception(e.getMessage());
        } finally {
          return con;
        }
      }

    /**
     * @param con
     * @param module
     * @param language
     * @param english
     * @return the translated string
     * 
     * queries the module table that is passed. However, if the translation does not exist within the current module,
     * there will be a search in all modules
     */
    @SuppressWarnings("finally")
      private static String databaseReplace(Connection con, String module, String language, String english) throws Exception {
        String result = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
          pst = con.prepareStatement("SELECT " + language + " FROM " + module + " WHERE english = " + "\'" + english + "\'");
          rs = pst.executeQuery();

          //if there is a result due to the query, the translated value is returned
          //else, there will be queries on all of the modules until a translated value is found (it will return the first one it finds)
          if (rs.next()) {
            result = (rs.getString(1));
            System.out.println("Replaced english: " + english + " with " + language + ": " + result);
          } else {
            System.out.println("Could not find the translation for " + english + " in the " + module + " module");
            int counter = 0;
            String[] tables = tableNames(con);

            while (tables[counter] != null){
              PreparedStatement pst_ifExists = null;
              ResultSet rs_ifExists = null;

              pst_ifExists = con.prepareStatement("SHOW columns from `" + tables[counter] + "` where field='" + language + "'");
              rs_ifExists = pst_ifExists.executeQuery();

              //checks to see if the language is in that particular module
              if (rs_ifExists.next()){
                PreparedStatement pst_temp = null;
                ResultSet rs_temp = null;

                pst_temp = con.prepareStatement("SELECT " + language + " FROM " + tables[counter] + " WHERE english = " + "\'" + english + "\'");
                rs_temp = pst_temp.executeQuery();
                if (rs_temp.next()) {
                  result = (rs_temp.getString(1));
                  System.out.println("Replaced english: " + english + " with " + language + ": " + result + " from the " + tables[counter] + " module");
                  break;
                } else {
                  System.out.println("Could not find the translation for " + english + " in the " + tables[counter] + " module");
                  result = english;
                  rs_temp.close();
                  pst_temp.close();
                  counter ++;
                }
              } else {
                System.out.println("The table: " + tables[counter] + " does not contain the language: " + language);
                counter ++;
              }

            }
          }
        } catch (SQLException e) {
          throw new Exception(e.getMessage());
        } finally {	
          return result;
        }
      }

    /**
     * @param con
     * @return an array of Table names within the database that is being queried
     * 
     * This function is used in the databaseReplace function
     */
    private static String[] tableNames(Connection con) throws Exception {
      int counter = 0; 
      try {
        DatabaseMetaData dbmd = con.getMetaData();
        String[] tables = new String[dbmd.getMaxTablesInSelect()];

        String[] types = {"TABLE"};
        ResultSet resultSet = dbmd.getTables(null, null, "%", types);

        // Get the table names
        while (resultSet.next()) {
          String tableName = resultSet.getString(3);
          tables[counter] = tableName;
          counter++;
        }
        return tables;
      } catch (SQLException e) {
        throw new SQLException(e.getMessage());
      }
    }

    /**
     * @author wli
     *
     * This function was implemented for Voodoo2 to replace 
     * the [Expected Value] from the following assert statement: Assert.assertEquals( [Message], [Expected Value], [Actual Value] );
     * to a language-specific value retrieved from the DATABASE
     *
     * @param str
     * @return a string representing the value to be translated 
     */
    private static String assertSplit(String assertStr) throws Exception {
      String tempString = "";
      String prevString = "";
      String[] statement = assertStr.split("");
      ArrayList<String> argumentListString = new ArrayList<String>();
      int assertType = 0; 
      boolean withinQuote = false;

      // Extracting arguments from the given string
      for (int index = 0; index < statement.length; index++) {
        if (statement[index].equals("\"") && !withinQuote){ // beginning of a quoted string
          prevString = statement[index];
          tempString += statement[index]; // build tempString
          withinQuote = true;
        } else if (withinQuote) { // within a quoted string
          if (!prevString.equals("\\") && statement[index].equals("\"")) { // Is within quote but has reached the end of the quoted string
            prevString = statement[index];
            tempString += statement[index];
            withinQuote = false;
          } else { // still within quoted string and not yet the end
            // keep building the string
            prevString = statement[index];
            tempString += statement[index];
          }
        } else { //This is not within a quote
          //simply a variable - loop until a comma is seen
          if (statement[index].equals(",")) {
            // if a comma is seen then place tempString into  the ArgumentList
            prevString = statement[index];
            argumentListString.add(tempString);
            tempString = "";
          } else {
            // else keep building the tempString
            prevString = statement[index];
            tempString += statement[index];
          }
        }
      }
      // Concatenating the last element
      argumentListString.add(tempString);

      // Defining assertType according to te number of arguments retrieved
      if (argumentListString.size() == 2) assertType = 0;
      else if (argumentListString.size() == 3) assertType = 1;
      else throw new Exception("Invalid Number of Arguments, got " + argumentListString.size() + " argument(s)");

      // trim off white spaces
      String ret = argumentListString.get(assertType).trim();

      // if possible trim off beginning and ending quotes marks
      if (ret.substring(0,1).equals("\"") && ret.substring(ret.length()-1, ret.length()).equals("\"")) {
        ret = ret.substring(1,ret.length()-1);
        return ret;
      } else {
        return ret;
      }

    }

    /**
     * @author wli
     *
     * Used to get argument values from the properties file 
     *
     * @param props
     * @param defaultValue
     * @param key 
     * @return a string representing the value from a given key 
     */
    private static String getCascadingPropertyValue(Properties props, String defaultValue, String key) {
      String value = defaultValue;
      if (props.containsKey(key) && value == "null") value = props.getProperty(key);
      if (!props.containsKey(key) && value == "null") {
        System.out.println("Property file does not contain such key: " + key);
        System.exit(0);
      }
      if (value != "null" && props.containsKey(key)) {
        value = props.getProperty(key);
      }
      return value;
    }

    /**
     * @author wli
     *
     * Gets the Module string from the file name (e.g. Accounts_0123.java = Accounts)
     *
     * @param fileName
     * @return a string representing the the module name
     */
    private static String getFileModuleName(String fileName) {
      String tempModule = "";
      for (int index = 0; index < fileName.length(); index++) {
        String sString = fileName.substring(index,index+1);
        if (sString.equals("_")) break;
        else { tempModule += sString; } 
      }
      return tempModule;
    }

    /**
     * @author wli
     *
     * Check whether a string exist in the ModulesList array [helper function]
     *
     * @param mName
     * @return a boolean value, true if the mName (module name) exist in the ModuleList array, else return false 
     */
    private static boolean moduleExist(String mName){
      for (String module : listOfModules) {
        if (module.equals(mName)) return true;
      }
      return false;
    }

    /**
     * @author wli
     *
     * read from a file (a list of modules from the given path) and place them into an array (ModulesList)
     *
     * @param path 
     */
    private static void getModuleListIntoArray(String path) throws Exception {
      BufferedReader BR = null;
      String line = null;
      String modules = "";
      try {
        File testFile = new File(path);
        if (!testFile.isFile()) {
          System.out.println("Using the following module: " + path);
          listOfModules = (path + ":").split(":");
        } else {
          System.out.println("Obtaining modules from file: " + path);

          BR = new BufferedReader (new FileReader(path));
          while ((line = BR.readLine()) != null) {
            modules = modules + ":" + line;
          }
          listOfModules = modules.split(":");
        }
      } catch (Exception e) {
        throw new Exception(e.getMessage());
      } finally {
        try {
          if (BR != null) BR.close();
        } catch (IOException e) {
          throw new Exception(e.getMessage());
        }
      }
    }
  }

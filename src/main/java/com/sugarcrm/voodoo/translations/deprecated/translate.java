import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.*;
import java.lang.System;


/**
 * @author Sid Bhargava
 *
 *The goal of this program is a proof of concept.
 *The idea is for QA to test on multiple languages using an automated process
 *This program is passed in a file and the appropriate arguments with the language specified
 *The translation database is queried for the appropriate translation and a file is generated with the appropriate translated changes
 */
public class translate {

	/**
	 * @param args
	 * first arg: database (translate_6_3, translate_6_2, translate_6_1)
	 * second arg: module (Account, Quotes, Contacts, etc)
	 * third arg: language (fr_FR, ja_JP, zh_CH, en_UK, etc)
	 * forth arg: testPath (path to the test that needs to be converted)
	 * 
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Connection con = null;
		String database = args[0];
		String module = args[1];
		String language = args[2];
		String testPath = args[3];
		
		fileReaderWriter(connect(con, database), module, language, testPath, testPath + "_" + language);
}
	
	/**
	 * @param con
	 * @param database
	 * @return a mysql connection object that is used for database queries
	 */
	@SuppressWarnings("finally")
	public static Connection connect(Connection con, String database){
        System.out.println("Accessing " + database + " database.");
        
        String serverName = "10.8.31.10";
        String username = "translator";
        String password = "Sugar123!";
        
		try {
			// Create a connection to the database
            String driverName = "com.mysql.jdbc.Driver"; // MySQL MM JDBC driver
            Class.forName(driverName);
            
			String url = "jdbc:mysql://" + serverName +  "/" + database; // a JDBC url
            
            con = DriverManager.getConnection(url, username, password);
            
            System.out.println("Connection to database successfull!");

		} catch (Exception ex){
			System.out.println(ex.getMessage());
			// Could not connect to the database
            Logger lgr = Logger.getLogger(translate.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
		} finally {
			return con;
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
	public static void fileReaderWriter(Connection con, String module, String language, String inputFile, String outputFile){
        
		try {
			File file = new File(inputFile);
	        File convertedFile = new File(outputFile);
	        
			Scanner fileScanner = new Scanner(file);
			Writer output = null;

	        Pattern pattern_assert = Pattern.compile("assert=\"(.*?)\"");
	        Pattern pattern_link = Pattern.compile("link text=\"(.*?)\"");
	        
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
	        	
	        	if (match_assert.find()){
	        		if(match_variable.find() || match_pageNumber.find()){
	        			System.out.println("Not finding translation for " + match_assert.group(1));
	        			output.write(line);
	        		}else {
		        		String newLine = line.replace(match_assert.group(1), databaseReplace(con, module, language, match_assert.group(1)));
		        		output.write(newLine);
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
	        		output.write(line);
	        	}
	        }
			output.close();
		} catch (Exception ex){
			System.out.println(ex.getMessage());
			// Could not connect to the database
            Logger lgr = Logger.getLogger(translate.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
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
	public static String databaseReplace(Connection con, String module, String language, String english){
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
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
			// Could not connect to the database
            Logger lgr = Logger.getLogger(translate.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
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
	public static String[] tableNames(Connection con){
		
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
		    
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
			// Could not connect to the database
            Logger lgr = Logger.getLogger(translate.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
		}
		return null;
	}
}

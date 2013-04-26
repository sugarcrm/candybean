<?php

# Author: Jason Lin (ylin), Sid
# Purpose: Update a database (create it if necessary) with the provided php files containing key-value pairs. Each key-value pair is a label-to-text mapping for some language.
# Output: A database containing tables of language strings that can be used for translations. Each table contains a common label and texts in different languages.
# Input: $db_name $en_folder $for_folder $db_server $db_user $db_pass
#   $db_name - name of the database to be updated (or created if necessary), for example Translations_6_7
#   $en_folder - directory of English php files (Sugar installation)
#   $for_folder - directory of foreign language php files (Sugar installation or sugarcrm/translations repository)
#	  $db_server - database server (centralized server: 10.8.31.10)
#	  $db_user - database username (centralized server: translator)
#	  $db_pass - database password (centralized server: Sugar123!)

// load in two arrays, $module_array and $language_array from updateDB_LangsAndModules.php
require ('updateDB_LangsAndModules.php');

$db_name = $argv[1];
$en_folder = $argv[2]; // dir containing English php files, should be set to a Sugar installation
$for_folder = $argv[3]; // dir containing foreign language php files, should be set to a Sugar installation or sugarcrm/translations repository
$link = mysqli_connect($argv[4], $argv[5], $argv[6]);
mysqli_set_charset($link, "utf8");

echo "\nUsing foreign language files from $for_folder.\n";

define("sugarEntry", "true");

$create_db = "CREATE DATABASE IF NOT EXISTS $db_name DEFAULT CHARACTER SET = utf8 DEFAULT COLLATE utf8_general_ci";
mysqli_query($link, $create_db) or die(mysql_error($link));

mysqli_select_db($link, $db_name);

if (!$link) {
    die("\nCould not connect: " . mysqli_error($link) . ".\n");
}
else echo "\nConnected to database $argv[4] successfully.\n";

echo "\nUsing database $db_name.\n";

for ($counter = 0; $counter < count($module_array); $counter++) {
    build_table($link, $module_array[$counter]);
    echo "\n";
    store_by_language($link, $language_array, $module_array[$counter], $en_folder, $for_folder);
}

mysqli_close($link);

/* construct a table for each module in module_array
 * 
 * input:
 * $link - active MySQL connection
 * $module - current module
 */
function build_table($link, $module)
{
    $drop_table = "DROP TABLE IF EXISTS `" . $module . "`";
    mysqli_query($link, $drop_table) or die(mysqli_error($link));

    $add_table = "CREATE TABLE " . $module . "(ID INTEGER AUTO_INCREMENT PRIMARY KEY, Label TEXT) ENGINE=MyISAM";
    mysqli_query($link, $add_table) or die(mysqli_error($link));
}

/*
 * construct a column for each language in language_array
 * return 0 if no english file was found in the current build of sugar
 *
 * input:
 * $link - active MySQL connection
 * $language_array - array of language codes from updateDB_LangsAndModules.php
 * $module - current module
 * $en_folder - directory of English php files
 * $for_folder - directory of foreign language php files
 */
function store_by_language($link, $language_array, $module, $en_folder, $for_folder)
{
	$en_file = "$en_folder/modules/$module/language/en_us.lang.php";
    	//echo "\nLoading file into database: " . strstr($en_file, "$module/") . ".\n";
    	if (file_exists($en_file)){
        	include_once "$en_folder/modules/$module/language/en_us.lang.php";
		array_to_db($link, $module, $mod_strings, "en_us");
        	echo "Loaded file into database: " . strstr($en_file, "$module/") . ".\n";
		
        	for ($i = 0; $i < count($language_array); $i++) {
            		$for_file = "$for_folder/modules/$module/language/" . $language_array[$i] . ".lang.php";
	    		//echo "\nLoading file into database: " . strstr($for_file, "$module/") . ".\n";
            		if (file_exists($for_file)){
                		include_once "$for_folder/modules/$module/language/" . $language_array[$i] . ".lang.php";
				array_to_db($link, $module, $mod_strings, $language_array[$i]);
	        		echo "Loaded file into database: " . strstr($for_file, "$module/") . ".\n";
               		} else {
                    		echo "      The language file: " . $language_array[$i] . " in the module $module does not exist.\n";
                	}
		}
	} else {
        	echo "The module $module could not be added.\n\n";
        	return 0;
	}
}

/*
 * convert key-value pairs in a language php file to keys and values in the database
 *
 * input:
 * $link - active MySQL connection
 * $module - current module
 * $array_data - the array of key-value pairs inside a language php file
 * $language - current language code
 */
function array_to_db($link, $module, $array_data, $language)
{
	$add_column = "ALTER TABLE $module ADD $language TEXT";
	mysqli_query($link, $add_column) or die (mysqli_error($link));
	array_to_db_recursive($link, $module, $array_data, $language, "");
}

/* helper function for array_to_db to handle nested arrays
 * current key generation policy: append key in inner array to key generated up to the outer array
 */ 
function array_to_db_recursive($link, $module, $array_data, $language, $prev_key) {
	foreach ($array_data as $key => $value) {
		$keys[] = $key;
		$values[] = $value;

		if ($prev_key != "") {
			$new_key = $prev_key . "_" . $key;
		} else {
			$new_key = $key;
		}
		$new_value = str_replace("'", "", $value);
		
		// current value is not an array, write current key and current value to DB
		if (!is_array($value)) {
			insert_key($link, $new_key, $module);
			update_value($link, $new_value, $new_key, $module, $language);
		}
		// current value is an array, recursively process key-value pairs inside current value 
		else {
			array_to_db_recursive($link, $module, $value, $language, $new_key);
		}
	}
}

/*
 * insert new key in the current module
 *
 * inputs:
 * $link - active MySQL connection
 * $key - key to be inserted
 * $module - current module
 */
function insert_key($link, $key, $module)
{
	$insert_key = "INSERT IGNORE INTO `$module` (`Label`) VALUES ('" . mysqli_real_escape_string($link, $key) . "')";
	mysqli_query($link, $insert_key) or die (mysqli_error($link));
}

/*
 * update the value in the current module where Label matches key
 *
 * inputs:
 * $link - active MySQL connection
 * $value - value to be inserted
 * $key - key to be matched in the Label column
 * $module - current module
 * $language - current language code, used to select column
 */
function update_value($link, $value, $key, $module, $language)
{
	$update_value = "UPDATE `$module` SET `$language` = '" . mysqli_real_escape_string($link, $value) . "' WHERE Label = '" . mysqli_real_escape_string($link, $key) . "'";
    	mysqli_query($link, $update_value) or die (mysqli_error($link));
}

?>

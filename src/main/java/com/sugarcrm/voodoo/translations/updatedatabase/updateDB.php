<?php

require ('updateDB_LangsAndModules.php');

$db = $argv[1];
$en_folder = $argv[2]; // dir containing English php files, should be set to a Sugar installation
$for_folder = $argv[3]; // dir containing foreign language php files, should be set to a Sugar installation or sugarcrm/translations repository
$link = mysqli_connect($argv[4], $argv[5], $argv[6]);

echo "\nUsing English files from $en_folder.\n";
echo "\nUsing foreign language files from $for_folder.\n";

define("sugarEntry", "true");

$create_db = "CREATE DATABASE IF NOT EXISTS $db DEFAULT CHARACTER SET = utf8 DEFAULT COLLATE utf8_general_ci";
mysqli_query($link, $create_db) or die(mysql_error($link));

mysqli_select_db($link, $db);

if (!$link) {
    die("\nCould not connect: " . mysqli_error($link) . ".\n");
}
else echo "\nConnected to database $argv[4] successfully.\n";

echo "\nUsing database $db.\n";

for ($counter = 0; $counter < count($module_array); $counter++) {
    build_table($link, $module_array[$counter]);
    mysqli_set_charset($link, 'utf8');
    store_by_language($link, $language_array, $module_array[$counter], $en_folder, $for_folder);
}

mysqli_close($link);

/*
 * Contructs the module specific table
 * Note that the mysql engine being used is MyISAM, this is because of a bug in the default mysql engine
 *
 * inputs:
 * module - the array that contains the key-value pairs of a particular language (from the translation repository)
 */
function build_table($link, $module)
{
    $drop_table = "DROP TABLE IF EXISTS `" . $module . "`";
    mysqli_query($link, $drop_table) or die(mysqli_error($link));

    $add_table = "CREATE TABLE " . $module . "(ID INTEGER AUTO_INCREMENT PRIMARY KEY, Label TEXT) ENGINE=MyISAM";
    mysqli_query($link, $add_table) or die(mysqli_error($link));
}

/*
 *constructs a columns based on language
 *
 * returns 0 if no english file was detected in the current build of sugar
 *
 * input:
 * language - values taken from the language array
 * module - the array that contains the key-value pairs of a particular language (from the translation repository)
 * jobspace - arg[1], passed into the program by Jenkins build
 *
 */
function store_by_language($link, $language_array, $module, $en_folder, $for_folder)
{
	echo "\n";
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
 * Converts values from the php array to keys and values in the database
 *
 * input:
 * module - the array that contains the key-value pairs of a particular language (from the translation repository)
 * array_data - the array, one key-value pair at a time
 * language - values taken from the language array
 */
function array_to_db($link, $module, $array_data, $language)
{
	$add_column = "ALTER TABLE $module ADD $language TEXT";
	mysqli_query($link, $add_column) or die (mysqli_error($link));
	array_to_db_recursive($link, $module, $array_data, $language, "");
}

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
			update_key($link, $new_key, $module);
			update_value($link, $new_value, $new_key, $module, $language);
		}
		// current value is an array, recursively process key-value pairs inside current value 
		else {
			array_to_db_recursive($link, $module, $value, $language, $new_key);
		}
	}
}

/*
 * insert the keys into the appropriate cell in the database
 *
 * inputs:
 * key - the key in the array
 * module - the array that contains the key-value pairs of a particular language (from the translation repository)
 * index - used as the ID
 */
function update_key($link, $key, $module)
{
	$update_key = "INSERT IGNORE INTO `$module` (`Label`) VALUES ('" . mysqli_real_escape_string($link, $key) . "')";
	mysqli_query($link, $update_key) or die (mysqli_error($link));
}

/*
 * insert the value into the appropriate cell in the database depending on the key
 *
 * inputs:
 * value - the value in the array
 * key - the key in the array
 * module - the array that contains the key-value pairs of a particular language (from the translation repository)
 * index - used as the ID
 */
function update_value($link, $value, $key, $module, $language)
{
	$update_value = "UPDATE `$module` SET `$language` = '" . mysqli_real_escape_string($link, $value) . "' WHERE Label = '" . mysqli_real_escape_string($link, $key) . "'";
    	mysqli_query($link, $update_value) or die (mysqli_error($link));
}

?>

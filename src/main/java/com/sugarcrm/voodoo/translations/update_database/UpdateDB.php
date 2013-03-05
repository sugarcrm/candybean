<?php

require ('UpdateDB_LangsAndModules.php');

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

for ($counter = 0; $counter < sizeof($module); $counter++) {
    build_table($link, $module[$counter]);
    mysqli_set_charset($link, 'utf8');
    store_by_language($link, $language, $module[$counter], $en_folder, $for_folder);
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
function store_by_language($link, $language, $module, $en_folder, $for_folder)
{
	echo "\n";
	$en_file =  $en_folder . '/modules/' . $module . '/language/en_us.lang.php';
    	//echo "\nLoading file into database: " . strstr($en_file, "$module/") . ".\n";
    	if (file_exists($en_file)){
        	include_once $en_folder . '/modules/' . $module . '/language/en_us.lang.php';
		array_to_db($link, $module, $mod_strings, "en_us");
        	echo 'Loaded file into database: ' . strstr($en_file, "$module/") . ".\n";
		
        	for ($i = 0; $i < sizeof($language); $i++) {
            		$for_file = $for_folder . '/modules/' . $module . '/language/' . $language[$i] . '.lang.php';
	    		//echo "\nLoading file into database: " . strstr($for_file, "$module/") . ".\n";
            		if (file_exists($for_file)){
                		include_once $for_folder . '/modules/' . $module . '/language/' . $language[$i] . '.lang.php';
				array_to_db($link, $module, $mod_strings, $language[$i]);
	        		echo 'Loaded file into database: ' . strstr($for_file, "$module/") . ".\n";
               		} else {
                    		echo "      The language file: " . $language[$i] . " in the module: " . $module . " does not exist.\n";
                	}
		}
	} else {
        	echo "The module " . $module . " could not be added.\n\n";
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
	$add_column = "ALTER TABLE " . $module . " ADD " . $language . " TEXT";
	mysqli_query($link, $add_column) or die (mysqli_error($link));
	echo "add column query: $add_column\n";
	$db_index = 1;
	array_to_db_recursive($link, $module, $array_data, $language, "", $db_index);
}

function array_to_db_recursive($link, $module, $array_data, $language, $prev_key, &$db_index) {
	$array_index = 0;
	foreach ($array_data as $key => $value) {
		$keys[] = $key;
		$values[] = $value;
		$new_key = $keys[$array_index];
		if ($prev_key != "") {
			$new_key = $prev_key . "_" . $new_key;
		}
		$new_value = str_replace("'", "", $values[$array_index]);
		
		if (!is_array($values[$array_index])) {
			update_key($link, $new_key, $module, $db_index);
			update_value($link, $new_value, $new_key, $module, $language);
		} else {
			array_to_db_recursive($link, $module, $values[$array_index], $language, $new_key, $db_index);
		}

		$array_index++;
		$db_index++;
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
function update_key($link, $key, $module, $index)
{
	$update_key = "INSERT IGNORE INTO `" . $module . "` (`ID`, `Label`) VALUES (" . $index . ", '" . mysqli_real_escape_string($link, $key) . "')";
	mysqli_query($link, $update_key) or die (mysqli_error($link));
	//echo "update_key query: $update_key.\n";
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
	$update_value = "UPDATE `" . $module . "` SET `" . $language . "` = '" . mysqli_real_escape_string($link, $value) . "' WHERE Label = '" . mysqli_real_escape_string($link, $key) . "'";
    	mysqli_query($link, $update_value) or die (mysqli_error($link));
	//echo "update_value query: $update_value.\n";
}

?>

<?php

require ('updateDB_LangsAndModules.php');

$db_name = $argv[1];
$en_folder = $argv[2]; // dir containing English php files, should be set to a Sugar installation
$for_folder = $argv[3]; // dir containing foreign language php files, should be set to a Sugar installation or sugarcrm/translations repository
$mysqli = new mysqli($argv[4], $argv[5], $argv[6]);
if ($mysqli->connect_errno) {
    echo "Failed to connect to MySQL: (" . $mysqli->connect_errno . ") " . $mysqli->connect_error;
}
$mysqli->set_charset("utf8");

echo "\nUsing English files from $en_folder.\n";
echo "\nUsing foreign language files from $for_folder.\n";


$create_db = "CREATE DATABASE IF NOT EXISTS $db_name";
mysqli->query($create_db) or die(mysql_error($link));

mysqli->select_db($db_name);

else echo "\nConnected to $argv[4] successfully.\n";

echo "\nUsing database $db_name.\n";

$table_name = "all_language_strings";
for ($i = 0; $i < sizeof($module); $i++) {
  build_table($link, $table_name]);
  mysqli_set_charset($link, 'utf8');
  store_by_language($link, $language, $module[$i], $en_folder, $for_folder);
}

mysqli->close();

/*
 * Contructs the module specific table
 * Note that the mysql engine being used is MyISAM, this is because of a bug in the default mysql engine
 *
 * inputs:
 * module - the array that contains the key-value pairs of a particular language (from the translation repository)
 */
function build_table($link, $table_name)
{
  $drop_table = "DROP TABLE IF EXISTS `" . $table_name . "`";
  mysqli->query($drop_table) or die (mysql->error());

  $add_table = "CREATE TABLE " . $table_name . "(ID INTEGER AUTO_INCREMENT PRIMARY KEY, Module TEXT, Label TEXT)";
  mysqli->query($add_table) or die (mysql->error());

  for ($i = 0; $i < sizeof($language); $i++) {
    $add_column = "ALTER TABLE " . $table_name . " ADD " . $language . " TEXT";
	  mysqli->query($add_column) or die (mysqli->error());
  }
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
	$en_file = "$en_folder/modules/$module/language/en_us.lang.php";
  //echo "\nLoading file into database: " . strstr($en_file, "$module/") . ".\n";
  if (file_exists($en_file)){
    include_once "$en_folder/modules/$module/language/en_us.lang.php";
    array_to_db($link, $module, $mod_strings, "en_us");
    echo 'Loaded file into database: ' . strstr($en_file, "$module/") . ".\n";

    for ($i = 0; $i < sizeof($language); $i++) {
      $for_file = "$for_folder/modules/$module/language/" . $language[$i] . ".lang.php";
      //echo "\nLoading file into database: " . strstr($for_file, "$module/") . ".\n";
      if (file_exists($for_file)){
        include_once "$for_folder/modules/$module/language/" . $language[$i] . "lang.php";
        array_to_db($link, $module, $mod_strings, $language[$i]);
        echo "Loaded file into database: " . strstr($for_file, "$module/") . ".\n";
      } else {
      echo "      The language file: " . $language[$i] . " in the module $module does not exist.\n";
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
		
		// current value is not an array, write current key and current value to DB
		if (!is_array($values[$array_index])) {
			update_key($link, $new_key, $module, $db_index);
			update_value($link, $new_value, $new_key, $module, $language);
		}
		// current value is an array, recursively process key-value pairs inside current value 
		else {
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
  $escaped_key = mysqli->real_escape_string($key);
	$update_key = "INSERT IGNORE INTO $table_name (Module, Label) VALUES ('$module', '$escaped_key')";
	mysqli->query($update_key) or die (mysqli->error());
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
  $escaped_key = mysqli->real_escape_string($key);
  $escaped_value = mysqli->real_escape_string($value);
	$update_value = "UPDATE $table_name SET $language='$escaped_value' WHERE Label = '$escaped_key' AND ";
  mysqli->query($update_value) or die (mysqli->error());
}

?>

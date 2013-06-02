<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8"/>
  </head>
  <body>
    <p>
<?php 

$english = $_POST["english"]; 
$language = $_POST["language"]; 
$mysqli = new mysqli("10.8.31.10", "translator", "Sugar123!", "Translations_6_7_latest");

if ($mysqli->connect_errno) {
    echo "Failed to connect to MySQL: (" . $mysqli->connect_errno . ") " . $mysqli->connect_error;
}
$mysqli->set_charset("utf8");
$results = $mysqli->query("SHOW TABLES");

for ($i = 0; $i < $results->num_rows; $i++) {
  $result = $results->fetch_array();
  $modules[] = $result[0];
}

foreach ($modules as $module) {
  $results = $mysqli->query("SELECT $language, Label FROM $module WHERE en_us='$english'");
  for ($i = 0; $i < $results->num_rows; $i++) {
    $result = $results->fetch_array();
    echo $module . ": " . $result[0] . " (" . $result[1] . ")<br />";
  }
}

?>
    </p>
    <p><a href="javascript:history.go(-1)">Back</a></p>
  </body>
</html>

<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8"/>
  </head>
  <body>
    <p>
<?php 
$english = $_POST["english"]; 
$translate_language = $_POST["language"];

$mysqli = new mysqli("10.8.31.10", "translator", "Sugar123!", "Translations_6_7_latest");
if ($mysqli->connect_errno) {
    echo "Failed to connect to MySQL: (" . $mysqli->connect_errno . ") " . $mysqli->connect_error;
}
$mysqli->set_charset("utf8");

// add all DB table names into $modules[]
$results = $mysqli->query("SHOW TABLES");
for ($i = 0; $i < $results->num_rows; $i++) {
  $result = $results->fetch_array();
  $modules[] = $result[0];
}

// user clicked "View translations", print all translations of $english in the selected $translate_language
if ($_POST["translate"]) {

  // for each module, find and print all translations for $english
  foreach ($modules as $module) {
    $results = $mysqli->query("SELECT $translate_language, Label FROM $module WHERE en_us='$english'");

    // print all results
    for ($i = 0; $i < $results->num_rows; $i++) {
      $result = $results->fetch_array();
      echo "$module: " . $result[$translate_language] . "(" . $result['Label'] . ")<br />";
    }
  }
}

// user clicked "Validate", print valid or invalid; if invalid, print ambiguous translations
if ($_POST["validate"]) {
    $valid = true;

    // store all translations for $english in all languages except English
    include_once("language.php");
    foreach ($language as $code => $native) {
      // skip English
      if ($code != "en_us") {
        foreach ($modules as $module) {
          $results = $mysqli->query("SELECT Label, $code FROM $module WHERE en_us='$english'");
          for ($i = 0; $results && $i < $results->num_rows; $i++) {
            $result = $results->fetch_array();
            $label = $result["Label"];
            $value = $result[$code];
            if ($label != null && $value != null) {
              // === used solely as delimiter for string splitting later on
              $lang_strings[] = "$code===$module===$value===$label";
            }
          }
        } // end of for each module
      }
    }

    // sort by language, then by module, then by value, then by label
    sort($lang_strings);

    for ($i = 0; $i < count($lang_strings); $i++) {
      $splitted = explode("===", $lang_strings[$i]);
      $current_code = $splitted[0];
      $current_module = $splitted[1];
      $current_value = $splitted[2];
      if ($i == 0) {
        $last_code = $current_code;
        $values_found[] = $current_value;
        $translations_found[] = $lang_strings[0];
      } else if ($last_code == $current_code && !in_array($current_value, $values_found)) {
        // within the same language, found two different translations
        $values_found[] = $current_value;
        $translations_found[] = $lang_strings[$i];
      } else if ($last_code != $current_code) {
        // finished processing all translations of the last language ($last_code)
        if (count($translations_found) > 1) {
          echo "$last_code<br/>";
          for ($j = 0; $j < count($translations_found); $j++) {
            $splitted2 = explode("===", $translations_found[$j]);
            $translation_module = $splitted2[1];
            $translation_value = $splitted2[2];
            $translation_label = $splitted2[3];
            echo "$translation_module: $translation_value ($translation_label)<br/>";
          }
          echo "<br/>";
        }
        // reset variables for next language
        $last_code = $current_code;
        $values_found = array();
        $values_found[] = $current_value;
        $translations_found = array();
        $translations_found[] = $lang_strings[$i];
      } else if ($current_code == "zh_CN" && $i == count($lang_strings) - 1) {
        // finished processing the last language, zh_CN
        if (count($translations_found) > 1) {
          echo "$current_code<br/>";
          for ($j = 0; $j < count($translations_found); $j++) {
            $splitted2 = explode("===", $translations_found[$j]);
            $translation_module = $splitted2[1];
            $translation_value = $splitted2[2];
            $translation_label = $splitted2[3];
            echo "$translation_module: $translation_value ($translation_label)<br/>";
          }
        }
      } else {
        // within the same language, found the same translation again
        // do nothing
      }
    } // end of for each language
}
?>
    </p>
    <p><a href="javascript:history.go(-1)">Back</a></p>
  </body>
</html>

<!-- Candybean is a next generation automation and testing framework suite.
   - It is a collection of components that foster test automation, execution
   - configuration, data abstraction, results illustration, tag-based execution,
   - top-down and bottom-up batches, mobile variants, test translation across
   - languages, plain-language testing, and web service testing.
   - Copyright (C) 2013 SugarCRM, Inc. <candybean@sugarcrm.com>
   - 
   - This program is free software: you can redistribute it and/or modify
   - it under the terms of the GNU Affero General Public License as
   - published by the Free Software Foundation, either version 3 of the
   - License, or (at your option) any later version.
   - 
   - This program is distributed in the hope that it will be useful,
   - but WITHOUT ANY WARRANTY; without even the implied warranty of
   - MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   - GNU Affero General Public License for more details.
   - 
   - You should have received a copy of the GNU Affero General Public License
   - along with this program.  If not, see <http://www.gnu.org/licenses/>. -->
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

  $found_translation = false;
  // for each module, find and print all translations for $english
  foreach ($modules as $module) {
    $results = $mysqli->query("SELECT $translate_language, Label FROM $module WHERE en_us='$english'");

    if ($results->num_rows >= 1) {
      $found_translation = true;
    }
    // print all results
    for ($i = 0; $i < $results->num_rows; $i++) {
      $result = $results->fetch_array();
      echo "$module: " . $result[$translate_language] . "(" . $result['Label'] . ")<br />";
    }
  }

  include_once "language.php";
  echo "No translation found for $english in " . $language[$translate_language] . "!";
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
  } // end of for each language

  // sort by language, then by module, then by value, then by label
  sort($lang_strings);

  if (empty($lang_strings)) {
    echo "No translation found for $english!";
  } else {
    $valid = true;
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
        $valid = false;
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

    if ($valid) {
      echo "$english is a valid string for translation!";
    }
  }
}
?>
    </p>
    <p><a href="javascript:history.go(-1)">Back</a></p>
  </body>
</html>

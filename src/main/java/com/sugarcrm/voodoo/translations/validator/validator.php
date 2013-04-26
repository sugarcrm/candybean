<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8"/>
    <title>Translation Validator</title>
  </head>
  
  <body>
    <h1>Translation Validator</h1>
    <p>Validates whether an English string is suitable for translation.</p>
    <p>Lists all translations of the input string in the selected language.</p>
    <p>When using an English string in a SodaIceBox test, use this tool to check if the string is a candidate for translations. If the string is, using it means the resulting test can be translated to another language and be fully functional.</p>
    <p>Validating the string returns either: 1) the string is valid (go ahead and use it!) or 2) a list of different translations that exist for this string (consider using another string!)</p>
    <p>View translations can be used to see translations for an English string in a specific language</p>
    <form action="validator_results.php" method="post">
      English: <input type="text" name="english"/><br/>
      <input type="submit" name="validate" value="Validate"/><br/><br/>
      Language: <select name="language">
<?php
  include_once "language.php";
  // language.php contains an array of key-value pairs that represent language codes mapped to language names in their native languages
  foreach ($language as $code => $native) {
    echo "      <option value=\"$code\">$native</option>\n";
  }
?>
      </select><br/>
      <input type="submit" name="translate" value="View translations"/><br/> 
    </form>
  </body>
</html>

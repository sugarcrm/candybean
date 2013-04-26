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

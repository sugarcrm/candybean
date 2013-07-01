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
/*
 * Candybean is a next generation automation and testing framework suite.
 * It is a collection of components that foster test automation, execution
 * configuration, data abstraction, results illustration, tag-based execution,
 * top-down and bottom-up batches, mobile variants, test translation across
 * languages, plain-language testing, and web service testing.
 * Copyright (C) 2013 SugarCRM, Inc. <candybean@sugarcrm.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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

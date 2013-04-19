<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8"/>
    <title>Translation Validator</title>
  </head>
  
  <body>
    <h1>Translation Validator</h1>
    <p>Validates whether an English string is suitable for translation.</p>
    <p>Finds all instances of the input string in the selected language.</p>
    <p>If the number of English instances is 0, translation does not exist.</p>
    <p>If the number of English instances exceeds 1, translation may be ambiguous (2+ unique mappings).*</p>
    <p><small>	*Porting an existing feature to PHP that informs the user if the translation is in fact ambiguous.</small></p>
    <form action="php_action_page2.php" method="post">
      Validate: <input type="text" name="english" /><br />
      Language: <select name="language">
	<option value="en_us">English (US)</option>
	<option value="en_UK">English (UK)</option>
	<option value="bg_BG">Български</option>
	<option value="ca_ES">Català</option>
	<option value="cs_CZ">Česky</option>
	<option value="da_DK">Dansk</option>
	<option value="de_DE">Deutsch</option>
	<option value="el_EL">Ελληνικά</option>
	<option value="es_ES">Español</option>
	<option value="fr_FR">Français</option>
	<option value="he_IL">עברית</option>
	<option value="hu_HU">Magyar</option>
	<option value="it_it">Italiano</option>
	<option value="lt_LT">Lietuvių</option>
	<option value="ja_JP">日本語</option>
	<option value="ko_KR">한국어</option>
	<option value="lv_LV">Latviešu</option>
	<option value="nb_NO">Bokmål</option>
	<option value="nl_NL">Nederlands</option>
	<option value="pl_PL">Polski</option>
	<option value="pt_BR">Português Brasileiro</option>
	<option value="pt_PT">Português</option>
	<option value="ro_RO">Română</option>
	<option value="ru_RU">Русский</option>
	<option value="sk_SK">Türkçe</option>
	<option value="sq_AL">Shqip</option>
	<option value="sr_RS">Српски</option>
	<option value="sv_SE">Svenska</option>
	<option value="tr_TR">Türkçe</option>
        <option value="zh_CN">简体中文</option>
      </select><br /><br />
      <input type="submit" />
    </form>
  </body>
</html>

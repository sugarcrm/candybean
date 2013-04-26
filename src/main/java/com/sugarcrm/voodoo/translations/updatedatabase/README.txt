Note the following requires that you have access to sugarcrm/translations and sugarcrm/Voodoo2 repositories and you have SSH set up (for more information on SSH, see help.github.com/articles/generating-ssh-keys).

Create a job on Jenkins named UpdateDatabase > Configure UpdateDatabase > Add build step > Execute shell > Copy and paste the code below:
***beginning of code***
if [ ! -e ~/git_repos/translations ]
then
   mkdir -p ~/git_repos; cd ~/git_repos
   git clone git@github.com:sugarcrm/translations.git
fi
cd ~/git_repos/translations; git stash; git checkout master; git pull

if [ ! -e ~/git_repos/Voodoo2 ]
then
   cd ~/git_repos
   git clone git@github.com:sugarcrm/Voodoo2.git
fi

cd ~/git_repos/Voodoo2/src/main/java/com/sugarcrm/voodoo/translations/updatedatabase; git checkout master; git pull

./updateDatabaseSuite $WORKSPACE ~/git_repos/translations 6_7 Translations_6_7_latest 10.8.31.10 translator Sugar123!
***end of code***

See updateDatabaseSuite for documentation on each of the parameters used in the last line of code.

The above code creates a database using English php files from a Sugar extraction (Sugar does not have to be installed) and foreign language php files from the sugarcrm/translations repository. The language php files from the sugarcrm/translations repository contain the latest language strings hence the database is named Translations_6_7_latest. This usage should be done continuously as sugarcrm/translations gets updated (run this job continuously now, in the future make the job trigger only when sugarcrm/translations is updated).

To create a database using php files entirely from a Sugar installation, change the second parameter of updateDatabaseSuite to point to a Sugar extraction (ex. change ~/git_repos/translations to $WORKSPACE/sugar). If version parameter was 6_7, this database should be named Translations_6_7 or Translations_6_7_1 (if Sugar 6.7.1 was downloaded by downloadSugar.rb and version numbers really matter). This usage should be done once per version of Sugar.

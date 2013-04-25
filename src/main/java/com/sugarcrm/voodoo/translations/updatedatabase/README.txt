Note the following requires that you have access to sugarcrm/translations and sugarcrm/Voodoo2 repositories and you have SSH set up (for more information on SSH, see help.github.com/articles/generating-ssh-keys).

See updateDatabaseSuite for documentation on each of the parameters used in the last line of code.

Create a job on Jenkins named UpdateDatabase > Configure UpdateDatabase > Add build step > Execute shell > Copy and paste the code below:

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

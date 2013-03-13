Note the following requires you have access to sugarcrm/translations and sugarcrm/AutoUtils repositories and you have SSH set up (for more information on SSH, see help.github.com/articles/generating-ssh-keys).

Create a job on Jenkins named UpdateDatabase > Configure UpdateDatabase > Add build step > Execute shell > Copy and paste the code below

cd $WORKSPACE
if [ ! -e ~/git_repos/translations ]
then
   mkdir -p ~/git_repos; cd ~/git_repos
   git clone git@github.com:sugarcrm/translations.git
fi
cd translations; git pull; git checkout master

if [ ! -e ~/git_repos/Voodoo2 ]
then
  cd ~/git_repos
  git clone git@github.com:sugarcrm/Voodoo2.git
fi

cd ~/git_repos/Voodoo2/src/main/java/com/sugarcrm/voodoo/translations/update_database; git pull

./UpdateDatabaseSuite $WORKSPACE $WORKSPACE/translations 6_7 Translations_6_7

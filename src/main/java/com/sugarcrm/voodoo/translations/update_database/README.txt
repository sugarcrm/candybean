Note the following requires you have access to sugarcrm/translations and sugarcrm/AutoUtils repositories and you have SSH set up (for more information on SSH, see help.github.com/articles/generating-ssh-keys).

Create a job on Jenkins named UpdateDatabase > Configure UpdateDatabase > Add build step > Execute shell > Copy and paste the code below

cd $WORKSPACE
if [ ! -e "./translations" ]
then
   git clone git@github.com:sugarcrm/translations.git
fi
cd translations; git pull; git checkout master; cd ..

if [ ! -e "./AutoUtils" ]
then
  git clone git@github.com:sugarcrm/AutoUtils.git
fi

cd AutoUtils/Translations; git pull

./updateDB_Suite $WORKSPACE $WORKSPACE/translations 6_7 Translations_6_7

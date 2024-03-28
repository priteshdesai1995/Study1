#!/usr/bin/env sh
echo "----------"
ls -la

echo "----------"
echo "Printing APP_VARS:"
echo "$APP_VARS"

echo "----------"
echo "Printing vars into files..."
echo "$APP_VARS" > app_vars.sh
echo "$APP_SECRETS" > app_secrets.sh
chmod +x app_vars.sh
chmod +x app_secrets.sh

ls -la

echo "----------"
echo "Running unset APP_VARS and APP_SECRETS ..."
unset APP_VARS
unset APP_SECRETS

echo "----------"
echo "Sourcing app_vars.sh ..."
. ./app_vars.sh

echo "----------"
echo "Running printenv before sourcing app secrets ..."
printenv

echo "----------"
echo "Sourcing app_secrets.sh ..."
. ./app_secrets.sh

echo "----------"
echo "Removing not needed files and packages..."
rm -f ./app_vars.sh
rm -f ./app_secrets.sh

echo "----------"
echo "Starting the application..."
java -jar $APP_EXECUTABLE_PATH
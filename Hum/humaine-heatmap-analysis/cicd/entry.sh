#!/usr/bin/env bash

echo "----------"
echo "Printing jq version..."
jq --version
echo "$SHELL"

echo "----------"
echo "Printing APP_VARS:"
echo "$APP_VARS"

echo "----------"
echo "Parsing APP_VARS into app_vars.sh ..."
echo "$APP_VARS" | jq -r 'to_entries[] | "export " + .key + "=\"" + (.value|tostring) + "\""' > app_vars.sh
chmod +x app_vars.sh

echo "----------"
echo "Parsing APP_SECRETS into app_secrets.sh ..."
echo "$APP_SECRETS" | jq -r 'to_entries[] | "export " + .key + "=\"" + (.value|tostring) + "\""' > app_secrets.sh
chmod +x app_secrets.sh

echo "----------"
echo "Running unset APP_VARS and APP_SECRETS ..."
unset APP_VARS
unset APP_SECRETS

echo "----------"
echo "Sourcing app_vars.sh ..."
source app_vars.sh

echo "----------"
echo "Running printenv ..."
printenv

echo "----------"
echo "Sourcing app_secrets.sh ..."
source app_secrets.sh

echo "----------"
echo "Removing not needed files and packages..."
rm -f app_vars.sh
rm -f app_secrets.sh
# apk del jq

echo "----------"
echo "Starting the application..."
node ./index.js
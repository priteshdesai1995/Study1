## CI/CD
Configured in `bitbucket-pipelines.yml`  
Currently only CD configured for `develop` branch. Basic build and deploy with 

## Deployment process
Currently configured as automatic for `develop` branch changes.

TODO: Build once deploy everywhere to be implemented.

### Application variables
All app vars should be stored in AWS SSM Parameter store in two parameteres:
- `app_vars` - should contain variables that are not secret, e.g. can show up in logs, can be visible in AWS web console etc.
- `app_secrets` - secret variables, e.g. passwords, keys etc.

Using `cicd/entry.sh` these parameters are automatically retrieved during application startup and converted to envronment variables.

### Adding and removing application variables
Variables are stored in Parameter store in JSON format, hence to add/remove a variable it is needed:
1. Modify Parameter store values to reflect desired state (either add or remove vairables).  
Keep in mind to modify correct parameter depending if particular variable is secret or not.
2. Re-deploy the service in order to pickup new values from Parameter store

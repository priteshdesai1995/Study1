// The file contents for the current environment will overwrite these during build.
// The build system defaults to the dev environment which uses `environment.ts`, but if you do
// `ng build --prod` or `ng build --configuration=production` then `environment.prod.ts` will be used instead.
// `ng build --configuration=staging` then `environment.stage.ts` will be used instead.
// The list of which env maps to which file can be found in `angular.json`.

export const environment = {
  production: false,
  // apiEndpoint: 'https://lumen-lts.brainvire.dev',
  apiEndpoint: "http://localhost:8081",
  // apiAdminVersion: '/admin/api/v1',
  apiAdminVersion: "/base",
  apiFrontVersion: "/front/api/v1",
};

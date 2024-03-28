// This file can be replaced during build by using the `fileReplacements` array.
// `ng build --prod` replaces `environment.ts` with `environment.prod.ts`.
// The list of file replacements can be found in `angular.json`.

export const environment = {
  production: false,
   //baseUrl: 'https://pta.dev.nonprod.bodegaswimwear.com/humaine/portal/api',
   baseUrl: 'http://localhost:8083/humaine/admin/api',

  //baseUrl: 'https://aa.dev.nonprod.bodegaswimwear.com/humaine/admin/api',

  // baseUrl: 'http://81ea-2405-205-c866-1d37-1c16-e525-469f-ec8b.ngrok.io/humaine/portal/api',

  cognitoPool: {
    UserPoolId: "us-east-2_qkG4rrFJK",
    ClientId: "54mpkao65icasgnauctu053bkd"
  },
  // reCaptchaSiteKey: '6LdhIpIbAAAAAHzhK97vuqzbURZRKzcux0vNXxhB'
  reCaptchaSiteKey: '6Lcb5-8bAAAAAI9D7wBmEA4-5mBs0D1SU6W_7gmP',

  frontUrl:'http://localhost:3000/#/home',


};


/*
 * For easier debugging in development mode, you can import the following file
 * to ignore zone related error stack frames such as `zone.run`, `zoneDelegate.invokeTask`.
 *
 * This import should be commented out in production mode because it will have a negative impact
 * on performance if an error is thrown.
 */
// import 'zone.js/dist/zone-error';  // Included with Angular CLI.

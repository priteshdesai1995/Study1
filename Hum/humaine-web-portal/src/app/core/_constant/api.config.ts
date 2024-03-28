
import { environment } from '../../../environments/environment';
const BASE_URL = environment.baseUrl;

export const URLS = {
  // Common
  'login': `${BASE_URL}/admin/user/login`,
  'home': `${BASE_URL}/employees`,
  'signUp': `${BASE_URL}/signup`,
  'emailVerify': `${BASE_URL}/signup/email`,
  'register': `${BASE_URL}/register`,
  'getAccountDetails': `${BASE_URL}/register`,
  'getDemographic': `${BASE_URL}/demographic`,
  'getCognitive': `${BASE_URL}/cognitive`,
  'addUsergroup': `${BASE_URL}/usergroup`,
  'UserList': `${BASE_URL}/usergroups`,
  'deleteUser': `${BASE_URL}/usergroup`,
  'myUserGroupList': `${BASE_URL}/my-usergroups`,
  'myUserGroupDelete': `${BASE_URL}/my-usergroups`,
  'aiGeneratedUserGroupList': `${BASE_URL}/ai-usergroups`,
  'deleteAiGeneratedUserGroup': `${BASE_URL}/ai-usergroups`,
  'aiGeneratedUserGroupSave': `${BASE_URL}/ai-usergroups/save`,
  'savedAiGeneratedUserGroupList': `${BASE_URL}/ai-usergroups/saved`,
  'deletesavedAiGeneratedUserGroup': `${BASE_URL}/ai-usergroups/saved`,
  'manualViewPersona': `${BASE_URL}/usergroups/persona`,  
  'aiViewPersona': `${BASE_URL}/ai-usergroups/persona`,  
  'savedAiViewPersona': `${BASE_URL}/ai-usergroups/saved/persona`,  
  'myUserViewPersona': `${BASE_URL}/my-usergroups/persona`,
  'editGroupNamePersona': `${BASE_URL}/usergroup`,
  'testJourneyElement': `${BASE_URL}/test-journey/elements`,
  'testJourneyGroups': `${BASE_URL}/test-journey/groups`,
  'testJourneyCreate': `${BASE_URL}/test-journey`,
  'testJourneyList': `${BASE_URL}/test-journey`,
  'myJourneyList':`${BASE_URL}/my-journey`,
  'myJourneyDetail': `${BASE_URL}/my-journey`,
  'groupContainerPersona':`${BASE_URL}/my-usergroups/groups/persona`,
  'saveTestNewJourney':`${BASE_URL}/test-journey`,
  'deeteTestNewJourney':`${BASE_URL}/test-journey`,
  'testJourneyDetail':`${BASE_URL}/test-journey`,
  'updateTestNewJourney':`${BASE_URL}/test-journey`,
  'dashboard':`${BASE_URL}/dashboard?userName=dhaval%2B21@humaineai.com`,
  'dashboardJourney':`${BASE_URL}/dashboard/journey`,
  'UXInsight':`${BASE_URL}/ux-insights`,
  'UXInsightProduct':`${BASE_URL}/ux-insights/products/data`,
  'settingAPIKEY':`${BASE_URL}/account/settings/api-key`,
  'settingAccount':`${BASE_URL}/account/settings`,
  'UserGroupStatistics':`${BASE_URL}/my-usergroups/statistics`,
  'deleteMultipleTestJourney':`${BASE_URL}/test-journey`,
  'deleteMultipleUserGroup':`${BASE_URL}/usergroup`,
  'deleteMyJourney':`${BASE_URL}/my-journey`,
  'deleteMultipleMyJourney':`${BASE_URL}/my-journey`,

 

  //Live page
    // API which have page informations 
  'liveTopCountries':`${BASE_URL}/live/dashboard/active/countries`,
  'liveActiveDevice':`${BASE_URL}/live/dashboard/active/device`,
  'liveApdexScore':`${BASE_URL}/live/dashboard/apdex-score`,
  'liveTileStatistics':`${BASE_URL}/live/dashboard/statastics`,
  'livePageLoadTime':`${BASE_URL}/live/dashboard/page-loadtime`,
  'liveSessionDuration':`${BASE_URL}/live/dashboard/session/duration`,
  'liveDashboard':`${BASE_URL}/live/dashboard/users/hourly`,  
  'liveBounceRate':`${BASE_URL}/live/dashboard/bounce-rate`,
    // API which have refresh information 
  'livePostPageRefreshTime':`${BASE_URL}/live/dashboard/page-refresh`,
  'liveLastRefreshTime':`${BASE_URL}/live/dashboard/last-refresh-time`,

  // heatMAP
  'heatmapsImages':`${BASE_URL}/heatmap`,  
  'heatmapsSignUrl':`${BASE_URL}/heatmap/signed-url`,

  // Product Intelligence
  'productList': `${BASE_URL}/merchandise/intelligence/products`,


  // state Insigths 
  'stateInsight': `${BASE_URL}/dashboard/state`,


  'populerProductList': `${BASE_URL}/merchandise/intelligence/products/productsByPersona/`
}
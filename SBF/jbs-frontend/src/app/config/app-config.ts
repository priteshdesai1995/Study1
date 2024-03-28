import { environment } from "../../environments/environment";

const BASE_URL = environment.apiEndpoint;
const API = "/api";
const API_V1 = environment.apiAdminVersion;
const API_V1_FRONT = environment.apiFrontVersion;

export const CONFIG = {
  EncrDecrKey: "123456$#@$^@1ERF",
  // oauth
  userAuthURL: BASE_URL + API_V1 + "/api/oauth/token",
  forgotPassURL: BASE_URL + API_V1 + "/api/user/forgotpassword",
  validateResetPassURL:
    BASE_URL + API_V1 + "/api/user/password/validate-reset-token",
  resetPassURL: BASE_URL + API_V1 + "/api/user/reset-password",
  registerURL: BASE_URL + "/user/signup",
  // after oauth
  getUserProfileIdURL: BASE_URL + API_V1 + "/api/user",

  /* CMS MANAGEMENT - API */
  createCmsURL: BASE_URL + API_V1 + "/api/content/create",
  updateCmsURL: BASE_URL + API_V1 + "/api/content/update/",
  getCmsByIdURL: BASE_URL + API_V1 + "/api/content/get/",
  getAllCmsListURL: BASE_URL + API_V1 + "/api/content/search",
  changeCmsStatusURL: BASE_URL + API_V1 + "/api/content/changeStatus",
  storePageBuilderURL: BASE_URL + API_V1 + "/api/content/addEditContent/",
  loadPageBuilderURL: BASE_URL + API_V1 + "/api/content/cmsPageLoad/",
  loadComponentURL: BASE_URL + API_V1 + "/api/content/cmsPageLoad/",

  /* EMAIL TEMPLATE - API */
  updateEmailURL: BASE_URL + API_V1 + "/api/template/update/",
  addEmailURL: BASE_URL + API_V1 + "/api/template/create",
  getEmailByIdURL: BASE_URL + API_V1 + "/api/template/get/",
  getAllEmailListURL: BASE_URL + API_V1 + "/api/template/list",
  changeEmailStatusURL: BASE_URL + API_V1 + "/api/template/changeStatus",

  /* FAQ MANAGEMENT - API */
  getAllFaqListURL: BASE_URL + API_V1 + "/api/faq/list",
  getFaqByIdURL: BASE_URL + API_V1 + "/api/faq/get/",
  changeFaqStatusURL: BASE_URL + API_V1 + "/api/faq/changeStatus",
  getFaqTopicListURL: BASE_URL + API_V1 + "/api/faq/faqTopic/list",
  deleteFaqURL: BASE_URL + API_V1 + "/api/faq/delete/",
  updateFaqURL: BASE_URL + API_V1 + "/api/faq/update/",
  createFaqURL: BASE_URL + API_V1 + "/api/faq/create",

  /* MANAGE SUBADMIN - API */
  getAllSubadminListURL: BASE_URL + API_V1 + "/api/user/search/query",
  getSubadminListForDropdownURL:
    BASE_URL + API_V1 + "/api/event/subadmin/dropdown-list",
  changeSubadminPassURL: BASE_URL + API_V1 + "/subadmin/password/change",
  changeSubadminStatusURL: BASE_URL + API_V1 + "/subadmin/change_status",
  deleteSubadminURL: BASE_URL + API_V1 + "/subadmin/delete/",
  getActiveRoleURL: BASE_URL + API_V1 + "/role/list_active/0",
  getSubadminByIdURL: BASE_URL + API_V1 + "/subadmin/show/",
  updateSubadminURL: BASE_URL + API_V1 + "/subadmin/update/",
  createSubadminURL: BASE_URL + API_V1 + "/api/user",

  /* MANAGE USER - API */
  getAllManageUserListURL: BASE_URL + API_V1 + "/api/user/search/query",
  changeManageUserPassURL: BASE_URL + API_V1 + "/api/password/change",
  changeManageUserStatusURL: BASE_URL + API_V1 + "/api/user/changestatus",
  deleteManageUserURL: BASE_URL + API_V1 + "/api/user/delete",
  deleteUserProfileURL: BASE_URL + API_V1 + "/user/delete-profile/",
  getManagerUserByIdURL: BASE_URL + API_V1 + "api/user",
  updateManageUserURL: BASE_URL + API_V1 + "/api/user/update/",
  createManageUserURL: BASE_URL + API_V1 + "/api/user/add-user",
  importCSVFileURL: BASE_URL + API_V1 + "/api/user/import",
  lockManageUserURL: BASE_URL + API_V1 + "/entity-lock/check-or-lock",
  unlockManageUserURL: BASE_URL + API_V1 + "/entity-lock/unlock-entity",

  /* SETTINGS API */
  getSettingsDataURL: BASE_URL + API_V1 + "/api/configuration/list",
  getSettingsSaveDataURL: BASE_URL + API_V1 + "/api/configuration/update",
  getSettingsRemoveImageURL:
    BASE_URL + API_V1 + "/api/configuration/delete-image/",
  // getSettingsImageDataURL: BASE_URL + API + '/configuration/images',
  getSettingsImageDataURL: BASE_URL + API_V1 + "/api/configuration/logo",
  updateSettingsImage: BASE_URL + API_V1 + "/api/configuration/updateimage",

  /* MANAGE CATEGORY - API */
  getAllCategoryListURL: BASE_URL + API_V1 + "/api/category/parent-list",
  getCategoryByIdURL: BASE_URL + API_V1 + "/api/category/getCategory/",
  changeCategoryStatusURL: BASE_URL + API_V1 + "/api/category/changeStatus",
  deleteCategoryURL: BASE_URL + API_V1 + "/api/category/delete/",
  updateCategoryURL: BASE_URL + API_V1 + "/api/category/update/",
  createCategoryURL: BASE_URL + API_V1 + "/api/category/create-category",
  getCategoryTreeviewURL: BASE_URL + API_V1 + "/api/category/categoryTreeView",
  createCategoryTreeURL: BASE_URL + API_V1 + "/api/category/treeview/update",
  parentCategoryListURL: BASE_URL + API_V1 + "/api/category/parent-list/",

  /* SYSTEM PERMISSIONS - API */
  getAllRoleListURL: BASE_URL + API_V1 + "/api/role/list",
  changeRoleStatusURL: BASE_URL + API_V1 + "/api/role/change_status",
  createRoleURL: BASE_URL + API_V1 + "/api/role/create",
  updateRoleURL: BASE_URL + API_V1 + "/api/role/update?role_id=",
  getAllPermissionListURL: BASE_URL + API_V1 + "/api/role/permission/list",
  assignPermissionURL:
    BASE_URL + API_V1 + "/api/role/permission/role_assign?role_id=",

  updateProfileInfoURL: BASE_URL + API_V1 + "/api/user/update",
  changeProfilePassURL: BASE_URL + API_V1 + "/api/password/change",

  videoUploadURL: BASE_URL + "/front/api/v1/chunk/upload",

  /* MANAGE CONTACT - API */
  getAllContactListURL: BASE_URL + API_V1 + "/api/contact/search",
  deleteContactURL: BASE_URL + API_V1 + "/contact/delete/",
  changeStatusContactListURL: BASE_URL + API_V1 + "/contact/change-status",

  // Suggestion
  userSuggestionListURL: BASE_URL + API_V1 + "/suggestion/userlist",
  userSuggestionCreateURL: BASE_URL + API_V1 + "/suggestion/store",
  adminSuggestionListURL: BASE_URL + API_V1 + "/api/suggestion/list",
  suggestionChangeStatusURL:
    BASE_URL + API_V1 + "/api/suggestion/update?suggestion_id=",
  suggestionDeleteURL:
    BASE_URL + API_V1 + "/api/suggestion/delete?suggestion_id=",
  getActiveCategoryListURL:
    BASE_URL + API_V1 + "/api/category/getchild?parent_id=",
  exportSuggestionExcelURL: BASE_URL + API_V1 + "/api/suggestion/export_excel",
  exportSuggestionPDFURL: BASE_URL + API_V1 + "/api/suggestion/export_pdf",
  exportSuggestionCSVURL: BASE_URL + API_V1 + "/api/suggestion/export_csv",

  /* MANAGE SURVEY - API */
  getSurveyListURL: BASE_URL + API_V1 + "/api/survey/list",
  changeSurveyStatusURL: BASE_URL + API_V1 + "/api/survey/update_status",
  deleteSurveyURL: BASE_URL + API_V1 + "/api/survey/delete?survey_id=",
  getSurveyDetailsByIdURL:
    BASE_URL + API_V1 + "/api/survey/get_detailed_survey?survey_id=",
  getSurveyByIdURL: BASE_URL + API_V1 + "/api/survey/get_survey?survey_id=",
  updateSurveyURL: BASE_URL + API_V1 + "/api/survey/update?survey_id=",
  createSurveyURL: BASE_URL + API_V1 + "/api/survey/create",
  createSurveyQuestionsURL:
    BASE_URL + API_V1 + "/api/survey/add_questions?survey_id=",
  updateSurveyQuestionsURL:
    BASE_URL + API_V1 + "/api/survey/update_questions?survey_id=",
  getSurveyQuesAnsByIdURL:
    BASE_URL + API_V1 + "/api/survey/get_questions?survey_id=",
  getAnswerForTextTypeURL: BASE_URL + API_V1 + "/survey/get_text_answers",
  getAnswerForSingleMultipleTypeURL:
    BASE_URL + API_V1 + "/survey/get_answered_users",
  getSurveyUserReportURL: BASE_URL + API_V1 + "/survey/get_survey_users",
  notifySurveyUserURL: BASE_URL + API_V1 + "/survey/resend_notification",
  getActiveSurveyListURL: BASE_URL + API_V1_FRONT + "/survey/active_surveys",
  updateUserSurveyAnsURL: BASE_URL + API_V1_FRONT + "/survey/save_answers",
  getAnswerDetailsByIdURL:
    BASE_URL + API_V1_FRONT + "/survey/get_user_answers/",

  /* MANAGE ANNOUNCEMENT - API */
  getAllAnnouncementListURL: BASE_URL + API_V1 + "/api/announcement/list",
  createAnnouncementURL: BASE_URL + API_V1 + "/api/announcement/create",
  getAnnouncementDetailsByIdURL:
    BASE_URL + API_V1 + "/api/announcement/show?announcement_id=",
  getAnnouncementUserDataURL:
    BASE_URL + API_V1 + "/api/announcement/users?announcement_id=",
  getAnnouncementUserSelectionListURL:
    BASE_URL + API_V1 + "/api/announcement/user_selection_list",

  /* ACTIVITY TRACKING - API */
  getAllActivityTrackingListURL: BASE_URL + API_V1 + "/api/activity/list",
  deleteActivityURL: BASE_URL + API_V1 + "/api/activity/delete?activity_id=",

  /* MANAGE BANNER - API */
  getAllBannerListURL: BASE_URL + API_V1 + "/api/banner/list",
  changeManageBannerStatusURL: BASE_URL + API_V1 + "/api/banner/change_status",
  deleteManageBannerURL: BASE_URL + API_V1 + "/api/banner/delete?banner_id=",
  getManageBannerByIdURL: BASE_URL + API_V1 + "/api/banner/show?banner_id=",
  createManageBannerURL: BASE_URL + API_V1 + "/api/banner/store",
  updateManageBannerURL: BASE_URL + API_V1 + "/api/banner/update?banner_id=",

  /* MANAGE SUBSCRIPTION - API */
  getAllManageSubscrptionListURL: BASE_URL + API_V1 + "/api/subscription/list",
  getManagerSubscriptionByIdURL:
    BASE_URL + API_V1 + "/api/subscription/show?subscription_id=",
  updateManageSubscriptionURL:
    BASE_URL + API_V1 + "/api/subscription/update?subscription_id=",
  createManageSubscriptionURL: BASE_URL + API_V1 + "/api/subscription/create",
  changeManageSubscriptionStatusURL:
    BASE_URL + API_V1 + "/api/subscription/change_status",
  deleteManageSubscriptionURL:
    BASE_URL + API_V1 + "/api/subscription/delete?subscription_id=",

  // Download API
  fileDownloadURL: BASE_URL + API_V1 + "/download",
  exportUsersExcelURL: BASE_URL + API_V1 + "/api/user/export-users/xlsx",
  exportUsersPDFURL: BASE_URL + API_V1 + "/api/user/export-users/pdf",
  exportUsersCSVURL: BASE_URL + API_V1 + "/api/user/export-users/csv",

  // Manage Location
  /* COUNTRY - API */
  getAllCountryURL: BASE_URL + API_V1 + "/api/country/search",
  createCountryURL: BASE_URL + API_V1 + "/api/country/create",
  updateCountryURL: BASE_URL + API_V1 + "/api/country/update/",
  getCountryByIdURL: BASE_URL + API_V1 + "/api/country/get/",
  changeCountryStatusURL: BASE_URL + API_V1 + "/api/country/changeStatus",
  deleteCountryURL: BASE_URL + API_V1 + "/api/country/delete/",

  /* STATE - API */
  getAllStateURL: BASE_URL + API_V1 + "/api/state/search",
  createStateURL: BASE_URL + API_V1 + "/api/state/create",
  updateStateURL: BASE_URL + API_V1 + "/api/state/update/",
  getStateByIdURL: BASE_URL + API_V1 + "/api/state/get/",
  changeStateStatusURL: BASE_URL + API_V1 + "/api/state/changeStatus",
  deleteStateURL: BASE_URL + API_V1 + "/api/state/delete/",
  getActiveCountryURL: BASE_URL + API_V1 + "/api/country/getAllCountries",

  /* CITY - API */
  getAllCityURL: BASE_URL + API_V1 + "/api/city/search",
  createCityURL: BASE_URL + API_V1 + "/api/city/create",
  updateCityURL: BASE_URL + API_V1 + "/api/city/update/",
  getCityByIdURL: BASE_URL + API_V1 + "/api/city/get/",
  changeCityStatusURL: BASE_URL + API_V1 + "/api/city/changeStatus",
  deleteCityURL: BASE_URL + API_V1 + "/api/city/delete/",
  getActiveStateURL: BASE_URL + API_V1 + "/api/state/getAllStates/",

  /* MANAGE BS MEDIA - API */
  createFolderURL: BASE_URL + API_V1 + "/api/v1/media/create-folder",
  deleteFolderURL: BASE_URL + API_V1 + "/api/v1/media/delete/",
  getAllMediaFolderAndFileURL: BASE_URL + "/base" + API + "/v1/media/search",
  uploadBsMediaURL: BASE_URL + "/base" + API + "/v1/media/create",
  renameBsMediaURL: BASE_URL + API_V1 + "/api/v1/media/rename-media",
  moveBsMediaURL: BASE_URL + "/base" + API + "/v1/media/move-media",

  /* MANAGE OFFER - API */
  getAllManageOfferListURL: BASE_URL + API_V1 + "/api/offer/list",
  updateManageOfferURL: BASE_URL + API_V1 + "/api/offer/update?offer_id=",
  createManageOfferURL: BASE_URL + API_V1 + "/api/offer/store",
  changeManageOfferStatusURL: BASE_URL + API_V1 + "/api/offer/change_status",
  deleteManageOfferURL: BASE_URL + API_V1 + "/api/offer/delete?offer_id=",
  exportOfferExcelURL: BASE_URL + API_V1 + "/api/offer/export_excel",
  exportOfferPDFURL: BASE_URL + API_V1 + "/api/offer/export_pdf",
  exportOfferCSVURL: BASE_URL + API_V1 + "/api/offer/export_csv",
  getManageOfferByIdURL: BASE_URL + API_V1 + "/api/offer/show?offer_id=",
  exportUserExcelURL:
    BASE_URL + API_V1 + "/api/offer/export/export_offer_user_excel",
  exportUserPDFURL:
    BASE_URL + API_V1 + "/api/offer/export/export_offer_user_pdf",
  exportUserCSVURL:
    BASE_URL + API_V1 + "/api/offer/export/export_offer_user_csv",
  getOfferReportListURL: BASE_URL + API_V1 + "/api/offer/getUserList/",
  getActiveUserURL: BASE_URL + API_V1 + "/api/offer/getActiveUserList",

  /* Report - API */
  getUserReportListURL: BASE_URL + API_V1 + "/api/report/list",
  deleteUserReportURL: BASE_URL + API_V1 + "/api/report/delete?report_id=",
  changeReportStatusURL: BASE_URL + API_V1 + "/api/report/change_status",

  /* Review - API */
  getReviewListURL: BASE_URL + API_V1 + "/api/review/list",
  deleteReviewURL: BASE_URL + API_V1 + "/api/review/delete?review_id=",
  changeReviewStatusURL: BASE_URL + API_V1 + "/api/review/change_status",

  /* Ruleset - API */
  getRulesetListURL: BASE_URL + API_V1 + "/api/rule/list",
  createRulesetURL: BASE_URL + API_V1 + "/api/rule/create",
  updateRulesetURL: BASE_URL + API_V1 + "/api/rule/update",
  getRulesetByIdURL: BASE_URL + API_V1 + "/api/rule/view?rule_id=",
  changeRulesetStatusURL: BASE_URL + API_V1 + "/api/rule/change_status",
  deleteRulesetURL: BASE_URL + API_V1 + "/api/rule/delete",

  /* EVENT - API */
  getAllEventURL: BASE_URL + API_V1 + "/api/event/list",
  createEventURL: BASE_URL + API_V1 + "/api/event/create",
  updateEventURL: BASE_URL + API_V1 + "/api/event/update",
  getEventByIdURL: BASE_URL + API_V1 + "/api/event/show?event_id=",
  deleteEventURL: BASE_URL + API_V1 + "/api/event/delete",
  getEventActiveUserURL: BASE_URL + API_V1 + "/api/event/getActiveUserList",

  //  DASHBOARD - API
  getDashboardCountURL: BASE_URL + API_V1 + "/firebase/get-analytics",
};

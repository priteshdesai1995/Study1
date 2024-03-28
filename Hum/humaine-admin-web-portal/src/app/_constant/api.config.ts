import { environment } from "src/environments/environment";

const BASE_URL = environment.baseUrl;

export const URLS = {
  // Common
  'login': `${BASE_URL}/oauth/token`,
  'emailVerify': `${BASE_URL}/signup/email`,
  'getAccountDetails': `${BASE_URL}/customers/`,
  'signUp': `${BASE_URL}/signup`,
  'customersList':`${BASE_URL}/customers`,
  'deleteCustomer':`${BASE_URL}/customers/delete`,
  'deleteMultipleCustomers':`${BASE_URL}/customers/delete`,
  'customerCreate':'',
  'customerEdit':`${BASE_URL}/customers/`,
  'customerDetail':'',
  'registeredCustomers':`${BASE_URL}/home/getRegisteredCustomers`,
  'accountEventsInfo':`${BASE_URL}/customers/eventsdata/`
}
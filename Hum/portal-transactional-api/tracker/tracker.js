const accountId = "##ACCOUNT_ID##";
var oldSessionId;
const BASE_URL = "##API_URL##";
const getBaseURL = function () {
  if (BASE_URL.endsWith("/")) {
    return BASE_URL.substr(0, BASE_URL.length-1);
  }
  return BASE_URL;
}
const userEventURL = `${getBaseURL()}/userEvent`;
const pageLoadEvent = `${getBaseURL()}/userEvent/pageLoad`;
const webSocketURL = BASE_URL.replace("https","wss")+'/eventRecording?';
var socket;
var APIKEY;
var lastMove = 0;
const EVENTS = {
  START: 'START',
  END: 'END',
  NAV: 'NAV',
  PRODVIEW: 'PRODVIEW',
  RATE: 'RATE',
  BUY: 'BUY',
  REVIEW: 'REVIEW',
  ADDCART: 'ADDCART',
  ADDLIST: 'ADDLIST',
  REMLIST: 'REMLIST',
  REMCART: 'REMCART',
  ADDTOCARD: 'ADDCART',
  REVIEW: 'REVIEW',
  DISCOVER: 'DISCOVER',
  NEWSLETTER_SUBSCRIBE: 'NEWSLETTER_SUBSCRIBE',
  SEARCH: 'SEARCH',
  MENU: 'MENU',
  BACK_NAV: 'BACK_NAV',
  SAVE_FOR_LATER: 'SAVE_FOR_LATER',
  DELETE: 'DELETE',
  PROD_RETURN: 'PROD_RETURN',
  VISIT_BLOG_POST: 'VISIT_BLOG_POST',
  VISIT_SOCIAL_MEDIA: 'VISIT_SOCIAL_MEDIA',
  TEXT_HIGHLIGHT: 'TEXT_HIGHLIGHT',
  IMG_VIEW: 'IMG_VIEW',
  DOUBLE_CLICK: 'DOUBLE_CLICK',
  NEW_TAB: 'NEW_TAB'

};
var isAuthorizing = false;
const ACTION = {
  ADD: 'ADD',
  REMOVE: 'REMOVE'
};

/**
 * @desc To check cookie is present or not
 */
const isCookieePresent = () => {
  return getCookie("humaine_tracker");
}

/**
 * @desc function to start session
 */
const startSession = async function () {
  //If cookie is not present
  checkAuthorization();
  if (!isCookieePresent()) {
    await creatCookie()
    return;
  }
  let cookieData = JSON.parse(getCookie("humaine_tracker"));
  let lastSessionTime = new Date(parseInt(cookieData.session_time));
  let currentTime = new Date();

  //checking condition for session in between last time and current time 
  let localLastTime = (lastSessionTime.getTime() / 1000);
  let localCurrentTime = (currentTime.getTime() / 1000);
  const timeOut = await sessionTimeout();
  if (localCurrentTime < (localLastTime + timeOut)) {
    console.warn("Session Alive");
    return;
  }
  // else if (localCurrentTime > (localLastTime + timeOut)) {
  //   endSession();
  //   console.warn("Session End");
  // }

}


/**
 * @desc EndSession is use to end current session
 */
const endSession = function () {
  let cookieData = JSON.parse(getCookie("humaine_tracker"));
  oldSessionId = cookieData.session_id;
  userEvent(EVENTS.END);
  cookieData.session_id = sessionId();
  cookieData.session_time = new Date().getTime();
  document.cookie = "humaine_tracker=" + JSON.stringify(cookieData)+"; domain=" + 
  location.hostname + "; path=/";
  //to Update loaction and start USER EVENT api with param START 
  getCordinates();
}

/**
 * @desc function to get timeout value from API
 */
const sessionTimeout = async function () {
  checkAuthorization();
  return new Promise((resolve, reject) => {
    var xhttp = new XMLHttpRequest();
    xhttp.open("GET", `${getBaseURL()}/sessionTimeout/` + accountId, true);
    xhttp.setRequestHeader('Content-Type', 'application/json');
    xhttp.setRequestHeader('version', 'v1');
    xhttp.setRequestHeader('API-KEY', APIKEY);
    xhttp.onreadystatechange = function () {
      if (this.readyState === 4) {
        if (this.status === 200) {
          var result = JSON.parse(this.responseText);
          timeout = result.responseData.timeout;
          resolve(timeout);
        } else {
          resolve(0);
        }
      }
    };
    xhttp.send();
  });
}

/**
 * @desc function to create session ID 
 */
var sessionId = function () {
  var dt = new Date().getTime();
  var uuid = 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
    var r = (dt + Math.random() * 16) % 16 | 0;
    dt = Math.floor(dt / 16);
    return (c == 'x' ? r : (r & 0x3 | 0x8)).toString(16);
  });
  return accountId + "_" + uuid;
}


/**
 * @desc function to get IP address
 */
const getIP = async function () {
  return new Promise((resolve, reject) => {
    var xhttp = new XMLHttpRequest();
    xhttp.open("GET", "https://api.ipify.org/?format=json", true);
    xhttp.onreadystatechange = function () {
      if (this.readyState == 4) {
        if (this.status === 200) {
          let ipAd = this.responseText;
          resolve(ipAd);
        }
        else {
          resolve(this.errorText);
        }
      }
    };
    xhttp.send();
  })
}

const initializeTracker = function (apiKey) {
  APIKEY = apiKey;
  window.onload = savePageLoadEventData();
  document.addEventListener("mouseup", function(){
    saveHighlightedTextData();
  });
  document.addEventListener("dblclick",function(event){
    saveDblClickEventData(event);
  });

  document.addEventListener("mousedown", function (event) {
    saveNewTabEventData(event);
  });
  const mouseeMoveHandler = function (event) {
    recordMouseMove(event);
  };

  document.addEventListener("mousemove", mouseeMoveHandler);

  setTimeout(() => {
    document.removeEventListener("mousemove", mouseeMoveHandler);
  }, 1000 * 120);

}

const checkAuthorization = function () {
  if (!APIKEY) {
    throw "Unauthorised";
  }
}
/**
 * @desc function to create local data and store collected data and set cookie
 */
const creatCookie = async function () {
  let expiryDate = new Date();
  expiryDate.setFullYear(expiryDate.getFullYear() + 1);
  let ipAddress = (JSON.parse(await getIP()).ip).replace(/\./g, "");
  let appversion = navigator.appVersion.replaceAll(";", " ");
  const data = {
    session_id: sessionId(),
    session_time: new Date().getTime(),
    user_Id: accountId
      + "_" +
      navigator.platform
      + "_" +
      navigator.appName
      + "_" +
      ipAddress,
    device_Id: navigator.platform
      + "_" +
      navigator.appName,
    deviceType: navigator.appName
      + "_" +
      appversion
  }
  document.cookie =
    "humaine_tracker=" +
    JSON.stringify(data) +
    ";expires=" +
    expiryDate.toGMTString()+"; domain=." + location.hostname+ "; path=/";
  const re = await getCordinates();
  savePageLoadEventData();
  console.warn("Session Started..");
}

/**
 * @desc function to get lattitude and longitude value
 */
async function getCordinates() {
  let data;
  return new Promise((resolve, reject) => {
    navigator.geolocation.getCurrentPosition(async function (position) {
      lat = position.coords.latitude;
      long = position.coords.longitude;
      let xhttp = new XMLHttpRequest();
      xhttp.open("GET", "https://api.bigdatacloud.net/data/reverse-geocode-client?latitude=" + lat + "&longitude=" + long + "&localityLanguage=en", true);
      xhttp.onreadystatechange = async function () {
        if (this.readyState == 4 && this.status == 200) {
          result = JSON.parse(this.responseText);
          data = {
            city: result.locality,
            state: result.principalSubdivision,
            country: result.countryName,
            lat: lat,
            long: long
          }
          data = JSON.parse(this.responseText);
          const loc = await setLocationAndUserEvent(data);
          resolve();
        }
        else {
          if (this.readyState == 4) {
            console.error(this.errorText);
            reject();
          }
        }
      };
      xhttp.send();
    },
      async function (error) {
        data = {
          // city: "not available",
          // lat: "undefined",
          // long: "undefined",
          // state: "not available",
          // country: "not available"
        }
        const loc = await setLocationAndUserEvent(data);
        resolve();
      });
  });
}

const setLocationAndUserEvent = async function (jsonData) {
  let cookieData = JSON.parse(getCookie("humaine_tracker"));
  cookieData["latitude"] = jsonData.lat;
  cookieData["longitude"] = jsonData.long;
  cookieData["city"] = jsonData.city;
  cookieData["state"] = jsonData.state;
  cookieData["country"] = jsonData.country;
  document.cookie = 'humaine_tracker=' + JSON.stringify(cookieData)+"; domain=" + location.hostname + "; path=/";
  return await userEvent(EVENTS.START);
}

/* Function used to get cookie data  */
const getCookie = function (cookieName) {
  // Split cookie string and get all individual name=value pairs in an array
  var cookieArr = document.cookie.split(";");

  // Loop through the array elements
  for (var i = 0; i < cookieArr.length; i++) {
    var cookiePair = cookieArr[i].split("=");

    /* Removing whitespace at the beginning of the cookie name
    and compare it with the given string */
    if (cookieName == cookiePair[0].trim()) {
      // Decode the cookie value and return
      return decodeURIComponent(cookiePair[1]);
    }
  }
}

const processUnAuthorized = async function () {
    if (!isAuthorizing) return false;
    checkAuthorization();
    if (!isCookieePresent()) {
        await creatCookie();
        isAuthorizing = false;
        processQueue();
      } else {
        let cookieData = JSON.parse(getCookie("humaine_tracker"));
        cookieData.session_id = sessionId();
        cookieData.session_time = new Date().getTime();
        document.cookie = "humaine_tracker=" + JSON.stringify(cookieData)+"; domain=." +location.hostname + "; path=/";
        await userEvent(EVENTS.START);
        isAuthorizing = false;
        processQueue();
    }
}

const processQueue = function() {
    let cookieData = JSON.parse(getCookie("humaine_tracker"));
    const queue = readQueue();
    Object.keys(queue["EVENT"]).forEach(e => {
        const req = queue["EVENT"][e];
        if (req['eventID']!= EVENTS.END) {
          req["sessionID"]=cookieData.session_id;
          saveEventData(req);
        }
    });
    Object.keys(queue["LOAD"]).forEach(e => {
        const req = queue["LOAD"][e];
        req["sessionID"]=cookieData.session_id;
        savePageLoadData(req);
    });
}
/**
 * @desc UserEvent function to call API as per Body param 
 */
const userEvent = function (eventId, param, quantity, amount, post_title, post_id, social_media_platform, social_media_url,
  menuId, menuURL, menuName, targetElement = null, highlightedText = null, productImageUrl = null, ratingValue = null, selectedElement = null, couponCode = null) {
  checkAuthorization();
  let localLoadTime = totalTimeToLoadDocument();//window.performance.timing.domContentLoadedEventEnd - window.performance.timing.navigationStart;
  let cookieData = JSON.parse(getCookie("humaine_tracker"));
  const data = {
    accountID: accountId,
    userID: cookieData.user_Id,
    eventID: eventId,
    sessionID: cookieData.session_id,
  }

  if (targetElement) {
    data['targetElement']=getElementPath(targetElement);
  }

  switch (eventId) {
    case EVENTS.START:
      data["city"] = cookieData.city;
      data["latitude"] = cookieData.latitude;
      data["longitude"] = cookieData.longitude;
      data["state"] = cookieData.state;
      data["country"] = cookieData.country;
      data["deviceId"] = cookieData.device_Id;
      data["deviceType"] = cookieData.deviceType;
      data["pageLoadTime"] = localLoadTime;
      break;

    case EVENTS.END:
      data['deviceId'] = cookieData.device_Id;
      data['sessionID'] = oldSessionId;
      break;

    case EVENTS.NAV:
      if (!param) {
        console.log("NAV URL is missing");
        return;
      }
      data['pageURL'] = param;
      break;

    case EVENTS.DISCOVER:
    case EVENTS.BACK_NAV:
      if (!param) {
        console.log("Page URL is missing");
        return;
      }
      data['pageURL'] = param;
      break;

    case EVENTS.BUY:
      if (!param && !quantity && !amount) {
        console.log("ProductID or action is missing");
        return;
      }
      data['productID'] = param;
      data['productQuantity'] = quantity;
      data['saleAmount'] = amount;
      data['couponCode'] = couponCode;
      break;

    case EVENTS.RATE:
      data['ratingValue'] = ratingValue;
      break;

    case EVENTS.PRODVIEW:
    case EVENTS.REVIEW:
    case EVENTS.ADDCART:
    case EVENTS.REMCART:
    case EVENTS.ADDLIST:
    case EVENTS.REMLIST:
    case EVENTS.SAVE_FOR_LATER:
    case EVENTS.DELETE:
    case EVENTS.PROD_RETURN:
      if (!param) {
        console.log("Product ID is missing");
        return;
      }
      data['productID'] = param;
      break;

    case EVENTS.MENU:
      if (!menuURL || !menuName) {
        console.error("menu URL,menuName URL is missing");
        return;
      }
      data['menuId'] = menuId;
      data['menuURL'] = menuURL;
      data['menuName'] = menuName;
      break;

    case EVENTS.VISIT_BLOG_POST:
      if (!post_title || !post_id) {
        console.log("Post title or Post ID is missing");
        return;
      }
      data['postTitle'] = post_title;
      data['postId'] = post_id;
      break;

    case EVENTS.VISIT_SOCIAL_MEDIA:
      if (!social_media_url || !social_media_platform) {
        console.error("social media platform, social media URL is missing");
        return;
      }
      data['socialMediaPlateForm'] = social_media_platform;
      data['socialMediaURL'] = social_media_url;
      break;

    case EVENTS.NEWSLETTER_SUBSCRIBE:
    case EVENTS.SEARCH:
      break;
    case EVENTS.TEXT_HIGHLIGHT:
      data['highlightedText'] = highlightedText;
      break;
    
    case EVENTS.IMG_VIEW:
      data['productID'] = param;
      data['productImageURL'] = productImageUrl;
      break;
    
    case EVENTS.DOUBLE_CLICK:
      data['selectedElement'] = selectedElement;
      break;

    case EVENTS.NEW_TAB:
      if (!param) {
        console.log("New Tab URL is missing");
        return;
      }
      data['pageURL'] = param;
      break;

    default:
      console.log("Invalid Request");
      return;
  }
  return saveEventData(data);
}

const saveEventData = async function (data) {
  return new Promise((resolve, reject) => {
    let xhttp = new XMLHttpRequest();
    xhttp.open("POST", `${userEventURL}`, true);
    xhttp.setRequestHeader('Content-Type', 'application/json');
    xhttp.setRequestHeader('version', 'v1');
    xhttp.setRequestHeader('API-KEY', APIKEY);
    xhttp.onreadystatechange = function () {
      if (this.readyState === 4) {
        let localData = JSON.parse(this.responseText);
        if (this.status === 200 && localData.status == "SUCCESS") {
          resolve();
        } else if (localData.status == "FAIL") {
          if (["ERRCODE19", "ERRCODE14"].includes(localData.errorList[0].code && localData.errorList[0].code)) {
              addToQueue("EVENT",data, data.eventID);
              if (!isAuthorizing) {
                isAuthorizing = true;
                processUnAuthorized(data);
              }
          }
          reject();
        }
      }
    };
    xhttp.send(JSON.stringify(data));
  });
}
/**
 * @desc function used to capture when user navigates to a certain page
 */
const pageNavigate = function (navURL) {
  //If cookie is not present
  if (!isCookieePresent) {
    console.error("Cookie not present");
    return;
  }
  if (!navURL) {
    console.error("NAV URL is missing");
    return;
  }
  // param to send on UserEvent API
  userEvent(EVENTS.NAV, navURL);
  console.log("Success");
}

/**
 * @desc function use to display product detail
 */
const viewProduct = function (productID) {
  //If cookie is not present
  if (!isCookieePresent) {
    console.error("Cookie not present");
    return;
  }
  if (!productID) {
    console.error("ProductID is missing");
    return;
  }
  // param to send on UserEvent API
  userEvent(EVENTS.PRODVIEW, productID);
  console.log("Success");
}

/**
 * @desc fnction integrated to be called on Add to Cart / Remove
 */
const cartAction = function (productID, action, targetElement) {
  //If cookie is not  present
  if (!isCookieePresent) {
    //Cookie not present throw error
    console.error("Cookie not present");
    return;
  }
  if (!productID || !action) {
    console.error("ProductID or action is missing");
    return;
  }
  // param to send on UserEvent API
  if (action == ACTION.ADD) {
    userEvent(EVENTS.ADDCART, productID, null, null, null, null, null, null, null, null, null,targetElement)
  }
  else if (action == ACTION.REMOVE) {
    userEvent(EVENTS.REMCART, productID, null, null, null, null, null, null, null, null, null,targetElement)
  }
}

/**
 * @desc fnction integrated to be called on Add to Cart / Remove
 */
const wishlistAction = function (productID, action) {
  //If cookie is not present
  if (!isCookieePresent) {
    //Cookie not present throw error
    console.error("Cookie not present");
    return;
  }
  if (!productID || !action) {
    console.error("ProductID or action is missing");
    return;
  }
  // param to send on UserEvent API
  if (action == ACTION.ADD) {
    userEvent(EVENTS.ADDLIST, productID)
  }
  else if (action == ACTION.REMOVE) {
    userEvent(EVENTS.REMLIST, productID)
  }
}

/**
 * @desc function must be integrated to be called on Buy Now
 */
const buyProduct = function (productID, quantity, amount, product_metadata, couponCode) {
  //If cookie is not present
  if (!isCookieePresent) {
    console.error("Cookie not present");
    return;
  }
  if (!productID || !quantity || !amount) {
    console.error("ProductID ,quantity or sale amount  is missing");
    return;
  }
  // param to send on UserEvent API
  userEvent(EVENTS.BUY, productID, quantity, amount, null, null, null, null, null, null, null, null, null, null, null, null, couponCode);
  console.log("Success");
}

/**
 * @desc function must be integrated to be called on Rate button click
 */
const rateProduct = function (productID, targetElement, ratingValue) {
  //If cookie is not present
  if (!isCookieePresent) {
    console.error("Cookie not present");
    return;
  }
  if (!productID) {
    console.error("ProductID is missing");
    return;
  }
  if (!ratingValue) {
    console.error("ratingValue is missing");
    return;
  }

  // param to send on UserEvent API
  userEvent(EVENTS.RATE, productID, null, null, null, null, null, null, null, null, null, targetElement, null, null, ratingValue);
  console.log("Success");
}

/**
 * @desc function must be integrated to be called on Submit Review button click
 */
const reviewProduct = function (productID, targetElement) {
  //If cookie is not present
  if (!isCookieePresent) {
    console.error("Cookie not present");
    return;
  }
  if (!productID) {
    console.error("ProductID is missing");
    return;
  }
  // param to send on UserEvent API
  userEvent(EVENTS.REVIEW, productID, null, null, null, null, null, null, null, null, null, targetElement);
}

/**
 * @desc Function used to Logout current session
 */
const logout = function () {
  //If cookie is present
  if (isCookieePresent) {
    const cookieData = JSON.parse(getCookie("humaine_tracker"));
    oldSessionId = cookieData.session_id;
    if (!oldSessionId) {
      console.error("Old sessionID is missing")
      return;
    }
    userEvent(EVENTS.END);
    console.log("Logout successfully..")
  }
}


/**
 * @desc Function used to Discover Navigation
 */
const discoverNavigate = function (pageURL) {
  //If cookie is not present
  if (!isCookieePresent) {
    console.error("Cookie not present");
    return;
  }
  if (!pageURL) {
    console.error("Page URL is missing");
    return;
  }
  // param to send on UserEvent API
  userEvent(EVENTS.DISCOVER, pageURL);
  console.log("Success");
}


/**
 * @desc Function used to newsletter_subscriber
 */
const newsletter_subscriber = function (targetElement) {
  //If cookie is not present
  if (!isCookieePresent) {
    console.error("Cookie not present");
    return;
  }
  // param to send on UserEvent API
  userEvent(EVENTS.NEWSLETTER_SUBSCRIBE,null, null, null, null, null, null, null, null, null, null, targetElement);
  console.log("Success");
}


/**
 * @desc Function used to search item
 */
const search = function () {
  //If cookie is not present
  if (!isCookieePresent) {
    console.error("Cookie not present");
    return;
  }
  // param to send on UserEvent API
  userEvent(EVENTS.SEARCH);
  console.log("Success");
}


/**
 * @desc Function used to newsletter_subscriber
 */
const menu = function (menuId, menuURL, menuName, targetElement) {
  //If cookie is not present
  if (!isCookieePresent) {
    console.error("Cookie not present");
    return;
  }
  if (!menuURL || !menuName) {
    console.error("menu URL,menuName URL is missing");
    return;
  }
  // param to send on UserEvent API
  userEvent(EVENTS.MENU, null, null, null, null, null, null, null, menuId, menuURL, menuName, targetElement);
  console.log("Success");
}


/**
 * @desc Function used to back Navigation if button exist in cart page
 */
const backNavigate = function (pageURL) {
  //If cookie is not present
  if (!isCookieePresent) {
    console.error("Cookie not present");
    return;
  }
  if (!pageURL) {
    console.error("Page URL is missing");
    return;
  }
  // param to send on UserEvent API
  userEvent(EVENTS.BACK_NAV, pageURL);
  console.log("Success");
}

/**
 * @desc Function used to back Navigation if button exist in cart page
 */
const saveForLater = function (productID, targetElement) {
  //If cookie is not present
  if (!isCookieePresent) {
    console.error("Cookie not present");
    return;
  }
  if (!productID) {
    console.error("Product ID is missing");
    return;
  }
  // param to send on UserEvent API
  userEvent(EVENTS.SAVE_FOR_LATER, productID, null, null, null, null, null, null, null, null, null, targetElement);
  console.log("Success");
}


/**
 * @desc Function used to delete Item
 */
const deleteItem = function (productID, targetElement) {
  //If cookie is not present
  if (!isCookieePresent) {
    console.error("Cookie not present");
    return;
  }
  if (!productID) {
    console.error("ProductID is missing");
    return;
  }
  // param to send on UserEvent API
  userEvent(EVENTS.DELETE, productID, null, null, null, null, null, null, null, null, null, targetElement);
  console.log("Success");
}


/**
 * @desc Function used to when product return
 */
const productReturn = function (productID, targetElement) {
  //If cookie is not present
  if (!isCookieePresent) {
    console.error("Cookie not present");
    return;
  }
  if (!productID) {
    console.error("ProductID is missing");
    return;
  }
  // param to send on UserEvent API
  userEvent(EVENTS.PROD_RETURN, productID, null, null, null, null, null, null, null, null, null, targetElement);
  console.log("Success");
}


/**
 * @desc Function used to when product return
 */
const visitBlogPost = function (post_title, post_id, targetElement) {
  //If cookie is not present
  if (!isCookieePresent) {
    console.error("Cookie not present");
    return;
  }
  if (!post_title || !post_id) {
    console.error("Post Title, Post ID is missing");
    return;
  }
  // param to send on UserEvent API
  userEvent(EVENTS.VISIT_BLOG_POST, null, null, null, post_title, post_id, null, null, null, null, null, targetElement);
  console.log("Success");
}


/**
 * @desc Function used to when product return
 */
const visitSocialMedia = function (social_media_platform, social_media_url, targetElement) {
  //If cookie is not present
  if (!isCookieePresent) {
    console.error("Cookie not present");
    return;
  }
  if (!social_media_platform || !social_media_url) {
    console.error("social media platform, social media URL is missing");
    return;
  }
  // param to send on UserEvent API
  userEvent(EVENTS.VISIT_SOCIAL_MEDIA, null, null, null, null, null, social_media_platform, social_media_url, null, null, null, targetElement);
  console.log("Success");
}

/**
 * @desc Function used to when products have multiple images and viewed by user
 */
const viewProductImage = function(product_id, image_url){
  //If cookie is not present
  if (!isCookieePresent) {
    console.error("Cookie not present");
    return;
  }

  if(!product_id || !image_url)
  {
    console.error("product_id or image_url for image view is missing");
    return;
  }
  
  // param to send on UserEvent API
  userEvent(EVENTS.IMG_VIEW, product_id, null, null, null, null, null, null, null, null, null, null, null, image_url);
  console.log("Success");

}

function savePageLoadEventData() {
  checkAuthorization();
  if (!isCookieePresent() || socket) {
    return;
  }
  let cookieData = JSON.parse(getCookie("humaine_tracker"));
  let userID = cookieData.user_Id;
  let sessionID = cookieData.session_id;
  if (APIKEY && accountId && userID && sessionID) {
  let url = webSocketURL + 'APIKEY=' + APIKEY + '&accountID=' + accountId + '&userID=' + userID + "&sessionID=" + sessionID;
    socket = new WebSocket(url);
    socket.addEventListener('open', function (event) {
      console.log("On openCall");
    });
  }
  const loadTime = totalTimeToLoadDocument();//window.performance.timing.domContentLoadedEventEnd - window.performance.timing.navigationStart;
  const data = {
    accountID: accountId,
    userID: cookieData.user_Id,
    sessionID: cookieData.session_id,
    pageURL: window.location.href,
    pageLoadTime: loadTime,
    performanceData: window.performance,
    pageSource: document.querySelector("html").outerHTML
  }
  return savePageLoadData(data);
};

function saveHighlightedTextData()
{
  checkAuthorization();
  if (!isCookieePresent()) {
    return;
  }

  var selectedText = (document.all) ? document.selection.createRange().text : document.getSelection();

  if(selectedText == undefined || selectedText == null || selectedText == '' || selectedText == 'None' || (selectedText || '').toString().trim().length == 0){
    return;
  }
  userEvent(EVENTS.TEXT_HIGHLIGHT, null, null, null, null, null, null, null, null, null, null, null, selectedText.toString());
}

function saveDblClickEventData(event){

  checkAuthorization();
  if (!isCookieePresent()) {
    return;
  }
  let selectedElement = event.target.outerHTML;
  if(selectedElement == undefined || selectedElement == null || selectedElement == '' || selectedElement == 'None' || (selectedElement || '').toString().trim().length == 0){
    return;
  }
  userEvent(EVENTS.DOUBLE_CLICK, null, null, null, null, null, null, null, null, null, null, null, null, null, null, selectedElement.toString());
}

function saveNewTabEventData(event) {

  checkAuthorization();
  if (!isCookieePresent()) {
    return;
  }

  let origin = event.target.closest("a");

  if (event && (event.which == 1 || event.which == 2) && (event.metaKey || event.ctrlKey || event.shiftKey)) {
    if (origin) {
      userEvent(EVENTS.NEW_TAB, origin.href, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
    }
  }
}

function savePageLoadData(data) {
  return new Promise((resolve, reject) => {
    let xhttp = new XMLHttpRequest();
    xhttp.open("POST", `${pageLoadEvent}`, true);
    xhttp.setRequestHeader('Content-Type', 'application/json');
    xhttp.setRequestHeader('version', 'v1');
    xhttp.setRequestHeader('API-KEY', APIKEY);

    xhttp.onreadystatechange = function () {
      if (this.readyState === 4) {
        let localData = JSON.parse(this.responseText);
        if (this.status === 200 && localData.status == "SUCCESS") {
          console.log(localData.responseData);
          resolve();
        } else if (localData.status == "FAIL") {
          if (["ERRCODE19", "ERRCODE14"].includes(localData.errorList[0].code && localData.errorList[0].code)) {
            addToQueue("LOAD", data, data.eventID);
            if (!isAuthorizing) {
              isAuthorizing = true;
              processUnAuthorized(data);
            }
          }
          reject();
        }
      }
    };
    xhttp.send(JSON.stringify(data));
  });
}

function addToQueue(type, data, eventType) {
    let queue = localStorage.getItem("HUM_QUEUE");
    if (queue) {
        queue = JSON.parse(queue);
    } else {
        queue = {
            "EVENT": {},
            "LOAD": {}
        }
    }

    if(queue[type]) {
        queue[type][+new Date() + '_' + eventType]=data;
    }
    localStorage.setItem("HUM_QUEUE", JSON.stringify(queue));
}

function readQueue() {
    let queue = localStorage.getItem("HUM_QUEUE");
    if (queue) {
        queue = JSON.parse(queue);
    } else {
        queue = {
            "EVENT": {},
            "LOAD": {}
        }
    }
    localStorage.removeItem("HUM_QUEUE");
    return queue;
}

function getElementPath(el) {
  if (el.id) {
    return `#${el.id}`;
  }
  const fullPath = [];
  const fn = el => {
      let tagName = el.tagName.toLowerCase();
      let elPath = '';
      if (el.classList.length) {
          var classes = [...el.classList];
          var resultclasses = classes.filter(e => {
            return /[0-9]{6}/gi.test(e) == false
          });
          elPath += '.' + [...resultclasses].join('.');
      }
      if (el.parentElement) {
          if (el.previousElementSibling || el.nextElementSibling) {
              let nthChild = 1;
              for (let e = el.previousElementSibling; e; e = e.previousElementSibling, nthChild++);
              // tagName += `:nth-child(${nthChild})`;
          }
          fn(el.parentElement);
      }
      fullPath.push(tagName + elPath);
  };
  fn(el);
  return fullPath.join('>');
}

function recordMouseMove(event) {
  if (Date.now() - lastMove > 1000) {
    if (this.socket == undefined || this.socket.readyState == 3 || this.socket.readyState == 2 || this.socket.readyState == 0) {
      return;
    }
    var rect = event.target.getBoundingClientRect();
    var x = event.clientX - rect.left; //x position within the element.
    var y = event.clientY - rect.top;  //y position within the element.
    var purl = document.URL;
    const obj = { cursorX: x, cursorY: y, pageUrl: purl, windowSize: {"innerHeight":window.innerHeight,"outerHeight":window.outerHeight,"innerWidht":window.innerWidth,"outerWidth":window.outerWidth}};
    this.socket.send(JSON.stringify(obj));
    lastMove = Date.now();
  }
};

function totalTimeToLoadDocument() {
  const navEntities = performance.getEntriesByType("navigation");
  return (Math.ceil(navEntities[0].loadEventEnd));
}



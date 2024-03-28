/*!
Humaine AI Client Side Tracker
Date:2021-09-23 
*/

const accountId=181;var oldSessionId;const BASE_URL="https://dca.dev.nonprod.bodegaswimwear.com//humaine/collection/api",getBaseURL=function(){return BASE_URL.endsWith("/")?BASE_URL.substr(0,BASE_URL.length-1):BASE_URL},userEventURL=`${getBaseURL()}/userEvent`,pageLoadEvent=`${getBaseURL()}/userEvent/pageLoad`;var APIKEY=null;const EVENTS={START:"START",END:"END",NAV:"NAV",PRODVIEW:"PRODVIEW",RATE:"RATE",BUY:"BUY",REVIEW:"REVIEW",ADDCART:"ADDCART",ADDLIST:"ADDLIST",REMLIST:"REMLIST",REMCART:"REMCART",ADDTOCARD:"ADDCART",DISCOVER:"DISCOVER",NEWSLETTER_SUBSCRIBE:"NEWSLETTER_SUBSCRIBE",SEARCH:"SEARCH",MENU:"MENU",BACK_NAV:"BACK_NAV",SAVE_FOR_LATER:"SAVE_FOR_LATER",DELETE:"DELETE",PROD_RETURN:"PROD_RETURN",VISIT_BLOG_POST:"VISIT_BLOG_POST",VISIT_SOCIAL_MEDIA:"VISIT_SOCIAL_MEDIA"};var isAuthorizing=!1;const ACTION={ADD:"ADD",REMOVE:"REMOVE"},isCookieePresent=()=>getCookie("humaine_tracker"),startSession=async function(){if(console.log("cookie Present::",isCookieePresent()),checkAuthorization(),isCookieePresent()){var t=JSON.parse(getCookie("humaine_tracker"));console.log("cookie",t);let e=new Date(parseInt(t.session_time)),o=new Date;var s=e.getTime()/1e3,n=o.getTime()/1e3,t=await sessionTimeout();n<s+t?console.warn("Session Alive"):s+t<n&&(endSession(),console.warn("Session End"))}else await creatCookie()},endSession=function(){let e=JSON.parse(getCookie("humaine_tracker"));oldSessionId=e.session_id,userEvent(EVENTS.END),e.session_id=sessionId(),e.session_time=(new Date).getTime(),document.cookie="humaine_tracker="+JSON.stringify(e)+"; domain="+location.hostname+"; path=/",getCordinates()},sessionTimeout=async function(){return checkAuthorization(),new Promise((o,e)=>{var t=new XMLHttpRequest;t.open("GET",`${getBaseURL()}/sessionTimeout/`+accountId,!0),t.setRequestHeader("Content-Type","application/json"),t.setRequestHeader("version","v1"),t.setRequestHeader("API-KEY",APIKEY),t.onreadystatechange=function(){var e;4===this.readyState&&(200===this.status?(e=JSON.parse(this.responseText),timeout=e.responseData.timeout,o(timeout)):o(0))},t.send()})};var sessionId=function(){var t=(new Date).getTime(),e="xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx".replace(/[xy]/g,function(e){var o=(t+16*Math.random())%16|0;return t=Math.floor(t/16),("x"==e?o:3&o|8).toString(16)});return accountId+"_"+e};const getIP=async function(){return new Promise((o,e)=>{var t=new XMLHttpRequest;t.open("GET","https://api.ipify.org/?format=json",!0),t.onreadystatechange=function(){var e;4==this.readyState&&(200===this.status?(e=this.responseText,o(e)):o(this.errorText))},t.send()})},initializeTracker=function(e){APIKEY=e,window.onload=savePageLoadEventData()},checkAuthorization=function(){if(!APIKEY)throw"Unauthorised"},creatCookie=async function(){let e=new Date;e.setFullYear(e.getFullYear()+1);var o=JSON.parse(await getIP()).ip.replace(/\./g,""),t=navigator.appVersion.replaceAll(";"," "),t={session_id:sessionId(),session_time:(new Date).getTime(),user_Id:accountId+"_"+navigator.platform+"_"+navigator.appName+"_"+o,device_Id:navigator.platform+"_"+navigator.appName,deviceType:navigator.appName+"_"+t};document.cookie="humaine_tracker="+JSON.stringify(t)+";expires="+e.toGMTString()+"; domain=."+location.hostname+"; path=/";await getCordinates();savePageLoadEventData(),console.warn("Session Started..")};async function getCordinates(){let n;return new Promise((t,s)=>{navigator.geolocation.getCurrentPosition(async function(e){lat=e.coords.latitude,long=e.coords.longitude;let o=new XMLHttpRequest;o.open("GET","https://api.bigdatacloud.net/data/reverse-geocode-client?latitude="+lat+"&longitude="+long+"&localityLanguage=en",!0),o.onreadystatechange=async function(){4==this.readyState&&200==this.status?(result=JSON.parse(this.responseText),n={city:result.locality,state:result.principalSubdivision,country:result.countryName,lat:lat,long:long},await setLocationAndUserEvent(n),t()):4==this.readyState&&(console.error(this.errorText),s())},o.send()},async function(e){n={};await setLocationAndUserEvent(n);t()})})}const setLocationAndUserEvent=async function(e){let o=JSON.parse(getCookie("humaine_tracker"));return o.latitude=e.lat,o.longitude=e.long,o.city=e.city,o.state=e.state,o.country=e.country,document.cookie="humaine_tracker="+JSON.stringify(o)+"; domain="+location.hostname+"; path=/",userEvent(EVENTS.START)},getCookie=function(e){for(var o=document.cookie.split(";"),t=0;t<o.length;t++){var s=o[t].split("=");if(e==s[0].trim())return decodeURIComponent(s[1])}},processUnAuthorized=async function(){if(!isAuthorizing)return!1;if(checkAuthorization(),isCookieePresent()){let e=JSON.parse(getCookie("humaine_tracker"));e.session_id=sessionId(),e.session_time=(new Date).getTime(),document.cookie="humaine_tracker="+JSON.stringify(e)+"; domain=."+location.hostname+"; path=/",await userEvent(EVENTS.START),isAuthorizing=!1,processQueue()}else await creatCookie(),isAuthorizing=!1,processQueue()},processQueue=function(){let t=JSON.parse(getCookie("humaine_tracker"));const s=readQueue();Object.keys(s.EVENT).forEach(e=>{const o=s.EVENT[e];o.sessionID=t.session_id,saveEventData(o)}),Object.keys(s.LOAD).forEach(e=>{const o=s.LOAD[e];o.sessionID=t.session_id,savePageLoadData(o)})},userEvent=function(e,o,t,s,n,i,r,a,c,u,E){checkAuthorization();var l=window.performance.timing.domContentLoadedEventEnd-window.performance.timing.navigationStart,d=JSON.parse(getCookie("humaine_tracker"));const S={accountID:accountId,userID:d.user_Id,eventID:e,sessionID:d.session_id};switch(e){case EVENTS.START:S.city=d.city,S.latitude=d.latitude,S.longitude=d.longitude,S.state=d.state,S.country=d.country,S.deviceId=d.device_Id,S.deviceType=d.deviceType,S.pageLoadTime=l;break;case EVENTS.END:S.deviceId=d.device_Id,S.sessionID=oldSessionId;break;case EVENTS.NAV:if(!o)return void console.log("NAV URL is missing");S.pageURL=o;break;case EVENTS.DISCOVER:case EVENTS.BACK_NAV:if(!o)return void console.log("Page URL is missing");S.pageURL=o;break;case EVENTS.BUY:if(!o&&!t&&!s)return void console.log("ProductID or action is missing");S.productID=o,S.productQuantity=t,S.saleAmount=s;break;case EVENTS.PRODVIEW:case EVENTS.RATE:case EVENTS.REVIEW:case EVENTS.ADDCART:case EVENTS.REMCART:case EVENTS.ADDLIST:case EVENTS.REMLIST:case EVENTS.SAVE_FOR_LATER:case EVENTS.DELETE:case EVENTS.PROD_RETURN:if(!o)return void console.log("Product ID is missing");S.productID=o;break;case EVENTS.MENU:if(!u||!E)return void console.error("menu URL,menuName URL is missing");S.menuId=c,S.menuURL=u,S.menuName=E;break;case EVENTS.VISIT_BLOG_POST:if(!n||!i)return void console.log("Post title or Post ID is missing");S.postTitle=n,S.postId=i;break;case EVENTS.VISIT_SOCIAL_MEDIA:if(!a||!r)return void console.error("social media platform, social media URL is missing");S.socialMediaPlateForm=r,S.socialMediaURL=a;break;case EVENTS.NEWSLETTER_SUBSCRIBE:case EVENTS.SEARCH:break;default:return void console.log("Invalid Request")}return saveEventData(S)},saveEventData=async function(s){return new Promise((o,t)=>{let e=new XMLHttpRequest;e.open("POST",`${userEventURL}`,!0),e.setRequestHeader("Content-Type","application/json"),e.setRequestHeader("version","v1"),e.setRequestHeader("API-KEY",APIKEY),e.onreadystatechange=function(){var e;4===this.readyState&&(e=JSON.parse(this.responseText),200===this.status&&"SUCCESS"==e.status?o():"FAIL"==e.status&&(["ERRCODE19","ERRCODE14"].includes(e.errorList[0].code&&e.errorList[0].code)&&(addToQueue("EVENT",s,s.eventID),isAuthorizing||(isAuthorizing=!0,processUnAuthorized(s))),t()))},e.send(JSON.stringify(s))})},pageNavigate=function(e){isCookieePresent?e?(userEvent(EVENTS.NAV,e),console.log("Success")):console.error("NAV URL is missing"):console.error("Cookie not present")},viewProduct=function(e){isCookieePresent?e?(userEvent(EVENTS.PRODVIEW,e),console.log("Success")):console.error("ProductID is missing"):console.error("Cookie not present")},cartAction=function(e,o){isCookieePresent?e&&o?o==ACTION.ADD?userEvent(EVENTS.ADDCART,e):o==ACTION.REMOVE&&userEvent(EVENTS.REMCART,e):console.error("ProductID or action is missing"):console.error("Cookie not present")},wishlistAction=function(e,o){isCookieePresent?e&&o?o==ACTION.ADD?userEvent(EVENTS.ADDLIST,e):o==ACTION.REMOVE&&userEvent(EVENTS.REMLIST,e):console.error("ProductID or action is missing"):console.error("Cookie not present")},buyProduct=function(e,o,t,s){isCookieePresent?e&&o&&t?(userEvent(EVENTS.BUY,e,o,t),console.log("Success")):console.error("ProductID ,quantity or sale amount  is missing"):console.error("Cookie not present")},rateProduct=function(e){isCookieePresent?e?(userEvent(EVENTS.RATE,e),console.log("Success")):console.error("ProductID is missing"):console.error("Cookie not present")},reviewProduct=function(e){isCookieePresent?e?userEvent(EVENTS.REVIEW,e):console.error("ProductID is missing"):console.error("Cookie not present")},logout=function(){var e;isCookieePresent&&(e=JSON.parse(getCookie("humaine_tracker")),(oldSessionId=e.session_id)?(userEvent(EVENTS.END),console.log("Logout successfully..")):console.error("Old sessionID is missing"))},discoverNavigate=function(e){isCookieePresent?e?(userEvent(EVENTS.DISCOVER,e),console.log("Success")):console.error("Page URL is missing"):console.error("Cookie not present")},newsletter_subscriber=function(){isCookieePresent?(userEvent(EVENTS.NEWSLETTER_SUBSCRIBE),console.log("Success")):console.error("Cookie not present")},search=function(){isCookieePresent?(userEvent(EVENTS.SEARCH),console.log("Success")):console.error("Cookie not present")},menu=function(e,o,t){isCookieePresent?o&&t?(userEvent(EVENTS.MENU,null,null,null,null,null,null,null,e,o,t),console.log("Success")):console.error("menu URL,menuName URL is missing"):console.error("Cookie not present")},backNavigate=function(e){isCookieePresent?e?(userEvent(EVENTS.BACK_NAV,e),console.log("Success")):console.error("Page URL is missing"):console.error("Cookie not present")},saveForLater=function(e){isCookieePresent?e?(userEvent(EVENTS.SAVE_FOR_LATER,e),console.log("Success")):console.error("Product ID is missing"):console.error("Cookie not present")},deleteItem=function(e){isCookieePresent?e?(userEvent(EVENTS.DELETE,e),console.log("Success")):console.error("ProductID is missing"):console.error("Cookie not present")},productReturn=function(e){isCookieePresent?e?(userEvent(EVENTS.PROD_RETURN,e),console.log("Success")):console.error("ProductID is missing"):console.error("Cookie not present")},visitBlogPost=function(e,o){isCookieePresent?e&&o?(userEvent(EVENTS.VISIT_BLOG_POST,null,null,null,e,o),console.log("Success")):console.error("Post Title, Post ID is missing"):console.error("Cookie not present")},visitSocialMedia=function(e,o){isCookieePresent?e&&o?(userEvent(EVENTS.VISIT_SOCIAL_MEDIA,null,null,null,null,null,e,o),console.log("Success")):console.error("social media platform, social media URL is missing"):console.error("Cookie not present")};function savePageLoadEventData(){if(checkAuthorization(),isCookieePresent()){var e=window.performance.timing.domContentLoadedEventEnd-window.performance.timing.navigationStart,o=JSON.parse(getCookie("humaine_tracker"));return savePageLoadData({accountID:accountId,userID:o.user_Id,sessionID:o.session_id,pageURL:window.location.href,pageLoadTime:e,performanceData:window.performance,pageSource:document.querySelector("html").outerHTML})}}function savePageLoadData(s){return new Promise((o,t)=>{let e=new XMLHttpRequest;e.open("POST",`${pageLoadEvent}`,!0),e.setRequestHeader("Content-Type","application/json"),e.setRequestHeader("version","v1"),e.setRequestHeader("API-KEY",APIKEY),e.onreadystatechange=function(){var e;4===this.readyState&&(e=JSON.parse(this.responseText),200===this.status&&"SUCCESS"==e.status?(console.log(e.responseData),o()):"FAIL"==e.status&&(["ERRCODE19","ERRCODE14"].includes(e.errorList[0].code&&e.errorList[0].code)&&(addToQueue("LOAD",s,s.eventID),isAuthorizing||(isAuthorizing=!0,processUnAuthorized(s))),t()))},e.send(JSON.stringify(s))})}function addToQueue(e,o,t){let s=localStorage.getItem("HUM_QUEUE");s=s?JSON.parse(s):{EVENT:{},LOAD:{}},s[e]&&(s[e][+new Date+"_"+t]=o),localStorage.setItem("HUM_QUEUE",JSON.stringify(s))}function readQueue(){let e=localStorage.getItem("HUM_QUEUE");return e=e?JSON.parse(e):{EVENT:{},LOAD:{}},localStorage.removeItem("HUM_QUEUE"),e}
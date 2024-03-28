/*!
Humaine AI Client Side Tracker
Date:2021-07-05 
*/

const accountId=9;var oldSessionId;const BASE_URL="https://dca.dev.nonprod.bodegaswimwear.com/humaine/portal/api",userEventURL=`${BASE_URL}/userEvent`,EVENTS={START:"START",END:"END",NAV:"NAV",PRODVIEW:"PRODVIEW",RATE:"RATE",BUY:"BUY",REVIEW:"REVIEW",ADDCART:"ADDCART",ADDLIST:"ADDLIST",REMLIST:"REMLIST",REMCART:"REMCART",ADDTOCARD:"ADDCART"},ACTION={ADD:"ADD",REMOVE:"REMOVE"},isCookieePresent=()=>getCookie("humaine_tracker"),startSession=async function(){if(isCookieePresent()){var o=JSON.parse(getCookie("humaine_tracker"));let e=new Date(parseInt(o.session_time)),t=new Date;var s=e.getTime()/1e3,n=t.getTime()/1e3,o=await sessionTimeout();n<s+o?console.warn("Session Alive"):s+o<n&&(endSession(),console.warn("Session End"))}else await creatCookie()},endSession=function(){let e=JSON.parse(getCookie("humaine_tracker"));oldSessionId=e.session_id,userEvent(EVENTS.END),e.session_id=sessionId(),e.session_time=(new Date).getTime(),document.cookie="humaine_tracker="+JSON.stringify(e),getCordinates()},sessionTimeout=async function(){return new Promise((t,e)=>{var o=new XMLHttpRequest;o.open("GET",`${BASE_URL}/sessionTimeout/`+accountId,!0),o.setRequestHeader("Content-Type","application/json"),o.setRequestHeader("version","v1"),o.onreadystatechange=function(){var e;4===this.readyState&&(200===this.status?(e=JSON.parse(this.responseText),timeout=e.responseData.timeout,t(timeout)):t(0))},o.send()})};var sessionId=function(){var o=(new Date).getTime(),e="xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx".replace(/[xy]/g,function(e){var t=(o+16*Math.random())%16|0;return o=Math.floor(o/16),("x"==e?t:3&t|8).toString(16)});return accountId+"_"+e};const getIP=async function(){return new Promise((t,e)=>{var o=new XMLHttpRequest;o.open("GET","https://api.ipify.org/?format=json",!0),o.onreadystatechange=function(){var e;4==this.readyState&&(200===this.status?(e=this.responseText,t(e)):t(this.errorText))},o.send()})},creatCookie=async function(){let e=new Date;e.setFullYear(e.getFullYear()+1);var t=JSON.parse(await getIP()).ip.replace(/\./g,""),o=navigator.appVersion.replaceAll(";"," "),o={session_id:sessionId(),session_time:(new Date).getTime(),user_Id:accountId+"_"+navigator.platform+"_"+navigator.appName+"_"+t,device_Id:navigator.platform+"_"+navigator.appName,deviceType:navigator.appName+"_"+o};document.cookie="humaine_tracker="+JSON.stringify(o)+";expires="+e.toGMTString();await getCordinates();console.warn("Session Started..")};async function getCordinates(){let n;return new Promise((o,s)=>{navigator.geolocation.getCurrentPosition(async function(e){lat=e.coords.latitude,long=e.coords.longitude;let t=new XMLHttpRequest;t.open("GET","https://api.bigdatacloud.net/data/reverse-geocode-client?latitude="+lat+"&longitude="+long+"&localityLanguage=en",!0),t.onreadystatechange=async function(){4==this.readyState&&200==this.status?(result=JSON.parse(this.responseText),n={city:result.locality,state:result.principalSubdivision,country:result.countryName,lat:lat,long:long},await setLocationAndUserEvent(n),o()):4==this.readyState&&(console.error(this.errorText),s())},t.send()},async function(e){n={};await setLocationAndUserEvent(n);o()})})}const setLocationAndUserEvent=async function(e){let t=JSON.parse(getCookie("humaine_tracker"));return t.latitude=e.lat,t.longitude=e.long,t.city=e.city,t.state=e.state,t.country=e.country,document.cookie="humaine_tracker="+JSON.stringify(t),userEvent(EVENTS.START)},getCookie=function(e){for(var t=document.cookie.split(";"),o=0;o<t.length;o++){var s=t[o].split("=");if(e==s[0].trim())return decodeURIComponent(s[1])}},userEvent=function(e,t,o,s){var n=window.performance.timing.domContentLoadedEventEnd-window.performance.timing.navigationStart,i=JSON.parse(getCookie("humaine_tracker"));const r={accountID:accountId,userID:i.user_Id,eventID:e,sessionID:i.session_id};switch(e){case EVENTS.START:r.city=i.city,r.latitude=i.latitude,r.longitude=i.longitude,r.state=i.state,r.country=i.country,r.deviceId=i.device_Id,r.deviceType=i.deviceType,r.pageLoadTime=n;break;case EVENTS.END:r.deviceId=i.device_Id,r.sessionID=oldSessionId;break;case EVENTS.NAV:if(!t)return void console.log("NAV URL is missing");r.pageURL=t;break;case EVENTS.BUY:if(!t&&!o&&!s)return void console.log("ProductID or action is missing");r.productID=t,r.productQuantity=o,r.saleAmount=s;break;case EVENTS.PRODVIEW:case EVENTS.RATE:case EVENTS.REVIEW:case EVENTS.ADDCART:case EVENTS.REMCART:case EVENTS.ADDLIST:case EVENTS.REMLIST:if(!t)return void console.log("Product ID is missing");r.productID=t;break;default:return void console.log("Invalid Request")}return new Promise((t,o)=>{let e=new XMLHttpRequest;e.open("POST",`${userEventURL}`,!0),e.setRequestHeader("Content-Type","application/json"),e.setRequestHeader("version","v1"),e.onreadystatechange=function(){var e;4===this.readyState&&(e=JSON.parse(this.responseText),200===this.status&&"SUCCESS"==e.status?(console.log(e.responseData),t()):"FAIL"==e.status&&(console.error(e.errorList[0]),o()))},e.send(JSON.stringify(r))})},pageNavigate=function(e){isCookieePresent?e?(userEvent(EVENTS.NAV,e),console.log("Success")):console.error("NAV URL is missing"):console.error("Cookie not present")},viewProduct=function(e){isCookieePresent?e?(userEvent(EVENTS.PRODVIEW,e),console.log("Success")):console.error("ProductID is missing"):console.error("Cookie not present")},cartAction=function(e,t){isCookieePresent?e&&t?t==ACTION.ADD?userEvent(EVENTS.ADDCART,e):t==ACTION.REMOVE&&userEvent(EVENTS.REMCART,e):console.error("ProductID or action is missing"):console.error("Cookie not present")},wishlistAction=function(e,t){isCookieePresent?e&&t?t==ACTION.ADD?userEvent(EVENTS.ADDLIST,e):t==ACTION.REMOVE&&userEvent(EVENTS.REMLIST,e):console.error("ProductID or action is missing"):console.error("Cookie not present")},buyProduct=function(e,t,o){isCookieePresent?e&&t&&o?(userEvent(EVENTS.BUY,e,t,o),console.log("Success")):console.error("ProductID ,quantity or sale amount  is missing"):console.error("Cookie not present")},rateProduct=function(e){isCookieePresent?e?(userEvent(EVENTS.RATE,e),console.log("Success")):console.error("ProductID is missing"):console.error("Cookie not present")},reviewProduct=function(e){isCookieePresent?e?userEvent(EVENTS.REVIEW,e):console.error("ProductID is missing"):console.error("Cookie not present")},logout=function(){var e;isCookieePresent&&(e=JSON.parse(getCookie("humaine_tracker")),(oldSessionId=e.session_id)?(userEvent(EVENTS.END),console.log("Logout successfully..")):console.error("Old sessionID is missing"))};

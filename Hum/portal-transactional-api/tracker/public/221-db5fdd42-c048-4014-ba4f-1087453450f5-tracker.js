/*!
Humaine AI Client Side Tracker
Date:2022-03-03 
*/

const accountId=221;var oldSessionId,socket,APIKEY;const BASE_URL="https://dca.dev.nonprod.bodegaswimwear.com/humaine/collection/api",getBaseURL=function(){return BASE_URL.endsWith("/")?BASE_URL.substr(0,BASE_URL.length-1):BASE_URL},userEventURL=`${getBaseURL()}/userEvent`,pageLoadEvent=`${getBaseURL()}/userEvent/pageLoad`,webSocketURL=BASE_URL.replace("https","wss")+"/eventRecording?";var lastMove=0;const EVENTS={START:"START",END:"END",NAV:"NAV",PRODVIEW:"PRODVIEW",RATE:"RATE",BUY:"BUY",REVIEW:"REVIEW",ADDCART:"ADDCART",ADDLIST:"ADDLIST",REMLIST:"REMLIST",REMCART:"REMCART",ADDTOCARD:"ADDCART",DISCOVER:"DISCOVER",NEWSLETTER_SUBSCRIBE:"NEWSLETTER_SUBSCRIBE",SEARCH:"SEARCH",MENU:"MENU",BACK_NAV:"BACK_NAV",SAVE_FOR_LATER:"SAVE_FOR_LATER",DELETE:"DELETE",PROD_RETURN:"PROD_RETURN",VISIT_BLOG_POST:"VISIT_BLOG_POST",VISIT_SOCIAL_MEDIA:"VISIT_SOCIAL_MEDIA",TEXT_HIGHLIGHT:"TEXT_HIGHLIGHT",IMG_VIEW:"IMG_VIEW",DOUBLE_CLICK:"DOUBLE_CLICK",NEW_TAB:"NEW_TAB"};var isAuthorizing=!1;const ACTION={ADD:"ADD",REMOVE:"REMOVE"},isCookieePresent=()=>getCookie("humaine_tracker"),startSession=async function(){if(checkAuthorization(),isCookieePresent()){var o=JSON.parse(getCookie("humaine_tracker"));let e=new Date(parseInt(o.session_time)),n=new Date;o=e.getTime()/1e3;n.getTime()/1e3<o+await sessionTimeout()&&console.warn("Session Alive")}else await creatCookie()},endSession=function(){let e=JSON.parse(getCookie("humaine_tracker"));oldSessionId=e.session_id,userEvent(EVENTS.END),e.session_id=sessionId(),e.session_time=(new Date).getTime(),document.cookie="humaine_tracker="+JSON.stringify(e)+"; domain="+location.hostname+"; path=/",getCordinates()},sessionTimeout=async function(){return checkAuthorization(),new Promise((n,e)=>{var o=new XMLHttpRequest;o.open("GET",`${getBaseURL()}/sessionTimeout/`+accountId,!0),o.setRequestHeader("Content-Type","application/json"),o.setRequestHeader("version","v1"),o.setRequestHeader("API-KEY",APIKEY),o.onreadystatechange=function(){var e;4===this.readyState&&(200===this.status?(e=JSON.parse(this.responseText),timeout=e.responseData.timeout,n(timeout)):n(0))},o.send()})};var sessionId=function(){var o=(new Date).getTime(),e="xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx".replace(/[xy]/g,function(e){var n=(o+16*Math.random())%16|0;return o=Math.floor(o/16),("x"==e?n:3&n|8).toString(16)});return accountId+"_"+e};const getIP=async function(){return new Promise((n,e)=>{var o=new XMLHttpRequest;o.open("GET","https://api.ipify.org/?format=json",!0),o.onreadystatechange=function(){var e;4==this.readyState&&(200===this.status?(e=this.responseText,n(e)):n(this.errorText))},o.send()})},initializeTracker=function(e){APIKEY=e,window.onload=savePageLoadEventData(),document.addEventListener("mouseup",function(){saveHighlightedTextData()}),document.addEventListener("dblclick",function(e){saveDblClickEventData(e)}),document.addEventListener("mousedown",function(e){saveNewTabEventData(e)});const n=function(e){recordMouseMove(e)};document.addEventListener("mousemove",n),setTimeout(()=>{document.removeEventListener("mousemove",n)},12e4)},checkAuthorization=function(){if(!APIKEY)throw"Unauthorised"},creatCookie=async function(){let e=new Date;e.setFullYear(e.getFullYear()+1);var n=JSON.parse(await getIP()).ip.replace(/\./g,""),o=navigator.appVersion.replaceAll(";"," "),o={session_id:sessionId(),session_time:(new Date).getTime(),user_Id:accountId+"_"+navigator.platform+"_"+navigator.appName+"_"+n,device_Id:navigator.platform+"_"+navigator.appName,deviceType:navigator.appName+"_"+o};document.cookie="humaine_tracker="+JSON.stringify(o)+";expires="+e.toGMTString()+"; domain=."+location.hostname+"; path=/";await getCordinates();savePageLoadEventData(),console.warn("Session Started..")};async function getCordinates(){let s;return new Promise((o,t)=>{navigator.geolocation.getCurrentPosition(async function(e){lat=e.coords.latitude,long=e.coords.longitude;let n=new XMLHttpRequest;n.open("GET","https://api.bigdatacloud.net/data/reverse-geocode-client?latitude="+lat+"&longitude="+long+"&localityLanguage=en",!0),n.onreadystatechange=async function(){4==this.readyState&&200==this.status?(result=JSON.parse(this.responseText),s={city:result.locality,state:result.principalSubdivision,country:result.countryName,lat:lat,long:long},s=JSON.parse(this.responseText),await setLocationAndUserEvent(s),o()):4==this.readyState&&(console.error(this.errorText),t())},n.send()},async function(e){s={};await setLocationAndUserEvent(s);o()})})}const setLocationAndUserEvent=async function(e){let n=JSON.parse(getCookie("humaine_tracker"));return n.latitude=e.lat,n.longitude=e.long,n.city=e.city,n.state=e.state,n.country=e.country,document.cookie="humaine_tracker="+JSON.stringify(n)+"; domain="+location.hostname+"; path=/",userEvent(EVENTS.START)},getCookie=function(e){for(var n=document.cookie.split(";"),o=0;o<n.length;o++){var t=n[o].split("=");if(e==t[0].trim())return decodeURIComponent(t[1])}},processUnAuthorized=async function(){if(!isAuthorizing)return!1;if(checkAuthorization(),isCookieePresent()){let e=JSON.parse(getCookie("humaine_tracker"));e.session_id=sessionId(),e.session_time=(new Date).getTime(),document.cookie="humaine_tracker="+JSON.stringify(e)+"; domain=."+location.hostname+"; path=/",await userEvent(EVENTS.START),isAuthorizing=!1,processQueue()}else await creatCookie(),isAuthorizing=!1,processQueue()},processQueue=function(){let o=JSON.parse(getCookie("humaine_tracker"));const t=readQueue();Object.keys(t.EVENT).forEach(e=>{const n=t.EVENT[e];n.eventID!=EVENTS.END&&(n.sessionID=o.session_id,saveEventData(n))}),Object.keys(t.LOAD).forEach(e=>{const n=t.LOAD[e];n.sessionID=o.session_id,savePageLoadData(n)})},userEvent=function(e,n,o,t,s,i,l,r,u,a,c,E=null,d=null,g=null,S=null,T=null,I=null){checkAuthorization();var m=totalTimeToLoadDocument(),v=JSON.parse(getCookie("humaine_tracker"));const p={accountID:accountId,userID:v.user_Id,eventID:e,sessionID:v.session_id};switch(E&&(p.targetElement=getElementPath(E)),e){case EVENTS.START:p.city=v.city,p.latitude=v.latitude,p.longitude=v.longitude,p.state=v.state,p.country=v.country,p.deviceId=v.device_Id,p.deviceType=v.deviceType,p.pageLoadTime=m;break;case EVENTS.END:p.deviceId=v.device_Id,p.sessionID=oldSessionId;break;case EVENTS.NAV:if(!n)return void console.log("NAV URL is missing");p.pageURL=n;break;case EVENTS.DISCOVER:case EVENTS.BACK_NAV:if(!n)return void console.log("Page URL is missing");p.pageURL=n;break;case EVENTS.BUY:if(!n&&!o&&!t)return void console.log("ProductID or action is missing");p.productID=n,p.productQuantity=o,p.saleAmount=t,p.couponCode=I;break;case EVENTS.RATE:p.ratingValue=S;break;case EVENTS.PRODVIEW:case EVENTS.REVIEW:case EVENTS.ADDCART:case EVENTS.REMCART:case EVENTS.ADDLIST:case EVENTS.REMLIST:case EVENTS.SAVE_FOR_LATER:case EVENTS.DELETE:case EVENTS.PROD_RETURN:if(!n)return void console.log("Product ID is missing");p.productID=n;break;case EVENTS.MENU:if(!a||!c)return void console.error("menu URL,menuName URL is missing");p.menuId=u,p.menuURL=a,p.menuName=c;break;case EVENTS.VISIT_BLOG_POST:if(!s||!i)return void console.log("Post title or Post ID is missing");p.postTitle=s,p.postId=i;break;case EVENTS.VISIT_SOCIAL_MEDIA:if(!r||!l)return void console.error("social media platform, social media URL is missing");p.socialMediaPlateForm=l,p.socialMediaURL=r;break;case EVENTS.NEWSLETTER_SUBSCRIBE:case EVENTS.SEARCH:break;case EVENTS.TEXT_HIGHLIGHT:p.highlightedText=d;break;case EVENTS.IMG_VIEW:p.productID=n,p.productImageURL=g;break;case EVENTS.DOUBLE_CLICK:p.selectedElement=T;break;case EVENTS.NEW_TAB:if(!n)return void console.log("New Tab URL is missing");p.pageURL=n;break;default:return void console.log("Invalid Request")}return saveEventData(p)},saveEventData=async function(t){return new Promise((n,o)=>{let e=new XMLHttpRequest;e.open("POST",`${userEventURL}`,!0),e.setRequestHeader("Content-Type","application/json"),e.setRequestHeader("version","v1"),e.setRequestHeader("API-KEY",APIKEY),e.onreadystatechange=function(){var e;4===this.readyState&&(e=JSON.parse(this.responseText),200===this.status&&"SUCCESS"==e.status?n():"FAIL"==e.status&&(["ERRCODE19","ERRCODE14"].includes(e.errorList[0].code&&e.errorList[0].code)&&(addToQueue("EVENT",t,t.eventID),isAuthorizing||(isAuthorizing=!0,processUnAuthorized(t))),o()))},e.send(JSON.stringify(t))})},pageNavigate=function(e){isCookieePresent?e?(userEvent(EVENTS.NAV,e),console.log("Success")):console.error("NAV URL is missing"):console.error("Cookie not present")},viewProduct=function(e){isCookieePresent?e?(userEvent(EVENTS.PRODVIEW,e),console.log("Success")):console.error("ProductID is missing"):console.error("Cookie not present")},cartAction=function(e,n,o){isCookieePresent?e&&n?n==ACTION.ADD?userEvent(EVENTS.ADDCART,e,null,null,null,null,null,null,null,null,null,o):n==ACTION.REMOVE&&userEvent(EVENTS.REMCART,e,null,null,null,null,null,null,null,null,null,o):console.error("ProductID or action is missing"):console.error("Cookie not present")},wishlistAction=function(e,n){isCookieePresent?e&&n?n==ACTION.ADD?userEvent(EVENTS.ADDLIST,e):n==ACTION.REMOVE&&userEvent(EVENTS.REMLIST,e):console.error("ProductID or action is missing"):console.error("Cookie not present")},buyProduct=function(e,n,o,t,s){isCookieePresent?e&&n&&o?(userEvent(EVENTS.BUY,e,n,o,null,null,null,null,null,null,null,null,null,null,null,null,s),console.log("Success")):console.error("ProductID ,quantity or sale amount  is missing"):console.error("Cookie not present")},rateProduct=function(e,n,o){isCookieePresent?e?o?(userEvent(EVENTS.RATE,e,null,null,null,null,null,null,null,null,null,n,null,null,o),console.log("Success")):console.error("ratingValue is missing"):console.error("ProductID is missing"):console.error("Cookie not present")},reviewProduct=function(e,n){isCookieePresent?e?userEvent(EVENTS.REVIEW,e,null,null,null,null,null,null,null,null,null,n):console.error("ProductID is missing"):console.error("Cookie not present")},logout=function(){var e;isCookieePresent&&(e=JSON.parse(getCookie("humaine_tracker")),(oldSessionId=e.session_id)?(userEvent(EVENTS.END),console.log("Logout successfully..")):console.error("Old sessionID is missing"))},discoverNavigate=function(e){isCookieePresent?e?(userEvent(EVENTS.DISCOVER,e),console.log("Success")):console.error("Page URL is missing"):console.error("Cookie not present")},newsletter_subscriber=function(e){isCookieePresent?(userEvent(EVENTS.NEWSLETTER_SUBSCRIBE,null,null,null,null,null,null,null,null,null,null,e),console.log("Success")):console.error("Cookie not present")},search=function(){isCookieePresent?(userEvent(EVENTS.SEARCH),console.log("Success")):console.error("Cookie not present")},menu=function(e,n,o,t){isCookieePresent?n&&o?(userEvent(EVENTS.MENU,null,null,null,null,null,null,null,e,n,o,t),console.log("Success")):console.error("menu URL,menuName URL is missing"):console.error("Cookie not present")},backNavigate=function(e){isCookieePresent?e?(userEvent(EVENTS.BACK_NAV,e),console.log("Success")):console.error("Page URL is missing"):console.error("Cookie not present")},saveForLater=function(e,n){isCookieePresent?e?(userEvent(EVENTS.SAVE_FOR_LATER,e,null,null,null,null,null,null,null,null,null,n),console.log("Success")):console.error("Product ID is missing"):console.error("Cookie not present")},deleteItem=function(e,n){isCookieePresent?e?(userEvent(EVENTS.DELETE,e,null,null,null,null,null,null,null,null,null,n),console.log("Success")):console.error("ProductID is missing"):console.error("Cookie not present")},productReturn=function(e,n){isCookieePresent?e?(userEvent(EVENTS.PROD_RETURN,e,null,null,null,null,null,null,null,null,null,n),console.log("Success")):console.error("ProductID is missing"):console.error("Cookie not present")},visitBlogPost=function(e,n,o){isCookieePresent?e&&n?(userEvent(EVENTS.VISIT_BLOG_POST,null,null,null,e,n,null,null,null,null,null,o),console.log("Success")):console.error("Post Title, Post ID is missing"):console.error("Cookie not present")},visitSocialMedia=function(e,n,o){isCookieePresent?e&&n?(userEvent(EVENTS.VISIT_SOCIAL_MEDIA,null,null,null,null,null,e,n,null,null,null,o),console.log("Success")):console.error("social media platform, social media URL is missing"):console.error("Cookie not present")},viewProductImage=function(e,n){isCookieePresent?e&&n?(userEvent(EVENTS.IMG_VIEW,e,null,null,null,null,null,null,null,null,null,null,null,n),console.log("Success")):console.error("product_id or image_url for image view is missing"):console.error("Cookie not present")};function savePageLoadEventData(){if(checkAuthorization(),isCookieePresent()&&!socket){var e=JSON.parse(getCookie("humaine_tracker")),n=e.user_Id,o=e.session_id;APIKEY&&accountId&&n&&o&&(t=webSocketURL+"APIKEY="+APIKEY+"&accountID="+accountId+"&userID="+n+"&sessionID="+o,(socket=new WebSocket(t)).addEventListener("open",function(e){console.log("On openCall")}));var t=totalTimeToLoadDocument();return savePageLoadData({accountID:accountId,userID:e.user_Id,sessionID:e.session_id,pageURL:window.location.href,pageLoadTime:t,performanceData:window.performance,pageSource:document.querySelector("html").outerHTML})}}function saveHighlightedTextData(){var e;checkAuthorization(),!isCookieePresent()||null!=(e=document.all?document.selection.createRange().text:document.getSelection())&&""!=e&&"None"!=e&&0!=(e||"").toString().trim().length&&userEvent(EVENTS.TEXT_HIGHLIGHT,null,null,null,null,null,null,null,null,null,null,null,e.toString())}function saveDblClickEventData(n){if(checkAuthorization(),isCookieePresent()){let e=n.target.outerHTML;null!=e&&null!=e&&""!=e&&"None"!=e&&0!=(e||"").toString().trim().length&&userEvent(EVENTS.DOUBLE_CLICK,null,null,null,null,null,null,null,null,null,null,null,null,null,null,e.toString())}}function saveNewTabEventData(e){var n;checkAuthorization(),isCookieePresent()&&(n=e.target.closest("a"),e&&(1==e.which||2==e.which)&&(e.metaKey||e.ctrlKey||e.shiftKey)&&n&&userEvent(EVENTS.NEW_TAB,n.href,null,null,null,null,null,null,null,null,null,null,null,null,null,null))}function savePageLoadData(t){return new Promise((n,o)=>{let e=new XMLHttpRequest;e.open("POST",`${pageLoadEvent}`,!0),e.setRequestHeader("Content-Type","application/json"),e.setRequestHeader("version","v1"),e.setRequestHeader("API-KEY",APIKEY),e.onreadystatechange=function(){var e;4===this.readyState&&(e=JSON.parse(this.responseText),200===this.status&&"SUCCESS"==e.status?(console.log(e.responseData),n()):"FAIL"==e.status&&(["ERRCODE19","ERRCODE14"].includes(e.errorList[0].code&&e.errorList[0].code)&&(addToQueue("LOAD",t,t.eventID),isAuthorizing||(isAuthorizing=!0,processUnAuthorized(t))),o()))},e.send(JSON.stringify(t))})}function addToQueue(e,n,o){let t=localStorage.getItem("HUM_QUEUE");t=t?JSON.parse(t):{EVENT:{},LOAD:{}},t[e]&&(t[e][+new Date+"_"+o]=n),localStorage.setItem("HUM_QUEUE",JSON.stringify(t))}function readQueue(){let e=localStorage.getItem("HUM_QUEUE");return e=e?JSON.parse(e):{EVENT:{},LOAD:{}},localStorage.removeItem("HUM_QUEUE"),e}function getElementPath(e){if(e.id)return`#${e.id}`;const s=[],i=o=>{var e,n=o.tagName.toLowerCase();let t="";if(o.classList.length&&(e=[...o.classList].filter(e=>0==/[0-9]{6}/gi.test(e)),t+="."+[...e].join(".")),o.parentElement){if(o.previousElementSibling||o.nextElementSibling){let n=1;for(let e=o.previousElementSibling;e;e=e.previousElementSibling,n++);}i(o.parentElement)}s.push(n+t)};return i(e),s.join(">")}function recordMouseMove(e){var n;1e3<Date.now()-lastMove&&null!=this.socket&&3!=this.socket.readyState&&2!=this.socket.readyState&&0!=this.socket.readyState&&(n=e.target.getBoundingClientRect(),n={cursorX:e.clientX-n.left,cursorY:e.clientY-n.top,pageUrl:document.URL,windowSize:{innerHeight:window.innerHeight,outerHeight:window.outerHeight,innerWidht:window.innerWidth,outerWidth:window.outerWidth}},this.socket.send(JSON.stringify(n)),lastMove=Date.now())}function totalTimeToLoadDocument(){var e=performance.getEntriesByType("navigation");return Math.ceil(e[0].loadEventEnd)}
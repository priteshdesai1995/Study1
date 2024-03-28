const fs = require('fs');
const { v4: uuidv4 } = require('uuid');
const parse = require('node-html-parser').parse;
const fileUtils = require('../utils/fileutils');
const puppeteer = require("puppeteer");
const { QueryTypes } = require('sequelize');
const cheerio = require('cheerio');
const rimraf = require("rimraf");
var s3FileUtils;
const extension = 'png'
const iamgeExtension = `.${extension}`;
const partailMenuItemMatch = true;
const browserRunningURL = "http://localhost:3000";
/**
 * Target Element Based Events 
 */
const targetElementBasedEvents = 
['ADDCART', 'BUY', 'NAV', 'REMCART','ADDLIST', 'REMLIST', 'REVIEW', 'RATE', 'DISCOVER', 'SAVE_FOR_LATER', 'PROD_RETURN', 'DELETE', 'VISIT_SOCIAL_MEDIA']; 

const DEVICES = {
    "HIGHEST": {
        "width": 1920,
        "height": 1080
    },
    "LAPTOP-1": {
        "width": 1366,
        "height": 768
    },
    "LAPTOP-2": {
        "width": 1360,
        "height": 768
    },
    "SMALL-DESKTOP-1": {
        "width": 1280,
        "height": 800
    },
    "SMALL-DESKTOP-2": {
        "width": 1024,
        "height": 768
    },
    "IPAD-TAB": {
        "width": 768,
        "height": 1024
    },
    "ANDROID": {
        "width": 360,
        "height": 640
    },
    "IPHONE": {
        "width": 375,
        "height": 667
    }
}
const homePageCSS = `<style>
.slick-track {
    width: 100% !important;
}
.slick-slide {
    width: 100% !important;
}
.flickity-viewport {
    height: 686px !important;
}
.carousel-cell:nth-child(1) {
    left: 0% !important;
}
.carousel-cell:nth-child(2) {
    left: 30% !important;
}
.carousel-cell:nth-child(3) {
    left: 60% !important;
}
.carousel-cell:nth-child(4) {
    left: 90% !important;
}
.carousel-cell:nth-child(5) {
    left: 120% !important;
}
.carousel-cell:nth-child(6) {
    left: 150% !important;
}
.carousel-cell:nth-child(7) {
    left: 180% !important;
}
.carousel-cell:nth-child(8) {
    left: 210% !important;
}
.carousel-cell:nth-child(9) {
    left: 240% !important;
}
.carousel-cell:nth-child(10) {
    left: 270% !important;
}
.carousel-cell:nth-child(11) {
    left: 300% !important;
}
</style>`;

module.exports = function(port,
    heatMapFile,
    customJsFile,
    toolTipJsFile,
    customCSS,
    db, s3) {
var module = {};
s3FileUtils = require('./aws-s3')(s3);
const DBSchema = process.env.SCHEMA;
const replaceAll = (string, search, replace) => {
    return string.split(search).join(replace);
}

const getFormattedHtml = (html) => {
    html = replaceAll(html, '"//', '"http://');
    html = replaceAll(html, "'//", "'http://");
    // html = replaceAll(html, '\n', '');
    return replaceUnescapeHTML(html);
}

const getFileName = (uuid, extension, withDevice = false, type ='') => {
    if (withDevice && type) {
        uuid+=("_" + type);
    }
    if (!uuid.endsWith(extension)) uuid+=extension;
    return uuid;
}

const getPublicHtmlFileLocation = (fileName) => {
    return './public/'+getFileName(fileName, ".html");
}

const getImageWritePath = (file, account, type, forAws = false) => {
    const path = []
    if (!forAws) {
        path.push('./images');
    }
    if (account) {
        path.push(account)
    }
    if (type) {
        path.push(type)
    }
    if (file) {
        path.push(file)
    }
    return `${path.join("/")}`;
}

const getJSFiles = () => {
    return `
        ${heatMapFile}

        ${customJsFile}

        ${toolTipJsFile}
    `;
}

const getCss = () => {
    return `
    ${customCSS}
    `
}

const getLocalServerFileUrl = (file) => {
    return `http://localhost:${port}/${file}`;
}

const getUrl = (url) => {
    if (url.endsWith("/")) {
        url = url.substr(0, url.length-1);
    }
    return url;
}
const processURL = (url) => {
    let urlObj = null;
    try {
        let urlObj = new URL(url);
        return {
            original: url,
            origin: urlObj.origin,
            path: urlObj.pathname,
            fullURL: getUrl(urlObj.origin+urlObj.pathname)
        }
    } catch {
    }
    let originalURL = url;
    url = getUrl(url);
    return {
        original: originalURL,
        origin: url,
        path: url,
        fullURL: url
    }
}

module.generateHeatMapImages = async(id) => {
    try {
        const allStart = +new Date();
    console.log(`------------------  Start Heatmap Generation for, Account: ${id}, started At: ${allStart} ------------------`);
    const pageUrls = {};

        const  accountPagesUrl   = db.sequelize.query(`
            select url, "type",events from ${DBSchema}.account_webpage_url awu where accountid =:account 
        `,{ replacements: { account: id },type: QueryTypes.SELECT});
        
        var menuQuery = `
                SELECT menu_url, count(*) as cnt,
                (
                    select count(u2.usereventid) from ${DBSchema}.userevent 
                    u2 where u2.accountid= :account and eventid = :event 
                    and u2."timestamp" > now() at time zone 'utc' - interval '24 hour'
                ) as total
                from ${DBSchema}.userevent u 
                where u.accountid = :account and eventid = :event
                and u."timestamp" > now() at time zone 'utc' - interval '24 hour'
                group by menu_url order by cnt desc
            `;
        if (!partailMenuItemMatch) {
            menuQuery = `
                SELECT target_element, count(*) as cnt,
                (
                    select count(u2.usereventid) from ${DBSchema}.userevent 
                    u2 where u2.accountid= :account and eventid = :event 
                    and u2."timestamp" > now() at time zone 'utc' - interval '24 hour'
                ) as total
                from ${DBSchema}.userevent u 
                where u.accountid = :account and eventid = :event
                and u."timestamp" > now() at time zone 'utc' - interval '24 hour'
                group by target_element order by cnt desc
            `;
        }

        const  menuClicks   = db.sequelize.query(menuQuery,{ replacements: { account: id,  event: 'MENU'},type: QueryTypes.SELECT});

        const  elementTargetEvents   = db.sequelize.query(`
                SELECT eventid,target_element,count(*) as cnt,
                (select count(*) from ${DBSchema}.userevent u2 where  
                u2.accountid =:account and u2."timestamp" > now() at time zone 'utc' - interval '24 hour' 
                and u2.eventid=ue.eventid) as totalCount
                FROM ${DBSchema}.userevent ue
                where accountid =:account and "timestamp" > now() at time zone 'utc' - interval '24 hour'
                and eventid in (:events)
                group by eventid,target_element
        `,{ replacements: { account: id,  events: [...targetElementBasedEvents]},type: QueryTypes.SELECT});

        const result = await Promise.all([accountPagesUrl, menuClicks, elementTargetEvents]);
    
        const accountPageResult = result[0];
        const menuClickResult = result[1];
        const elementTargetEventResult = result[2];

        const targetElementBasedResults = {};
        
        elementTargetEventResult.forEach(ele => {
            if (!targetElementBasedResults[ele.eventid]) {
                targetElementBasedResults[ele.eventid] = [];
            }
            targetElementBasedResults[ele.eventid].push({element: ele.target_element, value:Number(((ele.cnt*100)/ele.totalcount).toFixed(2)), count: ele.cnt, original: ele});
        });

        accountPageResult.forEach(page => {
            pageUrls[page['type']]=getUrl(page['url']);
        });

        let homePageSourceCode;

        /**
         * 
         * Generate Heat Map for Home page
         */
        if (pageUrls && pageUrls['HOME']) {
            let homepageResult   = await db.sequelize.query(`
                select pageurl , page_source from ${DBSchema}.pageload_data pd 
                where accountid =:account and pd."timestamp" > now() at time zone 'utc' - interval '24 hour'
                and pageurl = :pageUrl OR pageurl = CONCAT(:pageUrl,'/')
                OR pageurl = CONCAT(:pageUrl,'/#') OR 
                pageurl = CONCAT(:pageUrl,'#') order by "timestamp" DESC LIMIT 1
            `,{ replacements: { account: id, pageUrl: pageUrls['HOME'] },type: QueryTypes.SELECT}); 
            if (homepageResult && homepageResult.length > 0) {
                homePageSourceCode = homepageResult[0].page_source;
            }
        }
        
        let pageWiseSourceCode = await db.sequelize.query(`
                select "type",
                (
                    select page_source from ${DBSchema}.pageload_data pd 
                                    where accountid =:account and pd."timestamp" > now() at time zone 'utc' - interval '24 hour'
                                    and pageurl like CONCAT(awu.url ,'%') order by "timestamp" DESC, LENGTH(replace(CASE WHEN POSITION('?' IN pageurl) > 0 THEN SUBSTRING(pageurl, 0, POSITION('?'IN pageurl)-1) ELSE pageurl end,awu.url, '')) asc LIMIT 1
                ) as sourceCode
                from ${DBSchema}.account_webpage_url awu
                where accountid = :account and "type" != :homeType
        `,{ replacements: { account: id, homeType: 'HOME' },type: QueryTypes.SELECT});
        
        const sourceCodesByType = {};

        pageWiseSourceCode.forEach(e => {
            sourceCodesByType[e.type] = e.sourcecode;
        });
        sourceCodesByType['HOME']=homePageSourceCode;
        const urlData = {};
        menuClickResult.forEach(menu => {
            if (partailMenuItemMatch) {
                const menuOb = processURL(menu['menu_url']);
                if (urlData[menuOb['fullURL']]) 
                {
                    urlData[menuOb['fullURL']]['count'] = urlData[menuOb['fullURL']]['count'] + Number(menu['cnt']);
                } else {
                    urlData[menuOb['fullURL']]={
                        ...menuOb,
                        count: Number(menu['cnt']),
                        total: Number(menu['total']),
                    }
                }
            } else {
                urlData[menu['target_element']]={
                    element: menu['target_element'],
                    count: Number(menu['cnt']),
                    total: Number(menu['total']),
                }
            }
        });
       
        const dataArray = Object.values(urlData);
        dataArray.forEach(ele => {
            ele['percentage'] = Number(((ele.count*100)/ele.total).toFixed(2))
        });
        
        const homePageElementSource = dataArray.map(e => {
            if(partailMenuItemMatch) {
                return {element: "a[href='"+e.path+"']", value:e.percentage, count: e.count}
            }
            return {element: e.element, value:e.percentage, count: e.count}
        });

        // await db.sequelize.query(`
        //     DELETE FROM ${DBSchema}.account_heatmap hm
        //     where accountid =:account`,{ replacements: { account: id},type: QueryTypes.DELETE});

        // const folderDeleteREs = await s3FileUtils.deleteFolder(`${id}/`);

        const heatMapGenerationProgress = [];
        var browser;
        try {
            browser = await puppeteer.launch({
                args: ['--no-sandbox ', '--disable-setuid-sandbox','--disable-dev-shm-usage','--disable-web-security', '--disable-features=IsolateOrigins', ' --disable-site-isolation-trials'],
                headless: true
            });
        } catch(err) {
            console.log(`Chrome initialization error. (Terminating Process)`, err);
            process.exit(1);
        }
        // var browser = await puppeteer.connect({
        //     browserURL: browserRunningURL,
        //     args: ['--no-sandbox ', '--disable-setuid-sandbox','--disable-dev-shm-usage','--disable-web-security', '--disable-features=IsolateOrigins', ' --disable-site-isolation-trials'],
        //     headless: true
        // });
        for(let page of accountPageResult) {
            if (page.events && sourceCodesByType[page.type]) {
                let dataElements = [];
                page.events.forEach(ev => {
                    if (ev == 'MENU' && homePageElementSource) {
                        Array.prototype.push.apply(dataElements, homePageElementSource)
                    } else if (targetElementBasedResults[ev] && targetElementBasedResults[ev].length > 0) {
                        Array.prototype.push.apply(dataElements, targetElementBasedResults[ev])
                    }
                });
                if (dataElements.length > 0) {
                    heatMapGenerationProgress.push(generateHeatMapImage(browser, sourceCodesByType[page.type],dataElements, page.type, id, page.url));
                }
            }
        }
        const allComplete = await Promise.all(heatMapGenerationProgress);
        const allEnd = +new Date();
        try {
            await browser.close();
        } catch(err) {
            console.log(`Browser Close error. (Terminating Process)`, err);
            process.exit(1);
        }
        console.log(`------------------ End Heatmap Generation for, Account: ${id}, ended At: ${allEnd}, Total Time Taken: ${( (allEnd-allStart)/1000 )} secs. ------------------`);
    } catch(err) {
        console.log(`------------------ Heatmap Generation Faliled for, Account: ${id}, Error:${err}`);
    }
}

const captureScreenShot = async (browser, uuid,js,css, listOfElements, device, type, screenShotType, account) => {
    
    var start = +new Date();
    var buffer;
    try {
        const page = await browser.newPage();
        console.log(`Account:${account}, Type:${screenShotType}, Device: ${type}, Action: Page Crated`);
        await page.setViewport({ width: device.width, height: device.height })
        console.log(`Account:${account}, Type:${screenShotType}, Device: ${type}, Action: View Port Set`);
        var goToStart = +new Date(); 
        await page.goto(getLocalServerFileUrl(getFileName(uuid, ".html")), {waitUntil: "domcontentloaded",timeout: 0});
        var goToEnd = +new Date();
        console.log(`Account:${account}, Type:${screenShotType}, Device: ${type}, Action: Page Loaded, Time Taken: ${ ((goToEnd-goToStart)/1000) } sec.`);
        // await page.addScriptTag({content: js});
        // await page.addStyleTag({content: css});
        // console.log(`Account:${account}, Type:${screenShotType}, Device: ${type}, Action: Script and Styles loaded`);
        await page.evaluate((val)=> {generateHeatMap(val)}, listOfElements)
        console.log(`Account:${account}, Type:${screenShotType}, Device: ${type}, Action: HeatMap Function Called`);
        var screenShotStart = +new Date();
        buffer = await page.screenshot({type: extension, fullPage: true});
        await page.close();
        var screenShotEnd = +new Date();
        console.log(`Account:${account}, Type:${screenShotType}, Device: ${type}, Action: screenshot captured, Time Taken: ${((screenShotEnd-screenShotStart)/1000)}`);
        var endTime = +new Date();
        console.log(`Account:${account}, Type:${screenShotType}, Device: ${type}, Total Time Taken: ${((endTime-start)/1000)}`);    
    } catch(err) {
        console.log(`Browser Chrome Error. (Terminating Process)`, err);
        process.exit(1);
    }
    
    return {
        image: getFileName(uuid, iamgeExtension, true, type),
        awsPath: getImageWritePath(getFileName(uuid, iamgeExtension, true, type),account, screenShotType,true),
        buffer: buffer
    };
}

const replaceUnescapeHTML = (data) => {
    return data.replace(/&amp;/g, "&").replace(/&lt;/g, "<").replace(/&gt;/g, ">").replace(/&quot;/g, '"').replace(/&#39;/g, "'").replace(/&#x2F;/g, "/");
}

const generateHeatMapImage = async (browser, body, listOfElements, screenShotType, account, url) => {
    const html = getFormattedHtml(body);
    const $ = cheerio.load(html);
    const js = getJSFiles();
    const css = getCss();
    const id = account;
    if ($('head')) {
        $('body').append(`<script>${js}</script>`)
        $('head').append(`<style>${css}</style>`)
        if (screenShotType == 'HOME') {
            $('head').append(`${homePageCSS}`)
        }
    }
    var rootHtml = $.html();
    const uuid = uuidv4();

    fs.writeFileSync(getPublicHtmlFileLocation(uuid), rootHtml);
    const result = [];
    const awsResult = [];
    const deskTop = await captureScreenShot(browser, uuid, js,css, listOfElements, DEVICES['HIGHEST'], "desktop", screenShotType, account);
    result.push(deskTop);
    await db.sequelize.query(`
            DELETE FROM ${DBSchema}.account_heatmap hm
            where accountid =:account AND category=:category`,{ replacements: { account: id, category:screenShotType},type: QueryTypes.DELETE});

    const folderDeleteREs = await s3FileUtils.deleteFolder(`${id}/${screenShotType}/`);
    const deskTopImage = await s3FileUtils.uploadFile(result[0].buffer, result[0].awsPath, false);
    await insertRecord(deskTop,account, 'desktop', screenShotType, url, deskTopImage);
    const mobile =  await captureScreenShot(browser, uuid, js,css, listOfElements, DEVICES['ANDROID'], "mobile", screenShotType, account);
    result.push(mobile);
    const mobileImage = await s3FileUtils.uploadFile(result[1].buffer, result[1].awsPath, false)
    await insertRecord(mobile, account,'mobile', screenShotType, url, mobileImage);
    const tablet =  await captureScreenShot(browser, uuid, js,css, listOfElements, DEVICES['IPAD-TAB'], "tablet", screenShotType, account);
    result.push(tablet);
    const tabletImage = await s3FileUtils.uploadFile(result[2].buffer, result[2].awsPath, false)
    await insertRecord(tablet, account,'tablet', screenShotType, url, tabletImage);

    // const result =  ([deskTop, mobile, tablet]);
    // const result =  await Promise.all([deskTop, mobile, tablet]);
    
    // await db.sequelize.query(`
    //         DELETE FROM ${DBSchema}.account_heatmap hm
    //         where accountid =:account AND category=:category`,{ replacements: { account: id, category:screenShotType},type: QueryTypes.DELETE});

    // const folderDeleteREs = await s3FileUtils.deleteFolder(`${id}/${screenShotType}/`);

    // const deskTopImage = s3FileUtils.uploadFile(result[0].buffer, result[0].awsPath, false)
    // const mobileImage = s3FileUtils.uploadFile(result[1].buffer, result[1].awsPath, false)
    // const tabletImage = s3FileUtils.uploadFile(result[2].buffer, result[2].awsPath, false)

    // const awsResult = await Promise.all([deskTopImage, mobileImage, tabletImage]);

    // var insertQuery = [];

    // insertQuery.push(`(${account}, '${screenShotType}', 'desktop', '${url}', '${result[0].image}', '${result[0].image}', timezone('utc'::text, now()), '${result[0].awsPath}', ${awsResult[0].fail}, '${awsResult[0].errorMessage || ''}')`)
    // insertQuery.push(`(${account}, '${screenShotType}', 'mobile', '${url}', '${result[1].image}', '${result[1].image}', timezone('utc'::text, now()), '${result[1].awsPath}', ${awsResult[1].fail}, '${awsResult[1].errorMessage || ''}')`)
    // insertQuery.push(`(${account}, '${screenShotType}', 'tablet', '${url}', '${result[2].image}', '${result[2].image}', timezone('utc'::text, now()), '${result[2].awsPath}', ${awsResult[2].fail}, '${awsResult[2].errorMessage || ''}')`)

    // await db.sequelize.query(`
    //                 INSERT INTO ${DBSchema}.account_heatmap
    //                 (accountid, category, device, pageurl, file, localpath,createdon, awspath, aws_upload_fail, upload_error)
    //                 values
    //                 ${insertQuery.join(",")};
    //                 `,{ type: QueryTypes.INSERT});
    fs.unlinkSync(getPublicHtmlFileLocation(uuid));
    console.log("done!!")
} 

const insertRecord = async function(result,account, type, screenShotType, url, awsResult) {
    var insertQuery = [];
    insertQuery.push(`(${account}, '${screenShotType}', '${type}', '${url}', '${result.image}', '${result.image}', timezone('utc'::text, now()), '${result.awsPath}', ${awsResult.fail}, '${awsResult.errorMessage || ''}')`)

    return await db.sequelize.query(`
                    INSERT INTO ${DBSchema}.account_heatmap
                    (accountid, category, device, pageurl, file, localpath,createdon, awspath, aws_upload_fail, upload_error)
                    values
                    ${insertQuery.join(",")};
                    `,{ type: QueryTypes.INSERT});
}

module.generateHeatMapImage = generateHeatMapImage;

return module;
};
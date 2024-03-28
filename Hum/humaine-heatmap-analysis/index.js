const express = require("express");
const router = express.Router();
const app = express();
const fileUtils = require('./utils/fileutils');
require('dotenv').config();
const db = require("./config/db-config");
const port = process.env.PORT || 3000;
const parse = require('node-html-parser').parse;
var bodyParser = require('body-parser');
const AWS = require('aws-sdk');
const schedule = require('node-schedule');
const { QueryTypes } = require('sequelize');
const actuator = require('express-actuator');

jobIsRunning = false;

/**
 * For Heat Map Related JS and CSS Location
 */
const heatMapFileLocation =  process.env.HEAT_MAP_FILE_LOCATION;
const customJsLocation =  process.env.CUSTOM_JS_LOCATION;
const toolTipJsLocation = process.env.TOOL_TIP_JS_LOCATION;
const customCSSLocation =  process.env.CUSTOM_CSS_LOCATION;

AWS.config.update({
    accessKeyId: process.env.AWS_S3_ACCESS_KEY,
    secretAccessKey: process.env.AWS_S3_SECRET_KEY
});

const s3 = new AWS.S3();
s3FileUtils = require('./controllers/aws-s3')(s3);
const DBSchema = process.env.SCHEMA;

/**
 * Heat Map related all final JS that required to generate Heat Map
 *  */ 
var heatMapFile = "";
var customJsFile = "";
var toolTipJsFile = "";
var customCSS = "";

/**
 * Local server to be run to render all content
 */
app.use(express.static('public'));

// Actuator: For Application health check and monitoring

const actuatorOptions = {
    basePath: '/actuator', // It will set /management/info instead of /info
    infoGitMode: 'simple', // the amount of git information you want to expose, 'simple' or 'full',
    infoBuildOptions: null, // extra information you want to expose in the build object. Requires an object.
    infoDateFormat: null, // by default, git.commit.time will show as is defined in git.properties. If infoDateFormat is defined, moment will format git.commit.time. See https://momentjs.com/docs/#/displaying/format/.
    customEndpoints: [] // array of custom endpoints
};
app.use(actuator(actuatorOptions));

const main = async event => {
    const promise = new Promise(function(resolve, reject) {
        if (!event.account) {
            reject({
                fail: true,
                message: "account Id required"
            });
            return;
        }
        const resp = initalizeApp(event.account);
        resolve(resp);
    });
    return promise
};

// const cronExpression = process.env.CRON_EXPRESSION || '*/1 * * * *';

// const job = schedule.scheduleJob(cronExpression, async function(){
//     if (jobIsRunning == true) {
//         console.log(`Job Already Running, Cancle New Schedule Execution Request::${+new Date()}::${new Date()}`)
//         return;
//     }
    
//     console.log(`Heat Map Generation Scheduler Start => ${new Date()}`)

//     jobIsRunning = true;
//     const  accountIds   = await db.sequelize.query(`
//     select distinct accountid from ${DBSchema}.userevent u  
//     where u."timestamp" > now() at time zone 'utc' - interval '24 hour'
//     `,{type: QueryTypes.SELECT});

//     const heatMapRequests = [];
//     accountIds.forEach(element => {
//         heatMapRequests.push(heatMap.generateHeatMapImages(element.accountid));
//     });
//     const result = await Promise.all(heatMapRequests);
//     jobIsRunning = false;
//     console.log(`Heat Map Generation Scheduler End => ${new Date()}`)
// });

app.listen(port, async function() {
    heatMapFile =  await fileUtils.getData(heatMapFileLocation, 'utf8');
    customJsFile =  await fileUtils.getData(customJsLocation, 'utf8');
    toolTipJsFile = await fileUtils.getData(toolTipJsLocation, 'utf8');
    customCSS = await fileUtils.getData(customCSSLocation, 'utf8');
    heatMap = require('./controllers/heatmap')(port,heatMapFile,customJsFile,toolTipJsFile,customCSS, db, s3);

    /**
     * Check S3 Initialization
     */
    try {
        const result = await s3FileUtils.listObjects("/");
        if (result.fail == true) {
            console.log("AWS S3 Initializatoin Failed,(Terminating Process) \n", result.error);
            process.exit(1);    
        } else {
            console.log("AWS S3 inialized Successfully.");
        }
    } catch(err) {
        console.log("AWS S3 Initializatoin Failed,(Terminating Process) \n", err);
        process.exit(1);
    }

    /**
     * Heat Map Process Start code
     */
    console.log(`Heat Map Generation Scheduler Start => ${new Date()}`)
    jobIsRunning = true;
    const  accountIds   = await db.sequelize.query(`
    select distinct accountid from ${DBSchema}.userevent u  
    where u."timestamp" > now() at time zone 'utc' - interval '24 hour'
    `,{type: QueryTypes.SELECT});
    const heatMapRequests = [];
    accountIds.forEach(element => {
        heatMapRequests.push(heatMap.generateHeatMapImages(element.accountid));
    });
    const result = await Promise.all(heatMapRequests);
    console.log(`Heat Map Generation Scheduler End => ${new Date()}`)
    process.exit(0);
});

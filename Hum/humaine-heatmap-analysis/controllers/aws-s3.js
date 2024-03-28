const fs = require('fs');
const { resolve } = require('path');

module.exports = function(s3) {
var module = {};

const BUCKET_NAME = process.env.BUCKET_NAME;

const uploadFile = async (file, fileName, fromFilePath = true) => {
    // Read content from the file
    var fileContent;
    if (fromFilePath == true) {
        fileContent = fs.readFileSync(file);
    } else {
        fileContent = file;
    }

    // Setting up S3 upload parameters
    const params = {
        Bucket: BUCKET_NAME,
        Key: fileName,
        Body: fileContent
    };
    return new Promise((rsolve, reject) => {
        s3.upload(params, function(err, data) {
            if (err) {
                console.log(`File Upload Failed., Reason:: ${err.message}`);
                rsolve({
                    fail: true,
                    error: err,
                    errorMessage: err.message,
                    data: null
                });
                return;
            }
            console.log(`File uploaded successfully. ${data.Location}`);
            rsolve({
                fail: false,
                error: null,
                errorMessage: null,
                data: data
            });
        });
    })
};

const deleteObject = async (path) => {
    const params = {
        Bucket: BUCKET_NAME,
        Key: path
    };
    return new Promise((rsolve, reject) => {
        s3.deleteObject(params, function(err, data) {
            if (err) {
                console.log(`Delete Object Failed., Reason:: ${err.message}`);
                rsolve({
                    fail: true,
                    error: err,
                    errorMessage: err.message,
                    data: null
                });
                return;
            }
            console.log(`Delete Object successfully. ${data}`);
            rsolve({
                fail: false,
                error: null,
                errorMessage: null,
                data: data
            });
        });
    })
};

function deleteFolder(path){
    var params = {
      Bucket: BUCKET_NAME,
      Prefix: path
    };
  
    return new Promise((resolve, reject) => {
        s3.listObjects(params, function(err, data) {
            if (err) {
                resolve({
                    fail: true,
                    error: err
                });
                return;
            };
            if (data.Contents.length == 0) {
                return resolve({
                    fail: false
                });
            } 

            params = {Bucket: BUCKET_NAME};
            params.Delete = {Objects:[]};
            
            data.Contents.forEach(function(content) {
              params.Delete.Objects.push({Key: content.Key});
            });
        
            s3.deleteObjects(params, function(errObj, dataObj) {
              if (errObj) {
                resolve({
                    fail: true,
                    error: errObj
                });
                return;
              }
              if (dataObj.IsTruncated) {
                deleteFolder(path);
              } else {
                resolve({
                    fail: false
                });
                return;
              }
            });
        });
    })
}

const listObjects = async (path) => {
    var params = {
      Bucket: BUCKET_NAME,
      Prefix: path
    };
  
    return new Promise((resolve, reject) => {
        s3.listObjects(params, function(err, data) {
            if (err) {
                resolve({
                    fail: true,
                    error: err
                });
                return;
            };
            if (data.Contents.length == 0) {
                return resolve({
                    fail: false
                });
            }
        });
    })
}

module.uploadFile = uploadFile;
module.deleteObject = deleteObject;
module.deleteFolder = deleteFolder;
module.listObjects = listObjects;
return module;
}
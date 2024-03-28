const fs = require('fs');

module.exports.getData = (fileName, type) =>
new Promise((resolve, reject) =>
  fs.readFile(fileName, type, (err, data) => {
    return err ? reject(err) : resolve(data);
  })
);
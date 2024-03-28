const Sequelize = require('sequelize');
const sequelize = new Sequelize(process.env.DB, process.env.DB_USER, process.env.DB_PASSWORD, {
  host: process.env.HOST,
  dialect: process.env.DIALECT,
  pool: {
    max: parseInt(process.env.POOL_MAX, 10),
    min: parseInt(process.env.POOL_MIN, 10),
    acquire: parseInt(process.env.POOL_ACQUIRE, 10),
    idle: parseInt(process.env.POOL_IDLE, 10)
  }
});
sequelize.sync({force: true}).then(function(e){
  console.log('DB connection sucessfull');
}, function(err){
  console.error('DB connection Error. (Terminating Process)',err);
  process.exit(1);
});

const db = {};

db.Sequelize = Sequelize;
db.sequelize = sequelize;
module.exports = db;
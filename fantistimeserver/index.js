const https = require('https');
const fs = require('fs');
const logger = require('bunyan');
const express = require('express');
const url = require('url');
const bodyParser = require('body-parser');
const dataManager = require('./DataManager.js');
require('./config.json');

var app = express();

app.use(bodyParser.json());

// Read the configuration file
const config = fs.readFileSync('./config.json') // The configruations file
var con = JSON.parse(config); // The configurations

// Make a list with all the keys
var keyHashes = [];
var i;
for (i = 0; i < con.clients.length; i++) {
   keyHashes.push(con.clients[i].keyHash);
}
delete i;

// Configure HTTPS
const httpsOptions = {
   key: fs.readFileSync(con.connection.key),
   cert: fs.readFileSync(con.connection.cert),
   method: 'POST'
};

var log = new logger({"name": "FantisTimeServer"});

/*
 * The main part of the server.
 */
var dm = new dataManager(log, con, keyHashes);
var reserTimer = setInterval(function(){dm.cm.resetMinute();}, 60 * 1000); // Tell a minute is over every minute.

app.post('/connect/', function(req, res) {
   res.statusCode = 200;
   res.setHeader('Content-Type', 'application/json');
   try {
      dm.connectClient(req.body.key, res);
   } catch (err) {
      log.warn(err.message);
   }
});

app.post('/report/', function(req, res) {
   res.statusCode = 200;
   res.setHeader('Content-Type', 'application/json');
   if (req.body.active)
      dm.reportTime(req.body.key, res);
   res.end();
});

// Start the server
https.createServer(httpsOptions, app).listen(con.connection.port);
log.info('Started the server');

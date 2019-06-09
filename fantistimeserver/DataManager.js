const hash = require('js-sha512');
const clientManager = require('./ClientManager.js');

const internal = {};

class UnregisteredKeyError extends Error {
   constructor(keyHash) {
      this.name = "UnregisteredKeyError";
      super("Got unregistered key from a client: " + keyHash);
   }
}
class DuplicatedKeyError extends Error {
   constructor(keyHash) {
      super("Got duplicated key from a client: " + keyHash);
      this.name = "DuplicatedKeyError";
   }
}

function checkClient(keyHash, res, keyHashes, clientManager) {
   var registered = false; // Is the key registered in the configurations
   keyHashes.forEach(function(e) {
      if (e == keyHash)
         registered = true;
   });

   // In case the key is not valid
   if (!registered) {
      res.status(401).end(); // Unauthorized client
      throw new UnregisteredKeyError(key, keyHash);
   }
   
   // In case the key is already used by another client
   if (clientManager.checkUsed(keyHash)) {
      res.status(409).end(); // Conflict message
      throw new DuplicatedKeyError(keyHash);
   }
}

function getClientConf(keyHash, config) {
   return config.clients.filter(function(val) {
      return val.keyHash == keyHash;
   })[0];
}

module.exports = internal.DataManager = class {
   constructor (log, config, keyHashes) {
      this.log = log;
      this.con = config;
      this.keyHashes = keyHashes;
      this.cm = new clientManager(this.con.time);
   }
   
   connectClient(key, res) {
      var keyHash = hash.sha512(key); // Calclulate the hash of the key
      checkClient(keyHash, res, this.keyHashes, this.cm); // Check if the client key is valid
      var clientJSON = getClientConf(keyHash, this.con); // Find the matching client configurations
      var clientConf = {
         "timeInterval": clientJSON.timeInterval,
         "period": clientJSON.period,
         "sensitivity": clientJSON.sensitivity
      }; // Generate the configurations for the client
      res.end(JSON.stringify(clientConf)); // Send the configurations to the client
      this.log.info("Connected client with the original key of: " + key + ".");
   }
   
   reportTime(key, res) {
      var keyHash = hash.sha512(key); // Calclulate the hash of the key
      checkClient(keyHash, res, this.keyHashes, this.cm); // Check if the client key is valid
      this.cm.report();
      var resJSON = {
         "timeLeft": this.cm.timeLeft()
      };
     console.log(resJSON);
      res.end(JSON.stringify(resJSON));
   }
}


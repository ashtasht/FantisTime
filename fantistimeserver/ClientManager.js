const internal = {};

module.exports = internal.ClientManager = class {
   constructor (time) {
      this.time = time;
      this.clients = [];
      this.minUsed = false;
      this.min = 0;
   }
   
   resetMinute() {
      if (this.minUsed)
         this.min += 1;
      this.minUsed = false;
   }
   
   report() {
      this.minUsed = true;
   }

   addClient(keyHash) {
      this.clients.push({
         "keyHash": keyHash
      });
   }

   checkUsed(keyHash) {
      var i;
      for (i = 0; i < this.clients.length; i++)
         if (keyHash == this.clients[i].keyHash)
            return true;
      return false;
   }
   
   getClient(id) {
      return this.clients[id];
   }
   
   timeLeft() {
      return this.time - this.min;
   }
}

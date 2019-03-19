package com.amitg.fanstistimeclient;

import java.io.*;
import java.net.*;

/**
 * The NetworkReporter class is a class used to send reports from the client to the server.
 *
 * @author Amit Goren
 * @version 0.1
 * @since 2019-03-19
 */
public class NetworkReporter {
   private String ip;
   private short port;
   private short checkTime;
   private BufferedWriter sWrite;
   private BufferedReader sRead;

   /**
    * Create and initialize a NetworkReporter.
    *
    * @param ip The IP of the server (eg: 192.168.0.1).
    * @param port The port the server is listening on.
    * @param checkTime The used amount of check time.
    */
   NetworkReporter(String ip, short port, short checkTime) {
      this.ip = ip;
      this.port = port;
      this.checkTime = checkTime;
   }

   void report() {
      try {
         Socket s = new Socket(ip, port);
         PrintWriter pr = new PrintWriter(s.getOutputStream());
         pr.println("p" + checkTime);
         pr.flush();
         s.close();
         pr.close();
      } catch (IOException e) {
         System.err.println("Error while reporting to server: " + e.getMessage());
         System.exit(-1);
      }
   }
}

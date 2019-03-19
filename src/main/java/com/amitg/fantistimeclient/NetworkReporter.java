package com.amitg.fanstistimeclient;

import java.io.*;
import java.net.Socket;

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
   private Socket socket;
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

      try {
         socket = new Socket(ip, port);
         sRead = new BufferedReader(new InputStreamReader(socket.getInputStream()));
         sWrite = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
         System.out.println("HI!");
      } catch (Exception e) {
         System.err.println("Error while creating socket and its components: " + e.getMessage());
         System.exit(-1);
      }
   }

   void report() {
      try {
         sWrite.write("p" + checkTime);
      } catch (IOException e) {
         System.err.println("Error while reporting to server: " + e.getMessage());
         System.exit(-1);
      }
   }

   /**
    * Close the used socket. Call this when the there's no more usage to the NetworkReporter.
    */
   void close() {
      socket.close();
   }
}

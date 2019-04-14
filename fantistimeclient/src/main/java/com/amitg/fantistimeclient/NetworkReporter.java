package com.amitg.fantistimeclient;

import java.io.*;
import javax.net.ssl.HttpsURLConnection;

/**
 * The NetworkReporter class is a class used to send reports from the client to the server.
 *
 * @author Amit Goren
 */
public class NetworkReporter {
   private HttpsURLConnection con;

   /**
    * Create and initialize a NetworkReporter.
    *
    * @param con The https URL to connect to
    */
   NetworkReporter(HttpsURLConnection con) {
      this.con = con;
   }

   /**
    * Report the server the status of the computer.
    *
    * @param active Is the computer active and not idle
    */
   void report(boolean active) {
         System.out.println("HI!");
      short x = 0;
      if (active) x = 1;
      try {
         PrintWriter pr = new PrintWriter(con.getOutputStream());
         pr.print("a" + x);
         pr.flush();
         pr.close();
      } catch (IOException e) {
         System.err.println("Error while reporting to server: " + e.getMessage());
         System.exit(-1);
      }
   }
}

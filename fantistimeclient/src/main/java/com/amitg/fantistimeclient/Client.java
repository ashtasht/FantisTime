package com.amitg.fantistimeclient;

import java.net.*;
import javax.net.ssl.*;

public class Client {
   public static void main (String[] args) {
      HttpsURLConnection con; // The main HTTPS connection.

      try {
         ConnectionConfig cc = new ConnectionConfig(); // Load the local configurations file used to connect to the server
         URL url = new URL(cc.cv.url); // Initizlize a URL from the configurations file
         con = (HttpsURLConnection) url.openConnection(); // Start the connection with the server
         // Disable certification validation
         if (!cc.cv.cert)
            new ByPassSSL().callAPI(con);
         // Enable writing
         con.setDoOutput(true);
         con.setDoInput(true);

         final NetworkReporter np = new NetworkReporter(con);

         Config c = new Config(con, cc.cv.key);
         c.request(); // Request the configurations from the server

         IdleChecker idleChecker = new IdleChecker(c.cv.timeInterval, c.cv.period, c.cv.sensitivity, new IdleChecker.reportFunction() {
               public void report(boolean isIdle) {
                  np.report(isIdle);
               }
            });
         idleChecker.start(); // Start checking (in a new thread)

         Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
               idleChecker.close();
               con.disconnect();
            }
         }, "Shutdown-thread"));
      } catch (java.net.ConnectException e) {
         System.err.println("Error, can't connect to the server: " + e.getMessage());
      } catch (Exception e) {
         System.err.println("Error: " + e.getMessage());
         e.printStackTrace();
         System.exit(-1);
         return;
      }
   }
}


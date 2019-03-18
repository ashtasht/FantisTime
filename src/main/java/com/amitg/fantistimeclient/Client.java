package com.amitg.fanstistimeclient;


public class Client {
   static short port = 3141;
   static String ip = "127.0.0.1";
   static short checkTimeInterval = 3;
   static short checksPerReport = 8;
   static float idleSensitivity = 0.8f;

   public static void main (String[] args) {
      try {
         Config c = new Config();
      } catch (Exception e) {
         System.err.println("Error: " + e.getMessage());
         System.exit(-1);
      }
      final NetworkReporter np = new NetworkReporter(ip, port, (short)(checkTimeInterval * checksPerReport));
      IdleChecker idleChecker = new IdleChecker(checkTimeInterval, checksPerReport, idleSensitivity, new IdleChecker.reportFunction() {
         public void report() {
            np.report();
         }
      });
      idleChecker.start();
   }
}


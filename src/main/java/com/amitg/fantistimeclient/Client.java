package com.amitg.fanstistimeclient;


public class Client {
   public static void main (String[] args) {
      Config c;
      try {
         c = new Config();
         final NetworkReporter np = new NetworkReporter(c.getValues().serverIP, c.getValues().serverPort, (short)(c.getValues().checkInterval * c.getValues().checksPerReport));
         IdleChecker idleChecker = new IdleChecker(c.getValues().checkInterval, c.getValues().checksPerReport, c.getValues().idleSensitivity, new IdleChecker.reportFunction() {
               public void report() {
                  np.report();
               }
            });
         idleChecker.start();
      } catch (Exception e) {
         System.err.println("Error while parsing/reading file: " + e.getMessage());
         e.printStackTrace();
         System.exit(-1);
         return;
      }
   }
}


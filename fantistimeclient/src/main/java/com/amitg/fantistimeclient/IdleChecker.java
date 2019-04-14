package com.amitg.fantistimeclient;

import java.awt.MouseInfo;
import java.awt.Point;
import java.util.concurrent.TimeUnit;

/**
 * A class used to check and report if the computer is idle or not.
 *
 * @author Amit Goren
 */
public class IdleChecker extends Thread {
   private short timeInterval; // The amount of seconds between checks of the mouse location
   private short period; // The amount of checks to be down before reporting to the server
   private float sensitivity; // The maximum ratio of mouse movement that still considers idle
   private reportFunction reportFunc; // The function to be called every period to report the server the idle status

   /**
    * @param timeInterval The amount of seconds between checks of the mouse location
    * @param period The amount of checks to be down before reporting to the server
    * @param sensitivity The maximum ratio of mouse movement that still considers idle
    * @param reportFunc The function to call every period to report the server the idle status
    */
   IdleChecker(short timeInterval, short period, float sensitivity, reportFunction reportFunc) {
      this.timeInterval = timeInterval;
      this.period = period;
      this.sensitivity = sensitivity;
      this.reportFunc = reportFunc;
   }

   private boolean shouldClose = false;
   /**
    * Stop the checking loop.
    */
   public void close() {
       shouldClose = true;
   }

   /**
    * Start the thread (this function shouldn't be called directly, it should be called through a thread)/
    * 
    */
   public void run() {
      while (!shouldClose) {
         short mouseMovements = 0; // Number of mouse movements in this period
         for (short p = 0; p < period; p++) {
            Point lastMouseLoc = MouseInfo.getPointerInfo().getLocation(); // The mouse position before the sleep
            try {
               TimeUnit.SECONDS.sleep(timeInterval);
            } catch (InterruptedException e) {
               System.err.println("Error: thread interrupted: " + e.getMessage());
            }
            // Count a mouse movement if the current mouse location is different than the mouse location before the sleep
            if (!lastMouseLoc.equals(MouseInfo.getPointerInfo().getLocation())) {
               mouseMovements++;
            }
         }
         float rt = 1-((float)mouseMovements/(float)period); // How "idly" is the current period
         reportFunc.report(rt < sensitivity); // Report the server
      }
   }

   public interface reportFunction {
      void report(boolean isIdle);
   }
}

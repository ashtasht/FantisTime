package com.amitg.fanstistimeclient;

import java.awt.MouseInfo;
import java.awt.Point;
import java.util.concurrent.TimeUnit;

/**
 * A class used to check if the computer is idle.
 */
public class IdleChecker extends Thread {
   private short timeInterval;
   private short period;
   private float sensitivity;
   private reportFunction reportFunc;
   
   public interface reportFunction {
      void report();
   }

   /**
    * @param timeInterval The amount of seconds between checks of mouse movement.
    * @param period The amount of checks to be down before reporting.
    * @param sensitivity The maximum precentage of mouse movement that still considers idle.
    * @param reportFunc The function to call every period to tell the idle status.
    */
   IdleChecker(short timeInterval, short period, float sensitivity, reportFunction reportFunc) {
      if (timeInterval < 0) {
         System.err.println("Error: the time interval is out of range [0,infinity).");
         System.exit(-1);
      }
      this.timeInterval = timeInterval;
      this.period = period;
      if (sensitivity > 1 || sensitivity < 0) {
         System.err.println("Error: the sensitivity is out range [0,1].");
         System.exit(-1);
      }
      this.sensitivity = sensitivity;
      this.reportFunc = reportFunc;
   }

   private boolean stop = false;
   public void stopChecking() {
      stop = true;
   }

   public void run() {
      while (!stop) {
         short mouseMovements = 0; // Number of mouse movements
         for (short p = 0; p < period; p++) {
            Point lastMouseLoc = MouseInfo.getPointerInfo().getLocation();
            try {
               TimeUnit.SECONDS.sleep(timeInterval);
            } catch (InterruptedException e) {
               System.err.println(e.getMessage());
            }
            if (!lastMouseLoc.equals(MouseInfo.getPointerInfo().getLocation())) {
               mouseMovements++;
            }
         }
         float pr = 1-((float)mouseMovements/(float)period);
         if (pr < sensitivity)
            reportFunc.report();
      }
   }
}

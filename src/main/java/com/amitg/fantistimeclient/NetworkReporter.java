package com.amitg.fanstistimeclient;

import java.io.*;
import java.net.Socket;

public class NetworkReporter {
   private String ip;
   private short port;
   private short checkTime;
   private Socket socket;
   private BufferedWriter sWrite;
   private BufferedReader sRead;

   NetworkReporter(String ip, short port, short checkTime) {
      this.ip = ip;
      this.port = port;
      this.checkTime = checkTime;

      try {
         socket = new Socket(ip, port);
         sRead = new BufferedReader(new InputStreamReader(socket.getInputStream()));
         sWrite = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
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
}

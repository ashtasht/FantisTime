package com.amitg.fantistimeclient;

import java.io.*;
import javax.net.ssl.HttpsURLConnection;
import org.apache.commons.io.IOUtils;
import com.owlike.genson.*;

/**
 * A class used to request and parse the configurations file from the server.
 *
 * @author Amit Goren
 */
class Config {
   private HttpsURLConnection con; // The connection with the server
   private String key; // The key used to identification with the server
   public ConfigValues cv;

   Config(HttpsURLConnection con, String key) {
      this.con = con;
      this.key = key;
   }

   /**
    * Request the configurations file from the server (starting the connection proccess).
    */
   public void request() throws IOException {
      // Send key
      PrintWriter pw = new PrintWriter(con.getOutputStream());
      pw.print("k" + key); // Report the server about adding more time
      pw.flush();
      pw.close();
      
      // parse configurations file
      String json = IOUtils.toString(
            new BufferedReader(
               new InputStreamReader(
                     con.getInputStream()
                  )
               )
            );
      try {
         cv = new Genson().deserialize(json, ConfigValues.class);
      } catch (com.owlike.genson.JsonBindingException e) {
         System.err.println("Error: got invalid json from the server.");
         System.exit(-1);
      }
   }
}

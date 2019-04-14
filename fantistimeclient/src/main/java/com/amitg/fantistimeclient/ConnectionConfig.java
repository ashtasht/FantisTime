package com.amitg.fantistimeclient;

import java.io.*;
import java.util.Scanner; 
import com.owlike.genson.*;

class ConnectionConfig {
   private static String fileName = "fantistime.json"; // Default file name

   private String location;
   private File f;
   public ConnectionValues cv;

   private Scanner scan;   

   ConnectionConfig(String location) throws FileNotFoundException, IOException {
      scan = new Scanner(System.in);

      // Set to this file location
      this.location = location;

      // If the file doesn't exist, exit
      f = new File(location);
      if (!f.exists()) {
         System.err.println("Error: there is no such a file " + location + ".");
         System.exit(-1);
      }
      if (f.isDirectory()) {
         System.err.println("Error: the config file location (" + location + ") contains a directory.");
         System.exit(-1);
      }
      // Read and parse the file
      parse(f);

   }
   ConnectionConfig() throws java.io.FileNotFoundException, java.io.IOException {
      scan = new Scanner(System.in);

      // Set to default file location
      String slashType;
      if (System.getProperty("os.name").contains("Windows"))
         slashType = "\\.";
      else
         slashType = "/.config/";
      this.location = System.getProperty("user.home") + slashType + fileName;
      System.out.println("Config file location is not given, using " + this.location + " instead.");
      // If this file doesn't exist, ask the user to create a new default file location
      f = new File(location);
      if (f.isDirectory()) {
         System.err.println("Error while parsing the config file: " + location + " is a directory.");
         System.exit(-1);
      }
      if(!f.exists()) {
         System.out.println("The default file location (" + location + ") does not contain any file, do you want to create a new configurations file? (y/n)");
         boolean answer = scan.next().toLowerCase().startsWith("y");
         if (!answer)
            System.exit(0);
         System.out.println("Generating new a config file in " + location + "...");
         createConfig();
      }
      // Read and parse the file
      parse(f);
   }

   private void parse(File file) throws FileNotFoundException, IOException {
      FileInputStream inputStream = new FileInputStream(file);
      byte[] data = new byte[(int) file.length()];
      inputStream.read(data);
      inputStream.close();
      String text = new String(data, "UTF-8");
      cv = new Genson().deserialize(text, ConnectionValues.class);
   }

   private void createConfig() throws java.io.FileNotFoundException, java.io.IOException {
      FileOutputStream fos = new FileOutputStream(f);
      ConnectionValues vals = new ConnectionValues();
      System.out.print("Enter server URL (eg: https://127.0.0.1:3141/, myfantisserver.com): " + scan.nextLine());
      vals.url = scan.nextLine();
      scan.nextLine();
      if (!vals.url.substring(0, 8).equals("https://")) { // Add https:// at the beginning in case the user didn't wrote that.
         vals.url = "https://" + vals.url;
         System.out.println("There was no \"https://\" at the beginning of the given string, changing the URL to: " + vals.url);
      }
      System.out.print("Enter the key (it should much the key you created for this computer in the server configurations file): ");
      vals.key = scan.nextLine();
      scan.nextLine();
      System.out.print("Do you have valid certificate value on the server? (y/n): ");
      vals.cert = scan.next().toLowerCase().startsWith("y");
      Genson genson = new GensonBuilder()
         .useIndentation(true)
         .create();
      fos.write(genson.serializeBytes(vals));
      fos.close();
      System.out.println("Saved the configurations file.");
   }
}

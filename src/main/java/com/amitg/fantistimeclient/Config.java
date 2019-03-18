package com.amitg.fanstistimeclient;

import java.io.*;
import java.util.Scanner; 
import com.owlike.genson.*;

class Config {
   private static String fileName = "fantistime.json"; // Default file name

   private String location;
   private File f;
   private ConfigValues cv;

   private Scanner scan;   
   private Genson genson;

   Config(String location) throws FileNotFoundException, IOException {
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
   Config() throws java.io.FileNotFoundException, java.io.IOException {
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
         System.err.println("Error while parsing config file: " + location + " is a directory.");
         System.exit(-1);
      }
      if(!f.exists()) {
         System.out.println("The default file location (" + location + ") does not contains any file, do you want to create a new configurations file? (y/n)");
         boolean answer = scan.next().toLowerCase().startsWith("y");
         if (!answer)
            System.exit(0);
         System.out.println("Generating new config file in " + location + "...");
         createConfig();
      }
      // Read and parse the file
      parse(f);
   }

   public ConfigValues getValues() {
      return cv;
   }

   private void parse(File file) throws FileNotFoundException, IOException {
      FileInputStream inputStream = new FileInputStream(file);
      byte[] data = new byte[(int) file.length()];
      inputStream.read(data);
      inputStream.close();
      String text = new String(data, "UTF-8");
      cv = new ConfigValues();
      cv = new Genson().deserialize(text, ConfigValues.class);
   }

   private void createConfig() throws java.io.FileNotFoundException, java.io.IOException {
      FileOutputStream fos = new FileOutputStream(f);
      ConfigValues vals = new ConfigValues();
      //fos.write("{\n".getBytes());
      System.out.print("Enter server IP (eg: 127.0.0.1): " + scan.nextLine());
      vals.serverIP = scan.nextLine();
      //fos.write(("    \"serverIP\":\"" + scan.nextLine() + "\",\n").getBytes());
      System.out.print("Enter server port (eg: 3141): ");
      //fos.write(("    \"serverPort\":" + scan.nextLine() + ",\n").getBytes());
      vals.serverPort = scan.nextShort();
      System.out.print("Enter check time interval (in seconds, eg: 3): ");
      vals.checkInterval = scan.nextShort();
      //fos.write(("    \"checkInterval\":" + scan.nextLine() + ",\n").getBytes());
      System.out.print("Enter idle sensitivity (from 0 to 1, higher is more sensitive, eg: 0.8): ");
      vals.idleSensitivity = scan.nextFloat();
      //fos.write(("    \"idleSensitivity\":" + scan.nextLine() + ",\n").getBytes());
      System.out.print("Enter checks per report (eg: 8): ");
      vals.checksPerReport = scan.nextShort();
      //fos.write(("    \"checksPerReport\":" + scan.nextLine() + "\n").getBytes());
      //fos.write("}\n".getBytes());
      Genson genson = new GensonBuilder()
         .useIndentation(true)
         .create();
      fos.write(genson.serializeBytes(vals));
      fos.close();
   }

}

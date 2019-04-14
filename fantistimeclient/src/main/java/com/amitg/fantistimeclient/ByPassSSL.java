package com.amitg.fantistimeclient;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class ByPassSSL {
   public void callAPI(HttpsURLConnection connection) {
      try {
         // Create a context that doesn’t check certificates.
         SSLContext sslContext = SSLContext.getInstance("TLS");
         TrustManager[] trustManager = getTrustManager();
         sslContext.init(null, trustManager, new SecureRandom());
         HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
         //set Method Here
         connection.setRequestMethod("PUT");
         // Guard against "bad hostname" errors during handshake.
         connection.setHostnameVerifier(
            new HostnameVerifier() {
               public boolean verify(String host, SSLSession sess) {
                  return true;
               }
            }
         );
      } catch (IOException e) {
         e.printStackTrace();
      } catch (NoSuchAlgorithmException e) {
         e.printStackTrace();
      } catch (KeyManagementException e) {
         e.printStackTrace();
      }
   }
   
   private TrustManager[] getTrustManager() {
      TrustManager[] certs = new TrustManager[] {
         new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
               return null;
            }
            public void checkClientTrusted(X509Certificate[] certs, String t) {}
            public void checkServerTrusted(X509Certificate[] certs, String t) {}
         }
      };
      return certs;
   }
}

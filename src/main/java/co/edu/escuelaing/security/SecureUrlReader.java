package co.edu.escuelaing.security;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

public class SecureUrlReader {

    public static void main(String[] args) throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException, KeyManagementException {
        changeSSLContext();
        // We can now read this URL
        readURL("https://localhost:5000/hello");
        // This one can't be read because the Java default truststore has been
        // changed.
        readURL("https://www.google.com");
    }

    private static void changeSSLContext() throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException, KeyManagementException {
        // Create a file and a password representation
        File trustStoreFile = new File("keystore/myTrustStore");
        char[] trustStorePassword = "654321".toCharArray();
        // Load the trust store, the default type is "pkcs12", the alternative is "jks"
        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        trustStore.load(new FileInputStream(trustStoreFile), trustStorePassword);
        // Get the singleton instance of the TrustManagerFactory
        TrustManagerFactory tmf = TrustManagerFactory
                .getInstance(TrustManagerFactory.getDefaultAlgorithm());

        // Itit the TrustManagerFactory using the truststore object
        tmf.init(trustStore);

        //Set the default global SSLContext so all the connections will use it
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, tmf.getTrustManagers(), null);
        SSLContext.setDefault(sslContext);
    }

    private static void readURL(String s){
        try{
            URL url = new URL(s);
            URLConnection conn = url.openConnection();
            System.out.println("---------------Headers-----------------");
            conn.getHeaderFields().forEach((k,v) -> System.out.println(k + " -> " + v));
            System.out.println("---------------Body-----------------");
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            br.lines().forEach(System.out::println);
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }

}

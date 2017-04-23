/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.dundee.computing.aec.growexample1.Lib;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonValue;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author andy
 */
public class Web {

    static String RequestBody = null;
    static String user = null;
    static String password = null;

    /**
     *
     */
    public Web() {
        /**
         * ************************************************
         * This class expects a config.properties file in the classpath. See
         * this
         * http://stackoverflow.com/questions/2161054/where-to-place-and-how-to-read-configuration-resource-files-in-servlet-based-app
         *
         * However, if you are using netbeans and Maven (the environment used to
         * develop this app then go to the files tab and look for src. Under src
         * find main and create a directory resources. Place the file there.
         *
         * The file should contain the following user=user password=password
         *
         * Contact bram.degraaf@hydrologic.com for the correct username and
         * password
         *
         */

        InputStream inputStream = null;
        try {
            Properties prop = new Properties();
            String propFileName = "config.properties";

            inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

            if (inputStream != null) {
                prop.load(inputStream);
            } else {
                throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
            }


            // get the property value and print it out
            user = prop.getProperty("user");
            password = prop.getProperty("password");

        } catch (Exception e) {
            System.out.println("Exception: " + e);
        } finally {
            try {
                inputStream.close();
            } catch (Exception et) {

            }
        }

    }

    public static JsonValue GetJson(String url, String Body) throws IOException {
        RequestBody = Body;
        JsonValue RespObj=null;
        RespObj = GetJson(url);
        RequestBody = null;
        return RespObj;
    }

    public static JsonValue GetJson(String url) throws IOException {
        URL HN4S = null;
        JsonValue RespObj=null;
        try {
            HN4S = new URL(url);
        } catch (Exception et) {
            System.out.println("Videos URL is broken");
            return null;
        }
        HttpURLConnection hc = null;
        try {
            hc = (HttpURLConnection) HN4S.openConnection();
            String login = user + ":" + password;  // Contact bram.degraaf@hydrologic.com for details of this login

            Base64 b = new Base64();
            String encoded = b.encodeAsString(new String(login).getBytes());

            hc.setRequestProperty("Authorization", "Basic " + encoded);

            hc.setDoInput(true);
            hc.setDoOutput(true);

            hc.setUseCaches(false);
            hc.setRequestMethod("POST");
            //hc.setRequestProperty("Accept-Encoding", "gzip, deflate, sdch");
            hc.setRequestProperty("Content-Type", "application/json");
            //hc.setRequestProperty("Accept", "application/json");
            hc.setRequestProperty("Accept", "application/json,text/html,application/hal+json,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*");

        } catch (Exception et) {
            System.out.println("Can't prepare http URL con");
            return (null);
        }
        BufferedReader br = null;
        try {
            //OutputStreamWriter writer = new OutputStreamWriter(hc.getOutputStream());
            //writer.write(RequestBody);
            OutputStream os = hc.getOutputStream();
            os.write(RequestBody.toString().getBytes("UTF-8"));
            os.close();

        } catch (Exception et) {
            System.out.println("Can't get reader to HN4S stream" + et);
        }
        int rc = -1;
        try {
            rc = hc.getResponseCode();
        } catch (Exception et) {
            System.out.println("Can't get reponse code " + et);
        }
        if (rc == HttpURLConnection.HTTP_SERVER_ERROR) {
            return null;
        }
        if (rc == HttpURLConnection.HTTP_UNAUTHORIZED) {
            return null;
        }
        if ((rc == HttpURLConnection.HTTP_OK) || (rc == HttpURLConnection.HTTP_CREATED)) {
            int Length = hc.getContentLength();
            String Content = hc.getContentType();
            String Encoding = hc.getContentEncoding();

            InputStreamReader in = new InputStreamReader((InputStream) hc.getInputStream());
            BufferedReader buff = new BufferedReader(in);

            StringBuffer response = new StringBuffer();
            String line = null;
            try {
                do {
                    line = buff.readLine();
                    if (line != null) {
                        response.append(line);
                    }
                } while (line != null);
            } catch (Exception et) {
                System.out.println("Can't read from input " + et);
            }

            try {
                int JsonStart=response.indexOf("{"); //Needed in case there are leading characters (in the case of timeseries for instance) that need to be removed in order to parse the json
                if (JsonStart>0)
                   response=response.delete(0,JsonStart);
                //RespObj = JsonValue.readFrom(response.toString());
                RespObj=Json.parse(response.toString());
            } catch (Exception et) {
                System.out.println(response);
                System.out.println("Can't create json object from response"+et);
                return null;
            }

      }
        return RespObj;

    }

}

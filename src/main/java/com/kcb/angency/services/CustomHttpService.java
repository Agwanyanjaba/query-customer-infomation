package com.kcb.angency.services;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import com.google.gson.Gson;

@SuppressWarnings("deprecation")
public class CustomHttpService<T> {
    Class<T> entityClass;
    private static String url;
    static List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
    static List<NameValuePair> nameValuePairsArray = new ArrayList<NameValuePair>();
    static HttpParams httpParams;
    static HttpClient httpclient;
    static HttpPost httppost;
    private static byte[] result;
    private static HttpResponse response;
    // private static final Logger LOGGER =
    // Logger.getLogger(CustomHTTP.class.getName());
    private static int httpconnerror = 1;

    public static String doCustomHTTP(String actionURI, int timeout, String requestxml) {
        url = actionURI;
        httpParams = fixParams(timeout);
        httpclient = new DefaultHttpClient(httpParams);
        httppost = new HttpPost(url);

        String strresponse = null;

        StringEntity stringentity;
        try {
            stringentity = new StringEntity(requestxml, "UTF-8");
            stringentity.setChunked(true);
            httppost.setEntity(stringentity);
            httppost.addHeader("Accept", "text/xml");
            httppost.addHeader("SOAPAction", actionURI);

            // Execute and get the response.
            httpclient = new DefaultHttpClient();
            response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                strresponse = EntityUtils.toString(entity);
            }
        } catch (UnsupportedEncodingException ex) {
            java.util.logging.Logger.getLogger(CustomHttpService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(CustomHttpService.class.getName()).log(Level.SEVERE, null, ex);
        }

        // make it comprehensible to backend
        addParameter("", requestxml);
        // send http post
        sendPost(url);
        // reponse

        // return toMyString();
        return strresponse;
    }

    // @SuppressWarnings("deprecation")
    private static HttpParams fixParams(int timeout) {
        HttpParams h = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(h, timeout);
        return h;
    }// fixParams

    public static void addParameter(String key, String object) {
        nameValuePairs.add(new BasicNameValuePair(key, object));
        if (nameValuePairs != null) {
            // System.out.println("nameValuePairs: "+nameValuePairs.toString());
            // System.out.println("manual nameValuePairs:
            // "+this.manualFormatting(nameValuePairs.toString()));
        }
    }// addParameter

    // HTTP POST request
    public static void sendPost(String url) {

        String manual_nvp = nameValuePairs.toString();

        try {

            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            // add reuqest header
            con.setRequestMethod("POST");
            // con.setRequestProperty("User-Agent", USER_AGENT);//header
            // con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

            // String urlParameters = "Data=[{C02G8416DRJM}]";
            String urlParameters = manual_nvp;

            // Send post request
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();

            int responseCode = con.getResponseCode();
            System.out.println("Post parameters : " + urlParameters);

            StringBuilder strbufresponse;
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

            String inputLine;
            strbufresponse = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                strbufresponse.append(inputLine);
            }

            // print result
            String newStr = strbufresponse.toString();
            System.out.println(newStr);

            result = newStr.getBytes();
            // set httpconnerror to 0
            setHTTPConnectErrorCode(0);

        } catch (Exception ex) {
            System.err.println("Http error IN CustomHTTP Sendpost: " + ex.getMessage());
            // capture connection error
            setHTTPConnectErrorCode(1);
        }

    }// sendPost

    public void connect() {

        try {
            // set Entity
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            // Execute HTTP Post Request
            response = httpclient.execute(httppost);
            ByteArrayOutputStream outstream = new ByteArrayOutputStream();
            response.getEntity().writeTo(outstream);
            result = outstream.toByteArray();
            // set httpconnerror to 0
            setHTTPConnectErrorCode(0);
            // Diagnostics d = Diagnostics.getInstance();
            // d.setOnline(d.getDashboard());
        } catch (ClientProtocolException e) {
            // Diagnostics d = Diagnostics.getInstance();
            // d.setOffline(d.getDashboard());
            // LOGGER.log(Level.SEVERE, "ClientProtocolException IN CustomHTTP connect :
            // {0}", e.getMessage());
            System.err.println("ClientProtocolException IN CustomHTTP connect : " + e.getMessage());
            // capture connection error
            setHTTPConnectErrorCode(1);
        } catch (IOException e) {
            // Diagnostics d = Diagnostics.getInstance();
            // d.setOffline(d.getDashboard());
            // LOGGER.log(Level.SEVERE, "IO error IN CustomHTTP connect : {0}",
            // e.getMessage());
            System.err.println("IO error IN CustomHTTP connect : " + e.getMessage());
            // capture connection error
            setHTTPConnectErrorCode(1);
        }
    }// connect

    /**
     * Return the result in the form of a byte array
     *
     * @return
     */
    public static byte[] getResult() {
        return result;
    }

    /**
     * Return the result in form of a String.
     *
     * @return
     */
    public static String toMyString() {
        return new String(getResult());
    }

    /**
     * Constructs a class from the JSON serialized form of the class.
     *
     * @return T
     */
    public T getNamedClass() {
        Gson gson = new Gson();
        return gson.fromJson(this.toString(), entityClass);
    }// getNamedClass

    /**
     * Help bind a custom Output Stream.
     *
     * @param out
     * @return OutputStream
     */
    public OutputStream bindtoOutPutStream(OutputStream out) {
        try {
            response.getEntity().writeTo(out);
            return out;
        } catch (IOException ex) {
            return null;
        }
    }// bindtoOutPutStream

    private static void setHTTPConnectErrorCode(int err) {
        System.out.println(errDescript(err));
        System.out.println("url is: " + url);
        httpconnerror = err;
    }// setHTTPConnectErrorCode

    public int getHTTPConnError() {
        return httpconnerror;
    }// getHTTPConnError

    private static String errDescript(int errcode) {
        String ret = "Http connection established..";
        if (errcode == 1) {
            ret = "Http connection NOT established..";
        }
        return ret;
    }// errDescript

    /**
     *
     * @param soapAction
     * @param soapEnvBody
     * @return
     */

    public static String callWithdrawalService(String endpoint, String reqXML) {
        String strResponse = "";
        try {

            // String authStr = user + ":" + pass;
            // String authEncoded = Base64.encodeBytes(authStr.getBytes());
            // Create a StringEntity for the SOAP XML.
            StringEntity stringEntity = new StringEntity(reqXML, "UTF-8");
            stringEntity.setChunked(true);

            // Request parameters and other properties.
            HttpPost httpPost = new HttpPost(endpoint);
            httpPost.setEntity(stringEntity);
            // String auth = "kcbbank" + ":" + "Kcb$12#34";
            // String auth = user + ":" + pass;
            String auth = "" + ":" + "";
            byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")));
            String authHeader = "Basic " + new String(encodedAuth);
            httpPost.setHeader(HttpHeaders.AUTHORIZATION, authHeader);
            httpPost.addHeader("Accept", "text/xml;charset=utf-8");
            httpPost.addHeader("Content-Type", "text/xml; charset=utf-8");
            httpPost.setHeader("Accept-Encoding", "gzip,deflate");
            httpPost.addHeader("SOAPAction", endpoint);
            // Execute and get the response.
            HttpClient httpClient = new DefaultHttpClient();
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            // Logger.info("ENC##\t"+entity.getContentLength());
            // Logger.info("RES##\t"+response.getStatusLine().getStatusCode());
            if (entity != null) {
                InputStream inputStream = entity.getContent();
                ByteArrayOutputStream content = new ByteArrayOutputStream();
                int readBytes = 0;
                final byte[] sBuffer = new byte[512];
                while ((readBytes = inputStream.read(sBuffer)) != -1) {
                    content.write(sBuffer, 0, readBytes);
                }

                final String dataString = new String(content.toByteArray());
                // Logger.info("START RESPONSE ##");
                // Logger.info(dataString);
                // Logger.info("END RESPONSE ##");
                strResponse = dataString;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return strResponse;
    }

}

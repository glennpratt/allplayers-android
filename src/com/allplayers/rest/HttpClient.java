package com.allplayers.rest;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.GeneralSecurityException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.json.JSONException;
import org.json.JSONObject;

public class HttpClient {

    public static final String BASE_URL = "https://www.allplayers.com";
    public static final String PATH_PREFIX = "/?q=api/v1/rest";
    private static String session_cookie = ""; // first session cookie
    private static String chocolatechip_cookie = ""; // second cookie

    public JSONObject httpRequest(String verb, String path, QueryParams query, JSONObject data) {
        String urlString = BASE_URL + PATH_PREFIX + "/" + path;
        if (query != null) {
            urlString += "?" + query.toQueryString();
        }
        try {
            URL url = new URL(urlString);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

            TrustManager[] trustAllCerts = getTrustManager();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, trustAllCerts, null);

            connection.setSSLSocketFactory(context.getSocketFactory());
            connection.setHostnameVerifier(new AllowAllHostnameVerifier());

            // General connection setup.
            connection.setDoInput(true);
            connection.setRequestMethod(verb);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            injectAuthToken(connection);

            // Send output JSON data only as needed and allowed.
            if (data != null && verb == "POST" || verb == "PUT") {
                connection.setDoOutput(true);
                connection.setUseCaches(false);

                DataOutputStream printout = new DataOutputStream(connection.getOutputStream());
                printout.writeBytes(data.toString());
                printout.flush();
                printout.close();
            }

            // Get response data - support input on all verbs.
            BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String str;

            String result = "";
            while ((str = input.readLine()) != null) {
                result += str;
            }

            input.close();

            JSONObject ret = new JSONObject(result);
            ret.put("authToken", parseAuthToken(connection));

            return ret;
        }
        catch (IOException ex) {
            // TODO - Convert exceptions to Runtime to avoid catching them or supressing them.
            // In other words, for now they should cause a crash until the UI can handle them.
            throw new RuntimeException("Network Error: " + ex.getMessage(), ex);
        }
        catch (GeneralSecurityException ex) {
            // TODO - Like above, this should be limited to the constructor though - and only when
            // using the TrustManager to avoid SSL Cert verification - so only in testing.
            throw new RuntimeException("General Security Error: " + ex.getMessage(), ex);
        }
        catch (JSONException ex) {
            throw new RuntimeException("Parse Error: " + ex.getMessage(), ex);
        }
    }

    private static JSONObject parseAuthToken(HttpURLConnection connection) {
        JSONObject auth = new JSONObject();
        String headerName=null;
        for (int i=1; (headerName = connection.getHeaderFieldKey(i))!=null; i++) {
            if (headerName.equals("Set-Cookie")) {
                String cookie = connection.getHeaderField(i);
                if (cookie.startsWith("SESS") || cookie.startsWith("CHOCOLATECHIP")) {
                    cookie = cookie.substring(0, cookie.indexOf(";"));
                    String cookieName = cookie.substring(0, cookie.indexOf("="));
                    String cookieValue = cookie.substring(cookie.indexOf("=") + 1, cookie.length());
                    try {
                        auth.put(cookieName, cookieValue);
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }

        return auth;
    }

    /**
     * Override-able method to inject the authentication token to a configured connection.
     * 
     * @param connection
     */
    protected void injectAuthToken(HttpURLConnection connection) {
        connection.setRequestProperty("Cookie", chocolatechip_cookie + ";" + session_cookie);
    }

    public static TrustManager[] getTrustManager() {
        // Create a trust manager that does not validate certificate chains
        return new TrustManager[] { new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                // Do nothing.
            }

            public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                // Do nothing
            }
        }
        };
    }
}

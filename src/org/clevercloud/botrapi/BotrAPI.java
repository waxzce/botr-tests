/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clevercloud.botrapi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author waxzce
 */
public class BotrAPI {

    private String apiKey;
    private String apiSecret;
    private String baseUrl;

    public BotrAPI(String apiKey, String apiSecret, String baseUrl) {
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
        this.baseUrl = baseUrl;
    }

    public BotrAPI(String apiKey, String apiSecret) {
        this(apiKey, apiSecret, "https://api.bitsontherun.com/v1/");
    }

    public String getVideos() {
        Map<String, String> m = new HashMap<String, String>();
        m.put("result_limit", "0");
        return makeRequest("videos/list", m);
    }

    public String getAccountContents() {
        Map<String, String> m = new HashMap<String, String>();
        m.put("include_empty_days", "true");
        return makeRequest("accounts/content/list", m);
    }

    private String makeRequest(String url, Map<String, String> args) {
        try {
            args.put("api_key", this.apiKey);
            args.put("api_format", "json");
            args.put("api_timestamp", String.valueOf(System.currentTimeMillis()).substring(0, 10));
            args.put("api_nonce", getNewNonce());
            ArrayList<String> listkey = new ArrayList<String>(args.keySet());
            Collections.sort(listkey);
            String querystring = "";
            for (String string : listkey) {
                querystring = querystring + string + "=" + URLEncoder.encode(args.get(string)) + "&";
            }
            querystring = querystring.substring(0, querystring.length() - 1); //suppression du caractere & en fin de chaine
            String signature = querystring + this.apiSecret;
            MessageDigest md;
            md = MessageDigest.getInstance("SHA-1");
            //querystring = URLEncoder.encode(querystring);
            signature = convertToHex(md.digest(signature.getBytes()));
            URL urlrequested = new URL(this.baseUrl + url + "?" + querystring + "&api_signature=" + signature);
            HttpURLConnection connection = (HttpURLConnection) urlrequested.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = null;
            String full = "";
            while ((line = in.readLine()) != null) {
                full = full + line;
            }
            return full;
        } catch (ProtocolException ex) {
            Logger.getLogger(BotrAPI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(BotrAPI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(BotrAPI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(BotrAPI.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    private String makeRequest(String url) {

        return makeRequest(url, new HashMap<String, String>());

    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getApiSecret() {
        return apiSecret;
    }

    public void setApiSecret(String apiSecret) {
        this.apiSecret = apiSecret;
    }

    private String getNewNonce() {
        String nonce = "";
        Random random = new Random();
        while (nonce.length() < 8) {
            nonce += random.nextInt(10);
        }
        return nonce.substring(0, 7);
    }

    private String convertToHex(byte[] data) {
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < data.length; i++) {
            int halfbyte = (data[i] >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                if ((0 <= halfbyte) && (halfbyte <= 9)) {
                    buf.append((char) ('0' + halfbyte));
                } else {
                    buf.append((char) ('a' + (halfbyte - 10)));
                }
                halfbyte = data[i] & 0x0F;
            } while (two_halfs++ < 1);
        }
        return buf.toString();
    }
}

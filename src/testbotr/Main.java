/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package testbotr;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import org.clevercloud.botrapi.BotrAPI;

/**
 *
 * @author waxzce
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws NoSuchAlgorithmException,
            IOException {
        Properties properties = new Properties();
        properties.load(new FileReader("/etc/botrcreds.properties"));
        String key = properties.getProperty("key");
        String secret = properties.getProperty("secret");
        BotrAPI botr = new BotrAPI(key, secret);

        System.out.println(botr.getVideos());
        System.out.println(botr.getAccountContents());

    }
}

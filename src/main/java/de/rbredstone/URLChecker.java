package de.rbredstone;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class URLChecker {
    public static boolean checkURL(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("HEAD");
            int statusCode = connection.getResponseCode();
            return statusCode == HttpURLConnection.HTTP_OK;
        } catch (IOException e) {
            return false;
        }
    }
}

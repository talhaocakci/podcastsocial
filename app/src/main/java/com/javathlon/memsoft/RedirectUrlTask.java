package com.javathlon.memsoft;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by talha on 14.07.2015.
 */
public class RedirectUrlTask extends AsyncTask<String, Void, String> {
    @Override
    protected String doInBackground(String... strings) {

        String newUrl = strings[0];
        // normally, 3xx is redirect
        int status = 0;
        while (status != 200) {
            URL obj = null;
            try {
                obj = new URL(newUrl);
            } catch (MalformedURLException e1) {
                e1.printStackTrace();
            }
            HttpURLConnection conn = null;
            HttpURLConnection.setFollowRedirects(false);
            try {
                conn = (HttpURLConnection) obj.openConnection();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            conn.setInstanceFollowRedirects(false);
            conn.setReadTimeout(5000);
            conn.addRequestProperty("Accept-Language", "en-US,en;q=0.8");

            conn.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
            conn.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.101 Safari/537.36");
            conn.addRequestProperty("Referer", "google.com");
            conn.addRequestProperty("Connection", "keep-alive");
            conn.addRequestProperty("Cache-Control", "max-age=0");

            //System.out.println("Request URL ... " + url);

            boolean redirect = false;


            try {
                status = conn.getResponseCode();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            if (status != HttpURLConnection.HTTP_OK) {
                if (status == HttpURLConnection.HTTP_MOVED_TEMP
                        || status == HttpURLConnection.HTTP_MOVED_PERM
                        || status == HttpURLConnection.HTTP_SEE_OTHER)
                    redirect = true;
            }

            System.out.println("Response Code ... " + status);


            if (redirect) {

                // get redirect url from "location" header field

                if (null != conn.getHeaderField("Location"))
                    newUrl = conn.getHeaderField("Location");
                else {

                    int protocolEnd = newUrl.indexOf("://");
                    protocolEnd = Math.max(0, protocolEnd);
                    if (newUrl.length() > protocolEnd + 3) ;
                    protocolEnd += 3;
                    String s = newUrl.substring(protocolEnd);
                    s = s.substring(0, s.indexOf("/"));
                    newUrl = "NOACCESS-" + s;

                    return newUrl;
                }


                System.out.println("Redirect to URL : " + newUrl);

            }

        }

        return newUrl;
    }

}

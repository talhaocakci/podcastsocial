package com.javathlon.memsoft;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

/**
 * param1: Server URL,<br>
 * param2: relative service path<br>
 * param3: input json<br><br>
 * <p/>
 * returns jsonstring
 */
public class WebServiceAsyncTaskPost extends AsyncTask<String, Void, ResponseHolder> {

    @Override
    protected ResponseHolder doInBackground(String... arg) {

        ResponseHolder responseHolder = new ResponseHolder();
        String res = "";
        int respCode;

        try {

            String path = arg[0];

            String json = "{}";
            if (arg.length > 1)
                json = arg[1];

            DefaultHttpClient httpclient = new DefaultHttpClient();

            // url with the post data
            HttpPost httpost = new HttpPost(path);

            // passes the results to a string builder/entity

            if(arg.length  > 1) {
                StringEntity se = new StringEntity(json);

                // sets the post request as the resulting string
                httpost.setEntity(se);
            }
            // sets a request header so the page receving the request
            // will know what to do with it
            httpost.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
            httpost.setHeader("Content-type", "application/json");
            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httpost);

            HttpEntity resEntity = response.getEntity();

            responseHolder.setResponseCode(response.getStatusLine().getStatusCode());

            if (resEntity != null) {

                InputStream is = resEntity.getContent();
                if (is != null) {
                    Writer writer = new StringWriter();

                    char[] buffer = new char[1024];
                    try {
                        Reader reader = new BufferedReader(
                                new InputStreamReader(is, "UTF-8"));
                        int n;
                        while ((n = reader.read(buffer)) != -1) {
                            writer.write(buffer, 0, n);
                        }
                    } finally {
                        is.close();
                    }
                    res = writer.toString();
                    responseHolder.setResponseText(res);
                    Log.d("server result", res);

                }
            }

        } catch (ClientProtocolException e) {
            // Log.e ( "Exception" , "posting xml  Message = " +
            // e.toString ( ) ) ;
            e.printStackTrace();
        } catch (IOException e) {
            // Log.e ( "Exception" , "posting xml  Message = " +
            // e.toString ( ) ) ;
            e.printStackTrace();
        }

        return responseHolder;
    }
}

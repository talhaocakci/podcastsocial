package com.javathlon.memsoft;

import android.util.Log;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class WebServiceUtil {

    public static ResponseHolder postXMLContent(String serverUrl, String relativePath, String json) {

        ResponseHolder responseHolder = null;

        try {
            responseHolder = new WebServiceAsyncTaskPost().execute(serverUrl, relativePath, json).get(14, TimeUnit.SECONDS);
            Log.d("server result", responseHolder.getResponseText());
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (TimeoutException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return responseHolder;

    }
}

package com.javathlon.download;

import android.widget.Toast;

import com.google.gson.Gson;
import com.javathlon.ApplicationSettings;
import com.javathlon.memsoft.ResponseHolder;
import com.javathlon.memsoft.SecureUrl;
import com.javathlon.memsoft.WebServiceAsyncTaskGet;
import com.javathlon.model.podcastmodern.Application;
import com.javathlon.model.podcastmodern.Podcast;

import java.net.URLEncoder;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by ocakcit on 12/11/15.
 */
public class PodcstModernUtil {


    public static List<Podcast> getPodcastsByApplication(Integer applicationId){

        ResponseHolder s = null;
        try {
            s = new WebServiceAsyncTaskGet().execute(ApplicationSettings.podcastModernServerUrl+"application/"+applicationId).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        Gson gson = new Gson();

        Application application = gson.fromJson(s.getResponseText(), Application.class);

        if(application != null)
          return application.getPodcasts();
        else
            return null;
    }

    public static String getSignedUrl(String course, String fileKey, boolean isFree){

        if(fileKey.startsWith("http") || fileKey.startsWith("file://") ) {
            return fileKey;
        }

        if(!isFree && (ApplicationSettings.memberMode == ApplicationSettings.MemberMode.FREE)) {
            return null;
        }

        ResponseHolder s = null;
        try {
            s = new WebServiceAsyncTaskGet()
                    .execute(ApplicationSettings.podcastModernServerUrl + "url/generateurl/" + URLEncoder.encode(fileKey)+ "/" + course).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        Gson gson = new Gson();

        SecureUrl secureUrl = gson.fromJson(s.getResponseText(), SecureUrl.class);

        if(secureUrl != null)
            return secureUrl.getUrl();
        else
            return null;
    }


}

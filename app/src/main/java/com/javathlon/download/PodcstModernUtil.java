package com.javathlon.download;

import com.google.gson.Gson;
import com.javathlon.ApplicationSettings;
import com.javathlon.memsoft.ResponseHolder;
import com.javathlon.memsoft.WebServiceAsyncTaskGet;
import com.javathlon.model.podcastmodern.Application;
import com.javathlon.model.podcastmodern.Podcast;

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


}

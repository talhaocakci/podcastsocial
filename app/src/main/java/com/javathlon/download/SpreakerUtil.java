package com.javathlon.download;

import com.google.gson.Gson;
import com.javathlon.PodcastData;
import com.javathlon.memsoft.ResponseHolder;
import com.javathlon.memsoft.WebServiceAsyncTaskGet;
import com.javathlon.model.spreaker.EpisodeResult;
import com.javathlon.model.spreaker.Result;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by talha on 17.07.2015.
 */
public class SpreakerUtil {

public static List<PodcastData> getEpisodesFromSpreakerUrl(String url, int catalogId) {

    ResponseHolder s = null;
    try {
        s = new WebServiceAsyncTaskGet().execute(url + "/episodes?max_per_page=50").get();
    } catch (InterruptedException e) {
        e.printStackTrace();
    } catch (ExecutionException e) {
        e.printStackTrace();
    }
    Gson gson = new Gson();
    EpisodeResult episodeResult = gson.fromJson(s.getResponseText(), EpisodeResult.class);
    System.out.print(episodeResult);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    List<PodcastData> dataList = new ArrayList<PodcastData>();
    for (Result r : episodeResult.getResponse().getPager().getResults()) {
        PodcastData d = new PodcastData();
        d.url = r.getDownloadUrl();
        d.chapterTitle = r.getTitle();
        d.editionTitle = r.getTitle();
        d.description = r.getDescription();
        d.duration = r.getLength() / 1000;

        StringBuilder durationString = new StringBuilder();
        if (d.duration > 3600) {
            durationString.append(String.format("%02d", d.duration / 3600)).append(":");
        }
        durationString.append(String.format("%02d", (d.duration % 3600) / 60)).append(":");
        durationString.append(String.format("%02d", d.duration % 60));
        d.durationString = durationString.toString();


        d.catalogId = catalogId;
        String time = r.getPublishedAt();
        try {
            Date timeD = sdf.parse(time);
            if (timeD != null)
                d.publishDateLong = timeD.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        dataList.add(d);

    }
    return dataList;
}

}

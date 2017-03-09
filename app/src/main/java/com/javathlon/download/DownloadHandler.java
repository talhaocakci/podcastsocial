package com.javathlon.download;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;

import com.javathlon.CatalogData;
import com.javathlon.PodcastData;
import com.javathlon.adapters.PodcastAdapter;
import com.javathlon.db.DBAccessor;
import com.javathlon.memsoft.MemsoftUtil;
import com.javathlon.rss.MyXMLHandlerItems;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by talha on 10.07.2015.
 */
public class DownloadHandler extends Handler {

    PodcastAdapter adapter;
    MyXMLHandlerItems myXMLHandler;
    DBAccessor dbHelper;
    Long catalogId;
    String rssUrl;
    List<PodcastData> podcastDataList;
    private String candidateArtwork;
    private String artworkImage;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressDialog progressDialog;

    public DownloadHandler(Context con, PodcastAdapter adapter, MyXMLHandlerItems myXMLHandler, Long catalogId, DBAccessor dbHelper, String rssUrl, List<PodcastData> podcastDataList, String candidateArtwork, String artworkImage, SwipeRefreshLayout swipeRefreshLayout, ProgressDialog progressDialog) {
        this.adapter = adapter;
        this.myXMLHandler = myXMLHandler;
        this.catalogId = catalogId;
        this.dbHelper = dbHelper;
        this.rssUrl = rssUrl;
        this.podcastDataList = podcastDataList;
        this.candidateArtwork = candidateArtwork;
        this.artworkImage = artworkImage;
        this.swipeRefreshLayout = swipeRefreshLayout;
        this.progressDialog = progressDialog;
        if (dbHelper == null) {
            dbHelper = new DBAccessor(con);
            dbHelper.open();
        }

    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case RSSDownloaderParser.XMLDOWNLOADFINISHED:


                artworkImage = (myXMLHandler.podcastImage != null && myXMLHandler.podcastImage.length() < 3) ? candidateArtwork : myXMLHandler.podcastImage;

                CatalogData catalog = dbHelper.getPodcastCatalogByRss(rssUrl);
                catalogId = catalog.id;

                DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH);
                Date d = new Date();
                if (catalog != null && catalog.lastRssUpdate != null)
                    try {
                        d = formatter.parse(catalog.lastRssUpdate);
                    } catch (ParseException e) {
                        SimpleDateFormat sdf = new SimpleDateFormat("EEE dd MMM yyyy HH:mm:ss zzz", Locale.ENGLISH);
                        try {
                            d = sdf.parse(catalog.lastRssUpdate);
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                            sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
                            try {
                                d = sdf.parse(catalog.lastRssUpdate);
                            } catch (ParseException e2) {
                                e2.printStackTrace();
                            }

                        }
                        e.printStackTrace();
                    }

                // rss refresh ediliyor, her şeyi sıfırdan yapmaya gerek yok
                if (catalogId != 0L && catalog.lastRssUpdate != null) {

                    if (podcastDataList.size() > 0)
                        dbHelper.updatePodcastCatalogRSSDownload(catalogId, MemsoftUtil.getTimeAsString());
                    swipeRefreshLayout.setRefreshing(false);
                    List<PodcastData> newestItems = new ArrayList<PodcastData>();
                    for (PodcastData data : podcastDataList) {

                        if (data.publishDateLong > d.getTime()) {
                            newestItems.add(data);
                            podcastDataList.add(0, data);
                        } else {
                            break;
                        }

                    }
                    /* dinlenme oranı, downloaded gibi bilgiler için dbden güncelleyip, yeni gelenleri başa eklememiz gerekiyor.*/
                    podcastDataList.clear();

                    podcastDataList.addAll(0, newestItems);
                    podcastDataList.addAll(dbHelper.getPodcastsByCatalogId(catalogId, "n"));
                    dbHelper.bulkInsertPodcastData(newestItems, catalogId);

                }

                if (catalogId == 0L || catalog.lastRssUpdate == null) {
                    CatalogData data = new CatalogData();
                    data.rss = rssUrl;
                    data.name = myXMLHandler.podcastTitle;
                    data.image = artworkImage;
                    data.author = myXMLHandler.podcastAuthor;
                    data.summary = myXMLHandler.podcastSubtitle;
                    data.trackCount = 0;
                    data.bucketName = "javacore_course";
                    if (catalogId == 0L)
                        catalogId = dbHelper.createPodcastCatalogItem(data, "n");
                    else
                        dbHelper.updatePodcastCatalogRSSDownload(catalogId, MemsoftUtil.getTimeAsString());
                       /* Set<PodcastData> dataSet = new HashSet<PodcastData>();
                        for(PodcastData pdata : podcastDataList)
                        dataSet.add(pdata);
                        podcastDataList.clear();
                        podcastDataList.addAll(dataSet);*/
                    for (PodcastData p : podcastDataList) {
                        p.catalogId = catalogId.intValue();
                    }
                    dbHelper.bulkInsertPodcastData(podcastDataList, catalogId);
                    dbHelper.updatePodcastCatalogRSSDownload(catalogId, MemsoftUtil.getTimeAsString());
                    //  podcastDataList.clear();
                    //  podcastDataList.addAll(podcastDataList);


                }
                adapter.notifyDataSetChanged();
                if (progressDialog != null && progressDialog.isShowing())
                    try {
                        progressDialog.dismiss();
                    } catch (Exception e) {

                    }

                break;
            case RSSDownloaderParser.XMLDOWNLOADNOTFINISHED:
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();

                break;
        }
    }

    ;
}

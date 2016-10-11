package com.javathlon.scheduledtask;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.javathlon.CatalogData;
import com.javathlon.CheckInternet;
import com.javathlon.PodcastData;
import com.javathlon.db.DBAccessor;
import com.javathlon.download.RSSDownloaderParser;
import com.javathlon.download.RssRefreshAndDownloadItemsAsyncTask;
import com.javathlon.rss.MyXMLHandlerItems;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by talha on 09.07.2015.
 */
public class RssUpdateTask extends BroadcastReceiver {

    DBAccessor dbHelper;

    private static boolean isProcessing = false;

    @Override
    public void onReceive(Context context, Intent ıntent) {
        if (!isProcessing) {
            List<CatalogData> list = new ArrayList();

            if (dbHelper == null) {
                dbHelper = new DBAccessor(context);
                dbHelper.open();
            }
            list = dbHelper.getSubscribedCatalogList();
            if (list != null && list.size() > 0)
                isProcessing = true;

            List<PodcastData> dataList = new ArrayList<PodcastData>();

            RSSDownloaderParser parser = new RSSDownloaderParser(context, false, null, new MyXMLHandlerItems(), dataList);

            for (CatalogData data : list) {
                new RssRefreshAndDownloadItemsAsyncTask(context).execute(data);

            }


            boolean isWifiConnected = CheckInternet.checkWifi(context);

            //isProcessing = true;
        }
        // Toast.makeText(context, "Wifi connected: "+isWifiConnected, Toast.LENGTH_SHORT).show();
    }
}


package com.javathlon.download;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.javathlon.PodcastData;
import com.javathlon.db.DBAccessor;
import com.javathlon.rss.RssListPlayerActivity;

/**
 * Created by talha on 23.07.2015.
 */
public class DownloadProgressThread implements Runnable {
    DownloadManager downloadManager;
    DownloadManager.Query q;
    Context context;
    DBAccessor dbAccessor;

    public DownloadProgressThread(DownloadManager manager, Context context) {
        this.downloadManager = manager;
        q = new DownloadManager.Query();
        this.context = context;
    }

    @Override
    public void run() {

        while (true) {
            Object[] oArray = RssListPlayerActivity.storeTable.keySet().toArray();
            long[] lArray = new long[oArray.length];
            for (int i = 0; i < oArray.length; i++)
                lArray[i] = ((Long) oArray[i]).longValue();

            q.setFilterById(lArray);

            try {
                if (lArray.length == 0) {

                    Thread.currentThread().sleep(5000);
                    continue;
                } else
                    Thread.currentThread().sleep(2000);
            } catch (InterruptedException e) {
                //Thread.currentThread().interrupt();
                Log.d("", "interrupted");
            }

            if (lArray.length > 0) {
                Cursor cursor = downloadManager.query(q);
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    long bytesDownloaded = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                    long totalBytes = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                    Log.d("downloaded", bytesDownloaded + "");

                    long id = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_ID));
                    long podcastId = -1;
                    if (null != RssListPlayerActivity.storeTable.get(id)) {
                        PodcastData d = RssListPlayerActivity.storeTable.get(id);
                        if (d == null)
                            try {
                                //we might started the download before the download is added to the queue.
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        d = RssListPlayerActivity.storeTable.get(id);

                        if (d == null)
                            continue;

                        podcastId = d.id;
                    }
                    for (PodcastData data : RssListPlayerActivity.podcastDataList) {
                        if (data.id == podcastId) {
                            data.downloadPercentage = (int) ((bytesDownloaded * 100) / totalBytes);
                            if (data.downloadPercentage == 100) {
                                String filePath = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                                data.devicePath = filePath;
                                if (dbAccessor == null) {
                                    dbAccessor = new DBAccessor(context);
                                    dbAccessor.open();
                                }
                                dbAccessor.updatePodcastIsDownloaded(data.id, "y", filePath);
                            }
                            break;
                        }
                    }
                    cursor.moveToNext();
                }
                cursor.close();
            }

            Intent i = new Intent("downloadProgressUpdate");
            i.putExtra("downloadCount", RssListPlayerActivity.storeTable.size());
            LocalBroadcastManager.getInstance(context).sendBroadcast(i);
            if (lArray.length == 0)
                return;
        }
    }
}

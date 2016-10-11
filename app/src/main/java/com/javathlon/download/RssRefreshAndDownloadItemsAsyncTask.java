package com.javathlon.download;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;

import com.javathlon.CatalogData;
import com.javathlon.PodcastData;
import com.javathlon.db.DBAccessor;
import com.javathlon.memsoft.MemsoftUtil;
import com.javathlon.rss.MyXMLHandlerItems;
import com.javathlon.rss.RssListPlayerActivity;

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
public class RssRefreshAndDownloadItemsAsyncTask extends AsyncTask<CatalogData, Void, List<PodcastData>> {

    Context context;
    CatalogData data;
    DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH);
    DownloadManager downloadManager;
    private long downloadReference;
    private DBAccessor dbAccessor;

    public RssRefreshAndDownloadItemsAsyncTask(Context context) {
        this.context = context;
        downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

    }

    @Override
    protected List doInBackground(CatalogData... dataArray) {
        this.data = dataArray[0];
        List<PodcastData> dataList = new ArrayList<PodcastData>();
        RSSDownloaderParser parser = new RSSDownloaderParser(context, false, null, new MyXMLHandlerItems(), dataList);
        if(data.rss.startsWith("http://api.spreaker.com/show/")){
            dataList = SpreakerUtil.getEpisodesFromSpreakerUrl(data.rss, data.id.intValue());
        }
        else {
            dataList = parser.getRSSListFromUrl(data.rss, context);
        }
        return dataList;
    }

    @Override
    protected void onPostExecute(List<PodcastData> list) {
        super.onPostExecute(list);
        Date lastUpdateDate = null;

        if (dbAccessor == null) {
            dbAccessor = new DBAccessor(context);
            dbAccessor.open();
        }

        if (data.lastRssUpdate != null)
            try {
                lastUpdateDate = formatter.parse(data.lastRssUpdate);
                List<PodcastData> dataToAdd = new ArrayList<PodcastData>();
                for (PodcastData p : list) {
                    if (p.publishDateLong > lastUpdateDate.getTime())
                        dataToAdd.add(p);
                    else
                        break;
                }
                dbAccessor.bulkInsertPodcastData(dataToAdd, data.id);
                dbAccessor.updatePodcastCatalogRSSDownload(data.id, MemsoftUtil.getTimeAsString());

            } catch (ParseException e) {
                e.printStackTrace();
            }
        else {
            dbAccessor.bulkInsertPodcastData(list, data.id);
            dbAccessor.updatePodcastCatalogRSSDownload(data.id, MemsoftUtil.getTimeAsString());
        }


        int downloadStartedItemCount = 0;

        for (PodcastData data : (List<PodcastData>) list) {
            // String last = formatter.format(lastUpdateDate.getTime());

          /*  if(data.publishDateLong > lastUpdateDate.getTime()- AlarmManager.INTERVAL_DAY*4){

                System.out.println("Download: "+data.url);
            }*/
            // downloaded before?
            data = dbAccessor.getPodcastByUrl(data.url);

            // incremented by one even if download does not start. because we dont want to propopage the download queue to the
            // older records. we are dealing with only the newest 3 items.
            if (downloadStartedItemCount > 2)
                return;
            downloadStartedItemCount++;


            if (data.getIsDownloaded() == null || data.getIsDownloaded().equals("n")) {

                // less than half is listened
                if (data.progressSecond == null) data.progressSecond = "0";
                // if (Long.parseLong(data.progressSecond) < data.duration / 2) {
                if (data.devicePath != null && !data.devicePath.equals(""))
                    return;


                   Uri Download_Uri = Uri.parse(data.url);
               // Uri Download_Uri = Uri.parse("http://data.giss.nasa.gov/gistemp/tabledata_v3/GLB.Ts+dSST.txt");
                DownloadManager.Request request = new DownloadManager.Request(Download_Uri);

                //Restrict the types of networks over which this download may proceed.
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
                //Set whether this download may proceed over a roaming connection.
                request.setAllowedOverRoaming(false);
                //Set the title of this download, to be displayed in notifications (if enabled).
                request.setTitle(data.editionTitle);
                //Set a description of this download, to be displayed in notifications (if enabled)
                request.setDescription("Podmark");
                //Set the local destination for the downloaded file to a path within the application's external files directory
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_PODCASTS, data.editionTitle.replaceAll("[^\\p{L}\\p{Nd}]+", "") + ".mp3");

                //Enqueue a new download and same the referenceId
                downloadReference = downloadManager.enqueue(request);

                data.devicePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PODCASTS).getPath() + "/" + data.editionTitle.replaceAll("[^\\x00-\\x7F]", "") + ".mp3";
                RssListPlayerActivity.storeTable.put(downloadReference, data);


            }
            //}
        }


        System.out.println(data.lastRssUpdate);
        System.out.println(list.size());
    }
}
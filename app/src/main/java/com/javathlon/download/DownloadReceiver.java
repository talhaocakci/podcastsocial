package com.javathlon.download;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.ParcelFileDescriptor;
import android.view.Gravity;
import android.widget.Toast;

import com.javathlon.PodcastData;
import com.javathlon.db.DBAccessor;
import com.javathlon.rss.RssListPlayerActivity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by talha on 10.07.2015.
 */
public class DownloadReceiver extends BroadcastReceiver {

    DBAccessor dbHelper;

    @Override
    public void onReceive(Context context, Intent intent) {

        if (dbHelper == null) {
            dbHelper = new DBAccessor(context);
            dbHelper.open();
        }
        //check if the broadcast message is for our Enqueued download
        long referenceId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
        PodcastData data = RssListPlayerActivity.storeTable.get(referenceId);
        int ch;
        ParcelFileDescriptor file;
        StringBuffer strContent = new StringBuffer("");
        StringBuffer countryData = new StringBuffer("");
        DateFormat parser = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        long now = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(now);
        Date dd = null;
        try {
            dd = parser.parse(parser.format(calendar.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // bu noktaya geldiğinde datanın idsi yok. Yeniden çekmemiz lazım.
        // rssten sıfır data geldi çünkü.
        if (data == null)
            return;
        String devicePath = data.devicePath;
        data = dbHelper.getPodcastByUrl(data.url);
        dbHelper.updatePodcastIsDownloaded(data.id, "y", devicePath);

        PodcastData dataInTable = RssListPlayerActivity.storeTable.get(referenceId);
        dataInTable.setIsDownloaded("y");


        RssListPlayerActivity.storeTable.remove(referenceId);

        Toast toast = Toast.makeText(context, data.editionTitle + " downloaded successfully", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP, 25, 400);
        toast.show();


    }


}

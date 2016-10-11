package com.javathlon.scheduledtask;

import android.app.AlarmManager;
import android.app.DownloadManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Debug;
import android.widget.Toast;

import com.javathlon.CommonStaticClass;
import com.javathlon.db.DBAccessor;
import com.javathlon.download.DownloadReceiver;

import java.util.Calendar;

/**
 * Created by talha on 09.07.2015.
 */
public class BootInit extends BroadcastReceiver {

    DBAccessor dbHelper;

    private static boolean isProcessing = false;

    @Override
    public void onReceive(Context context, Intent ıntent) {

        Toast.makeText(context, "Pod Addict başlangıç olayını kavradı: ", Toast.LENGTH_LONG).show();

        Debug.waitForDebugger();
        if (!CommonStaticClass.receivers.containsKey(CommonStaticClass.DAILY_RSSREFRESH_DOWNLOADITEMS)) {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent i = new Intent("com.paperify.podmark.scheduledtask.RssUpdateTask");
            IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
            DownloadReceiver dr = new DownloadReceiver();
            context.getApplicationContext().registerReceiver(dr, filter);

            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, 8);
            cal.set(Calendar.MINUTE, 53);

            PendingIntent pi = PendingIntent.getBroadcast(context.getApplicationContext(), 0, i, 0);
            alarmManager.set(AlarmManager.RTC, cal.getTimeInMillis() + 10000, pi);
            CommonStaticClass.receivers.put(CommonStaticClass.DAILY_RSSREFRESH_DOWNLOADITEMS, dr);
            /*****************************************************************/
        }

        }


}
package com.javathlon.upload;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class UploadNotificationHelper {
    private Context mContext;
    private int NOTIFICATION_ID;
    private Notification mNotification;
    private NotificationManager mNotificationManager;
    private PendingIntent mContentIntent;
    private CharSequence mContentTitle;
    private CharSequence tickerText;

    public UploadNotificationHelper(Context context, int notify) {
        mContext = context;
        this.NOTIFICATION_ID = notify;
        Log.e("NotificationHelper" + notify, "" + NOTIFICATION_ID);
    }

    /**
     * Put the notification into the status bar
     */
    public void createNotification(String text) {
        //get the notification manager
        mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

        //create the notification
        int icon = android.R.drawable.ic_popup_sync;
        tickerText = text; //Initial text that appears in the status bar
        long when = System.currentTimeMillis();
        mNotification = new Notification(icon, tickerText, when);

        //create the content which is shown in the notification pulldown
        mContentTitle = "Upload"; //Full title of the notification in the pull down
//        CharSequence contentText = "0% complete"; //Text of the notification in the pull down

        //you have to set a PendingIntent on a notification to tell the system what you want it to do when the notification is selected
        //I don't want to use this here so I'm just creating a blank one
//        Intent i = new In
//        Intent notificationIntent = new Intent(mContext,UploadController.class);
        Intent notificationIntent = new Intent();
        mContentIntent = PendingIntent.getActivity(mContext, 0, notificationIntent, 0);

        //add the additional content and intent to the notification
//        mNotification.setLatestEventInfo(mContext, mContentTitle, contentText, mContentIntent);
        // mNotification.setLatestEventInfo(mContext, mContentTitle, tickerText, mContentIntent);
        //make this notification appear in the 'Ongoing events' section
        mNotification.flags = Notification.FLAG_ONGOING_EVENT;

        //show the notification
        mNotificationManager.notify(NOTIFICATION_ID, mNotification);
    }


    //    public void progressUpdate(String fileName,int percentageComplete) {
//        //build up the new status message
//        CharSequence contentText = percentageComplete + "% complete";
//        //publish it to the status bar
//        mNotification.setLatestEventInfo(mContext, mContentTitle, contentText+" of "+fileName, mContentIntent);
//        mNotificationManager.notify(NOTIFICATION_ID, mNotification);
//        checkActivityForeground(fileName,contentText.toString(),percentageComplete);
//    }
    public void progressUpdate(String fileName) {
        //build up the new status message
//        CharSequence contentText = percentageComplete + "% complete";
        //publish it to the status bar
        //   mNotification.setLatestEventInfo(mContext, mContentTitle, tickerText + " of " + fileName, mContentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mNotification);
//        checkActivityForeground(fileName,tickerText.toString(),percentageComplete);
    }

    protected void checkActivityForeground(final String fileName, final String contentText, final int percentageComplete) {
//		Log.d("check", "start checking for Activity in foreground");
        Intent intent = new Intent();
        intent.setAction(UploadController.UE_ACTION_UPLOAD);
        mContext.sendOrderedBroadcast(intent, null, new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                int result = getResultCode();

                if (result != Activity.RESULT_CANCELED) { // Activity caught it
                    Log.d("check1", "An activity caught the broadcast, result " + result);
                    Intent intentUpdate = new Intent();
                    intentUpdate.setAction(UploadController.UE_UPDATE_UPLOAD);
                    intentUpdate.putExtra("upatetext", contentText);
                    intentUpdate.putExtra("NOTIFICATION_ID", NOTIFICATION_ID);
                    intentUpdate.putExtra("PERCENT_COMPLETE", percentageComplete);
                    intentUpdate.putExtra("FILE_NAME", fileName);
                    mContext.sendOrderedBroadcast(intentUpdate, null, new BroadcastReceiver() {

                        @Override
                        public void onReceive(Context context, Intent intent) {
                            int result = getResultCode();
//							Log.d("update", "done, result " + contentText);
                            if (result != Activity.RESULT_CANCELED) { // Activity caught it
                                Log.d("update", "done, result " + contentText);
                                return;
                            }
//							Log.d("check2", "No activity did catch the broadcast.");
                        }
                    }, null, Activity.RESULT_CANCELED, null, null);
                    return;
                }
//				Log.d("check2", "No activity did catch the broadcast.");
//				noActivityInForeground();
            }
        }, null, Activity.RESULT_CANCELED, null, null);
    }

    public void cancelNotifiction(int notify) {
        //remove the notification from the status bar

        mNotificationManager.cancel(NOTIFICATION_ID);
    }

    /**
     * called when the background task is complete, this removes the notification from the status bar.
     * We could also use this to add a new task complete notification
     */
    public void completed() {
        //remove the notification from the status bar

        mNotificationManager.cancel(NOTIFICATION_ID);
    }

    public void onShareNotify(String shareNotify) {
        // TODO Auto-generated method stub
        //  mNotification.setLatestEventInfo(mContext, mContentTitle, tickerText + " of " + shareNotify, mContentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mNotification);
    }

}

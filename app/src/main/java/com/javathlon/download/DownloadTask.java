package com.javathlon.download;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.javathlon.MainActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;


public class DownloadTask extends AsyncTask<String, Integer, Void> {
    private NotificationHelper mNotificationHelper;
    DownloadTask downloadObj;
    SharedPreferences settings;
    SharedPreferences.Editor editor;
    public String folderName = "";
    public String chapterNo = "0";
    public String authorName = "";
    public String downloadURL = "";

    private int notify;
    Context context;
    File mPFile;
    public boolean downloadPause = false, downloadCancel = false, downloadStop = false, downloadFinished = false;
    boolean notifyPause = true;

    public DownloadTask(Context context, int notify) {
        this.context = context;
        this.notify = notify;
        //Create the notification object from NotificationHelper class
        mNotificationHelper = new NotificationHelper(context, this.notify);
        Log.e("DownloadTask", "DownloadTask" + notify);

        downloadObj = this;
        MainActivity.map.put(this.notify, downloadObj);
    }


    protected void onPreExecute() {
        //Create the notification in the statusbar
        mNotificationHelper.createNotification();
    }

    @Override
    protected Void doInBackground(String... aurl) {
        //This is where we would do the actual download stuff
        //for now I'm just going to loop for 10 seconds
        // publishing progress every second

        int count;

        try {

            downloadURL = aurl[0];
            URL url = new URL(downloadURL);
            URLConnection conexion = url.openConnection();
            if (Build.VERSION.SDK != null && Build.VERSION.SDK_INT > 13) {
                conexion.setRequestProperty("Connection", "close");
            }
            conexion.setDoOutput(true);
            conexion.connect();
            folderName = folderName.replaceAll("%20", "_");
            folderName = folderName.replaceAll("[^a-zA-Z0-9]", "_");
            String PATH = Environment.getExternalStorageDirectory()
                    + "/podmark/" + folderName;
            Log.v("", "PATH: " + PATH);

            File file = new File(PATH);
            file.mkdirs();
            String[] path = Uri.parse(aurl[0]).getPath().split("/");

            String mp3 = path[path.length - 1];
            String fileName = "Chapter" + this.chapterNo;

            File outputFile = new File(file, fileName);

            int lenghtOfFile = conexion.getContentLength();
            Log.d("ANDRO_ASYNC", "Lenght of file: " + lenghtOfFile);

            InputStream input = conexion.getInputStream();

            OutputStream output = new FileOutputStream(outputFile);
            byte data[] = new byte[2048];

            long total = 0;
            int percentDownLoad = 0;
            while (!downloadStop) {
                if (downloadCancel) {
                    break;
                }

                if (!downloadPause) {
                    if ((count = input.read(data)) != -1) {
                        total += count;
                        percentDownLoad = ((int) ((total * 100) / lenghtOfFile));
                        onProgressUpdate(fileName, percentDownLoad);
                        output.write(data, 0, count);
                        if (percentDownLoad >= 100) {
                            downloadStop = true;
                        }
                    }
                }
            }

            output.flush();
            output.close();
            input.close();
            mPFile = outputFile;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public void setDownloadPause(boolean downloadPause) {
        this.downloadPause = downloadPause;
    }

    public void setDownloadCancel(boolean downloadCancel, int viewnum) {
        this.downloadCancel = downloadCancel;
        this.cancel(true);
        MainActivity.map.remove(this.notify);
        this.mNotificationHelper.cancelNotifiction(viewnum);
    }

    public boolean getDownloadCancel() {
        return this.downloadCancel;
    }

    public String getFilePath() {
        if (downloadFinished) {
            return mPFile.getAbsolutePath();
        } else {
            return "";
        }

    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        Log.e("Current file downloading is", "Cancelled");
    }

    protected void onProgressUpdate(String fileName, Integer... progress) {
        //This method runs on the UI thread, it receives progress updates
        //from the background thread and publishes them to the status bar
        mNotificationHelper.progressUpdate(fileName, progress[0]);
    }

    MediaScannerConnection msc = new MediaScannerConnection(context,

            new MediaScannerConnectionClient() {
                public void onMediaScannerConnected() {
                    msc.scanFile(mPFile.getAbsolutePath(), null);
                    Log.d("MSC", mPFile.getAbsolutePath());
                }

                public void onScanCompleted(String path, Uri uri) {
                    Log.d("MPSCAN", "Complete"
                            + MediaStore.getMediaScannerUri());
                    msc.disconnect();
                }
            });

    protected void onPostExecute(Void result) {
        //The task is complete, tell the status bar about it
        downloadFinished = true;
        mNotificationHelper.completed();
        MainActivity.map.remove(this.notify);
//    	msc.connect();
    }
}

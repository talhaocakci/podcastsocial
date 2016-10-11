package com.javathlon.upload;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;

import com.javathlon.R;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class UploadTask2 extends AsyncTask<String, Integer, Void> {
    private UploadNotificationHelper mNotificationHelper;
    UploadTask2 uploadObj;
    //private static final String PEFERENCE_FILE = "preference";
    //private static final String ISDOWNLOADED = "isdownloaded";
    SharedPreferences settings;
    SharedPreferences.Editor editor;
    private int notify;
    Context context;
    File mPFile;
    public boolean uploadPause = false, uploadCancel = false;

    public UploadTask2(Context context, int notify) {
        this.context = context;
        this.notify = notify;
        //Create the notification object from NotificationHelper class
        mNotificationHelper = new UploadNotificationHelper(context, this.notify);
        Log.e("UploadTask", "UploadTask" + notify);

        uploadObj = this;
//    	PlayerScreen.uploadmap.put(this.notify, uploadObj);
    }


    protected void onPreExecute() {
        //Create the notification in the statusbar
        mNotificationHelper.createNotification(context.getResources().getString(R.string.upload));
    }

    @Override
    protected Void doInBackground(String... aurl) {
        int count;


        try {


            String existingFileName = aurl[0];
            String[] path = existingFileName.split("/");
            String mp3 = path[path.length - 1];
            String fileName = mp3;

            HttpURLConnection conn = null;
            DataOutputStream dos = null;
            DataInputStream inStream = null;

            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "---------------" + Long.toHexString(System.currentTimeMillis());
            int bytesRead, bytesAvailable, bufferSize;
            byte[] buffer;
            int maxBufferSize = 1 * 1024 * 1024;
            String responseFromServer = "";
            String urlString = "http://localhost:8080/tripmark/FileUpload";

            try {
                //------------------ CLIENT REQUEST
                File f = new File(existingFileName);
                FileInputStream fileInputStream = new FileInputStream(f);
                // open a URL connection to the Servlet
                URL url = new URL(urlString);
                // Open a HTTP connection to the URL
                conn = (HttpURLConnection) url.openConnection();
                // Allow Inputs
                conn.setDoInput(true);
                // Allow Outputs
                conn.setDoOutput(true);
                // Don't use a cached copy.
                conn.setUseCaches(false);
                // Use a post method.
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                dos = new DataOutputStream(conn.getOutputStream());
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"file1\";filename=\"" + existingFileName + "\"" + lineEnd);
                dos.writeBytes("Content-Type: audio/mpeg");
                dos.writeBytes(lineEnd);
                // create a buffer of maximum size
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];
                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                long lenghtOfFile = f.length();
                long total = 0;
                while (bytesRead > 0) {

                    if (uploadCancel) {
                        break;
                    }
                    if (uploadPause) {
                        Log.e("upload", "is paused");
                    } else {

                        dos.write(buffer, 0, bufferSize);
                        bytesAvailable = fileInputStream.available();
                        bufferSize = Math.min(bytesAvailable, maxBufferSize);
                        bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                        total += bytesRead;
                        onProgressUpdate(fileName, ((int) ((total * 100) / lenghtOfFile)));
                    }

                }
                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
                // close streams
                Log.e("Debug", "File is written");
                fileInputStream.close();
                dos.flush();
                dos.close();
                int respCode = conn.getResponseCode();
                Log.e("respCode", "" + respCode);
            } catch (MalformedURLException ex) {
                Log.e("Debug", "error: " + ex.getMessage(), ex);
            } catch (IOException ioe) {
                Log.e("Debug", "error: " + ioe.getMessage(), ioe);
            }
            //------------------ read the SERVER RESPONSE
            //------------------ read the SERVER RESPONSE
//            try {
//            	inStream = new DataInputStream(conn.getInputStream());
//                StringBuilder response = new StringBuilder();
//
//                String line;
//                while ((line = inStream.readLine()) != null) {
//                    response.append(line).append('\n');
//                }
//                Log.e("Debug","Server Response "+response);
////                return response.toString();
//            } finally {
//                if (inStream != null) inStream.close();
//            }
            try {
                Log.e("", "" + conn.getResponseCode());
            } catch (Exception e) {
                Log.e("Debug", "error: " + e.getMessage(), e);
            }
            try {
                inStream = new DataInputStream(conn.getInputStream());
                String str;

                while ((str = inStream.readLine()) != null) {
                    Log.e("Debug", "Server Response " + str);
                }
                inStream.close();

            } catch (IOException ioex) {
                Log.e("Debug", "error: " + ioex.getMessage(), ioex);
            }
        } catch (Exception e) {
        }
        return null;
    }

    public void setUploadPause(boolean uploadPause) {
        this.uploadPause = uploadPause;
    }

    public void setUploadCancel(boolean uploadCancel, int viewnum) {
        this.uploadCancel = uploadCancel;
        this.cancel(true);
        this.mNotificationHelper.cancelNotifiction(viewnum);
    }

    public boolean getUploadCancel() {
        return this.uploadCancel;
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        Log.e("Current file downloading is", "Cancelled");
    }

    protected void onProgressUpdate(String fileName, Integer... progress) {
        //This method runs on the UI thread, it receives progress updates
        //from the background thread and publishes them to the status bar
//        mNotificationHelper.progressUpdate(fileName,progress[0]);
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
        mNotificationHelper.completed();
//    	msc.connect();
    }
}

package com.javathlon.upload;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.javathlon.BaseActivity;
import com.javathlon.PodcastData;
import com.javathlon.R;
import com.javathlon.db.DBAccessor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by talha on 20.07.2015.
 */


public class Mp3Cropper extends AsyncTask<Number, Void, Void> {
    DBAccessor dbAccessor;
    Context context;
    CropResultType type;

    public Mp3Cropper(Context context, CropResultType type) {
        this.context = context;
        this.type = type;
    }


    protected Void doInBackground(Number... values) {

        long podcastId = (Long) values[0];
        int beginSecond = (Integer) values[1];
        long noteId =  (Long) values[2];



        if (dbAccessor == null) {
            dbAccessor = new DBAccessor(context);
            dbAccessor.open();
        }

        PodcastData data = dbAccessor.getPodcastById(podcastId);


        int endSecond = beginSecond + 45;
        String filePath = data.devicePath;
        File outFile = null;
        CheapMP3 cheap = new CheapMP3();
        File a = new File(filePath);
        if (a.exists()) {
            Log.v("", "" + beginSecond);
        } else {
            Log.e("Something is", "going on");
        }
        String PATH = Environment.getExternalStorageDirectory()
                + "/download/croppedfolder/";
        Log.v("", "PATH: " + PATH);
        File file = new File(PATH);
        file.mkdirs();
        String mp3Song = filePath.substring(
                filePath.lastIndexOf("/") + 1, filePath.length());
        String newFileChunkName = "podaddict" + "@@@" + System.currentTimeMillis() + "@@@" + mp3Song;
        String newFileName = "";
        try {

            UploadNotificationHelper mNotificationHelper;

            mNotificationHelper = new UploadNotificationHelper(context, (int)System.currentTimeMillis()%999999);
            mNotificationHelper.createNotification(context.getResources().getString(R.string.croppingsnippet));

            cheap.ReadFile(a);
            outFile = new File(file, newFileChunkName);
            newFileName = outFile.getAbsolutePath();
            final int startFrame = cheap.secondsToFrames(beginSecond);
            final int endFrame = cheap.secondsToFrames(endSecond);
            cheap.WriteFile(outFile, startFrame, endFrame - startFrame);

            mNotificationHelper.completed();


        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        if(type == CropResultType.MP3){

            PackageManager manager = context.getPackageManager();
            try {
                PackageInfo packageInfo = manager.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("audio/*");

                i.setPackage("com.whatsapp");
                i.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(outFile));
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.getApplicationContext().startActivity(i);
            } catch (PackageManager.NameNotFoundException e) {
                new BaseActivity().showMyDialog("Whatsapp is not installed.");
            }
            return null;

        }


        if (newFileName.length() > 0) {
            // to make the notification unique
            UploadTask ut = new UploadTask(context, 10000+ (int)noteId);
            ut.noteStart = (int) beginSecond;
            ut.noteEnd = (int) endSecond;
            ut.noteId = noteId;
            ut.execute(newFileName);

        }
        Log.e("New File Name: ", newFileName);

        return null;
    }


    public enum CropResultType {
        MP3, URL;
    }
}

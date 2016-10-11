package com.javathlon.upload;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.cognito.CognitoSyncManager;
import com.amazonaws.mobileconnectors.s3.transfermanager.TransferManager;
import com.amazonaws.mobileconnectors.s3.transfermanager.Upload;
import com.amazonaws.mobileconnectors.s3.transfermanager.model.UploadResult;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.facebook.android.ShareOnFacebook;
import com.javathlon.BaseActivity;
import com.javathlon.CommonStaticClass;
import com.javathlon.R;
import com.javathlon.db.DBAccessor;
import com.javathlon.download.CustomEventHandler;
import com.javathlon.rss.MyXMLHandlerItems;
import com.javathlon.rss.XmlParseClass;
import com.podmark.twitter.TweetOnTwitter;

import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class UploadTask extends AsyncTask<String, Integer, String> {
    public String fileDownloadUrl = "";
    public Integer noteStart;
    public Integer noteEnd;
    public String noteText;
    public boolean uploadPause = false, uploadCancel = false;
    UploadTask uploadObj;
    Long noteId;
    //private static final String PEFERENCE_FILE = "preference";
    //private static final String ISDOWNLOADED = "isdownloaded";
    SharedPreferences settings;
    SharedPreferences.Editor editor;
    Context context;
    File mPFile;
    File f;
    int nullFlag;
    String pid;
    String urlpath = " ";
    DBAccessor dbAccessor;
    private UploadNotificationHelper mNotificationHelper;
    private int notifyUserForUpload;
    String existingFileName;

    public UploadTask(Context context, int notifyUserForUpload) {
        this.context = context;
        this.notifyUserForUpload = notifyUserForUpload;
        //Create the notification object from NotificationHelper class
        mNotificationHelper = new UploadNotificationHelper(context, this.notifyUserForUpload);
        Log.e("UploadTask", "UploadTask" + notifyUserForUpload);

        uploadObj = this;
        BaseActivity.uploadmap.put(this.notifyUserForUpload, uploadObj);
    }


    protected void onPreExecute() {
        //Create the notification in the statusbar
        mNotificationHelper.createNotification(context.getResources().getString(R.string.upload));
    }

    @Override
    protected String doInBackground(String... aurl) {
        existingFileName = aurl[0];
        String[] path = existingFileName.split("/");
        String fileName = path[path.length - 1];

        // initiate a credentials provider
        // Initialize the Amazon Cognito credentials provider
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                context,
                "us-east-1:e16e9b7f-d27e-4f6a-8452-f61b239dcd11", // Identity Pool ID
                Regions.US_EAST_1 // Region
        );

        AWSCredentials cred = new AWSCredentials() {
            @Override
            public String getAWSAccessKeyId() {
                return "AKIAISPPNAZRDL7OBQSA";
            }

            @Override
            public String getAWSSecretKey() {
                return "V07+Fftn0QcxoyCQ6lz1K20Z8uNnxNS8QCRFwkK1";
            }
        };

// Initialize the Cognito Sync client
        CognitoSyncManager syncClient = new CognitoSyncManager(
                context,
                Regions.US_EAST_1, // Region
                credentialsProvider);


        String clusterName = "podaddict";
        String  s="";
        TransferManager manager = new TransferManager(cred);
        Upload upload = manager.upload(new PutObjectRequest(clusterName, fileName, new File(existingFileName)).withCannedAcl(CannedAccessControlList.PublicRead));
        try {
            UploadResult result = upload.waitForUploadResult();
            s = "https://s3.amazonaws.com/" + clusterName + "/" + fileName;

            if (dbAccessor == null) {
                dbAccessor = new DBAccessor(context);
                dbAccessor.open();
            }
            dbAccessor.updateNoteRemoteLink(noteId, s);



        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        return s;
    }

    private void getResponseData(String response) {
        SAXParserFactory spf = SAXParserFactory.newInstance();
        SAXParser sp;
        Vector vvv = null;
        String tempUrl = "";
        try {
            sp = spf.newSAXParser();
            XMLReader xr = sp.getXMLReader();
            MyXMLHandlerItems myXMLHandler = new MyXMLHandlerItems();
            xr.setContentHandler(myXMLHandler);
            xr.parse(response);
            vvv = myXMLHandler.v;
        } catch (ParserConfigurationException e) {
            // Log.d("problem: ", "1");
            e.printStackTrace();
        } catch (SAXException e) {
            // Log.d("problem: ", "2");
            e.printStackTrace();
        } catch (IOException e) {
            // Log.d("problem: ", "3");
            e.printStackTrace();
        }
        if (vvv == null) {
            nullFlag = 0;
        } else {
            nullFlag = 1;
        }
        if (nullFlag == 0) {

        } else {
            int length = vvv.size();

            for (int i = 0; i < length; i++) {
                XmlParseClass stationListObj = (XmlParseClass) vvv
                        .elementAt(i);
                pid = stationListObj.getPid();
                urlpath = stationListObj.getPath();
                Log.e("for pid", "" + pid);
                Log.e("for path", "" + urlpath);
            }
        }

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

    protected void onShareNotify(String shareNotify) {
        //This method runs on the UI thread, it receives progress updates
        //from the background thread and publishes them to the status bar
//        mNotificationHelper.progressUpdate(fileName,progress[0]);
        mNotificationHelper.onShareNotify(shareNotify);
    }

    protected void onProgressUpdate(String fileName) {
        //This method runs on the UI thread, it receives progress updates
        //from the background thread and publishes them to the status bar
//        mNotificationHelper.progressUpdate(fileName,progress[0]);
        mNotificationHelper.progressUpdate(fileName);
    }

    protected void onPostExecute(final String result) {
        //The task is complete, tell the status bar about it
        BaseActivity.uploadmap.remove(this.notifyUserForUpload);
        mNotificationHelper.completed();

        CustomEventHandler customEventHandler = new CustomEventHandler(context);
        Message msg = new Message();
        msg.what = CustomEventHandler.UPLOAD_COMPLETED;
        Bundle b = new Bundle();
        b.putString("url", result);
        b.putString("devicepath", existingFileName);
        msg.setData(b);
        customEventHandler.sendMessage(msg);
        if (urlpath.length() > 0) {


            String longurl = "http://paperify.net/web/podmark_player.do?filepath=" + urlpath + "&pid=" + pid;
            //Url url = as("talhaocakci", "R_5dad426f4f02390484827d8a05ba8461").call(shorten(longurl));
            //String ur = url.getShortUrl();
            String ur = longurl;
//			String msg = "http://paperify.net/PodMarkSocial/podmark_player.pp?filepath=https://s3.amazonaws.com/paperifyapp-ec7fa6c3-82ec-4ce5-8365-c268421490d2/talha.mp3";
            if (CommonStaticClass.facebookLogin && !CommonStaticClass.twitterLogin) {
                Intent postOnFacebookWallIntent = new Intent(context, ShareOnFacebook.class);
                postOnFacebookWallIntent.putExtra("url", ur);
                postOnFacebookWallIntent.putExtra("only", true);
                postOnFacebookWallIntent.putExtra("pid", pid);
                context.startActivity(postOnFacebookWallIntent);
            } else if (!CommonStaticClass.facebookLogin && CommonStaticClass.twitterLogin) {
                Intent postOnFacebookWallIntent = new Intent(context, TweetOnTwitter.class);
                postOnFacebookWallIntent.putExtra("message", ur);
                postOnFacebookWallIntent.putExtra("only", true);
                postOnFacebookWallIntent.putExtra("pid", pid);
                context.startActivity(postOnFacebookWallIntent);
            } else if (CommonStaticClass.facebookLogin && CommonStaticClass.twitterLogin) {
                Intent postOnFacebookWallIntent = new Intent(context, ShareOnFacebook.class);
                postOnFacebookWallIntent.putExtra("url", ur);
                postOnFacebookWallIntent.putExtra("only", false);
                postOnFacebookWallIntent.putExtra("pid", pid);
                context.startActivity(postOnFacebookWallIntent);
            }
        }
    }
}

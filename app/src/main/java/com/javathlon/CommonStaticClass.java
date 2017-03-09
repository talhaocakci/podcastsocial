package com.javathlon;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;

import java.util.HashMap;
import java.util.Map;

public class CommonStaticClass {

    public static String APP_ID = "263435310362974";

    public static final String CONSUMER_KEY = "M7K1ZX9dtRYChao9oWDIIQ";//"DjTC91wLeKyTJQLSW5Jyiw";
    public static final String CONSUMER_SECRET = "8449befc531b3c9adcbdb3f8fdd63751";
    ;

    public static final String REQUEST_URL = "http://api.twitter.com/oauth/request_token";
    public static final String ACCESS_URL = "http://api.twitter.com/oauth/access_token";
    public static final String AUTHORIZE_URL = "http://api.twitter.com/oauth/authorize";

    public static final String OAUTH_CALLBACK_URL = "http://localhost.com";

    public static boolean playerShowing = false;

    public static boolean isPinFound = false;

    public static String emailTo = "";
    public static String emailTxt = "Test";
    public static String projectName = "podmark";
    public static String facebookUserId = "";
    public static String twitterUserId = "";

    public static String FBSHARE = "fbshare";
    public static String TWITTERSHARE = "twittershare";

    public static boolean twitterLogin;
    public static boolean facebookLogin;
    public static boolean twittershareon;
    public static boolean facebookshareon;

    public static String currentSongPath = "";

    public static boolean playerScreenResumed = false;
    public static boolean playerScreenPaused = false;

    public static int threadnumber = 0;
    public static boolean playerScreenFocused = false;
    public static int songspecificID;
    public static int playerInstance = 0;

    public static boolean streaming = false;

    private static PodcastData currentPodcast = new PodcastData();
//	public static String streammp3Url = "";


    public static boolean pressedHomeWhileInPlayer;

    public static String StreamFileTitle = "";
    public static String StreamFileAuthor = "";

    public static Map<String, BroadcastReceiver> receivers = new HashMap<String, BroadcastReceiver>();
    public static String DAILY_RSSREFRESH_DOWNLOADITEMS = "Download_Receiver";

    public static ProgressDialog progressDialog;

    public static PodcastData getCurrentPodcast() {
        return currentPodcast;
    }

    public static void setCurrentPodcast(PodcastData currentPodcast) {
        CommonStaticClass.currentPodcast = currentPodcast;
    }
}

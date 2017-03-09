package com.javathlon.player;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.javathlon.BaseActivity;
import com.javathlon.CommonStaticClass;
import com.javathlon.R;
import com.javathlon.memsoft.RedirectUrlTask;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;


public class MusicService extends Service implements MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnBufferingUpdateListener {

    private static final String ACTION_PLAY = "com.paperify.podmark.ACTION_PLAY_MUSIC";
    private static String mUrl;
    NotificationManager mNotificationManager;
    Notification mNotification = null;
    public static boolean pausedByUser = false;
    Context context;
    static BaseActivity activity;
    //	Timer coutnDownTimer;
    // The ID we use for the notification (the onscreen alert that appears at the notification
    // area at the top of the screen as an icon -- and as text as well if the user expands the
    // notification area).
    final int NOTIFICATION_ID = 1;

    private static MusicService mInstance = null;


    @Override
    public void onCreate() {
        mInstance = this;
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        tm.listen(myPhoneListener, PhoneStateListener.LISTEN_CALL_STATE);


    }

    final PhoneStateListener myPhoneListener = new PhoneStateListener() {
        public void onCallStateChanged(int state, String incomingNumber) {
            String TAG = "PhoneStateListener";

            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING:
                    Log.d(TAG, "Someone's calling. Let us stop the service");
                    if (isPlaying()) {
                        pauseMusic();
                    }
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    if (isPaused() && !MusicService.pausedByUser) {
                        startMusic();
                    }
                    break;
                case TelephonyManager.CALL_STATE_IDLE:

                    break;
                default:
                    Log.d(TAG, "Unknown phone state = " + state);
            }
        }
    };

    public class LocalBinder extends Binder {
        MusicService getService() {
            return MusicService.this;
        }
    }

    // The Media Player
    MediaPlayer mMediaPlayer = null;

    private final IBinder mBinder = new LocalBinder();

    // indicates the state our service:
    enum State {
        Retrieving, // the MediaRetriever is retrieving music
        Stopped, // media player is stopped and not prepared to play
        Preparing, // media player is preparing...
        Prepared, // media player is prepared for playback...
        Playing, // playback active (media player ready!). (but the media player may actually be
        // paused in this state if we don't have audio focus. But we stay in this state
        // so that we know we have to resume playback once we get focus back)
        Paused
        // playback paused (media player ready!)
    }

    ;

    State mState = State.Retrieving;
    private int mBufferPosition;
    private static String mSongTitle;
    private static int songspID;
    private static String mSongPicUrl;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getAction().equals(ACTION_PLAY)) {
            if (mMediaPlayer != null)
                mMediaPlayer.release();
            mMediaPlayer = new MediaPlayer(); // initialize it here
            mMediaPlayer.setOnPreparedListener(this);
            mMediaPlayer.setOnErrorListener(this);
            mMediaPlayer.setOnBufferingUpdateListener(this);
            mMediaPlayer.setOnCompletionListener(this);
            //mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            initMediaPlayer();
        }
        return START_NOT_STICKY;
    }

    public void initMediaPlayer() {
        try {

            File f = new File(MusicService.mUrl);
            f.exists();
            mMediaPlayer.setDataSource(MusicService.mUrl);
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            activity.showMyDialog(activity.getResources().getString(R.string.filecorruptedornotsupported));
        } catch (Exception e) {
            // TODO Workaround for bug: http://code.google.com/p/android/issues/detail?id=957
            mMediaPlayer.reset();
            try {


                mMediaPlayer.setDataSource(MusicService.mUrl);
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            e.printStackTrace();
        }

        try {
            mMediaPlayer.prepareAsync(); // prepare async to not block main thread
        } catch (Exception e) {
            // TODO Workaround for bug: http://code.google.com/p/android/issues/detail?id=957
            mMediaPlayer.reset();
            try {
                mMediaPlayer.setDataSource(MusicService.mUrl);
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            try {

                mMediaPlayer.prepare();
            } catch (Exception e1) {
                mMediaPlayer.reset();
                try {
                    mMediaPlayer.setDataSource(MusicService.mUrl);
                    mMediaPlayer.prepare();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
                e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        mState = State.Prepared;
//		setUpAsForeground(mSongTitle + " (loading)");
    }

    public void restartMusic() {
        mState = State.Retrieving;
        mMediaPlayer.reset();
        initMediaPlayer();
//		startMusic();
    }

    public static String followRedirectUrl(String url) throws ExecutionException, InterruptedException {
        if (url != null && url.startsWith("http")) {
            url = new RedirectUrlTask().execute(url).get();
        }

        return url;
    }

    protected void setBufferPosition(int progress) {
        mBufferPosition = progress;
    }

    /**
     * Called when MediaPlayer is ready
     */
    @Override
    public void onPrepared(MediaPlayer player) {
        /*mState = State.Playing;
		mMediaPlayer.start();
		mState = State.Prepared;*/
//		setUpAsForeground(mSongTitle + " (loaded)");
        //      Toast.makeText(getApplicationContext(), "Prepared", Toast.LENGTH_LONG).show();
        ((PlayerScreen) activity).hideDialog();
    }

    /**
     * Updates the notification.
     */
    void updateNotification(String text) {
        Intent i = new Intent(getApplicationContext(), PlayerScreen.class);
        i.putExtra("mediapath", MusicService.mUrl);
        if (songspID >= 0)
            i.putExtra("sppos", songspID);
        else
            i.putExtra("sppos", -1);
        i.putExtra("fromBackground", true);
        if (mUrl.contains("http")) {
            i.putExtra("stream", true);
        } else {
            i.putExtra("stream", false);
        }
        PendingIntent pi =
                PendingIntent.getActivity(this, 0, i,
                        PendingIntent.FLAG_UPDATE_CURRENT);
        //  mNotification.setLatestEventInfo(getApplicationContext(), getResources().getString(R.string.app_name), text, pi);
        mNotificationManager.notify(NOTIFICATION_ID, mNotification);
    }

    /**
     * Configures service as a foreground service. A foreground service is a service that's doing something the user is
     * actively aware of (such as playing music), and must appear to the user as a notification. That's why we create
     * the notification here.
     */
    void setUpAsForeground(String text) {
        Intent i = new Intent(getApplicationContext(), PlayerScreen.class);
        i.putExtra("mediapath", MusicService.mUrl);

        if (songspID >= 0)
            i.putExtra("sppos", songspID);
        else
            i.putExtra("sppos", -1);

        i.putExtra("fromBackground", true);
        if (mUrl.contains("/") || mUrl.contains(".com")) {
            i.putExtra("stream", true);
        } else {
            i.putExtra("stream", false);
        }
        // i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pi =
                PendingIntent.getActivity(this.getApplicationContext(), 15558, i, PendingIntent.FLAG_ONE_SHOT);
        mNotification = new NotificationCompat.Builder(this.getApplicationContext())
                .setContentTitle("Podaddict")
                .setContentText(text)
                .setTicker("Listening")
                .setWhen(System.currentTimeMillis())
                .setContentIntent(pi)
                //.setDefaults(Notification.DEFAULT_SOUND)
                .setAutoCancel(false)
                .setSmallIcon(R.drawable.ic_menu_play_clip)
                .build();
        mNotification.tickerText = text;

        mNotification.icon = R.drawable.ic_menu_play_clip;
        //	mNotification.flags |= Notification.FLAG_ONGOING_EVENT;
        //	mNotification.setLatestEventInfo(getApplicationContext(), getResources().getString(R.string.app_name), text, pi);
        //   mNotification.flags |= Notification.FLAG_AUTO_CANCEL;
        mNotificationManager.notify(15558, mNotification);
        startForeground(0, mNotification);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IBinder onBind(Intent arg0) {
        return mBinder;
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void onDestroy() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
        }
        mState = State.Retrieving;
    }

    public MediaPlayer getMediaPlayer() {
        return mMediaPlayer;
    }

    public void pauseMusic() {
        if (mState.equals(State.Playing)) {
            mMediaPlayer.pause();
            mState = State.Paused;
//			coutnDownTimer = new Timer();
//			coutnDownTimer.schedule(new TimerTask() {
//				
//				@Override
//				public void run() {
//					// TODO Auto-generated method stub
//					kill
//				}
//			},  400000);
//			updateNotification(mSongTitle + " (paused)");
            setUpAsForeground(mSongTitle + " (paused)");
        }
    }

    public void stopMusic() {
        if (mState.equals(State.Playing) || mState.equals(State.Paused)) {
            mMediaPlayer.stop();
            mState = State.Stopped;
            mNotificationManager.cancel(15558);
            //updateNotification(mSongTitle + " (stopped)");
            stopForeground(true);
//			if(!CommonStaticClass.streaming){

            mState = State.Retrieving;
            mMediaPlayer.reset();
            initMediaPlayer();
//			}
        }
    }

    public void startMusic() {
//		if(CommonStaticClass.streaming && !mState.equals(State.Prepared)){
//			initMediaPlayer();
//		}
        if (!mState.equals(State.Preparing) && !mState.equals(State.Retrieving)) {
            mMediaPlayer.start();
            Log.e("mMediaPlayer.start()", "also calling");
            mState = State.Playing;
//			updateNotification(mSongTitle + " (playing)");
            setUpAsForeground(mSongTitle + " (playing)");
        }
    }

    public boolean isPlaying() {
        if (mState.equals(State.Playing)) {
            return true;
        }
        return false;
    }

    public boolean isPaused() {
        if (mState.equals(State.Paused)) {
            return true;
        }
        return false;
    }

    public int getMusicDuration() {
        if (!mState.equals(State.Preparing) && !mState.equals(State.Retrieving)) {
            return mMediaPlayer.getDuration();
        }
        return 0;
    }

    public int getCurrentPosition() {
        if (!mState.equals(State.Preparing) && !mState.equals(State.Retrieving)) {
            return mMediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    public int getBufferPercentage() {
        // if (mState.equals(State.Preparing)) {
        return mBufferPosition;
        // }
        // return getMusicDuration();
    }

    public void seekMusicTo(int pos) {
        if (mState.equals(State.Playing) || mState.equals(State.Paused)) {
            mMediaPlayer.seekTo(pos);
        }

    }

    public void startFrom(int pos) {
        if (mState.equals(State.Playing) || mState.equals(State.Paused)) {
            mMediaPlayer.seekTo(pos);
        }
    }

    public static MusicService getInstance(BaseActivity activity) {
        MusicService.activity = activity;
        return mInstance;
    }

    public boolean isPrepared() {
        if (mState.equals(State.Prepared)) {
            return true;
        }
        return false;
    }

    public static void setSong(String url, String title, String songPicUrl) {
        MusicService.mUrl = url;
        if (!MusicService.mUrl.contains("http://") || !MusicService.mUrl.contains("https://")) {
            CommonStaticClass.streaming = false;

        }
        mSongTitle = title;
        mSongPicUrl = songPicUrl;
    }

    public static void setSongAndSongSpId(String url, String songTitle, int i, String songPicUrl) {
        MusicService.mUrl = url;
        if (!MusicService.mUrl.contains("http://") || !MusicService.mUrl.contains("https://")) {
            CommonStaticClass.streaming = false;
        }
        songspID = i;
        mSongPicUrl = songPicUrl;
        mSongTitle = songTitle;
    }

    public String getSongTitle() {
        return mSongTitle;
    }

    public String getSongPicUrl() {
        return mSongPicUrl;
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        Log.e("onBufferingUpdate", "" + percent);
        Log.e("onBufferingUpdate", "" + percent * getMusicDuration() / 100);
        setBufferPosition(percent * getMusicDuration() / 100);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        // TODO Auto-generated method stub
        stopForeground(true);
        mState = State.Stopped;
    }
}

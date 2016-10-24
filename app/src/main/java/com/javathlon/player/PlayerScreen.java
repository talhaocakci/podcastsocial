package com.javathlon.player;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.text.method.LinkMovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.MediaController.MediaPlayerControl;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.appevents.AppEventsLogger;
import com.javathlon.BaseActivity;
import com.javathlon.CatalogData;
import com.javathlon.CommonStaticClass;
import com.javathlon.NoteUtil;
import com.javathlon.PodcastData;
import com.javathlon.R;
import com.javathlon.Utils;
import com.javathlon.adapters.NoteAdapter;
import com.javathlon.db.DBAccessor;
import com.javathlon.memsoft.ImageUtil;
import com.javathlon.model.ListenStatistic;
import com.javathlon.model.ListenStatisticHolder;
import com.javathlon.model.Note;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class PlayerScreen extends BaseActivity implements MediaPlayerControl, SeekBar.OnSeekBarChangeListener {

    private ProgressDialog progressDialog;
    private AudioManager audioManager;
    boolean isSongNavigation;
    protected static final int UPDATE = 0, NOTEUPDATE = 1, CHANGESONTITLE = 2,
            CHANGEARTIST = 3, CHANGEALBUMART = 4, FOCUSCHANGE = 5, STOPSEEKING = 6;
    private static final String LIKE = "LIKE";
    private static final String SAVE = "SAVE";
    private Button markButton;
    private TextView playPauseButton, stopButton, nextSongButton, prevSongButton, addNoteButton, saveNoteBt, showInfoButton, infoText;
    private RelativeLayout addNoteLayout;
    private ScrollView infoScroll;
    private EditText noteEditText;
    private SeekBar seekBar;
    private Cursor cursor, cursorForCount;
    private final Handler handler = new Handler();
    private Thread songThread = null;
    private int beginPosInt, endPosInt;
    private ArrayList<String> siblingPaths = new ArrayList<String>();
    private ArrayList<String> fileNames = new ArrayList<String>();
    private Integer currentIndexInFolder = 0;
    private String noteText;
    private String fileName;
    private float bp, ep;
    private Context c;
    private Uri mainAudioUri;
    private long podcastID;
    int numberOfNotePerMedia;
    private ImageView albumArt;
    private TextView beginView, songTitleView,
            musicCurLoc, musicDuration;
    private WebView commentsWebView;
    private int notifyUser = 0, ps, maxVolume, curVolume;
    private static int HOUR_DIVIDER = 60 * 60 * 1000;
    private static int MINUTE_DIVIDER = 60 * 1000;
    private Context con;
    private boolean firstPlay = false;

    static ListenStatisticHolder statisticHolder;

    enum TYPE {
        IMAGE, AUDIO, VIDEO, TEXT, APP, AUDVID
    }

    private boolean noteFromOtherScreen = false;
    private ListView lv;
    private boolean beginPosSelected = false, endPosSelected = false,
            selectionPlaying = false;
    private Bitmap bmp;
    private String songpath;
    private long day = 0, hour = 0, min = 0, sec = 0;
    private volatile int currentPosition = 0;
    private boolean isStopped = true, stopButSeeked = false, fromBackground;
    private String songTitle, artistName;

    private PlayerThread plTherad;
    private volatile int maxToMark = -1;
    private volatile int minToMark = 0;
    private SeekBar noteBeginningBar;
    private volatile boolean usingMarker = false;
    private volatile String beginMarkTime, endMarkTime;
    private IntentFilter mIntentFilter;

    public static int LISTEN_STATISTIC_PRECISION = 15 * 60;
    private static long startSecond = 0;
    private static ListenStatistic currentStat = new ListenStatistic();

    private class PlayerThread implements Runnable {
        boolean firstRun = true;

        public PlayerThread() {
        }

        @Override
        public void run() {
            while (!musicThreadFinished) {
                try {
                    Thread.sleep(1000);
                    if (isPlaying()) {
                        currentPosition = getCurrentPosition();
                        currentStat.setEndPos(currentPosition);
                    }
                    if (isStopped && !stopButSeeked) {
                        currentPosition = 0;
                        continue;
                    }
                } catch (InterruptedException e) {
                    return;
                } catch (Exception e) {
                    return;
                }
                if (CommonStaticClass.streaming) {
                    //Log.e("seekBar","currentPosition :"+currentPosition);
                    //Log.e("seekBar","getBufferPercentage :"+getBufferPercentage());
                    seekBar.setSecondaryProgress(getBufferPercentage());
                }
                if (!isPaused()) {
                    final int total = getDuration();
                    maxToMark = total;
                    minToMark = 0;
                    final String totalTime = getAsTime(total);
                    final String curTime = getAsTime(currentPosition);

                    seekBar.setMax(total);
                    seekBar.setProgress(currentPosition);


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            /*if (isPlaying() ) {
                                playPauseButton.setText(R.string.material_play);

							} else {
                                playPauseButton.setText(R.string.material_pause);
							}*/
                            if (!totalTime.contains("-")) {
                                musicDuration.setText(totalTime);
                            }

                            noteBeginningBar.setMax(total);

                            musicCurLoc.setText(curTime);
                            if (firstRun) {
                                if (!totalTime.contains("-")) {
                                    firstRun = false;


                                    noteBeginningBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                                        @Override
                                        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                                            usingMarker = true;
                                            //      beginPosInt = minValue;
                                            //    endPosInt = maxValue;
                                            beginMarkTime = getAsTime(i);

                                            //endMarkTime = getAsTime(maxValue);
                                            beginView.setText(beginMarkTime);

                                        }

                                        @Override
                                        public void onStartTrackingTouch(SeekBar seekBar) {

                                        }

                                        @Override
                                        public void onStopTrackingTouch(SeekBar seekBar) {

                                        }
                                    });
                                }
                            }
                        }
                    });
                }
            }
        }

        void stopThread() {
            musicThreadFinished = true;
        }

        void startThread() {
            musicThreadFinished = false;
        }

    }


    public static void setLayoutAnim_slidedownfromtop(ViewGroup panel, Context ctx) {

        AnimationSet set = new AnimationSet(true);

        Animation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(100);
        set.addAnimation(animation);

        animation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF, 0.0f
        );
        animation.setDuration(500);
        set.addAnimation(animation);

        LayoutAnimationController controller =
                new LayoutAnimationController(set, 0.25f);
        panel.setLayoutAnimation(controller);

    }


    private void loadGui() {

        addNoteLayout = (RelativeLayout) findViewById(R.id.addNoteView);
        nextSongButton = (TextView) findViewById(R.id.nextSongButton);
        prevSongButton = (TextView) findViewById(R.id.prevSongButton);
        addNoteButton = (TextView) findViewById(R.id.addNoteButton);
        showInfoButton = (TextView) findViewById(R.id.showInfoButton);
        infoText = (TextView) findViewById(R.id.infoText);
        noteBeginningBar = (SeekBar) findViewById(R.id.noteBeginningSlider);
        markButton = (Button) findViewById(R.id.markBtn);
        stopButton = (TextView) findViewById(R.id.stopBtn);
        nextSongButton = (TextView) findViewById(R.id.nextSongButton);
        prevSongButton = (TextView) findViewById(R.id.prevSongButton);
        nextSongButton.setOnClickListener(onClickNext);
        prevSongButton.setOnClickListener(onClickPrev);
        nextSongButton.setEnabled(CommonStaticClass.streaming ? false : true);
        prevSongButton.setEnabled(CommonStaticClass.streaming ? false : true);
        seekBar = (SeekBar) findViewById(R.id.SeekBar01);

        infoText.setMovementMethod(LinkMovementMethod.getInstance());

        addNoteButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (albumArt.getVisibility() == View.VISIBLE) {
                    albumArt.setVisibility(View.GONE);
                    addNoteLayout.setVisibility(View.VISIBLE);
                    infoScroll.setVisibility(View.GONE);
                    addNoteButton.setText(R.string.material_home);
                } else {
                    albumArt.setVisibility(View.VISIBLE);
                    addNoteLayout.setVisibility(View.GONE);
                    infoScroll.setVisibility(View.GONE);
                    addNoteButton.setText(R.string.material_pencil);
                }
            }
        });

        showInfoButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                albumArt.setVisibility(View.GONE);
                addNoteLayout.setVisibility(View.GONE);
                infoScroll.setVisibility(View.VISIBLE);
                infoText.setText(CommonStaticClass.getCurrentPodcast().description);

            }
        });

        infoScroll = (ScrollView) findViewById(R.id.infoScroll);

        //artistView = (TextView) findViewById(R.id.artistName);
        songTitleView = (TextView) findViewById(R.id.songTitle);
        musicCurLoc = (TextView) findViewById(R.id.curPosition);
        musicDuration = (TextView) findViewById(R.id.totalPosition);
        musicDuration.setText(CommonStaticClass.getCurrentPodcast().durationString);
        playPauseButton = (TextView) findViewById(R.id.playPauseButton);
        seekBar.setOnSeekBarChangeListener(this);
        saveNoteBt = (TextView) findViewById(R.id.saveNoteBT);
        lv = (ListView) findViewById(R.id.noteList);
        beginView = (TextView) findViewById(R.id.beginPos);
        noteEditText = (EditText) findViewById(R.id.noteEditText);
        noteEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focus) {
                if (focus) {
                    if (MusicService.getInstance(PlayerScreen.this) != null) {
                        if (isPlaying()) {
                            beginPosSelected = true;

                        } else {
                            beginPosInt = 0;
                        }
                        beginPosInt = getCurrentPosition();
                        noteBeginningBar.setProgress(beginPosInt);
                        String b = musicCurLoc.getText().toString();
                        beginView.setText(b);
                        markButton.setText("End Label");

                    }
                }
            }
        });
    }

    private void tryConnecting() {
        if (path.contains("http://") || path.contains("https://")) {
            progressDialog = ProgressDialog.show(con, "Loading...", "Please wait while loading mp3 file from url");
            Thread t = new Thread() {

                @Override
                public void run() {
                    boolean a = true;
                    while (a) {
                        if (maxToMark > 0) {
                            a = false;
                            Message msg = new Message();
                            msg.what = STOPSEEKING;
                            searchHandler.sendMessage(msg);
                            return;
                        }
                    }
                }

            };
            t.start();
            try {
                Thread.currentThread().join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void playPauseStream() {
        if (!isPlaying()) {
            // tryConnecting();
            Log.e("play button", "click");
            if (checkInPodcast(path)) {
                updateInPodcast(path);
            } else {
                insertInPodcast(path);
            }
            start();

            if (ps > 0) {
                currentPosition = ps;
                seekTo((ps * 1000));
                seekBar.setProgress((ps * 100000) / getDuration());
            } else {
                statisticHolder.startStatistic(CommonStaticClass.getCurrentPodcast().id, currentPosition);
            }
            isStopped = false;
            if (stopButSeeked) {
                seekTo(currentPosition);
            }
            playPauseButton.setText(R.string.material_pause);

        } else { // Unchecked -> Play icon visible
            pause();
            statisticHolder.endStatistic(currentPosition);
            MusicService.pausedByUser = true;
            playPauseButton.setText(R.string.material_play);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playerscreen);
        if (CommonStaticClass.streaming) {
            Log.e("streaming", "is playing from stream");
        }
        PlayerScreen.statisticHolder = new ListenStatisticHolder(this.getApplicationContext());

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        curVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(UE_ACTION);

        con = this;
        if (dbHelper == null) {
            dbHelper = new DBAccessor(this);
            dbHelper.open();
        }
        mainAudioUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Intent intent = getIntent();
        c = this;
        Bundle bundle = intent.getExtras();
        if (bundle == null || null == bundle.getString("mediapath")) {
            this.showMyDialog(getResources().getString(R.string.noitemselected));
            return;
        }
        path = intent.getExtras().getString("mediapath");
        if (intent.getExtras().get("fromnotification") != null) {
            PodcastData p = new PodcastData();
            p.durationString = intent.getExtras().getString("duration", "Unknown duration");
            p.publishDateLong = intent.getExtras().getLong("publishtime", 0L);
            p.editionTitle = intent.getExtras().getString("title", "Unknown title");
            p.catalogId = intent.getExtras().getInt("catalogid", 0);
            p.duration = intent.getExtras().getInt("duration", 0);
            p.url = path;

            List<PodcastData> dataList = new ArrayList<PodcastData>();
            dataList.add(p);
            if (null == dbHelper.getPodcastByUrl(p.url))
                dbHelper.bulkInsertPodcastData(dataList, p.catalogId);

            CommonStaticClass.setCurrentPodcast(p);
        }


        albumArt = (ImageView) findViewById(R.id.albumArt);
        String mediaArt = intent.getExtras().getString("mediaarturl");
        if (mediaArt != null && !mediaArt.equals("")) {
            ImageUtil.displayImage(albumArt, mediaArt, null);
        } else {

            long catalogId = CommonStaticClass.getCurrentPodcast().catalogId;
            CatalogData catalogData = dbHelper.getPodcastCatalogById(catalogId);
            if (catalogData != null)
                ImageUtil.displayImage(albumArt, catalogData.image, null);
            else {

                bmp = getAlbumart(c, mainAudioUri, path);
                if (bmp == null) {
                    Drawable d = getResources().getDrawable(R.drawable.images);
                    bmp = ((BitmapDrawable) d).getBitmap();
                }
                albumArt.setImageBitmap(fixAlbumSize(bmp));
            }
//		albumArt.setImageBitmap(bmp);
        }

        CommonStaticClass.currentSongPath = path;
        if (path.contains("http://") || path.contains("https://")) {
            showMyDialog("Connecting to the host...");
            CommonStaticClass.streaming = true;
            Log.e("streaming", "is playing from stream");
            PodcastData d = dbHelper.getPodcastByUrl(path);
            if (d != null)
                CommonStaticClass.setCurrentPodcast(d);
        } else {
            CommonStaticClass.streaming = false;

            File f = new File(path);
            File ownerFolder = f.getParentFile();
            currentIndexInFolder = 0;

            if (!intent.getBooleanExtra("stream", true)) {

				/*Find all sibling files for song navigation in the folder*/
                File[] files = ownerFolder.listFiles();
                for (int i = 0; i < files.length; i++) {
                    if (!files[i].isDirectory()) {
                        if (files[i].getAbsolutePath().contains(".mp3")) {
                            siblingPaths.add(files[i].getAbsolutePath());

                        }
                        if (files[i].getAbsolutePath().equals(path)) {
                            currentIndexInFolder = i;
                        }
                    }
                }
            }
        }

        loadGui();


        initializeThePlayer(getIntent());


        playPauseButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                playPauseStream();
            }
        });


        beginView.setText("Start");

        beginView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (MusicService.getInstance(PlayerScreen.this) != null) {
                    if (isPlaying()) {
                        beginPosSelected = true;

                    } else {
                        beginPosInt = 0;
                    }
                    beginPosInt = getCurrentPosition();
                    String b = musicCurLoc.getText().toString();
                    beginView.setText(b);
                    markButton.setText("End Label");

                }
            }

        });

        markButton.setText("Start Label");
        markButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (MusicService.getInstance(PlayerScreen.this) != null) {
                    if (isPlaying())
                        if (beginPosSelected) {
                            endPosSelected = true;
                            endPosInt = getCurrentPosition();

                            String e = musicCurLoc.getText().toString();

                        } else {
                            beginPosSelected = true;
                            beginPosInt = getCurrentPosition();

                            String b = musicCurLoc.getText().toString();
                            beginView.setText(b);
                            markButton.setText("End Label");
                        }
                }
            }
        });
        stopButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (MusicService.getInstance(PlayerScreen.this) != null) {
                    if (isPlaying()) {
                        MusicService.getInstance(PlayerScreen.this).stopMusic();

                    }
                    if (isPaused()) {
                        MusicService.getInstance(PlayerScreen.this).stopMusic();
                    }

                    isStopped = true;
                    stopButSeeked = false;
                }
            }
        });
        saveNoteBt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String note = "";
                if (noteEditText.getText().toString().length() > 0) {
                    note = noteEditText.getText().toString();
                } else {
                    note = "Bookmark at " + getAsTime(beginPosInt);
                }
                if (endPosInt == 0)
                    endPosInt = beginPosInt;
                saveState(beginPosInt, endPosInt,
                        note);
                noteEditText.setText("");
            }
        });
        lv.setOnItemClickListener(new ItemClickListener());
    }


//	private void addMarker(){
//		
//		TableLayout tl = (TableLayout) findViewById(R.id.playerTable);
//		ViewGroup layout = (ViewGroup) tl.findViewById(R.id.markerLayout);
//		layout.removeAllViews();
//		
//		rangSeekBar = new RangeSeekBar<Integer>(minToMark, maxToMark, con);
//		rangSeekBar.setNotifyWhileDragging(true);
//		rangSeekBar.setSelectedMaxValue(endPosInt);
//		rangSeekBar.setSelectedMinValue(beginPosInt);
//		rangSeekBar.setOnRangeSeekBarChangeListener(new OnRangeSeekBarChangeListener<Integer>() {
//	        @Override
//	        public void rangeSeekBarValuesChanged(Integer minValue, Integer maxValue) {
//	        		usingMarker= true;
//	        		beginPosInt = minValue;
//	        		endPosInt = maxValue;
//	        		beginMarkTime = getAsTime(minValue);
//	        		endMarkTime = getAsTime(maxValue);
//	        		beginView.setText(beginMarkTime);
//	        		endView.setText(endMarkTime);
//	        	// handle changed range values
////	                Log.i("PlayerScreen", "User selected new range values: MIN=" + minValue + ", MAX=" + maxValue);
//	        }
//		});
//		// add RangeSeekBar to pre-defined layout
//
//		layout.addView(rangSeekBar);
//		
////		TableLayout tl = (TableLayout) findViewById(R.id.playerTable);
////		ViewGroup layout = (ViewGroup) tl.findViewById(R.id.markerLayout);
////		layout.);
////		layout.addView(rangSeekBar);
//	}


    private View.OnClickListener onClickNext = new OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            skipToNextSong();
        }
    };
    private View.OnClickListener onClickPrev = new OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            skipToPrevSong();
        }
    };

    public void skipToNextSong() {
        if (CommonStaticClass.streaming) {
            Toast.makeText(c, "Playing from stream, you can not go next", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(c, "Skipping to next song...", Toast.LENGTH_SHORT).show();
            fromBackground = false;
            ps = -1;
            currentIndexInFolder = (currentIndexInFolder + 1) % siblingPaths.size();
            path = siblingPaths.get(currentIndexInFolder);

            Intent i = new Intent(c, PlayerScreen.class);
            i.putExtra("mediapath", path);
            i.putExtra("sppos", -1);
            i.putExtra("fromBackground", false);
            i.putExtra("isSongNavigation", true);
//    		i.putExtra("stream",false);
            startActivity(i);
            finish();
        }
    }

    public void skipToPrevSong() {
        if (CommonStaticClass.streaming) {
            Toast.makeText(c, "Playing from stream, you can not go previous", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(c, "Skipping to previous song...", Toast.LENGTH_SHORT).show();
            fromBackground = false;
            ps = -1;
            if (currentIndexInFolder - 1 < 0)
                currentIndexInFolder = siblingPaths.size() - 1;
            else
                currentIndexInFolder--;
            path = siblingPaths.get(currentIndexInFolder);

            Intent i = new Intent(c, PlayerScreen.class);
            i.putExtra("mediapath", path);
            i.putExtra("sppos", -1);
            i.putExtra("fromBackground", false);
            i.putExtra("isSongNavigation", true);
            // i.putExtra("itemname", checkFile.getName().substring(0, checkFile.getName().lastIndexOf(".")));
//        i.putExtra("stream",false);
            startActivity(i);
            finish();
        }

    }

    private void startPlaying() {

        String musicRedirectedPath = "";
        try {
            musicRedirectedPath = MusicService.followRedirectUrl(path);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (noteFromOtherScreen || isSongNavigation) {
            MusicService.setSongAndSongSpId(musicRedirectedPath, songTitle, ps, null);

        } else {
            MusicService.setSong(musicRedirectedPath, songTitle, null);
        }

        if (MusicService.getInstance(PlayerScreen.this) != null) {
            if (isPlaying() || isPaused()) {
                MusicService.getInstance(PlayerScreen.this).stopMusic();
            } else {
                MusicService.getInstance(PlayerScreen.this).initMediaPlayer();
            }
        }

        if (!isMyServiceRunning()) {

            songThread = new Thread(new Runnable() {

                @Override
                public void run() {
                    startService(new Intent("com.paperify.podmark.ACTION_PLAY_MUSIC"));
                    while (noteFromOtherScreen) {
                        if (MusicService.getInstance(PlayerScreen.this) != null) {
                            if (MusicService.getInstance(PlayerScreen.this).isPrepared()) {
                                stopButSeeked = true;
                                Log.e("ps", "" + ps);
                                noteSelect = audioFilePathFromNote(ps);
                                Message msg = new Message();
                                msg.what = NOTEUPDATE;
                                searchHandler.sendMessage(msg);
                                noteFromOtherScreen = false;
                            }
                        }
                    }
                }
            });
            songThread.start();
            MusicService.pausedByUser = false;
        }

        if (isSongNavigation) {

            if (checkInPodcast(path)) {
                updateInPodcast(path);
            } else {
                insertInPodcast(path);
            }
            start();

            onStart();
            if (stopButSeeked) {
                seekTo(currentPosition);
            }
            //     playPauseButton.setChecked(true);
            MusicService.getInstance(PlayerScreen.this).mState = MusicService.State.Playing;
            isStopped = false;
        }

    }

    public void initializeThePlayer(Intent intent) {
        artistName = CommonStaticClass.streaming ? CommonStaticClass.StreamFileAuthor : getAudioArtist(c, mainAudioUri, path);
//        artistView.setText(artistName);

        songTitle = CommonStaticClass.streaming ? CommonStaticClass.StreamFileTitle : getSongTitle(c, mainAudioUri, path);
        if (songTitle == null || songTitle.equals("")) {
            songTitle = intent.getStringExtra("filelabel");
        }

        final long podId = intent.getLongExtra("podid", 0);
        songTitleView.setText(CommonStaticClass.getCurrentPodcast().editionTitle);

        podcastID = getPodcastNumber(path);

        String tempPath = "";
        try {
            tempPath = URLEncoder.encode(path, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        fillData(path);
        ps = intent.getIntExtra("sppos", -1);

        CommonStaticClass.songspecificID = ps;
        if (ps > -1) {
            noteFromOtherScreen = true;
        }

        fromBackground = intent.getBooleanExtra("fromBackground", false);
        isSongNavigation = intent.getBooleanExtra("isSongNavigation", false);

        if (!fromBackground) {

            if (path.startsWith("NOACCESS")) {
                String[] s = path.split("-");
                String s2 = getResources().getString(R.string.noaccesstourl) + " " + s[1];
                Toast.makeText(con, R.string.noaccesstourl + " " + s2, Toast.LENGTH_LONG).show();
                return;
            }

            if (!path.startsWith("http")) {
                String extState = Environment.getExternalStorageState();
                if (!extState.equals(Environment.MEDIA_MOUNTED)) {
                    System.out.println("unmounted");
                }
                File f = new File(path.replaceAll("file:///", ""));
                if (!f.exists()) {
                    showConfirmDialog(getResources().getString(R.string.downloadedfiledeletedbefore),
                            getResources().getString(R.string.yes),
                            getResources().getString(R.string.no),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    PodcastData data = dbHelper.getPodcastById(podId);
                                    path = data.url;
                                    startPlaying();
                                    playPauseStream();

                                }
                            }
                            , null);
                    return;
                }
            }

            startPlaying();

            if (MusicService.getInstance(PlayerScreen.this) != null)
                MusicService.getInstance(PlayerScreen.this).getMediaPlayer().setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        hideDialog();
                    }
                });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (path == null)
            return;

        CommonStaticClass.playerScreenFocused = true;
        plTherad = new PlayerThread();
        Thread UIPlayerThread = new Thread(plTherad, "UIPlayerThread");
        plTherad.startThread();
        UIPlayerThread.start();
        if (path.contains("http://") || path.contains("https://")) {
            //	progressDialog = ProgressDialog.show(con, "Loading...", "Please wait while loading mp3 file from url");
            new Thread() {

                @Override
                public void run() {
                    boolean a = true;
                    while (a) {
                        if (maxToMark > 0) {
                            a = false;
                            Message msg = new Message();
                            msg.what = STOPSEEKING;
                            searchHandler.sendMessage(msg);
                        }
                    }
                }

            }.start();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        CommonStaticClass.playerScreenFocused = false;
        if (plTherad == null) {
            return;
        }
        plTherad.stopThread();
        if (null != songThread && songThread.isAlive())
            songThread.stop();
    }


    private boolean isMyServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (RunningServiceInfo service : manager
                .getRunningServices(Integer.MAX_VALUE)) {
            if ("com.paperify.podmark.player.MusicService".equals(service.service
                    .getClassName())) {
                return true;
            }
        }
        return false;
    }

    private String getAsTime(int dur) {
        String a = "";
        sec = (dur / 1000);

        sec = (sec % (60));
        min = (dur / MINUTE_DIVIDER);
        a = String.format("%02d:%02d", min, sec);

        if (min > 59) {
            sec = (sec % (60));
            min = (min % (60));
            hour = (dur / HOUR_DIVIDER);
            a = String.format("%02d:%02d:%02d", hour, min, sec);
        }

        return a;
    }

    private boolean checkInPodcast(String path) {
        String sql = "Select * from podcast where full_device_path='" + path
                + "'";
        Cursor mCursor = null;
        try {
            mCursor = dbHelper.executeQuery(sql);
            if (mCursor.moveToFirst()) {
                do {
                    String fullSongPath = mCursor.getString(mCursor
                            .getColumnIndex("full_device_path"));
                    if (fullSongPath.equalsIgnoreCase(path)) {
                        return true;
                    }
                } while (mCursor.moveToNext());
            }
        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            if (null != mCursor)
                mCursor.close();
        }

        return false;
    }

    private void updateInPodcast(String path) {
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

        String currentNowMil = now + "";
        long id = dbHelper.updatePodcastEntry(path, dd.toString(),
                currentNowMil);
        if (id > 0) {
            Log.e("Podcast update", "Successful");
        }
    }

    private void insertInPodcast(String path) {
        DateFormat parser = new SimpleDateFormat("dd MM yyyy hh:mm:ss");
        long now = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(now);
        Date dd = null;
        try {
            dd = parser.parse(parser.format(calendar.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String currentNowMil = now + "";

        String podcastIncr = (dbHelper.getPodcastCount() + 1) + "";
        String filename = songTitle;

        /*if((filename == null || filename.equals("")) && path.contains("http:") || path.contains("https://"))
        {
            if(path.contains("/") && path.contains("."))
                filename = path.substring(path.lastIndexOf("/"), path.lastIndexOf("."));
        }
        else{
        filename = path.substring(path.lastIndexOf("/") + 1,
				path.length());
        }*/
        filename = songTitle;
        // String sql =
        // "Insert into podcast (podcast_id,full_device_path,file_name,download_link,last_listen_date,last_listen_date_mil,create_date) Values('"+podcastIncr+"','"+path+"','"+filname+"','','"+dd.toString()+"','"+currentNowMil+"','"+dd.toString()+"')";
        long id = dbHelper.createPodcastEntry(podcastIncr, 0, path, filename, "",
                dd.toString(), currentNowMil, dd.toString());
        if (id > -1) {
            Log.e("Podcast entry", "Successful");
        }
    }


    private long getPodcastNumber(String path) {
        String sql = "";

        if (path.startsWith("http"))
            sql = "Select _id from podcast where download_link = '" + path + "'";
        else
            sql = "Select _id from podcast where full_device_path = '" + path + "'";

        Cursor mCursor = null;
        long num = -1l;
        try {
            mCursor = dbHelper.executeQuery(sql);
            if (mCursor.moveToFirst()) {
                do {
                    num = mCursor.getLong(mCursor.getColumnIndex("_id"));
                    return num;
                } while (mCursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d("hata", e.getLocalizedMessage());
            // TODO: handle exception
        } finally {
            if (null != mCursor)
                mCursor.close();
        }

        return num;
    }

    public Bitmap getAlbumart(Context context, Uri audioUri, String path) {
        Cursor audioCursor = null;
        // int i=0;
        Long album_id = null;
        try {
            String[] projection = {MediaStore.Audio.Media.DATA,
                    MediaStore.Audio.Media.ALBUM_ID};
            audioCursor = c.getContentResolver().query(audioUri, projection,
                    null, null, null);
            if (audioCursor.moveToFirst()) {
                do {
                    // if(pos==i){
                    String audioFileALBUM_ID = audioCursor
                            .getString(audioCursor
                                    .getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
                    String audioFilePath = audioCursor.getString(audioCursor
                            .getColumnIndex(MediaStore.Audio.Media.DATA));
                    if (path.equalsIgnoreCase(audioFilePath))
                        album_id = new Long(audioFileALBUM_ID);
                    // }
                    // i++;
                } while (audioCursor.moveToNext());
            }
        } finally {
            if (audioCursor != null) {
                audioCursor.close();
            }
        }
        Bitmap bm = null;
        try {
            final Uri sArtworkUri = Uri
                    .parse("content://media/external/audio/albumart");

            Uri uri = ContentUris.withAppendedId(sArtworkUri, album_id);

            ParcelFileDescriptor pfd = c.getContentResolver()
                    .openFileDescriptor(uri, "r");

            if (pfd != null) {
                FileDescriptor fd = pfd.getFileDescriptor();
                bm = BitmapFactory.decodeFileDescriptor(fd);
            }
        } catch (Exception e) {
        }
        return bm;
    }

    private Bitmap fixAlbumSize(Bitmap bm) {
        // TODO Auto-generated method stub
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int newWidth = dm.widthPixels / 5;
        int newHeight = newWidth + 10;
        int width = bm.getWidth();
        int height = bm.getHeight();

        float scaleWidth = ((float) newWidth) / width;

        float scaleHeight = ((float) newHeight) / height;

        // create a matrix for the manipulation

        Matrix matrix = new Matrix();

        // resize the bit map

        matrix.postScale(scaleWidth, scaleHeight);

        // recreate the new Bitmap

        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);

        return resizedBitmap;
    }

    class ItemClickListener implements OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {
            // pos = pos;
            Message msg = new Message();
            Note note = (Note) parent.getItemAtPosition(pos);
            Bundle bundle = new Bundle();
            bundle.putString("filepath", note.getAudioPath());
            bundle.putInt("beginsecond", note.getBeginSec());
            msg.setData(bundle);
            stopButSeeked = true;
            playPauseButton.setText(R.string.material_pause);

            msg.what = NOTEUPDATE;
            searchHandler.sendMessage(msg);
        }

    }

    private void saveState(int beginpos, int endpos, String notetext) {
        long podnumber = podcastID;
        if (podcastID <= 0)
            podnumber = getPodcastNumber(path);
        long id = dbHelper.createNote(
                podnumber,
                numberOfNotePerMedia++,
                beginpos / 1000,
                endpos / 1000,
                path, notetext,
                "me", NoteUtil.getCurrentDate(), new Date().getTime(), "");
        if (path.contains("http://")) {
            Log.e("in save state streaming", "is playing from stream");
            String author = "anonymous";
            if (CommonStaticClass.twitterLogin) {
                author = CommonStaticClass.twitterUserId;
            } else if (CommonStaticClass.facebookLogin) {
                author = CommonStaticClass.facebookUserId;
            }
           /* new AsyncTask<String, Void, ResponseHolder>() {

                @Override
                protected ResponseHolder doInBackground(String... args) {
                    noteAction(PlayerScreen.SAVE, path, args[0], args[1], args[2], args[3], null, null);
                    return new ResponseHolder();
                }


            }.execute(author, beginpos, endpos, notetext);*/
        }
        beginPosSelected = false;
        endPosSelected = false;
        beginView.setText("Start");

        markButton.setText("Start Label");
        Message msg = new Message();
        msg.what = UPDATE;
        searchHandler.sendMessage(msg);
        Toast.makeText(c,
                "Your bookmark has been saved",
                Toast.LENGTH_SHORT).show();

    }

  /*  private void noteAction(String action, String path, String author, String beginpos,
                            String endpos, String noteText, String likeStart, String likeEnd) {
        // TODO Auto-generated method stub
        Log.e("in note action streaming", "is playing from stream");
        String xml = null;
        if (action.equalsIgnoreCase(PlayerScreen.LIKE)) {
            xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?><likes><like><likeStart>" + likeStart + "</likeStart><likeEnd>" + likeEnd + "</likeEnd></like></likes>";
        }
        if (action.equalsIgnoreCase(PlayerScreen.SAVE)) {
            xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?><notes><note><noteStart>" + beginpos + "</noteStart><noteEnd>" + endpos + "</noteEnd><noteText>" + noteText + "</noteText></note></notes>";
        }

        HttpClient httpclient = new DefaultHttpClient();
        try {
            String url = action.equalsIgnoreCase(PlayerScreen.LIKE) ? "http://107.170.10.28:8080/api/podmarkapi/saveLikes/upload.xml" : "http://107.170.10.28:8080/api/podmarkapi/uploadNotes/upload.xml";
            HttpPost httppost = new HttpPost(url);

            List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
            postParameters.add(new BasicNameValuePair("downloadLink", path));
            postParameters.add(new BasicNameValuePair("author", author));
            postParameters.add(new BasicNameValuePair("xml", xml));

            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(
                    postParameters, "UTF-8");
            formEntity.setContentType("application/x-www-form-urlencoded");
            httppost.setEntity(formEntity);

            Log.v("executing request ", "" + httppost.getRequestLine());
            HttpResponse response = httpclient.execute(httppost);
//            while(httpclient.execute(httppost))
            HttpEntity resEntity = response.getEntity();

            Log.v("", "----------------------------------------");
            Log.v("response.getStatusLine()", "" + response.getStatusLine());
            if (resEntity != null) {
                Log.e("Response content length: ", "" + resEntity.getContentLength());
                InputStream is = resEntity.getContent();
                if (is != null) {
                    Writer writer = new StringWriter();

                    char[] buffer = new char[1024];
                    try {
                        Reader reader = new BufferedReader(
                                new InputStreamReader(is, "UTF-8"));
                        int n;
                        while ((n = reader.read(buffer)) != -1) {
                            writer.write(buffer, 0, n);
                        }
                    } finally {
                        is.close();
                    }
                    Log.v("Response content: ", "" + writer.toString());

//                        getResponseData(writer.toString());
                } else {
                    Log.v("Response nothing: ", "nothing");
                }

            }
            EntityUtils.consume(resEntity);
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                httpclient.getConnectionManager().shutdown();
            } catch (Exception ignore) {
            }
        }
    }
    */



    private void fillData(String path) {

        List<Note> noteList = dbHelper.fetchAllNotes(podcastID);
        NoteAdapter adapter = new NoteAdapter(PlayerScreen.this.getApplicationContext(), noteList, this, CommonStaticClass.getCurrentPodcast().getIsDownloaded());
        lv.setAdapter(adapter);


        /*lv.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v,
                                            ContextMenuInfo menuInfo) {
                // TODO Auto-generated method stub
                menu.setHeaderTitle("Choose To?");
                menu.add(0, CONTEXTMENU_UPLOAD, 0, "Upload and Share");
                menu.add(0, CONTEXTMENU_DELETE, 0, "Delete Bookmark");
            }
        });*/
    }


    public void startPlayProgressUpdater() {
        int cur = getCurrentPosition();

        // ends the stream if note has an end point
        /*if (cur >= ep) {
            if (selectionPlaying) {
				Log.e("it's here","kjkdjskfds");
				pause();
			}

		}*/
        if (isPlaying()) {
            Runnable notification = new Runnable() {
                public void run() {
                    startPlayProgressUpdater();
                }
            };

            handler.postDelayed(notification, 1000);
        }
    }

    private void seekChange(View v) {
        if (MusicService.getInstance(PlayerScreen.this).isPlaying()) {
            SeekBar sb = (SeekBar) v;
            MusicService.getInstance(PlayerScreen.this).seekMusicTo(sb.getProgress());
        }
    }

    public TYPE getTypeDescription(File f) {
        String extension = Utils.getExtension(f);
        TYPE type = null;

        if (extension != null) {
            if (extension.equals(Utils.mp3)) {
                type = TYPE.AUDIO;
            }
        }
        return type;
    }

    public String audioFilePath(Context context, Uri audioUri, int pos) {
        Cursor audioCursor = null;
        int i = 0;

        try {
            String[] projection = {MediaStore.Audio.Media.DATA};
            audioCursor = context.getContentResolver().query(audioUri,
                    projection, null, null, null);
            if (audioCursor.moveToFirst()) {
                do {
                    if (pos == i) {
                        String audioFilePath = audioCursor
                                .getString(audioCursor
                                        .getColumnIndex(MediaStore.Audio.Media.DATA));
                        return audioFilePath;
                    }
                    i++;
                } while (audioCursor.moveToNext());
            }
        } finally {
            if (audioCursor != null) {
                audioCursor.close();
            }
        }

        return null;
    }

    public String getAudioArtist(Context context, Uri audioUri, String path) {
        Cursor audioCursor = null;
        // int i=0;

        try {
            String[] projection = {MediaStore.Audio.Media.DATA,
                    MediaStore.Audio.Media.ARTIST};
            audioCursor = context.getContentResolver().query(audioUri,
                    projection, null, null, null);
            if (audioCursor.moveToFirst()) {
                do {
                    // if(pos==i){
                    String audioAritst = audioCursor.getString(audioCursor
                            .getColumnIndex(MediaStore.Audio.Media.ARTIST));
                    String audioFilePath = audioCursor.getString(audioCursor
                            .getColumnIndex(MediaStore.Audio.Media.DATA));
                    if (path.equalsIgnoreCase(audioFilePath))
                        return audioAritst;
                    // }
                    // i++;
                } while (audioCursor.moveToNext());
            }
        } finally {
            if (audioCursor != null) {
                audioCursor.close();
            }
        }

        return null;
    }

    public String getSongTitle(Context context, Uri audioUri, String path) {
        Cursor audioCursor = null;
        // int i=0;

        try {
            String[] projection = {MediaStore.Audio.Media.DATA,
                    MediaStore.Audio.Media.TITLE};
            audioCursor = context.getContentResolver().query(audioUri,
                    projection, null, null, null);
            if (audioCursor.moveToFirst()) {
                do {
                    // if(pos==i){
                    String audioAritst = audioCursor.getString(audioCursor
                            .getColumnIndex(MediaStore.Audio.Media.TITLE));
                    String audioFilePath = audioCursor.getString(audioCursor
                            .getColumnIndex(MediaStore.Audio.Media.DATA));
                    if (path.equalsIgnoreCase(audioFilePath))
                        return audioAritst;
                    // }
                    // i++;
                } while (audioCursor.moveToNext());
            }
        } finally {
            if (audioCursor != null) {
                audioCursor.close();
            }
        }

        return null;
    }

    public void loadThumb(int mediaID) {
        String[] proj = {MediaStore.Audio.Media._ID};
        Cursor c = managedQuery(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                proj, MediaStore.Audio.Media._ID + "=" + mediaID, null, null);

        if (c != null && c.moveToFirst()) {
            Uri thumb = Uri.withAppendedPath(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, c.getLong(0)
                            + "");
            albumArt.setImageURI(thumb);
        }

    }

    private Handler searchHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case STOPSEEKING:
                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    break;
                case FOCUSCHANGE:
                    noteEditText.setFocusable(true);
                    break;
                case UPDATE:
                    fillData(path);
                    beginPosInt = 0;
                    endPosInt = 0;
                    if (CommonStaticClass.streaming) {
                        Log.e("CommonStatic.streaming", CommonStaticClass.streaming + "");
                    } else {
                        Log.e("CommonStatic.streaming", CommonStaticClass.streaming + "");
                    }
                    break;
                case NOTEUPDATE:
                    String rowID = "0";
                    String begPos = "0";
                    String enPos = "0";
                    String nText = "";
                    if (noteSelect != null) {
                        rowID = noteSelect.get("rowID");
                        enPos = noteSelect.get("endPos");
                        nText = noteSelect.get("noteText");
                        bp = Float.parseFloat(begPos);
                    } else {

                        bp = (Integer) msg.getData().get("beginsecond") * 1000;

                    }
//					String filePath = noteSelect.get("audioFilePath");

                    ep = Float.parseFloat(enPos);
                    float bpSec = bp / 1000;
                    float epSec = ep / 1000;
                    beginView.setText(getAsTime((int) bpSec));

                    noteEditText.setText(nText);
                    DateFormat parser = new SimpleDateFormat(
                            "dd MM yyyy hh:mm:ss");
                    long now = System.currentTimeMillis();
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(now);
                    Date dd = null;
                    try {
                        dd = parser.parse(parser.format(calendar.getTime()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    dbHelper.updateLastListenDate(Long.parseLong(rowID),
                            dd.toString(), now + "");
                    start();
                    isStopped = false;
                    if (stopButSeeked) {
                        seekTo((int) bp);
                    }
                    selectionPlaying = true;
                    startPlayProgressUpdater();
                    break;
            }


        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        AppEventsLogger.deactivateApp(this);
        CommonStaticClass.pressedHomeWhileInPlayer = true;
		/*if(dbHelper!=null){
			dbHelper.close();
			dbHelper = null;
		}        */
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppEventsLogger.activateApp(this);
//		if(!CommonStaticClass.playerScreenPaused){
//			finish();
//		}
//		CommonStaticClass.playerScreenPaused = false;
//		registerReceiver(mIntentReceiver, mIntentFilter);
//		if(fromBackground){
////			checkActivityForeground();
//			
//			finish();
//		}
//		if(CommonStaticClass.pressedHomeWhileInPlayer){
//			finish();
//		}
        if (MusicService.getInstance(PlayerScreen.this) != null) {
            if (isPlaying()) {
                currentPosition = getCurrentPosition();
                seekTo(currentPosition);
            }
        }
        CommonStaticClass.playerShowing = true;
        firstPlay = false;

    }

    protected void checkActivityForeground() {
//		Log.d(TAG, "start checking for Activity in foreground");
        Intent intent = new Intent();
        intent.setAction(PlayerScreen.UE_ACTION);
        sendOrderedBroadcast(intent, null, new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                int result = getResultCode();

                if (result != Activity.RESULT_CANCELED) { // Activity caught it
//					Log.d(TAG, "An activity caught the broadcast, result " + result);
                    activityInForeground();
                    return;
                }
//				Log.d(TAG, "No activity did catch the broadcast.");
                noActivityInForeground();
            }
        }, null, Activity.RESULT_CANCELED, null, null);
    }

    protected void activityInForeground() {

    }

    protected void noActivityInForeground() {

    }

    public static final String UE_ACTION = "com.podmark.player.inforeground";
    private BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(UE_ACTION)) {
                Log.d("UESurveilanceActivity", "i'm in the foreground");
                this.setResultCode(Activity.RESULT_OK);
            }
        }
    };

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }

    @Override
    public int getBufferPercentage() {
        if (MusicService.getInstance(PlayerScreen.this) != null) {
            return MusicService.getInstance(PlayerScreen.this).getBufferPercentage();
        }
        return 0;
    }

    @Override
    public int getCurrentPosition() {
        if (MusicService.getInstance(PlayerScreen.this) != null) {
            return MusicService.getInstance(PlayerScreen.this).getCurrentPosition();
        }
        return 0;
    }

    @Override
    public int getDuration() {
        if (MusicService.getInstance(PlayerScreen.this) != null) {
            return MusicService.getInstance(PlayerScreen.this).getMusicDuration();
        }
        return 0;
    }

    @Override
    public boolean isPlaying() {
        if (MusicService.getInstance(PlayerScreen.this) != null) {
            return MusicService.getInstance(PlayerScreen.this).isPlaying();
        }
        return false;
    }

    public boolean isPaused() {
        if (MusicService.getInstance(PlayerScreen.this) != null) {
            return MusicService.getInstance(PlayerScreen.this).isPaused();
        }
        return false;
    }

    @Override
    public void pause() {
        if (MusicService.getInstance(PlayerScreen.this) != null) {
            MusicService.getInstance(PlayerScreen.this).pauseMusic();
        }
    }

    @Override
    public void seekTo(int pos) {
        if (MusicService.getInstance(PlayerScreen.this) != null) {
            MusicService.getInstance(PlayerScreen.this).seekMusicTo(pos);
            // save previous listen range
            if (isPlaying()) {
                PlayerScreen.statisticHolder.endStatistic(currentPosition);
            }
            currentPosition = pos;

            if (isPlaying())
                PlayerScreen.statisticHolder.startStatistic(CommonStaticClass.getCurrentPodcast().id, currentPosition);

            if (isStopped) {
                stopButSeeked = true;
            }
        }

    }

    @Override
    public void start() {
        if (MusicService.getInstance(PlayerScreen.this) != null) {
            Log.e("startMusic", "calling");
            MusicService.getInstance(PlayerScreen.this).startMusic();
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            seekTo(progress);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub

    }

}
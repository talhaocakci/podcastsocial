package com.javathlon.video;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.javathlon.CommonStaticClass;
import com.javathlon.ItemNavigationActivity;
import com.javathlon.NoteUtil;
import com.javathlon.R;
import com.javathlon.db.DBAccessor;
import com.javathlon.model.ListenStatisticHolder;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class VideoScreen extends Activity implements OnSeekBarChangeListener, Callback, OnPreparedListener, OnCompletionListener, OnBufferingUpdateListener,
        OnClickListener, OnSeekCompleteListener, AnimationListener {
    private TextView textViewPlayed;
    private TextView textViewLength, newNoteTextView;
    private Button markButton;
    private SeekBar seekBarProgress;
    private SurfaceView surfaceViewFrame;
    private ImageView imageViewPauseIndicator;
    private MediaPlayer player;
    private SurfaceHolder holder;
    private ProgressBar progressBarWait;
    private Timer updateTimer;
    private Bundle extras;
    private Animation hideMediaController;
    private LinearLayout linearLayoutMediaController;
    private static final String TAG = "Video";
    protected DBAccessor dbHelper = null;
    String filePath;
    long fileId;
    long sppos = 0;
    int notePosition = 0;
    private boolean isEnteringNote = false;
    static ListenStatisticHolder statisticHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.videoscreen);
        extras = getIntent().getExtras();

        if (dbHelper == null) {
            dbHelper = new DBAccessor(this);
            dbHelper.open();
        }

        linearLayoutMediaController = (LinearLayout) findViewById(R.id.linearLayoutMediaController);
        linearLayoutMediaController.setVisibility(View.GONE);

        hideMediaController = AnimationUtils.loadAnimation(this, R.anim.disapearing);
        hideMediaController.setAnimationListener(this);

        imageViewPauseIndicator = (ImageView) findViewById(R.id.imageViewPauseIndicator);
        imageViewPauseIndicator.setVisibility(View.GONE);
        if (player != null) {
            if (!player.isPlaying()) {
                imageViewPauseIndicator.setVisibility(View.VISIBLE);
            }
        }

        markButton = (Button) findViewById(R.id.markBtn);

        textViewPlayed = (TextView) findViewById(R.id.textViewPlayed);
        textViewLength = (TextView) findViewById(R.id.textViewLength);

        newNoteTextView = (TextView) findViewById(R.id.newNoteTextView);

        surfaceViewFrame = (SurfaceView) findViewById(R.id.surfaceViewFrame);
        surfaceViewFrame.setOnClickListener(this);
        surfaceViewFrame.setClickable(false);

        seekBarProgress = (SeekBar) findViewById(R.id.seekBarProgress);
        seekBarProgress.setOnSeekBarChangeListener(this);
        seekBarProgress.setProgress(0);

        progressBarWait = (ProgressBar) findViewById(R.id.progressBarWait);

        holder = surfaceViewFrame.getHolder();
        holder.addCallback(this);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        player = new MediaPlayer();
        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.setOnBufferingUpdateListener(this);
        player.setOnSeekCompleteListener(this);
        player.setScreenOnWhilePlaying(true);

        statisticHolder = new ListenStatisticHolder(this.getApplicationContext());
    }


    private void playVideo() {


        filePath = extras.getString("video_item");

        long id = extras.getLong("video_item_id");

        sppos = extras.getInt("sppos", 0);


        fileId = id;

        dbHelper.updateDownloadLink(id, filePath);

        player.setDisplay(holder);

        if (filePath.equals("VIDEO_URI")) {
            showToast("Please, set the video URI in HelloAndroidActivity.java in onClick(View v) method");
        } else {
            new Thread(new Runnable() {
                public void run() {
                    try {
                        player.setDataSource(filePath);
                        player.prepareAsync();
                    } catch (IllegalArgumentException e) {
                        showToast("Error while playing video");
                        e.printStackTrace();
                    } catch (IllegalStateException e) {
                        showToast("Error while playing video");
                        e.printStackTrace();
                    } catch (IOException e) {
                        showToast("Error while playing video. Please, check your network connection.");
                        e.printStackTrace();
                    }
                }
            }).start();

        }
    }

    public void markTime(final View view) {
        notePosition = player.getCurrentPosition();
        markButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNote(view);
            }
        });
        markButton.setText("Save");
        newNoteTextView.setVisibility(View.VISIBLE);
        isEnteringNote = true;
    }

    public void saveNote(final View view) {
        NoteUtil.saveNote(view.getContext(), filePath, fileId, notePosition, notePosition, newNoteTextView.getText().toString());
        markButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                markTime(view);
            }
        });
        markButton.setText("Mark");
        newNoteTextView.setVisibility(View.GONE);
        isEnteringNote = false;
    }

    public void nextItem(final View view) {

    }

    public void prevItem(final View view) {

    }

    private void showToast(final String string) {
        runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(VideoScreen.this, string, Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    private void hideMediaController() {
        new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(5000);
                    runOnUiThread(new Runnable() {
                        public void run() {
                            linearLayoutMediaController.startAnimation(hideMediaController);
                        }
                    });
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (!fromUser) {
            textViewPlayed.setText(VidUtils.durationInSecondsToString(progress));
        }
    }

    public void onStartTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub
    }

    public void onStopTrackingTouch(SeekBar seekBar) {
        if (player.isPlaying()) {
            progressBarWait.setVisibility(View.VISIBLE);
            player.seekTo(seekBar.getProgress() * 1000);
            Log.i(TAG, "======== SeekTo : " + seekBar.getProgress());
        }
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // TODO Auto-generated method stub

    }

    public void surfaceCreated(SurfaceHolder holder) {
        playVideo();
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // TODO Auto-generated method stub

    }

    public void onPrepared(MediaPlayer mp) {
        int duration = mp.getDuration() / 1000; // duration in seconds

        CommonStaticClass.getCurrentPodcast().duration = duration;

        seekBarProgress.setMax(duration);
        textViewLength.setText(VidUtils.durationInSecondsToString(duration));
        progressBarWait.setVisibility(View.GONE);

        // Get the dimensions of the video
        int videoWidth = player.getVideoWidth();
        int videoHeight = player.getVideoHeight();
        float videoProportion = (float) videoWidth / (float) videoHeight;
        Log.i(TAG, "VIDEO SIZES: W: " + videoWidth + " H: " + videoHeight + " PROP: " + videoProportion);

        // Get the width of the screen
        int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
        int screenHeight = getWindowManager().getDefaultDisplay().getHeight();
        float screenProportion = (float) screenWidth / (float) screenHeight;
        Log.i(TAG, "VIDEO SIZES: W: " + screenWidth + " H: " + screenHeight + " PROP: " + screenProportion);

        // Get the SurfaceView layout parameters
        android.view.ViewGroup.LayoutParams lp = surfaceViewFrame.getLayoutParams();

        if (videoProportion > screenProportion) {
            lp.width = screenWidth;
            lp.height = (int) ((float) screenWidth / videoProportion);
        } else {
            lp.width = (int) (videoProportion * (float) screenHeight);
            lp.height = screenHeight;
        }

        // Commit the layout parameters
        surfaceViewFrame.setLayoutParams(lp);

        // Start video
        player.seekTo((int) sppos);
        if (!player.isPlaying()) {
            player.start();
            updateMediaProgress();
            linearLayoutMediaController.setVisibility(View.VISIBLE);
            hideMediaController();
        }
        surfaceViewFrame.setClickable(true);

    }

    public void onCompletion(MediaPlayer mp) {
        //mp.stop();
        if (updateTimer != null) {
            updateTimer.cancel();
        }
        if (mp.getCurrentPosition() > 0) {
            statisticHolder.endStatistic(player.getCurrentPosition());
            Intent i = new Intent(this, ItemNavigationActivity.class);
            i.setFlags(i.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(i);
        }
        // finish();
    }

    /**
     * Change progress of mediaController
     */
    private void updateMediaProgress() {
        updateTimer = new Timer("progress Updater");
        updateTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        seekBarProgress.setProgress(player.getCurrentPosition() / 1000);
                    }
                });
            }
        }, 0, 1000);
    }

    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        int progress = (int) ((float) mp.getDuration() * ((float) percent / (float) 100));
        seekBarProgress.setSecondaryProgress(progress / 1000);
    }

    public void onClick(View v) {
        if (v.getId() == R.id.surfaceViewFrame) {
            if (linearLayoutMediaController.getVisibility() == View.GONE) {
                linearLayoutMediaController.setVisibility(View.VISIBLE);
                if (!isEnteringNote)
                    hideMediaController();
            } else if (player != null) {
                if (player.isPlaying()) {
                    player.pause();
                    imageViewPauseIndicator.setVisibility(View.VISIBLE);
                    statisticHolder.endStatistic(player.getCurrentPosition());
                } else {
                    player.start();
                    imageViewPauseIndicator.setVisibility(View.GONE);
                    statisticHolder.startStatistic(CommonStaticClass.getCurrentPodcast().id, player.getCurrentPosition());
                }
            }
        }
    }

    public void onSeekComplete(MediaPlayer mp) {
        progressBarWait.setVisibility(View.GONE);
    }

    public void onAnimationEnd(Animation animation) {
        // TODO Auto-generated method stub

    }

    public void onAnimationRepeat(Animation animation) {
        // TODO Auto-generated method stub

    }

    public void onAnimationStart(Animation animation) {
        linearLayoutMediaController.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        try {
            player.stop();
            statisticHolder.endStatistic(player.getCurrentPosition());
        } catch (Exception e) {
            // TODO: handle exception
        }
        super.onBackPressed();
    }
}

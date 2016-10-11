package com.javathlon.upload;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.javathlon.BaseActivity;
import com.javathlon.R;
import com.javathlon.player.PlayerScreen;

public class UploadController extends BaseActivity {
    public static String UE_ACTION_UPLOAD = "com.cse15th.app.mpnote.upload.inforeground";
    public static String UE_UPDATE_UPLOAD = "com.cse15th.app.mpnote.upload.updateview";
    protected static final int UPDATEVIEWS = 1;
    protected int counter = 1;
    private IntentFilter mIntentFilter;
    String updateText;
    TextView t1, t2, t3, t4, t5;
    int percent = 0;
    int viewnum = 0;
    ProgressBar progress1, progress2, progress3, progress4, progress5;
    LinearLayout uploader1, uploader2, uploader3, uploader4, uploader5;
    Button pause1, pause2, pause3, pause4, pause5, cancel1, cancel2, cancel3,
            cancel4, cancel5;
    UploadTask[] dwq;
    private BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            String action = intent.getAction();
            if (action.equals(UE_ACTION_UPLOAD)) {
                // Log.d("DownLoadController","i'm in the foreground");
                this.setResultCode(Activity.RESULT_OK);
            }
            if (action.equals(UE_UPDATE_UPLOAD)) {
                // Log.d("DownLoadController","i'm in the foreground");
                this.setResultCode(Activity.RESULT_OK);
                String update = intent.getStringExtra("upatetext");
                updateText = update + " of "
                        + intent.getStringExtra("FILE_NAME");
                percent = intent.getIntExtra("PERCENT_COMPLETE", -1);
                // Log.e("",""+percent);
                viewnum = intent.getIntExtra("NOTIFICATION_ID", -1);
                // Log.e("",""+viewnum);
                Message msg = new Message();
                msg.what = UPDATEVIEWS;
                searchHandler.sendMessage(msg);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        LoadGui();
        hideDownLoaders();
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(UE_ACTION_UPLOAD);
        mIntentFilter.addAction(UE_UPDATE_UPLOAD);
    }

    private void LoadGui() {
        // TODO Auto-generated method stub
        uploader1 = (LinearLayout) findViewById(R.id.downloader1);
        uploader2 = (LinearLayout) findViewById(R.id.downloader2);
        uploader3 = (LinearLayout) findViewById(R.id.downloader3);
        uploader4 = (LinearLayout) findViewById(R.id.downloader4);
        uploader5 = (LinearLayout) findViewById(R.id.downloader5);
        t1 = (TextView) findViewById(R.id.t1);
        t2 = (TextView) findViewById(R.id.t2);
        t3 = (TextView) findViewById(R.id.t3);
        t4 = (TextView) findViewById(R.id.t4);
        t5 = (TextView) findViewById(R.id.t5);
        progress1 = (ProgressBar) findViewById(R.id.progressBar1);
        progress2 = (ProgressBar) findViewById(R.id.progressBar2);
        progress3 = (ProgressBar) findViewById(R.id.progressBar3);
        progress4 = (ProgressBar) findViewById(R.id.progressBar4);
        progress5 = (ProgressBar) findViewById(R.id.progressBar5);
        progress1.setMax(100);
        progress2.setMax(100);
        progress3.setMax(100);
        progress4.setMax(100);
        progress5.setMax(100);
        pause1 = (Button) findViewById(R.id.pause1);
        pause2 = (Button) findViewById(R.id.pause2);
        pause3 = (Button) findViewById(R.id.pause3);
        pause4 = (Button) findViewById(R.id.pause4);
        pause5 = (Button) findViewById(R.id.pause5);
        cancel1 = (Button) findViewById(R.id.cancel1);
        cancel2 = (Button) findViewById(R.id.cancel2);
        cancel3 = (Button) findViewById(R.id.cancel3);
        cancel4 = (Button) findViewById(R.id.cancel4);
        cancel5 = (Button) findViewById(R.id.cancel5);
    }

    private void hideDownLoaders() {
        // TODO Auto-generated method stub
        uploader1.setVisibility(View.GONE);
        uploader2.setVisibility(View.GONE);
        uploader3.setVisibility(View.GONE);
        uploader4.setVisibility(View.GONE);
        uploader5.setVisibility(View.GONE);
    }

    private Handler searchHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATEVIEWS:
                    if (viewnum == 1) {
                        final UploadTask dw1 = PlayerScreen.uploadmap.get(viewnum);
                        if (!dw1.getUploadCancel())
                            uploader1.setVisibility(View.VISIBLE);
                        t1.setText(updateText);
                        progress1.setProgress(percent);

                        cancel1.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                // TODO Auto-generated method stub
                                uploader1.setVisibility(View.GONE);
                                dw1.setUploadPause(true);
                                dw1.setUploadCancel(true, viewnum);

                            }
                        });
                        pause1.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                if (!dw1.getUploadCancel())
                                    if (pause1.getText().toString()
                                            .equalsIgnoreCase("Pause")) {
                                        dw1.setUploadPause(true);
                                        pause1.setText("Resume");
                                    } else if (pause1.getText().toString()
                                            .equalsIgnoreCase("Resume")) {
                                        dw1.setUploadPause(false);
                                        pause1.setText("Pause");
                                    }
                            }
                        });
                    }

                    if (viewnum == 2) {
                        final UploadTask dw2 = PlayerScreen.uploadmap.get(viewnum);
                        if (!dw2.getUploadCancel())
                            uploader2.setVisibility(View.VISIBLE);
                        t2.setText(updateText);
                        progress2.setProgress(percent);

                        cancel2.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                // TODO Auto-generated method stub
                                uploader2.setVisibility(View.GONE);
                                dw2.setUploadPause(true);
                                dw2.setUploadCancel(true, viewnum);

                            }
                        });
                        pause2.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                if (!dw2.getUploadCancel())
                                    if (pause2.getText().toString()
                                            .equalsIgnoreCase("Pause")) {
                                        dw2.setUploadPause(true);
                                        pause2.setText("Resume");
                                    } else if (pause2.getText().toString()
                                            .equalsIgnoreCase("Resume")) {
                                        dw2.setUploadPause(false);
                                        pause2.setText("Pause");
                                    }
                            }
                        });
                    }

                    if (viewnum == 3) {
                        final UploadTask dw3 = PlayerScreen.uploadmap.get(viewnum);
                        if (!dw3.getUploadCancel())
                            uploader3.setVisibility(View.VISIBLE);
                        t3.setText(updateText);
                        progress3.setProgress(percent);

                        cancel3.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                // TODO Auto-generated method stub
                                uploader3.setVisibility(View.GONE);
                                dw3.setUploadPause(true);
                                dw3.setUploadCancel(true, viewnum);

                            }
                        });
                        pause3.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                if (!dw3.getUploadCancel())
                                    if (pause3.getText().toString()
                                            .equalsIgnoreCase("Pause")) {
                                        dw3.setUploadPause(true);
                                        pause3.setText("Resume");
                                    } else if (pause3.getText().toString()
                                            .equalsIgnoreCase("Resume")) {
                                        dw3.setUploadPause(false);
                                        pause3.setText("Pause");
                                    }
                            }
                        });
                    }
                    if (viewnum == 4) {
                        final UploadTask dw4 = PlayerScreen.uploadmap.get(viewnum);
                        if (!dw4.getUploadCancel())
                            uploader4.setVisibility(View.VISIBLE);
                        t4.setText(updateText);
                        progress4.setProgress(percent);

                        cancel4.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                // TODO Auto-generated method stub
                                uploader4.setVisibility(View.GONE);
                                dw4.setUploadPause(true);
                                dw4.setUploadCancel(true, viewnum);

                            }
                        });
                        pause4.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                if (!dw4.getUploadCancel())
                                    if (pause4.getText().toString()
                                            .equalsIgnoreCase("Pause")) {
                                        dw4.setUploadPause(true);
                                        pause4.setText("Resume");
                                    } else if (pause4.getText().toString()
                                            .equalsIgnoreCase("Resume")) {
                                        dw4.setUploadPause(false);
                                        pause4.setText("Pause");
                                    }
                            }
                        });
                    }

                    if (viewnum == 5) {
                        final UploadTask dw5 = PlayerScreen.uploadmap.get(viewnum);
                        if (!dw5.getUploadCancel())
                            uploader5.setVisibility(View.VISIBLE);
                        t5.setText(updateText);
                        progress5.setProgress(percent);

                        cancel5.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                // TODO Auto-generated method stub
                                uploader5.setVisibility(View.GONE);
                                dw5.setUploadPause(true);
                                dw5.setUploadCancel(true, viewnum);
                            }
                        });
                        pause5.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                if (!dw5.getUploadCancel())
                                    if (pause5.getText().toString()
                                            .equalsIgnoreCase("Pause")) {
                                        dw5.setUploadPause(true);
                                        pause5.setText("Resume");
                                    } else if (pause5.getText().toString()
                                            .equalsIgnoreCase("Resume")) {
                                        dw5.setUploadPause(false);
                                        pause5.setText("Pause");
                                    }
                            }
                        });
                    }
                    break;
            }

        }
    };

    @Override
    protected void onResume() {
        registerReceiver(mIntentReceiver, mIntentFilter);

        super.onResume();
    }

    @Override
    protected void onPause() {
        unregisterReceiver(mIntentReceiver);
        finish();
        super.onPause();
    }
}

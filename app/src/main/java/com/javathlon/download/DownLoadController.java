package com.javathlon.download;

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
import com.javathlon.MainActivity;
import com.javathlon.R;
import com.javathlon.player.PlayerScreen;

public class DownLoadController extends BaseActivity {
    public static String UE_ACTION = "com.cse15th.app.mpnote.inforeground";
    public static String UE_UPDATE = "com.cse15th.app.mpnote.updateview";
    protected static final int UPDATEVIEWS = 1, NEEDTOCLOSE = 70;
    protected int counter = 1;
    private IntentFilter mIntentFilter;
    String updateText;
    TextView t1, t2, t3, t4, t5;
    int percent = 0;
    int viewnum = 0;
    ProgressBar progress1, progress2, progress3, progress4, progress5;
    LinearLayout downloader1, downloader2, downloader3, downloader4, downloader5;
    Button pause1, pause2, pause3, pause4, pause5, cancel1, cancel2, cancel3, cancel4, cancel5, playNow1, playNow2, playNow3, playNow4, playNow5;
    DownloadTask[] dwq;
    DownLoadController dwc;
    private BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            String action = intent.getAction();
            if (action.equals(UE_ACTION)) {
                this.setResultCode(Activity.RESULT_OK);
            }
            if (action.equals(UE_UPDATE)) {
                this.setResultCode(Activity.RESULT_OK);
                String update = intent.getStringExtra("upatetext");
                updateText = update + " of " + intent.getStringExtra("FILE_NAME");
                percent = intent.getIntExtra("PERCENT_COMPLETE", -1);
                viewnum = intent.getIntExtra("NOTIFICATION_ID", -1);
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
        dwc = this;
        LoadGui();
        hideDownLoaders();
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(UE_ACTION);
        mIntentFilter.addAction(UE_UPDATE);
    }

    private void LoadGui() {
        // TODO Auto-generated method stub
        downloader1 = (LinearLayout) findViewById(R.id.downloader1);
        downloader2 = (LinearLayout) findViewById(R.id.downloader2);
        downloader3 = (LinearLayout) findViewById(R.id.downloader3);
        downloader4 = (LinearLayout) findViewById(R.id.downloader4);
        downloader5 = (LinearLayout) findViewById(R.id.downloader5);
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
        playNow1 = (Button) findViewById(R.id.playNow1);
        playNow2 = (Button) findViewById(R.id.playNow2);
        playNow3 = (Button) findViewById(R.id.playNow3);
        playNow4 = (Button) findViewById(R.id.playNow4);
        playNow5 = (Button) findViewById(R.id.playNow5);
    }

    private void hideDownLoaders() {
        // TODO Auto-generated method stub
        downloader1.setVisibility(View.GONE);
        downloader2.setVisibility(View.GONE);
        downloader3.setVisibility(View.GONE);
        downloader4.setVisibility(View.GONE);
        downloader5.setVisibility(View.GONE);
    }

    private Handler searchHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case NEEDTOCLOSE:
                    if (MainActivity.map.size() == 0) {
                        finish();
                    }
                    break;
                case UPDATEVIEWS:
                    if (viewnum == 1) {
                        final DownloadTask dw1 = MainActivity.map.get(viewnum);
                        if (dw1 != null) {
                            if (!dw1.getDownloadCancel())
                                downloader1.setVisibility(View.VISIBLE);
                        }
                        t1.setText(updateText);
                        progress1.setProgress(percent);
                        if (percent > 99) {
                            playNow1.setVisibility(View.VISIBLE);
                            playNow1.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    // TODO Auto-generated method stub
                                    String fileName = dw1.getFilePath();
                                    if (fileName.length() > 1) {
                                        Intent i = new Intent(DownLoadController.this, PlayerScreen.class);
                                        i.putExtra("mediapath", fileName);
                                        i.putExtra("sppos", -1);
                                        i.putExtra("stream", false);
                                        startActivity(i);
                                    }
                                }
                            });
                        } else {
                            playNow1.setVisibility(View.GONE);
                        }
                        cancel1.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                // TODO Auto-generated method stub
                                downloader1.setVisibility(View.GONE);
                                dw1.setDownloadPause(true);
                                dw1.setDownloadCancel(true, viewnum);
                                if (MainActivity.map.size() == 0) {
                                    dwc.finish();
                                }
                            }
                        });
                        pause1.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                if (!dw1.getDownloadCancel())
                                    if (pause1.getText().toString().equalsIgnoreCase("Pause")) {
                                        dw1.setDownloadPause(true);
                                        pause1.setText("Resume");
                                    } else if (pause1.getText().toString().equalsIgnoreCase("Resume")) {
                                        dw1.setDownloadPause(false);
                                        pause1.setText("Pause");
                                    }
                            }
                        });
                    }

                    if (viewnum == 2) {
                        final DownloadTask dw2 = MainActivity.map.get(viewnum);
                        if (dw2 != null) {
                            if (!dw2.getDownloadCancel())
                                downloader2.setVisibility(View.VISIBLE);
                        }
                        t2.setText(updateText);
                        progress2.setProgress(percent);
                        if (percent > 99) {
                            playNow2.setVisibility(View.VISIBLE);
                            playNow2.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    // TODO Auto-generated method stub
                                    String fileName = dw2.getFilePath();
                                    if (fileName.length() > 1) {
                                        Intent i = new Intent(DownLoadController.this, PlayerScreen.class);
                                        i.putExtra("mediapath", fileName);
                                        i.putExtra("sppos", -1);
                                        i.putExtra("stream", false);
                                        startActivity(i);
                                    }
                                }
                            });
                        } else {
                            playNow2.setVisibility(View.GONE);

                        }
                        cancel2.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                // TODO Auto-generated method stub
                                downloader2.setVisibility(View.GONE);
                                dw2.setDownloadPause(true);
                                dw2.setDownloadCancel(true, viewnum);
                                if (MainActivity.map.size() == 0) {
                                    dwc.finish();
                                }
//							Message msg = new Message();
//							msg.what = NEEDTOCLOSE;
//							searchHandler.sendMessage(msg);
                            }
                        });
                        pause2.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                if (!dw2.getDownloadCancel())
                                    if (pause2.getText().toString().equalsIgnoreCase("Pause")) {
                                        dw2.setDownloadPause(true);
                                        pause2.setText("Resume");
                                    } else if (pause2.getText().toString().equalsIgnoreCase("Resume")) {
                                        dw2.setDownloadPause(false);
                                        pause2.setText("Pause");
                                    }
                            }
                        });
                    }

                    if (viewnum == 3) {
                        final DownloadTask dw3 = MainActivity.map.get(viewnum);
                        if (dw3 != null) {
                            if (!dw3.getDownloadCancel())
                                downloader3.setVisibility(View.VISIBLE);
                        }
                        t3.setText(updateText);
                        progress3.setProgress(percent);
                        if (percent > 99) {
                            playNow3.setVisibility(View.VISIBLE);
                            playNow3.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    // TODO Auto-generated method stub
                                    String fileName = dw3.getFilePath();
                                    if (fileName.length() > 1) {
                                        Intent i = new Intent(DownLoadController.this, PlayerScreen.class);
                                        i.putExtra("mediapath", fileName);
                                        i.putExtra("sppos", -1);
                                        i.putExtra("stream", false);
                                        startActivity(i);
                                    }
                                }
                            });
                        } else {
                            playNow3.setVisibility(View.GONE);
                        }
                        cancel3.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                // TODO Auto-generated method stub
                                downloader3.setVisibility(View.GONE);
                                dw3.setDownloadPause(true);
                                dw3.setDownloadCancel(true, viewnum);
                                if (MainActivity.map.size() == 0) {
                                    dwc.finish();
                                }
//							Message msg = new Message();
//							msg.what = NEEDTOCLOSE;
//							searchHandler.sendMessage(msg);
                            }
                        });
                        pause3.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                if (!dw3.getDownloadCancel())
                                    if (pause3.getText().toString().equalsIgnoreCase("Pause")) {
                                        dw3.setDownloadPause(true);
                                        pause3.setText("Resume");
                                    } else if (pause3.getText().toString().equalsIgnoreCase("Resume")) {
                                        dw3.setDownloadPause(false);
                                        pause3.setText("Pause");
                                    }
                            }
                        });
                    }
                    if (viewnum == 4) {
                        final DownloadTask dw4 = MainActivity.map.get(viewnum);
                        if (dw4 != null) {
                            if (!dw4.getDownloadCancel())
                                downloader4.setVisibility(View.VISIBLE);
                        }
                        t4.setText(updateText);
                        progress4.setProgress(percent);
                        if (percent > 99) {
                            playNow4.setVisibility(View.VISIBLE);
                            playNow4.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    // TODO Auto-generated method stub
                                    String fileName = dw4.getFilePath();
                                    if (fileName.length() > 1) {
                                        Intent i = new Intent(DownLoadController.this, PlayerScreen.class);
                                        i.putExtra("mediapath", fileName);
                                        i.putExtra("sppos", -1);
                                        i.putExtra("stream", false);
                                        startActivity(i);
                                    }
                                }
                            });
                        } else {
                            playNow4.setVisibility(View.GONE);
                        }
                        cancel4.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                // TODO Auto-generated method stub
                                downloader4.setVisibility(View.GONE);
                                dw4.setDownloadPause(true);
                                dw4.setDownloadCancel(true, viewnum);
                                if (MainActivity.map.size() == 0) {
                                    dwc.finish();
                                }
//							Message msg = new Message();
//							msg.what = NEEDTOCLOSE;
//							searchHandler.sendMessage(msg);
                            }
                        });
                        pause4.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                if (!dw4.getDownloadCancel())
                                    if (pause4.getText().toString().equalsIgnoreCase("Pause")) {
                                        dw4.setDownloadPause(true);
                                        pause4.setText("Resume");
                                    } else if (pause4.getText().toString().equalsIgnoreCase("Resume")) {
                                        dw4.setDownloadPause(false);
                                        pause4.setText("Pause");
                                    }
                            }
                        });
                    }

                    if (viewnum == 5) {
                        final DownloadTask dw5 = MainActivity.map.get(viewnum);
                        if (dw5 != null) {
                            if (!dw5.getDownloadCancel())
                                downloader5.setVisibility(View.VISIBLE);
                        }
                        t5.setText(updateText);
                        progress5.setProgress(percent);
                        if (percent > 99) {
                            playNow5.setVisibility(View.VISIBLE);
                            playNow5.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    // TODO Auto-generated method stub
                                    String fileName = dw5.getFilePath();
                                    if (fileName.length() > 1) {
                                        Intent i = new Intent(DownLoadController.this, PlayerScreen.class);
                                        i.putExtra("mediapath", fileName);
                                        i.putExtra("sppos", -1);
                                        i.putExtra("stream", false);
                                        startActivity(i);
                                    }
                                }
                            });
                        } else {
                            playNow5.setVisibility(View.GONE);
                        }
                        cancel5.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                // TODO Auto-generated method stub
                                downloader5.setVisibility(View.GONE);
                                dw5.setDownloadPause(true);
                                dw5.setDownloadCancel(true, viewnum);

                                if (MainActivity.map.size() == 0) {
                                    dwc.finish();
                                }
//							Message msg = new Message();
//							msg.what = NEEDTOCLOSE;
//							searchHandler.sendMessage(msg);
                            }
                        });
                        pause5.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                if (!dw5.getDownloadCancel())
                                    if (pause5.getText().toString().equalsIgnoreCase("Pause")) {
                                        dw5.setDownloadPause(true);
                                        pause5.setText("Resume");
                                    } else if (pause5.getText().toString().equalsIgnoreCase("Resume")) {
                                        dw5.setDownloadPause(false);
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

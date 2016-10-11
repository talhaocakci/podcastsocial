package com.javathlon.rss;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.javathlon.CatalogData;
import com.javathlon.CommonStaticClass;
import com.javathlon.PodcastData;
import com.javathlon.R;
import com.javathlon.adapters.PodcastAdapter;
import com.javathlon.db.DBAccessor;
import com.javathlon.download.DownloadHandler;
import com.javathlon.download.DownloadProgressThread;
import com.javathlon.download.DownloadReceiver;
import com.javathlon.download.RSSDownloaderParser;
import com.javathlon.download.SpreakerUtil;
import com.javathlon.memsoft.MemsoftUtil;
import com.javathlon.player.PlayerScreen;
import com.javathlon.video.VideoSample;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RssListPlayerActivity extends Fragment implements SwipeRefreshLayout.OnRefreshListener, OnClickListener, OnTouchListener, OnCompletionListener, OnBufferingUpdateListener {

    public static HashMap<Long, PodcastData> storeTable = new HashMap<Long, PodcastData>();
    private final Handler handler = new Handler();
    public MyXMLHandlerItems myXMLHandler = new MyXMLHandlerItems();
    protected int dialogCount = 1;
    String artworkImage;
    SwipeRefreshLayout swipeRefreshLayout;
    DBAccessor dbHelper;
    AlertDialog alert = null;

    private ImageButton buttonPlayPause;
    private SeekBar seekBarProgress;
    private String rssUrl = "";
    private ListView notesList;
    private String songUrl = "";
    private MediaPlayer mediaPlayer;
    private int mediaFileLengthInMilliseconds; // this value contains the song duration in milliseconds. Look at getDuration() method in MediaPlayer class
    private TextView podcastLabel, downloadPodcast, searchPodcastButton, searchKeyword;
    private Spinner searchOptions;
    private PodcastAdapter adapter;
    private Context con;
    private AlertDialog downLoadDialog;
    private AlertDialog.Builder builder;
    private int notifyUser = 0;
    private String podcastDescription;
    private String candidateArtwork;
    private long catalogId;
    public static List<PodcastData> podcastDataList = new ArrayList<PodcastData>();
    private  DownloadProgressThread downloadProgressRunnable;

    private  Thread downloadProgressThread;

    /**
     * This method initialise all the views in project
     */
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.catalogeditions, container, false);
        if (getArguments() == null || getArguments().getString("rss") == null)
            rssUrl = "https://s3.ap-south-1.amazonaws.com/javacore-course/javacourse.xml";
        else
            rssUrl = getArguments().getString("rss");

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);

        String needDownloaded = "n";
        if (getArguments() != null && getArguments().getString("needdownloaded") != null)
            needDownloaded = getArguments().getString("needdownloaded");

        if (getArguments() == null || getArguments().getString("artwork") == null)
            candidateArtwork = "";
        else
            candidateArtwork = getArguments().getString("artwork");

        notesList = (ListView) view.findViewById(R.id.editionList);
        con = this.getActivity();

        podcastLabel = (TextView) view.findViewById(R.id.podcastlabel);




        searchOptions = (Spinner)view.findViewById(R.id.searchOptions);
        final String[] items = new String[]{"All", "Downloaded", "Not downloaded", "Left half finished", "Not started"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, items) ;
        searchOptions.setAdapter(arrayAdapter);

        searchOptions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String keyword = searchKeyword.getText().toString();

                searchPodcasts(keyword, position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        searchPodcastButton = (TextView) view.findViewById(R.id.searchPodcastButton);
        searchKeyword = (TextView) view.findViewById(R.id.searchKeyword);
        searchPodcastButton.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int searchOption =  searchOptions.getSelectedItemPosition();

                        String keyword = searchKeyword.getText().toString();

                        searchPodcasts(keyword, searchOption);

                    }
                }
        );

        this.showMyDialog(getResources().getString(R.string.rsslistisupdating));

        initPodcastList(needDownloaded, false);

        this.hideDialog();

        LocalBroadcastManager.getInstance(con).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int count = 0;
                Integer downloadId = null;
                if(intent.getExtras() != null) {
                    intent.getExtras().getInt("downloadCount");
                    downloadId = intent.getExtras().getInt("downloadid");


                }


                adapter.notifyDataSetChanged();
                if(count == 0)
                {
                    if(downloadProgressThread != null)
                      downloadProgressThread.interrupt();
                    //downloadProgressThread = null;
                }
            }
        }, new IntentFilter("downloadProgressUpdate"));

        LocalBroadcastManager.getInstance(con).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                startDownloadProgressThread();
            }
        }, new IntentFilter("startDownloadProgress"));

        notesList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v, int pos,
                                    long id) {
                // TODO Auto-generated method stub
                Log.e("item", "clicked");
                songUrl = podcastDataList.get(pos).url;

                final int index = pos;
                PodcastData pod = podcastDataList.get(index);
                String path = pod.url;
                if (pod.getIsDownloaded() != null && pod.getIsDownloaded().equals("y")) {
                    path = pod.devicePath;
                    CommonStaticClass.streaming = false;
                }
                else
                    CommonStaticClass.streaming = true;

                if(!path.contains(".mp4")) {
                    Intent i = new Intent(getActivity(), PlayerScreen.class);
                    CommonStaticClass.setCurrentPodcast(pod);
                    i.putExtra("mediapath", path);
                    i.putExtra("sppos", -1);
                    i.putExtra("fromBackground", false);
                    i.putExtra("mediaarturl", artworkImage);
                    i.putExtra("filelabel", pod.editionTitle);
                    i.putExtra("podid", pod.id);
                    startActivity(i);
                    v.findViewById(R.id.downloadpodcastitem).setOnClickListener(new OnClickListener() {
                        public void onClick(View v) {

                        }
                    });
                } else {
                    Intent i = new Intent(getActivity(), VideoSample.class);
                    CommonStaticClass.setCurrentPodcast(pod);
                    i.putExtra("video_path", path);
                    startActivity(i);
                }
            }
        });

        return view;
    }

    private void searchPodcasts(String keyword, int searchOption){
        if (dbHelper == null) {
            dbHelper = new DBAccessor(getActivity());
            dbHelper.open();
        }


        int progress = -1;
        String downloaded = null;

        if(searchOption == 1)
            downloaded = "y";
        if(searchOption == 2)
            downloaded = "n";
        if(searchOption == 3)
            progress = 40;
        if(searchOption == 4)
            progress = 0;


        podcastDataList.clear();
        podcastDataList.addAll(dbHelper.getPodcastsByFilter(catalogId,keyword, downloaded, progress ));
        adapter.notifyDataSetChanged();

    }

    public  void startDownloadProgressThread(){
        if(downloadProgressRunnable == null)

        downloadProgressRunnable = new DownloadProgressThread((DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE),this.getActivity());

        if(downloadProgressThread == null || downloadProgressThread.isInterrupted() ||downloadProgressThread.getState().equals(Thread.State.TERMINATED) ) {
            downloadProgressThread = new Thread(downloadProgressRunnable);
            downloadProgressThread.start();
        }
    }

    private void initPodcastList(String needDownloaded, boolean forceRssDownload) {

        if (dbHelper == null) {
            dbHelper = new DBAccessor(getActivity());
            dbHelper.open();
        }
        CatalogData data = dbHelper.getPodcastCatalogByRss(rssUrl);
        catalogId = data.id;

        getActivity().getActionBar().setTitle(data.name.toUpperCase());

        //set filter to only when download is complete and register broadcast receiver
        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        getActivity().registerReceiver(new DownloadReceiver(), filter);
        if (data != null && data.image != null && !data.image.equals("")) {
            artworkImage = data.image;
        }
        if (!forceRssDownload) {
            if (data.id != 0L && data.lastRssUpdate != null && !data.lastRssUpdate.equals("")) {
                podcastDataList = dbHelper.getPodcastsByCatalogId(data.id, needDownloaded);
            }
        }
        adapter = new PodcastAdapter(this.getActivity(), podcastDataList);
        notesList.setAdapter(adapter);
        if (podcastDataList.size() > 0 && !forceRssDownload) {
            adapter.notifyDataSetChanged();
            return;
        }

        if(rssUrl.startsWith("http://api.spreaker.com/show/")){
            if(data == null || data.id == null || new Long(0).equals(data.id))
                return;
            List<PodcastData> dataList = SpreakerUtil.getEpisodesFromSpreakerUrl(rssUrl, data.id.intValue());

                List<PodcastData> podcastListToAdd = new ArrayList<PodcastData>();
                for(PodcastData podcastData : dataList){
                    if(data.lastRssUpdate == null || podcastData.publishDateLong >  MemsoftUtil.getTimeFromString(data.lastRssUpdate).getTime())
                        podcastListToAdd.add(podcastData);


                }

                dbHelper.bulkInsertPodcastData(podcastListToAdd, data.id);
                podcastDataList.clear();
                podcastDataList.addAll(dbHelper.getPodcastsByCatalogId(data.id, needDownloaded));



            adapter.notifyDataSetChanged();
            swipeRefreshLayout.setRefreshing(false);
            data.lastRssUpdate = MemsoftUtil.getTimeAsString();
            dbHelper.updatePodcastCatalogRSSDownload(data.id, MemsoftUtil.getTimeAsString());
            return;
        }


        CommonStaticClass.progressDialog = ProgressDialog.show(con, "Refreshing...", "Please wait");
        DownloadHandler handler = new DownloadHandler(getActivity().getBaseContext(), adapter, myXMLHandler, data.id, dbHelper, rssUrl, podcastDataList, candidateArtwork, artworkImage, swipeRefreshLayout, CommonStaticClass.progressDialog);
        RSSDownloaderParser parser = new RSSDownloaderParser(getActivity(), true, handler, myXMLHandler, podcastDataList);
        parser.getRSSListFromUrl(rssUrl, getActivity());

    }

    /**
     * Method which updates the SeekBar primary progress by current song playing position
     */
    private void primarySeekBarProgressUpdater() {
        seekBarProgress.setProgress((int) (((float) mediaPlayer.getCurrentPosition() / mediaFileLengthInMilliseconds) * 100)); // This math construction give a percentage of "was playing"/"song length"
        if (mediaPlayer.isPlaying()) {
            Runnable notification = new Runnable() {
                public void run() {
                    primarySeekBarProgressUpdater();
                }
            };
            handler.postDelayed(notification, 1000);
        }
    }



    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        return false;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        /** MediaPlayer onCompletion event handler. Method which calls then song playing is complete*/
        buttonPlayPause.setImageResource(R.drawable.button_play);
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        /** Method which updates the SeekBar secondary progress by current song loading from URL position*/
        seekBarProgress.setSecondaryProgress(percent);
    }


    public void showMyDialog(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        builder.setMessage(msg)
                .setCancelable(false)
                .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        alert = builder.create();
        alert.show();
    }

    public void hideDialog(){
        if(alert != null && alert.isShowing())
            alert.hide();
    }

    @Override
    public void onRefresh() {
        podcastDataList.clear();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initPodcastList("n", true);

            }
        }, 1000);
        System.out.println("Update et");
    }
}





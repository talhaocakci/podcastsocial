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
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.cognito.internal.util.StringUtils;
import com.javathlon.BuySubscriptionActivity;
import com.javathlon.CatalogData;
import com.javathlon.CommonStaticClass;
import com.javathlon.PodcastData;
import com.javathlon.PurchasedPodcastItem;
import com.javathlon.R;
import com.javathlon.adapters.PodcastAdapter;
import com.javathlon.apiclient.ApiClient;
import com.javathlon.apiclient.api.UrlResourceApi;
import com.javathlon.apiclient.model.SecureUrlVM;
import com.javathlon.db.DBAccessor;
import com.javathlon.download.DownloadHandler;
import com.javathlon.download.DownloadProgressThread;
import com.javathlon.download.DownloadReceiver;
import com.javathlon.download.RSSDownloaderParser;
import com.javathlon.download.SpreakerUtil;
import com.javathlon.memsoft.MemsoftUtil;
import com.javathlon.player.PlayerScreen;
import com.javathlon.video.VideoScreen;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import retrofit2.Response;

public class RssListPlayerActivity extends Fragment implements SwipeRefreshLayout.OnRefreshListener, OnClickListener, OnTouchListener {

    public static HashMap<Long, PodcastData> storeTable = new HashMap<Long, PodcastData>();
    private final Handler handler = new Handler();
    public MyXMLHandlerItems myXMLHandler = new MyXMLHandlerItems();
    String artworkImage;
    SwipeRefreshLayout swipeRefreshLayout;
    DBAccessor dbHelper;
    AlertDialog alert = null;
    private String rssUrl = "";
    private ListView itemsList;
    private TextView searchPodcastButton, searchKeyword;
    private Spinner searchOptions;
    private PodcastAdapter adapter;
    private Context con;
    private String candidateArtwork;
    private long catalogId;
    public static List<PodcastData> podcastDataList = new ArrayList<PodcastData>();
    public static int currentIndexInPodcastList = 0;
    private DownloadProgressThread downloadProgressRunnable;
    private Thread downloadProgressThread;
    private Context context;
    private Long podcastCatalogId;

    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.catalogeditions, container, false);
        if (getArguments() == null || getArguments().getString("rss") == null)
            rssUrl = "https://s3.ap-south-1.amazonaws.com/javacore-course/javacourse.xml";
        else
            rssUrl = getArguments().getString("rss");

        context = getActivity().getApplicationContext();

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);

        String needDownloaded = "n";
        if (getArguments() != null && getArguments().getString("needdownloaded") != null)
            needDownloaded = getArguments().getString("needdownloaded");

        if (getArguments() == null || getArguments().getString("artwork") == null)
            candidateArtwork = "";
        else
            candidateArtwork = getArguments().getString("artwork");

        itemsList = (ListView) view.findViewById(R.id.editionList);
        con = this.getActivity();

        searchOptions = (Spinner) view.findViewById(R.id.searchOptions);
        final String[] items = new String[]{"All", "Downloaded", "Not downloaded", "Left half finished", "Not started"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, items);
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
                        int searchOption = searchOptions.getSelectedItemPosition();
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
                if (intent.getExtras() != null) {
                    intent.getExtras().getInt("downloadCount");
                    downloadId = intent.getExtras().getInt("downloadid");
                }
                adapter.notifyDataSetChanged();
                if (count == 0) {
                    if (downloadProgressThread != null)
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

        itemsList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v, int pos,
                                    long id) {
                final int index = pos;
                currentIndexInPodcastList = pos;
                PodcastData pod = podcastDataList.get(index);
                String path = pod.url;
                if (pod.getIsDownloaded() != null && pod.getIsDownloaded().equals("y")) {
                    path = pod.devicePath;
                    CommonStaticClass.streaming = false;
                } else
                    CommonStaticClass.streaming = true;

                if (!path.contains(".mp4")) {
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

                    try {
                        path = getSignedUrl(String.valueOf(pod.catalogId), path, false);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    if (path == null) {
                        Intent i = new Intent(context, BuySubscriptionActivity.class);
                        i.putExtra("podcastId", podcastCatalogId);
                        startActivity(i);
                        Toast.makeText(getActivity(), "Can not open, upgrade your account", Toast.LENGTH_SHORT).show();
                        return;
                    }


                    Intent i = new Intent(getActivity(), VideoScreen.class);
                    CommonStaticClass.setCurrentPodcast(pod);
                    i.putExtra("video_item", path);
                    i.putExtra("video_item_id", pod.id);
                    startActivity(i);
                }
            }
        });

        return view;
    }

    public String getSignedUrl(String podcastId, String fileKey, boolean isFree) throws IOException {

        if (fileKey.startsWith("http") || fileKey.startsWith("file://")) {
            return fileKey;
        }

        PurchasedPodcastItem item = dbHelper.getPurchasedPodcastItem(Long.parseLong(podcastId));

        /**TODO: izin listesine göre kontrol et */

        if (!isFree && item == null) {
            return null;
        }

        final UrlResourceApi api = ApiClient.getApiClient(getActivity().getApplicationContext()).createService(UrlResourceApi.class);

        Response<SecureUrlVM> secureURLResponse = null;
        try {
            secureURLResponse = new AsyncTask<String, Void, Response<SecureUrlVM>>() {
                protected Response<SecureUrlVM> doInBackground(String... param) {

                    Response<SecureUrlVM> urlVMResponse = null;
                    try {
                        urlVMResponse = api.generateURLUsingGET(param[0].toString(), param[1].toString()).execute();


                        return urlVMResponse;

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    return null;
                }

            }.execute(podcastId, fileKey).get();


        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        if (secureURLResponse == null || (!StringUtils.isEmpty(secureURLResponse.message()) && secureURLResponse.message().contains("Unauthorized"))) {

            Intent i = new Intent(con, BuySubscriptionActivity.class);
            startActivity(i);
            return null;
        }


        if (secureURLResponse != null && secureURLResponse.isSuccessful())
            return secureURLResponse.body().getUrl();
        else
            return null;
    }

    private void searchPodcasts(String keyword, int searchOption) {
        if (dbHelper == null) {
            dbHelper = new DBAccessor(getActivity());
            dbHelper.open();
        }

        int progress = -1;
        String downloaded = null;

        if (searchOption == 1)
            downloaded = "y";
        if (searchOption == 2)
            downloaded = "n";
        if (searchOption == 3)
            progress = 40;
        if (searchOption == 4)
            progress = 0;

        podcastDataList.clear();
        podcastDataList.addAll(dbHelper.getPodcastsByFilter(catalogId, keyword, downloaded, progress));
        adapter.notifyDataSetChanged();
    }

    public void startDownloadProgressThread() {
        if (downloadProgressRunnable == null)

            downloadProgressRunnable = new DownloadProgressThread((DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE), this.getActivity());

        if (downloadProgressThread == null || downloadProgressThread.isInterrupted() || downloadProgressThread.getState().equals(Thread.State.TERMINATED)) {
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
        podcastCatalogId = data.id;


        PurchasedPodcastItem purchasedPodcastItem = dbHelper.getPurchasedPodcastItem(podcastCatalogId);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(data.name.toUpperCase());

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

        adapter.setPurchased(purchasedPodcastItem != null);

        itemsList.setAdapter(adapter);
        if (podcastDataList.size() > 0 && !forceRssDownload) {
            adapter.notifyDataSetChanged();
            return;
        }

        if (rssUrl.startsWith("http://api.spreaker.com/show/")) {
            if (data == null || data.id == null || new Long(0).equals(data.id))
                return;
            List<PodcastData> dataList = SpreakerUtil.getEpisodesFromSpreakerUrl(rssUrl, data.id.intValue());

            List<PodcastData> podcastListToAdd = new ArrayList<PodcastData>();
            for (PodcastData podcastData : dataList) {
                if (data.lastRssUpdate == null || podcastData.publishDateLong > MemsoftUtil.getTimeFromString(data.lastRssUpdate).getTime())
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

    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        return false;
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

    public void hideDialog() {
        if (alert != null && alert.isShowing())
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

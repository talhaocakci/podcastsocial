package com.javathlon.fragments;


import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.facebook.CallbackManager;
import com.google.gson.Gson;
import com.javathlon.model.podcastmodern.Application;
import com.nhaarman.listviewanimations.appearance.simple.SwingBottomInAnimationAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.OnDismissCallback;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.SwipeDismissAdapter;
import com.javathlon.ApplicationSettings;
import com.javathlon.BaseActivity;
import com.javathlon.CatalogData;
import com.javathlon.R;
import com.javathlon.db.DBAccessor;
import com.javathlon.download.PodcastModernClient;
import com.javathlon.memsoft.GoogleCardAdapter;
import com.javathlon.memsoft.MemsoftUtil;
import com.javathlon.memsoft.ResponseHolder;
import com.javathlon.memsoft.WebServiceAsyncTaskGet;
import com.javathlon.model.podcastmodern.Podcast;
import com.javathlon.model.spreaker.Author;
import com.javathlon.model.spreaker.ShowResult;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentHome extends Fragment implements OnDismissCallback {

    private static final int INITIAL_DELAY_MILLIS = 300;
    DBAccessor dbHelper;
    GoogleCardAdapter adapterBackup;
    private CallbackManager callbackManager;
    Button openSocialConnectButton;

    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.list_view, container, false);
        ListView listView = (ListView) view.findViewById(R.id.list_view);
        openSocialConnectButton = (Button) view.findViewById(R.id.openSocialConnect);
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this.getActivity());

        if (!pref.getString("fbtoken", "").equals("") || !pref.getString("googletoken", "").equals("")) {
            openSocialConnectButton.setVisibility(View.GONE);
        } else {
            openSocialConnectButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getActivity(), SignInActivity.class);
                    i.putExtra("intentionally", true);
                    startActivity(i);
                }
            });
        }

        if (dbHelper == null) {
            dbHelper = new DBAccessor(getActivity());
            dbHelper.open();
        }

        final List<CatalogData> list = dbHelper.getMainCatalogList();

        final GoogleCardAdapter mGoogleCardsAdapter = new GoogleCardAdapter(this.getActivity(), list, (BaseActivity) (this.getActivity()));
        adapterBackup = mGoogleCardsAdapter;

        int i = 1;
        if (list == null || list.size() <= 0) {
            List<Podcast> podcastList = null;
            try {
                PodcastModernClient.getPodcastsByApplication(
                        ApplicationSettings.appId.intValue(), new Callback<Application>() {
                            @Override
                            public void onResponse(Call<Application> call, Response<Application> response) {
                                for (Podcast podcast : response.body().getPodcasts()) {
                                    list.add(createCatalogDataItem(podcast));
                                }
                                mGoogleCardsAdapter.notifyDataSetChanged();
                            }
                            @Override
                            public void onFailure(Call<Application> call, Throwable t) {
                                Podcast podcast = new Podcast();
                                podcast.setAuthor("Default");
                                podcast.setName("Default");
                                podcast.setOtherRssUrl("https://s3.ap-south-1.amazonaws.com/javacore-course/javacourse.xml");
                                list.add(createCatalogDataItem(podcast));
                            }
                        });
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        SwingBottomInAnimationAdapter swingBottomInAnimationAdapter = new SwingBottomInAnimationAdapter(
                new SwipeDismissAdapter(mGoogleCardsAdapter, this));
        swingBottomInAnimationAdapter.setAbsListView(listView);

        assert swingBottomInAnimationAdapter.getViewAnimator() != null;
        swingBottomInAnimationAdapter.getViewAnimator().setInitialDelayMillis(
                INITIAL_DELAY_MILLIS);

        listView.setClipToPadding(false);
        listView.setDivider(null);
        Resources r = getResources();
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                8, r.getDisplayMetrics());
        listView.setDividerHeight(px);
        listView.setFadingEdgeLength(0);
        listView.setFitsSystemWindows(true);
        px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12,
                r.getDisplayMetrics());
        listView.setPadding(px, px, px, px);
        listView.setScrollBarStyle(ListView.SCROLLBARS_OUTSIDE_OVERLAY);
        listView.setAdapter(swingBottomInAnimationAdapter);

        return view;
    }

    private CatalogData createCatalogDataItem(Podcast podcast) {
        CatalogData data = new CatalogData();
        data.name = podcast.getName();
        data.author = podcast.getAuthor();
        data.image = podcast.getImageUrl();
        data.rss = podcast.getItunesUrl() != null && !podcast.getItunesUrl().equals("") ? podcast.getItunesUrl() : podcast.getOtherRssUrl();
        data.isMainCatalog = "y";
        data.isSubscribed = "y";

        // get more possible details from spreaker
        CatalogData c = dbHelper.getPodcastCatalogByRss(data.rss);
        if ((null == dbHelper.getPodcastCatalogByRss(data.rss) || c.id == 0) && data.rss.contains("spreaker")) {
            data = getSpreakerDetail(data.rss);
        }

        long id = dbHelper.createPodcastCatalogItem(data, "y");
        data.id = id;
        return data;
    }

    private CatalogData getSpreakerDetail(String rss) {

        CatalogData data = new CatalogData();
        try {
            ResponseHolder s = new WebServiceAsyncTaskGet().execute(rss).get();
            Gson gson = new Gson();
            ShowResult show = gson.fromJson(s.getResponseText(), ShowResult.class);
            data.image = show.getResponse().getShow().getImage().getLargeUrl();
            data.imageSmall = show.getResponse().getShow().getImage().getMediumUrl();
            data.author = show.getResponse().getShow().getAuthors().toString();
            data.infoUrl = show.getResponse().getShow().getSiteUrl();

            if (show.getResponse().getShow().getAuthors() != null) {
                StringBuilder b = new StringBuilder();
                for (Author a : show.getResponse().getShow().getAuthors()) {
                    b.append(a.getFullname()).append(", ");
                }
                data.author = b.toString();
            }
            data.createDate = MemsoftUtil.getTimeAsString();
            data.name = show.getResponse().getShow().getTitle();
            data.infoUrl = show.getResponse().getShow().getSiteUrl();
            data.rss = rss;
            data.isMainCatalog = "y";
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return data;
    }

    @Override
    public void onDismiss(final ViewGroup listView,
                          final int[] reverseSortedPositions) {
        for (int position : reverseSortedPositions) {
            adapterBackup.remove(adapterBackup.getItem(position));
        }
        adapterBackup.notifyDataSetChanged();
    }
}

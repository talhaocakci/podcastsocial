package com.javathlon.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.javathlon.CatalogData;
import com.javathlon.R;
import com.javathlon.adapters.SearchResultAdapter;
import com.javathlon.db.DBAccessor;
import com.javathlon.memsoft.ResponseHolder;
import com.javathlon.memsoft.WebServiceAsyncTaskPost;
import com.javathlon.rss.RssListPlayerActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by talha on 28.02.2015.
 */
public class SearchFragment extends Fragment {

    static String itunesSearchUrl = "https://itunes.apple.com/search?entity=podcast&term=";

    List<CatalogData> catalogDataList = new ArrayList<CatalogData>();
    ListView searchList;
    TextView searchButton;
    EditText keywordText;
    SearchResultAdapter adapter;
    DBAccessor dbHelper;
    ProgressDialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        View view = inflater.inflate(R.layout.searchresult, container, false);
        searchList = (ListView) view.findViewById(R.id.searchList);
        searchButton = (TextView) view.findViewById(R.id.searchPodcastButton);
        keywordText = (EditText) view.findViewById(R.id.searchKeyword);
        adapter = new SearchResultAdapter(getActivity(), this, catalogDataList);
        searchList.setAdapter(adapter);
        searchList.setOnItemClickListener(new SearchResultClickListener());
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = keywordText.getText().toString();
                text = text.replace(" ", "+");
                String searchUrl = itunesSearchUrl;
                searchUrl += text;

                dialog = new ProgressDialog(getActivity());
                dialog.setMessage("Podcast directories are processing...");
                dialog.setTitle("Searching...");
                dialog.show();

                searchUrl(searchUrl);

                InputMethodManager man = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                man.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);

                adapter.notifyDataSetChanged();

                dialog.hide();
            }
        });
        return view;
    }

    private void searchUrl(String searchUrl) {
        try {
            ResponseHolder response = new WebServiceAsyncTaskPost().execute(searchUrl).get(14, TimeUnit.SECONDS);
            catalogDataList.clear();
            try {
                JSONObject responseObject = new JSONObject(response.getResponseText());
                JSONArray fields = (JSONArray) responseObject.get("results");
                for (int i = 0; i < fields.length(); i++) {
                    JSONObject fieldObject = (JSONObject) fields.get(i);
                    String name = (String) fieldObject.get("collectionName");
                    String image = (String) fieldObject.get("artworkUrl600");
                    String imageSmall = (String) fieldObject.get("artworkUrl30");
                    String rssUrl = (String) fieldObject.get("feedUrl");
                    Integer trackCount = (Integer) fieldObject.get("trackCount");
                    String infoUrl = (String) fieldObject.get("collectionViewUrl");
                    String releaseDate = (String) fieldObject.get("releaseDate");
                    String artistName = (String) fieldObject.get("artistName");
                    String primaryGenreName = (String) fieldObject.get("primaryGenreName");

                    CatalogData catalog = new CatalogData();
                    catalog.image = image;
                    catalog.imageSmall = imageSmall;
                    catalog.name = name;
                    catalog.rss = rssUrl;
                    catalog.author = artistName;
                    catalog.trackCount = trackCount != null && !trackCount.equals("") ? Integer.valueOf(trackCount) : 0;
                    catalog.primaryGenreName = primaryGenreName;
                    catalog.lastReleaseDate = releaseDate;
                    catalog.infoUrl = infoUrl;
                    catalogDataList.add(catalog);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

    public void addItemToMainCatalog(CatalogData data) {
        if (dbHelper == null) {
            dbHelper = new DBAccessor(getActivity());
            dbHelper.open();
        }

        long catalogId = dbHelper.getPodcastCatalogByRss(data.rss).id;
        if (catalogId == 0L) {
            dbHelper.createPodcastCatalogItem(data, "y");

        } else {
            dbHelper.updatePodcastCatalogToMain(catalogId, "y");
        }
        Toast.makeText(this.getActivity(), R.string.addedToMain, Toast.LENGTH_SHORT).show();

    }

    private class SearchResultClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            CatalogData currentSelection = (CatalogData) adapterView.getAdapter().getItem(i);
            System.out.println(currentSelection.rss);
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();

            Bundle bundle = new Bundle();
            bundle.putString("rss", currentSelection.rss);
            bundle.putString("artwork", currentSelection.image);
            RssListPlayerActivity fragmentRss = new RssListPlayerActivity();
            fragmentRss.setArguments(bundle);
            ft.replace(R.id.content_frame, fragmentRss).addToBackStack("tag");
            ft.commit();
        }
    }
}
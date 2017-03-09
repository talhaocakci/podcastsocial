package com.javathlon.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.javathlon.CatalogData;
import com.javathlon.PodcastData;
import com.javathlon.R;
import com.javathlon.adapters.LibraryAdapter;
import com.javathlon.db.DBAccessor;
import com.javathlon.rss.RssListPlayerActivity;
import com.nhaarman.listviewanimations.appearance.simple.SwingBottomInAnimationAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.OnDismissCallback;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.SwipeDismissAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by talha on 27.02.2015.
 */
public class LibraryFragment extends Fragment implements OnDismissCallback {
    DBAccessor dbHelper;
    private ListView libraryList;
    private List<CatalogData> catalogDataList = new ArrayList<CatalogData>();
    LibraryAdapter adapter;
    Button clearItemsButton;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.library, container, false);
        libraryList = (ListView) view.findViewById(R.id.librarylist);
        clearItemsButton = (Button) view.findViewById(R.id.clearItems);

        clearItemsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (CatalogData podcast : catalogDataList) {
                    System.out.println(podcast.name);
                }

            }
        });

        if (null == dbHelper) {
            dbHelper = new DBAccessor(getActivity().getApplicationContext());
            dbHelper.open();
        }
        catalogDataList = dbHelper.getDownloadedItemInfoByCatalog();

        adapter = new LibraryAdapter(getActivity(), this, catalogDataList);

        libraryList.setAdapter(adapter);
        libraryList.setOnItemClickListener(new CatalogClickListener());

        SwingBottomInAnimationAdapter swingBottomInAnimationAdapter = new SwingBottomInAnimationAdapter(
                new SwipeDismissAdapter(adapter, this));
        swingBottomInAnimationAdapter.setAbsListView(libraryList);
        return view;
    }

    @Override
    public void onDismiss(ViewGroup listView, int[] reverseSortedPositions) {
        for (int position : reverseSortedPositions) {
            long catalogId = adapter.catalogList.get(position).id;


            List<PodcastData> podcastList = dbHelper.getPodcastsByCatalogId(catalogId, "y");
            for (PodcastData podcast : podcastList) {

                File file = new File(podcast.devicePath);
                boolean deleted = file.delete();
            }
            dbHelper.updatePodcastIsDownloadedByCatalogId(catalogId, "n");

            adapter.catalogList.remove(position);
        }
    }

    class CatalogClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View v, int pos, long id) {
            CatalogData currentSelection = (CatalogData) adapterView.getAdapter().getItem(pos);
            System.out.println(currentSelection.rss);
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();

            Bundle bundle = new Bundle();
            bundle.putString("rss", currentSelection.rss);
            bundle.putString("needdownloaded", "y");
            RssListPlayerActivity fragmentRss = new RssListPlayerActivity();
            fragmentRss.setArguments(bundle);
            ft.replace(R.id.content_frame, fragmentRss);
            ft.commit();
        }


    }
}

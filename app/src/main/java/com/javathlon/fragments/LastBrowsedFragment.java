package com.javathlon.fragments;


import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.javathlon.CommonStaticClass;
import com.javathlon.R;
import com.javathlon.adapters.LastBrowsedListAdapter;
import com.javathlon.components.LastBrowseClass;
import com.javathlon.components.LastPlayClass;
import com.javathlon.db.DBAccessor;
import com.javathlon.player.PlayerScreen;

import java.util.ArrayList;
import java.util.List;

public class LastBrowsedFragment extends Fragment {

    protected DBAccessor dbHelper = null;
    LastPlayClass lastPlayObj = null;
    List<LastBrowseClass> lastBrowseClassList = new ArrayList<LastBrowseClass>();
    private LastBrowsedListAdapter lastBrowsedArrayAdapter;
    private ListView lastBrowsedList;
    private LastBrowseClass lastBrowseObj;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.lastbrowsed, container, false);
        lastBrowsedList = (ListView) view.findViewById(R.id.lastbrowsedList);
        lastBrowsedList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {
                Log.e("mediapath", lastBrowseClassList.get(pos) + "");
                Log.e("sppos", lastBrowseClassList.get(pos) + "");
                String path = lastBrowseClassList.get(pos).getFull_device_path().get(0);
                Intent i = new Intent(getActivity(), PlayerScreen.class);
                i.putExtra("mediapath", path);
                i.putExtra("sppos",
                        Integer.parseInt("0"));
                i.putExtra("fromBackground", false);
                i.putExtra("filelabel", lastBrowseClassList.get(pos).getFile_name().get(0));
                if (path.contains("http://") || path.contains("https://")) {
//					i.putExtra("stream",true);
                    CommonStaticClass.streaming = true;
//					CommonStaticClass.currentSongPath = lastPlayObj.getSongpath().get(pos);
                } else {
//					i.putExtra("stream",false);
                    CommonStaticClass.streaming = false;
//					CommonStaticClass.currentSongPath = "";
                }
                startActivity(i);
            }
        });
        fillLastBrowsedList(view);
        return view;
    }

    private void fillLastBrowsedList(View view) {
        lastBrowseObj = new LastBrowseClass();

        Cursor mCursor = null;
        if (null == dbHelper) {
            dbHelper = new DBAccessor(LastBrowsedFragment.this.getActivity());
            dbHelper.open();


        }


        mCursor = dbHelper.getLastPlayedFilesListResult();

        if (mCursor.moveToFirst()) {
            do {

                String podcast_id = mCursor.getString(mCursor
                        .getColumnIndex("podcast_id"));
                String full_device_path = mCursor.getString(mCursor
                        .getColumnIndex("full_device_path"));
                String file_name = mCursor.getString(mCursor
                        .getColumnIndex("file_name"));
                String download_link = mCursor.getString(mCursor
                        .getColumnIndex("download_link"));
                String last_listen_date_mil = mCursor.getString(mCursor
                        .getColumnIndex("last_listen_date_mil"));
                String last_listen_date = mCursor.getString(mCursor
                        .getColumnIndex("last_listen_date"));
                String create_date = mCursor.getString(mCursor
                        .getColumnIndex("create_date"));
                lastBrowseObj.setPodcast_id(podcast_id);
                lastBrowseObj.setFull_device_path(full_device_path);
                lastBrowseObj.setFile_name(file_name);
                lastBrowseObj.setDownload_link(download_link);
                lastBrowseObj.setCreate_date(create_date);
                lastBrowseObj.setLast_listen_date_mil(last_listen_date_mil);
                lastBrowseObj.setLast_listen_date(last_listen_date);
                lastBrowseClassList.add(lastBrowseObj);
            } while (mCursor.moveToNext());
        }
        if (mCursor != null && !mCursor.isClosed()) {
            mCursor.close();
        }
        if (!(lastBrowseObj.getPodcast_id().size() > 0)) {
            //  emptyListString.setVisibility(View.VISIBLE);
            // emptyListString
            //       .setText("You did not listen any MP3 yet. After listening one,these files will be listed here for quick access.");
        } else {
            //   emptyListString.setVisibility(View.GONE);
        }

        lastBrowsedArrayAdapter = new LastBrowsedListAdapter(LastBrowsedFragment.this.getActivity(), lastBrowseClassList);
        lastBrowsedList.setAdapter(lastBrowsedArrayAdapter);


    }
}

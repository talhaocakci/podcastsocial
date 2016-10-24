package com.javathlon.fragments;


import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.javathlon.CommonStaticClass;
import com.javathlon.PodcastData;
import com.javathlon.R;
import com.javathlon.adapters.LastPlayedListAdapter;
import com.javathlon.components.LastPlayClass;
import com.javathlon.db.DBAccessor;
import com.javathlon.player.PlayerScreen;
import com.javathlon.video.VideoScreen;

import java.util.ArrayList;

import twitter4j.internal.logging.Logger;

public class LastNotesFragment extends Fragment {
    protected static final int CONTEXTMENU_UPLOAD = 11, CONTEXTMENU_EDIT = 12,
            CONTEXTMENU_LIKE = 13, CONTEXTMENU_DELETE = 14;
    protected DBAccessor dbHelper = null;
    LastPlayClass lastPlayObj;
    private LastPlayedListAdapter lastPlyedArrayAdapter;
    private ListView lastPlayedListView;
    private ArrayList<String> songListString, fileString1,
            fileStringWithPath, quicklistString, playlistString, mTitleList, mTypeList, mAuthorList, mUrlList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.layout_lastnotes, container, false);
        lastPlayedListView = (ListView) view.findViewById(R.id.lastnoteslist);
        fillLastPlayedList(view);
        if (!(lastPlayObj.getPodcast_id().size() > 0)) {
            //  emptyListString.setVisibility(View.VISIBLE);
            //   emptyListString
            //          .setText("You have not taken any notes before. After taking a note on an MP3 file,these notes will be listed here for quick access.");
        } else {
            //    emptyListString.setVisibility(View.GONE);
        }
        lastPlyedArrayAdapter = new LastPlayedListAdapter(this.getActivity(), lastPlayObj.getNote_text(), lastPlayObj.getSongpath(), lastPlayObj.podcastName);
        lastPlayedListView.setAdapter(lastPlyedArrayAdapter);
        lastPlayedListView.setOnItemClickListener(new ItemClickListener());
        lastPlayedListView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v,
                                            ContextMenu.ContextMenuInfo menuInfo) {
                // TODO Auto-generated method stub
                menu.setHeaderTitle("Choose To?");
                menu.add(0, CONTEXTMENU_UPLOAD, 0, "Upload and Share");
                menu.add(0, CONTEXTMENU_EDIT, 0, "Edit Bookmark");
                menu.add(0, CONTEXTMENU_LIKE, 0, "Like Bookmark");
                menu.add(0, CONTEXTMENU_DELETE, 0, "Delete Bookmark");
            }
        });
        return view;
    }

    protected void fillLastPlayedList(View v) {
        lastPlayObj = new LastPlayClass();
        String sql = "Select * from podcast_notes order by last_listen_date_mil desc";
        if (null == dbHelper) {
            dbHelper = new DBAccessor(v.getContext());
            dbHelper.open();


        }
        Cursor mCursor = null;
        try {
            mCursor = dbHelper.getLastPlayedNotesListResult();
            if (mCursor.moveToFirst()) {
                do {

                    String podcast_id = mCursor.getString(mCursor
                            .getColumnIndex("podcast_id"));
                    String name = mCursor.getString(mCursor
                            .getColumnIndex("name"));
                    String song_sp_id = mCursor.getString(mCursor
                            .getColumnIndex("song_sp_id"));
                    String begin_sec = mCursor.getString(mCursor
                            .getColumnIndex("begin_sec"));
                    String end_sec = mCursor.getString(mCursor
                            .getColumnIndex("end_sec"));
                    String beginend = mCursor.getString(mCursor
                            .getColumnIndex("beginend"));
                    String songpath = mCursor.getString(mCursor
                            .getColumnIndex("songpath"));
                    String note_text = mCursor.getString(mCursor
                            .getColumnIndex("note_text"));
                    String author = mCursor.getString(mCursor
                            .getColumnIndex("author"));
                    String create_date = mCursor.getString(mCursor
                            .getColumnIndex("create_date"));
                    String last_listen_date_mil = mCursor.getString(mCursor
                            .getColumnIndex("last_listen_date_mil"));
                    String last_listen_date = mCursor.getString(mCursor
                            .getColumnIndex("last_listen_date"));
                    lastPlayObj.setPodcast_id(podcast_id);
                    lastPlayObj.setSong_sp_id(song_sp_id);
                    lastPlayObj.setBegin_sec(begin_sec);
                    lastPlayObj.setEnd_sec(end_sec);
                    lastPlayObj.setBeginend(beginend);
                    lastPlayObj.setSongpath(songpath);
                    lastPlayObj.setNote_text(note_text);
                    lastPlayObj.setAuthor(author);
                    lastPlayObj.setCreate_date(create_date);
                    lastPlayObj.setLast_listen_date_mil(last_listen_date_mil);
                    lastPlayObj.setLast_listen_date(last_listen_date);
                    lastPlayObj.podcastName.add(name);

                } while (mCursor.moveToNext());
            }
        } catch (Exception e) {
            Logger.getLogger(LastNotesFragment.class).debug(e.getMessage());
        } finally {
            if (mCursor != null && !mCursor.isClosed()) {
                mCursor.close();
            }
        }

    }

    class ItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {

            Log.e("mediapath", lastPlayObj.getSongpath().get(pos) + "");
            Log.e("sppos", lastPlayObj.getBegin_sec().get(pos) + "");

            String filePath =  lastPlayObj.getSongpath().get(pos);

            if(filePath.endsWith(".mp4")) {
                Intent i = new Intent(getActivity(), VideoScreen.class);
                PodcastData podcastData = dbHelper.getPodcastByUrl(filePath);
                CommonStaticClass.setCurrentPodcast(podcastData);
                i.putExtra("video_item", filePath);
                i.putExtra("video_item_id", podcastData.id);
                i.putExtra("sppos", Integer.parseInt(lastPlayObj.getBegin_sec().get(pos)));
                startActivity(i);
                return;
            }

            Intent i = new Intent(getActivity(), PlayerScreen.class);
            i.putExtra("mediapath", lastPlayObj.getSongpath().get(pos));
            i.putExtra("sppos",
                    Integer.parseInt(lastPlayObj.getBegin_sec().get(pos)));
            i.putExtra("fromBackground", false);

            PodcastData d = dbHelper.getPodcastByUrl(lastPlayObj.getSongpath().get(pos));
            CommonStaticClass.setCurrentPodcast(d);

            // i.putExtra("filelabel",lastPlayObj.get);
            if (lastPlayObj.getSongpath().get(pos).contains("http://") || lastPlayObj.getSongpath().get(pos).contains("https://")) {
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

    }

}

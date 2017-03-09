/*
 * Copyright 2010 Facebook, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.javathlon;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.javathlon.db.DBAccessor;

import java.io.File;
import java.util.ArrayList;

public class PlaylistCreatorDialog extends Dialog {

    static final float[] DIMENSIONS_DIFF_LANDSCAPE = {20, 60};
    static final float[] DIMENSIONS_DIFF_PORTRAIT = {40, 60};
    static final FrameLayout.LayoutParams FILL =
            new FrameLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                    ViewGroup.LayoutParams.FILL_PARENT);
    static final int MARGIN = 4;
    static final int PADDING = 2;
    static final String DISPLAY_STRING = "touch";


    private ImageView mCrossImage;
    private FrameLayout mContent;
    private Context con;
    private LayoutInflater mInflater;
    DBAccessor dbHelper = null;
    private ListView playlistnamesview;
    private EditText newPlaylistName;
    private Button createPlaylistBt;
    String inlist;
    boolean inListOrNot;

    ArrayList<String> playlistNames = new ArrayList<String>();
    ArrayList<Boolean> inPlayList = new ArrayList<Boolean>();
    CheckAdapter checkAdapter;
    String pathToEntry;
    boolean isitFile;

    public PlaylistCreatorDialog(Context context, DBAccessor dbHelper, String pathToEntry) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        con = context;
        mInflater = LayoutInflater.from(con);
        this.dbHelper = dbHelper;
        this.pathToEntry = pathToEntry;
        File checkFile = new File(pathToEntry);
        isitFile = true;
        if (checkFile.isDirectory()) {
            isitFile = false;
        }
    }

    private void removeFromThisPlaylist(String model) {
        // TODO Auto-generated method stub
        String sql = "delete from playlist where  playlist_name='" + model + "' AND songpath='" + pathToEntry + "'";
        if (dbHelper.executeInsertQuery(sql)) {
            Log.e("YES  deleted", "ok " + pathToEntry);
            fetchPlaylists();
        } else {
            Log.e("NO   a", "asasas");
        }
    }

    private void insertNewPlaylistIntoDb(String newPlayList, boolean bb) {
        // TODO Auto-generated method stub
        Log.e("newPlayList", newPlayList);
        if (!bb) {
            if (!playlistNames.isEmpty()) {
                for (int i = 0; i < playlistNames.size(); i++) {
                    if (playlistNames.get(i).equalsIgnoreCase(newPlayList)) {
                        return;
                    }
                }
            }
        }
        String yesNo = "";
        if (isitFile) {
            yesNo = "YES";
        } else {
            yesNo = "NO";
        }
        String sql = "insert into playlist (playlist_name,isitFile,songpath) values ('" + newPlayList + "','" + yesNo + "','" + pathToEntry + "')";
        if (dbHelper.executeInsertQuery(sql)) {
            Log.e("YES  a", "ok " + pathToEntry);
            fetchPlaylists();
        } else {
            Log.e("NO   a", "asasas");
        }
    }

    private void fetchPlaylists() {
        // TODO Auto-generated method stub
        if (!playlistNames.isEmpty()) {
            playlistNames.clear();
            inPlayList.clear();
        }
        String sql = "Select distinct playlist_name from playlist";
        Cursor mCursor = null;
        try {
            mCursor = dbHelper.executeQuery(sql);
            if (mCursor.moveToFirst()) {
                do {
                    String playlist_name = mCursor.getString(mCursor
                            .getColumnIndex("playlist_name"));
                    playlistNames.add(playlist_name);
                    inPlayList.add(false);

                } while (mCursor.moveToNext());
            }
        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            if (mCursor != null && !mCursor.isClosed()) {
                mCursor.close();
            }

        }

        String sql2 = "Select playlist_name,songpath from playlist where songpath = '" + pathToEntry + "'";
        Cursor m2Cursor = null;
        try {
            m2Cursor = dbHelper.executeQuery(sql2);
            if (m2Cursor.moveToFirst()) {
                do {
                    String playlist_name = m2Cursor.getString(m2Cursor
                            .getColumnIndex("playlist_name"));
                    Log.e("playlist_name", playlist_name);
                    String songpath = m2Cursor.getString(m2Cursor
                            .getColumnIndex("songpath"));
                    Log.e("songpath", songpath);
                    if (songpath.length() > 0) {
                        inPlayList.set(playlistNames.indexOf(playlist_name), true);
                    }

                } while (m2Cursor.moveToNext());
            }
        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            if (m2Cursor != null && !m2Cursor.isClosed()) {
                m2Cursor.close();
            }

        }
        checkAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mContent = new FrameLayout(getContext());

        /* Create the 'x' image, but don't add to the mContent layout yet
         * at this point, we only need to know its drawable width and height 
         * to place the webview
         */
        createCrossImage();
        
        /* Now we know 'x' drawable width and height, 
         * layout the webivew and add it the mContent layout
         */
        int crossWidth = mCrossImage.getDrawable().getIntrinsicWidth();
        setUpView(crossWidth / 2);
        
        /* Finally add the 'x' image to the mContent layout and
         * add mContent to the Dialog view
         */
        mContent.addView(mCrossImage, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        addContentView(mContent, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));

        loadGui();
        checkAdapter = new CheckAdapter(playlistNames);
        playlistnamesview.setAdapter(checkAdapter);

        fetchPlaylists();
    }

    private void loadGui() {
        // TODO Auto-generated method stub
        playlistnamesview = (ListView) findViewById(R.id.playlistnamesview);
        newPlaylistName = (EditText) findViewById(R.id.newPlaylistName);
        createPlaylistBt = (Button) findViewById(R.id.createPlaylistBt);
        createPlaylistBt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String newPlayList = newPlaylistName.getText().toString();
                Log.e("newPlayList", newPlayList);
                if (newPlayList.length() > 0) {
                    insertNewPlaylistIntoDb(newPlayList, false);
                }
            }
        });
    }

    private void createCrossImage() {
        mCrossImage = new ImageView(getContext());
        // Dismiss the dialog when user click on the 'x'
        mCrossImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PlaylistCreatorDialog.this.dismiss();
            }
        });
        Drawable crossDrawable = getContext().getResources().getDrawable(R.drawable.close);
        mCrossImage.setImageDrawable(crossDrawable);
        /* 'x' should not be visible while webview is loading
         * make it visible only after webview has fully loaded
        */
        mCrossImage.setVisibility(View.INVISIBLE);
    }

    private void setUpView(int margin) {
        LinearLayout webViewContainer = new LinearLayout(getContext());
        View playlistView = mInflater.inflate(R.layout.playlisteditor, null);

        playlistView.setLayoutParams(FILL);

        webViewContainer.setPadding(margin, margin, margin, margin);
        webViewContainer.addView(playlistView);
        mContent.addView(webViewContainer);
    }


    class CheckAdapter extends ArrayAdapter<String> {
        CheckAdapter(ArrayList<String> list) {
            super(con, R.layout.listitem, list);
        }

        public View getView(int position, View convertView,
                            ViewGroup parent) {
            View row = convertView;
            ViewWrapper wrapper;
            if (row == null) {

                row = mInflater.inflate(R.layout.listitem, null);
                wrapper = new ViewWrapper(row);
                row.setTag(wrapper);
            } else {
                wrapper = (ViewWrapper) row.getTag();
            }
            inlist = playlistNames.get(position);
            inListOrNot = inPlayList.get(position);
            wrapper.getLabel().setText(inlist);
            wrapper.getCheckBox().setChecked(inListOrNot);
            wrapper.getCheckBox().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView,
                                             boolean isChecked) {
                    // TODO Auto-generated method stub
                    String aa = inlist;
                    if (isChecked) {
                        insertNewPlaylistIntoDb(aa, true);
                    } else {
                        removeFromThisPlaylist(aa);
                    }
                }

            });
            return (row);
        }
    }
}

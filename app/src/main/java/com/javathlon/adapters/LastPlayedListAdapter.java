package com.javathlon.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.javathlon.R;

import java.util.ArrayList;

public class LastPlayedListAdapter extends BaseAdapter {
    Activity context;
    ArrayList<String> mFileList, mSongList, podcastNames;
    private LayoutInflater mInflater;

    public LastPlayedListAdapter(Activity context, ArrayList<String> mFileList, ArrayList<String> mSongList, ArrayList<String> podcastNames) {
        this.mFileList = mFileList;
        this.mSongList = mSongList;
        this.podcastNames = podcastNames;
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }

    public View getView(int position, View convertView,
                        ViewGroup parent) {

        ViewHolder holder;

        convertView = mInflater.inflate(R.layout.audioplayout, null);
        holder = new ViewHolder();

        holder.positionTxt = (TextView) convertView.findViewById(R.id.audioTrackName);
        holder.icon = (ImageView) convertView.findViewById(R.id.audioTrackIcon);
        convertView.setTag(holder);
        holder.icon.setBackgroundResource(R.drawable.ic_menu_compose);
        String testNote = (String) mFileList.get(position);
        String testSong = (String) podcastNames.get(position);
        String songName = testSong != null  &&  testSong.contains("/") ?  testSong.substring(testSong.lastIndexOf("/") + 1) : "";
        if (testNote.length() > 100) {
            testNote = testNote.substring(0, 100) + "\n..." + songName;
        } else {
            testNote = testNote + "\n..." +  songName;
        }
        holder.positionTxt.setText(testNote);

        return (convertView);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mFileList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return mFileList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    static class ViewHolder {
        TextView positionTxt;
        ImageView icon;
    }
}

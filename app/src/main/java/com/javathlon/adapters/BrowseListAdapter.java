package com.javathlon.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.javathlon.R;

import java.io.File;
import java.util.List;

public class BrowseListAdapter extends BaseAdapter {
    Activity context;
    List<String> mFileList, mFileListWithPath;
    private LayoutInflater mInflater;

    public BrowseListAdapter(Activity context, List<String> mFileList, List<String> mFileListWithPath) {
        this.mFileList = mFileList;
        this.mFileListWithPath = mFileListWithPath;
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

        File f = new File(mFileListWithPath.get(position));
        if (f.isDirectory()) {
            holder.icon.setBackgroundResource(R.drawable.ic_menu_archive);
        } else {
            holder.icon.setBackgroundResource(R.drawable.ic_menu_play_clip);
        }
        holder.positionTxt.setText((String) mFileList.get(position));

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

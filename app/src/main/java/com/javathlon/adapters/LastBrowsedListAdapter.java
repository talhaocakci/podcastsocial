package com.javathlon.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.javathlon.R;
import com.javathlon.components.LastBrowseClass;

import java.util.List;

public class LastBrowsedListAdapter extends BaseAdapter {
    Activity context;
    List<LastBrowseClass> mFileList;
    private LayoutInflater mInflater;

    public LastBrowsedListAdapter(Activity context, List<LastBrowseClass> mFileList) {
        this.mFileList = mFileList;
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
        holder.icon.setBackgroundResource(R.drawable.ic_menu_play_clip);
        holder.positionTxt.setText((String) mFileList.get(position).getFile_name().get(0));

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

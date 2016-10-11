package com.javathlon.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.javathlon.R;

import java.util.ArrayList;

public class AudioVideoListAdapter extends BaseAdapter {
    Context context;
    ArrayList<String> mTitleList, mTypeList, mAuthorList, mUrlList;
    private LayoutInflater mInflater;

    public AudioVideoListAdapter(Context context, ArrayList<String> mTitleList, ArrayList<String> mTypeList, ArrayList<String> mAuthorList, ArrayList<String> mUrlList) {
        this.mTitleList = mTitleList;
        this.mTypeList = mTypeList;
        this.mAuthorList = mAuthorList;
        this.mUrlList = mUrlList;
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }

    public View getView(int position, View convertView,
                        ViewGroup parent) {

        ViewHolder holder;

        convertView = mInflater.inflate(R.layout.avlistitemlayout, null);
        holder = new ViewHolder();

        holder.positionTxt = (TextView) convertView.findViewById(R.id.audioTrackName);
        holder.byAuthor = (TextView) convertView.findViewById(R.id.byAuthor);
        holder.icon = (ImageView) convertView.findViewById(R.id.audioTrackIcon);
        convertView.setTag(holder);

//      String f = mTypeList.get(position);
        if (mUrlList.get(position).contains("mp3")) {
            holder.icon.setBackgroundResource(R.drawable.rssaudioicon);
            Log.e("mUrlList.get(position)", mUrlList.get(position));
        } else {
            holder.icon.setBackgroundResource(R.drawable.rssvideoicon);
            Log.e(" else mUrlList.get(position)", mUrlList.get(position));
        }
        holder.positionTxt.setText((String) mTitleList.get(position));
        holder.byAuthor.setText((String) mAuthorList.get(position));

        return (convertView);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mTitleList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return mTitleList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    static class ViewHolder {
        TextView positionTxt, byAuthor;
        ImageView icon;
    }
}

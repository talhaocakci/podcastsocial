package com.javathlon.adapters;

/**
 * Created by talha on 28.02.2015.
 */

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.javathlon.CatalogData;
import com.javathlon.R;
import com.squareup.picasso.Picasso;

import java.util.List;


public class PodcastCatalogAdapter extends BaseAdapter {
    Activity context;
    List<CatalogData> catalogList;
    ImageView imageView;
    private LayoutInflater mInflater;

    public PodcastCatalogAdapter(Activity context,
                                 List<CatalogData> catalogList) {

        this.context = context;
        mInflater = LayoutInflater.from(context);
        this.catalogList = catalogList;
    }

    public View getView(int position, View convertView,
                        ViewGroup parent) {

        ViewHolder holder;

        convertView = mInflater.inflate(R.layout.audioplayout, null);
        holder = new ViewHolder();

        holder.name = (TextView) convertView.findViewById(R.id.audioTrackName);
        holder.icon = (ImageView) convertView.findViewById(R.id.audioTrackIcon);
        convertView.setTag(holder);
        Picasso.with(context).load((String) catalogList.get(position).imageSmall).into(holder.icon);
        holder.name.setText((String) catalogList.get(position).name);
        return (convertView);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return catalogList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return catalogList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    static class ViewHolder {
        TextView name;
        ImageView icon;
    }
}

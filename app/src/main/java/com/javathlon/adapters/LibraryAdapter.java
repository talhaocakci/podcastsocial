package com.javathlon.adapters;

/**
 * Created by talha on 01.03.2015.
 */

import android.app.Activity;
import android.app.Fragment;
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

public class LibraryAdapter extends BaseAdapter {
    public List<CatalogData> catalogList;
    Activity context;
    Fragment fragment;
    private LayoutInflater mInflater;


    public LibraryAdapter(Activity context, Fragment fragment, List<CatalogData> catalogList) {
        this.catalogList = catalogList;
        this.context = context;
        mInflater = LayoutInflater.from(context);
        this.fragment = fragment;
    }

    public View getView(int position, View convertView,
                        ViewGroup parent) {

        ViewHolder holder;

        convertView = mInflater.inflate(R.layout.library_item, null);
        holder = new ViewHolder();

        holder.trackCount = (TextView) convertView.findViewById(R.id.trackcount);
        holder.icon = (ImageView) convertView.findViewById(R.id.podcastImage);
        convertView.setTag(holder);
        Picasso.with(convertView.getContext()).load(catalogList.get(position).image).into(holder.icon);
        String trackCount = catalogList.get(position).trackCount + " item";
        holder.trackCount.setText(trackCount);

        holder.podcastName = (TextView) convertView.findViewById(R.id.podcastname);
        holder.artist = (TextView) convertView.findViewById(R.id.artistName);

        holder.podcastName.setText(catalogList.get(position).name);
        holder.artist.setText(catalogList.get(position).author);


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
        TextView podcastName;
        TextView artist;
        TextView trackCount;

        ImageView icon;

    }
}


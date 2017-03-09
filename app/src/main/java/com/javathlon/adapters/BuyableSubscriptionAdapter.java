package com.javathlon.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.javathlon.R;
import com.javathlon.apiclient.model.SubscriptionItem;

import java.util.List;

/**
 * Created by ocakcit on 11/02/2017.
 */

public class BuyableSubscriptionAdapter extends BaseAdapter {

    List<SubscriptionItem> itemList;
    Activity context;
    private LayoutInflater mInflater;


    public BuyableSubscriptionAdapter(Activity context, List<SubscriptionItem> items) {
        this.itemList = items;
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return itemList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BuyableSubscriptionAdapter.ViewHolder holder;

        convertView = mInflater.inflate(R.layout.buyable_subscription_item, null);
        holder = new BuyableSubscriptionAdapter.ViewHolder();

        holder.itemName = (TextView) convertView.findViewById(R.id.itemname);
        holder.itemDescription = (TextView) convertView.findViewById(R.id.itemdescription);
        holder.price = (TextView) convertView.findViewById(R.id.itemprice);
        holder.duration = (TextView) convertView.findViewById(R.id.itemduration);
        convertView.setTag(holder);

        holder.itemName.setText(itemList.get(position).getName());
        holder.itemDescription.setText(itemList.get(position).getDescription());
        holder.price.setText(itemList.get(position).getPrice() + " USD");
        holder.duration.setText(itemList.get(position).getDurationDay() + " Day");

        return convertView;

    }

    static class ViewHolder {
        TextView itemName;
        TextView itemDescription;
        TextView duration;
        TextView price;
        ImageView icon;

    }
}

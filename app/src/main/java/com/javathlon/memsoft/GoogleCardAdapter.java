package com.javathlon.memsoft;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.javathlon.BaseActivity;
import com.javathlon.CatalogData;
import com.javathlon.R;
import com.javathlon.db.DBAccessor;
import com.javathlon.rss.RssListPlayerActivity;

import java.util.List;

public class GoogleCardAdapter extends ArrayAdapter<CatalogData>
        implements OnClickListener {

    private LayoutInflater mInflater;

    DBAccessor db;
    BaseActivity activity;

    public GoogleCardAdapter(Context context, List<CatalogData> items, BaseActivity activity) {
        super(context, 0, items);
        this.activity = activity;
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    private void openRssActivity(String rss){
        Intent intent = new Intent(getContext(), RssListPlayerActivity.class);
        FragmentManager fm = ((Activity) GoogleCardAdapter.this.getContext()).getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        RssListPlayerActivity fragmentHome = new RssListPlayerActivity();
        Bundle bundle = new Bundle();
        bundle.putString("rss", rss);
        fragmentHome.setArguments(bundle);
        ft.replace(R.id.content_frame, fragmentHome).addToBackStack("tag");
        ft.commit();
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).id;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(
                    R.layout.maincatalog, parent, false);
            holder = new ViewHolder();
            holder.image = (ImageView) convertView
                    .findViewById(R.id.list_item_google_cards_travel_image);
            holder.categoryName = (TextView) convertView
                    .findViewById(R.id.list_item_google_cards_travel_category_name);
            holder.title = (TextView) convertView
                    .findViewById(R.id.list_item_google_cards_travel_title);
            holder.text = (TextView) convertView
                    .findViewById(R.id.list_item_google_cards_travel_text);
            holder.subscribeButton = (TextView) convertView
                    .findViewById(R.id.list_item_google_cards_travel_explore);
            holder.rssItemsButton = (TextView) convertView
                    .findViewById(R.id.list_item_google_cards_travel_share);
            holder.rssItemsButton.setText(convertView.getResources().getString(R.string.episodes));
            holder.text.setText(this.getItem(position).author);

            holder.image.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    openRssActivity(GoogleCardAdapter.this.getItem(position).rss);
                }
            });
            //   holder.categoryName.setText(this.getItem(position).name);
            holder.rssItemsButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {

                        openRssActivity(GoogleCardAdapter.this.getItem(position).rss);
                }
            });
            CatalogData catalog = GoogleCardAdapter.this.getItem(position);
            if (catalog != null && catalog.isSubscribed != null && catalog.isSubscribed.equals("y")) {
                holder.subscribeButton.setText(R.string.unsubscribe);
            }
            holder.subscribeButton.setOnClickListener(
                    new OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            String confirmText ="";
                            CatalogData data = (GoogleCardAdapter.this.getItem(position));
                            if(data.isSubscribed != null && data.isSubscribed.equals("y"))
                                    confirmText = activity.getResources().getString(R.string.unsubscribeConfirm);
                            else
                            confirmText = activity.getResources().getString(R.string.subscribeConfirm);


                            activity.showConfirmDialog(confirmText,activity.getResources().getString(R.string.yes), activity.getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                   CatalogData data = (GoogleCardAdapter.this.getItem(position));
                                    if (db == null) {
                                        db = new DBAccessor(getContext());
                                        db.open();
                                    }
                                    String value = "y";
                                    if (data != null && data.isSubscribed != null && data.isSubscribed.equals("y")) {
                                        value = "n";

                                    }
                                    data.isSubscribed = value;
                                    db.updatePodcastIsSubscribed(data.id, value);


                                    TextView self = (TextView) holder.subscribeButton;
                                    self.setText(value != null && value.equals("y") ? R.string.unsubscribe : R.string.subscribe);
                                }
                            }, null);
                        }
                    }
            );

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        CatalogData item = getItem(position);
        ImageUtil.displayImage(holder.image, item.image, null);
        holder.title.setText(item.name);
        holder.subscribeButton.setTag(position);
        holder.rssItemsButton.setTag(position);

        return convertView;
    }

    private static class ViewHolder {
        public ImageView image;
        public TextView categoryName;
        public TextView title;
        public TextView text;
        public TextView subscribeButton;
        public TextView rssItemsButton;
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        int possition = (Integer) v.getTag();
       /* switch (v.getId()) {
            case R.id.list_item_google_cards_travel_subscribeButton:
                // click on subscribeButton button
                break;
            case R.id.list_item_google_cards_travel_rssItemsButton:
                // click on rssItemsButton button
                break;
        }*/
    }


}

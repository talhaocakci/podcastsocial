package com.javathlon.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.javathlon.AnimatedExpandableListView;
import com.javathlon.BuySubscriptionActivity;
import com.javathlon.ExpandableMediaListViewActivity;
import com.javathlon.R;
import com.javathlon.memsoft.FontelloTextView;
import com.javathlon.memsoft.ImageUtil;
import com.javathlon.memsoft.RobotoTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ocakcit on 08/03/2017.
 */

public class GroupAndChildAdapter extends AnimatedExpandableListView.AnimatedExpandableListAdapter implements View.OnClickListener {
    private LayoutInflater inflater;

    private List<GroupItem> items;
    
    private Context context;

    public GroupAndChildAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        this.context = context;
    }

    public void setData(List<GroupItem> items) {
        this.items = items;
    }

    @Override
    public ChildItem getChild(int groupPosition, int childPosition) {
        return items.get(groupPosition).items.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getRealChildView(int groupPosition, int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {
        ChildHolder holder;
        ChildItem item = getChild(groupPosition, childPosition);
        if (convertView == null) {
            holder = new ChildHolder();
            convertView = inflater.inflate(
                    R.layout.list_item_expandable_media_child, parent,
                    false);
            holder.title = (TextView) convertView
                    .findViewById(R.id.list_item_expandable_media_child_name);
            holder.publish = (TextView) convertView
                    .findViewById(R.id.list_item_expandable_media_child_publish);
            holder.image = (ImageView) convertView
                    .findViewById(R.id.list_item_expandable_media_child_image);
            convertView.setTag(holder);
        } else {
            holder = (ChildHolder) convertView.getTag();
        }

        holder.title.setText(item.title);
        ImageUtil.displayImage(holder.image,
                item.imageUrl, null);
        return convertView;
    }

    @Override
    public int getRealChildrenCount(int groupPosition) {
        return items.get(groupPosition).items.size();
    }

    @Override
    public GroupItem getGroup(int groupPosition) {
        return items.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return items.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        GroupHolder holder;
        GroupItem item = getGroup(groupPosition);

        if (convertView == null) {
            holder = new GroupHolder();
            convertView = inflater.inflate(
                    R.layout.list_item_expandable_media, parent, false);
            holder.image = (ImageView) convertView
                    .findViewById(R.id.list_item_expandable_media_image);
            holder.title = (TextView) convertView
                    .findViewById(R.id.list_item_expandable_media_title);
            holder.subtitle = (TextView) convertView
                    .findViewById(R.id.list_item_expandable_media_subtitle);
            holder.buy = (RobotoTextView) convertView
                    .findViewById(R.id.buyTextView);
            holder.buy.setOnClickListener(this);

            convertView.setTag(holder);
        } else {
            holder = (GroupHolder) convertView.getTag();
        }
        holder.buy.setTag(groupPosition);
        holder.title.setText(item.title);
        holder.subtitle.setText(item.subtitle.toUpperCase());
        ImageUtil.displayImage(holder.image,
                item.imageUrl, null);


        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int arg0, int arg1) {
        return true;
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        int position = (Integer) v.getTag();
        switch (v.getId()) {
            case R.id.buyTextView:
                ((BuySubscriptionActivity)context).initiatePurchasingItem(position);
                // click on explore button
            //    Toast.makeText(context, "Like " + possition, Toast.LENGTH_SHORT).show();
            //    break;

        }
    }

    public static class GroupItem {
        public String title;
        public String imageUrl;
        public String subtitle;
        public List<ChildItem> items = new ArrayList<ChildItem>();
        public String skuName;
    }

    public static class ChildItem {
        public String title;
        public String imageUrl;
    }

    private static class ChildHolder {
        public TextView title;
        public TextView publish;
        public ImageView image;
        public TextView iconPlay;
    }

    private static class GroupHolder {
        public TextView title;
        public ImageView image;
        public TextView subtitle;
        public TextView buy;
    }
}

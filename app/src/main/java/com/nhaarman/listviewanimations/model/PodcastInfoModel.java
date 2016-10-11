package com.nhaarman.listviewanimations.model;

import com.javathlon.CatalogData;

public class PodcastInfoModel {

    private long mId;
    private String mImageURL;
    private String mText;
    private int mIconRes;
    private String rssUrl;

    private CatalogData data;

    public PodcastInfoModel() {
    }

    public PodcastInfoModel(long id, String imageURL, String text, int iconRes) {
        mId = id;
        mImageURL = imageURL;
        mText = text;
        mIconRes = iconRes;
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public String getImageURL() {
        return mImageURL;
    }

    public void setImageURL(String imageURL) {
        mImageURL = imageURL;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }

    public int getIconRes() {
        return mIconRes;
    }

    public void setIconRes(int iconRes) {
        mIconRes = iconRes;
    }

    @Override
    public String toString() {
        return mText;
    }
}

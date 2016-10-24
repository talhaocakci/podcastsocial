package com.javathlon;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.javathlon.rss.RssListPlayerActivity;
import com.javathlon.video.VideoScreen;

public class ItemNavigationActivity extends Activity {

    private PodcastData nextItem;
    private PodcastData currentItem;

    private TextView nextItemDescription;
    Button nextItemButton, replayButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_navigation);

        nextItemDescription = (TextView) findViewById(R.id.nextItemDescription);
        replayButton = (Button) findViewById(R.id.prevItemButton);
        nextItemButton = (Button) findViewById(R.id.nextItemButton);

        if(RssListPlayerActivity.currentIndexInPodcastList < RssListPlayerActivity.podcastDataList.size()) {
            nextItem =  RssListPlayerActivity.podcastDataList.get(RssListPlayerActivity.currentIndexInPodcastList+1);
            nextItemDescription.setText(nextItem.editionTitle);
            nextItemButton.setVisibility(View.VISIBLE);
        }

            currentItem = RssListPlayerActivity.podcastDataList.get(RssListPlayerActivity.currentIndexInPodcastList);
            replayButton.setVisibility(View.VISIBLE);

    }

    public void goToNextItem(View view) {
        Intent i = new Intent(this, VideoScreen.class);
        CommonStaticClass.setCurrentPodcast(nextItem);
        i.putExtra("video_item", nextItem.url);
        i.putExtra("video_item_id", nextItem.id);
        i.putExtra("sppos", 0);
        startActivity(i);
    }

    public void replayItem(View view) {
        Intent i = new Intent(this, VideoScreen.class);
        CommonStaticClass.setCurrentPodcast(nextItem);
        i.putExtra("video_item", currentItem.url);
        i.putExtra("video_item_id", currentItem.id);
        i.putExtra("sppos", 0);
        startActivity(i);
    }

}

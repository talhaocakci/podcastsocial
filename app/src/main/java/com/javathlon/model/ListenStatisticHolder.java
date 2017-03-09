package com.javathlon.model;

import android.content.Context;

import com.javathlon.CommonStaticClass;
import com.javathlon.db.DBAccessor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by talha on 13.07.2015.
 */
public class ListenStatisticHolder {

    List<ListenStatistic> list = new ArrayList<ListenStatistic>();

    Context context;
    DBAccessor dbHelper;
    ListenStatistic statistic = new ListenStatistic();

    public ListenStatisticHolder(Context context) {
        this.context = context;
    }

    private void saveStatistics() {

        if (dbHelper == null) {
            dbHelper = new DBAccessor(context);
            dbHelper.open();
        }
        dbHelper.bulkInsertListenStatisctics(list);
        // şimdiye kadarki progress geçmişse %sini hesapla
        if (CommonStaticClass.getCurrentPodcast().progressSecond == null)
            CommonStaticClass.getCurrentPodcast().progressSecond = "0";
        if (list.get(list.size() - 1).getEndPos() > Long.parseLong(CommonStaticClass.getCurrentPodcast().progressSecond)) {
            dbHelper.updatePodcastProgressSecond(CommonStaticClass.getCurrentPodcast(), list.get(list.size() - 1).getEndPos());
            CommonStaticClass.getCurrentPodcast().progressSecond = list.get(list.size() - 1).getEndPos() + "";
        }
        list.clear();
    }

    /**
     * startPos: ms
     */
    public void startStatistic(long podcastId, long startPos) {

        statistic.setStartPos(startPos / 1000);
        statistic.setPodcastId(podcastId);

    }

    /**
     * endPos: ms
     */
    public void endStatistic(long endPos) {
        ListenStatistic s = new ListenStatistic();
        s.setPodcastId(statistic.getPodcastId());
        s.setStartPos(statistic.getStartPos());
        s.setEndPos(endPos / 1000);
        list.add(s);
        saveStatistics();
    }
}

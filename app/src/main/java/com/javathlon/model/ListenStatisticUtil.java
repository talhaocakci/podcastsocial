package com.javathlon.model;

import android.content.Context;

import com.javathlon.db.DBAccessor;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Created by talha on 13.07.2015.
 */
public class ListenStatisticUtil {

    private static DBAccessor dbHelper;

    public static List<ListenStatistic> mergeStatisticsForPodcast(long podcastId, Context context) {
        if (dbHelper == null) {
            dbHelper = new DBAccessor(context);
            dbHelper.open();
        }
        List<ListenStatistic> statisticList = dbHelper.getListenPositionsOfPodcast(podcastId);
        if (statisticList == null)
            return statisticList;
        Collections.sort(statisticList);
        Iterator<ListenStatistic> statisticIterator = statisticList.listIterator();
        while (statisticIterator.hasNext()) {
            ListenStatistic stat = statisticIterator.next();
            while (statisticIterator.hasNext()) {
                ListenStatistic statNext = statisticIterator.next();
                if (statNext.getStartPos() < stat.getEndPos()) {
                    stat.setEndPos(statNext.getEndPos());
                    statisticIterator.remove();
                } else
                    break;
            }

        }
        return statisticList;
    }

    public static void generateUsage(List<ListenStatistic> statistics, int precision, long duration) {

        short[] stats = new short[(int) (duration / precision / 10)];
        for (ListenStatistic statistic : statistics) {
            int start = (int) Math.floor(statistic.getStartPos() / precision);
            int end = (int) Math.ceil(statistic.getEndPos() / precision);
            for (int i = start; i <= end; i++) {
                stats[i] = 1;
            }
        }
        System.out.println(stats);
    }
}
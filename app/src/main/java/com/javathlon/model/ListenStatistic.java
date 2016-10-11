package com.javathlon.model;

/**
 * Created by talha on 13.07.2015.
 */
public class ListenStatistic implements Comparable<ListenStatistic> {
    @Override
    public int compareTo(ListenStatistic statistic) {
        return this.startPos - statistic.getStartPos() > 0 ? 1 : -1;
    }

    private long podcastId;
    private long startPos;
    private long endPos;
    private String createDate;

    public ListenStatistic() {

    }

    public ListenStatistic(long podcastId, long startPos, long endPos) {
        this.podcastId = podcastId;
        this.startPos = startPos;
        this.endPos = endPos;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public long getPodcastId() {
        return podcastId;
    }

    public void setPodcastId(long podcastId) {
        this.podcastId = podcastId;
    }

    public long getEndPos() {
        return endPos;
    }

    public void setEndPos(long endPos) {
        this.endPos = endPos;
    }

    public long getStartPos() {
        return startPos;
    }

    public void setStartPos(long startPos) {
        this.startPos = startPos;
    }
}

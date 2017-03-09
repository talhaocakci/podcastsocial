package com.javathlon;

/**
 * Created by talha on 25.02.2015.
 */
public class PodcastData {
    public String editionTitle;
    public String chapterTitle;
    public String url;
    public long publishDateLong;
    private String isDownloaded;
    public String progressSecond;
    public String devicePath;
    public Long id;
    public int catalogId;
    public long duration;
    public String durationString;
    public long size;
    public int progressPercentage = 0;
    public String description;
    public int downloadPercentage = 0;


    public String getIsDownloaded() {
        return isDownloaded;
    }

    public void setIsDownloaded(String isDownloaded) {
        this.isDownloaded = isDownloaded;
        if (isDownloaded != null && isDownloaded.equals("y"))
            downloadPercentage = 100;
        else
            downloadPercentage = 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PodcastData that = (PodcastData) o;

        if (!url.equals(that.url)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return url.hashCode();
    }
}

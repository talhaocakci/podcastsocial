package com.javathlon;

/**
 * Created by talha on 25.02.2015.
 */
public class CatalogData {
    public String name;
    public String image;
    public String rss;
    public String summary;
    public String lastRssUpdate;
    public String createDate;
    public String author;
    public Long id;
    public String imageSmall;
    public int trackCount;
    public String infoUrl;
    public String lastReleaseDate;
    public String primaryGenreName;
    public String isSubscribed = "n";
    public String isMainCatalog = "n";

    public CatalogData() {

    }

    public CatalogData(long id, String name, String image, String rss, String summary, String author) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.rss = rss;
        this.summary = summary;
        this.author = author;
    }
}

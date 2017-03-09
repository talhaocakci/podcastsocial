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
    public String bucketName = "";

    public CatalogData() {

    }

    public CatalogData(long id, String name, String image, String rss, String summary, String author, String bucketName) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.rss = rss;
        this.summary = summary;
        this.author = author;
        this.bucketName = bucketName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CatalogData that = (CatalogData) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (image != null ? !image.equals(that.image) : that.image != null) return false;
        if (rss != null ? !rss.equals(that.rss) : that.rss != null) return false;
        if (summary != null ? !summary.equals(that.summary) : that.summary != null) return false;
        if (author != null ? !author.equals(that.author) : that.author != null) return false;
        return imageSmall != null ? imageSmall.equals(that.imageSmall) : that.imageSmall == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (image != null ? image.hashCode() : 0);
        result = 31 * result + (rss != null ? rss.hashCode() : 0);
        result = 31 * result + (summary != null ? summary.hashCode() : 0);
        result = 31 * result + (author != null ? author.hashCode() : 0);
        result = 31 * result + (imageSmall != null ? imageSmall.hashCode() : 0);
        return result;
    }
}

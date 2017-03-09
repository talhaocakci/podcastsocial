package com.javathlon.model.podcastmodern;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Podcast {

    @SerializedName("podcastId")
    @Expose
    private Integer podcastId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("itunesUrl")
    @Expose
    private String itunesUrl;
    @SerializedName("otherRssUrl")
    @Expose
    private String otherRssUrl;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("currentItemCount")
    @Expose
    private String currentItemCount;
    @SerializedName("publisher")
    @Expose
    private String publisher;
    @SerializedName("isPublic")
    @Expose
    private String isPublic;
    @SerializedName("lastItemDate")
    @Expose()

    private String lastItemDate;
    @SerializedName("imageurl")
    @Expose
    private String imageUrl;
    @SerializedName("author")
    @Expose
    private String author;

    /**
     * No args constructor for use in serialization
     */
    public Podcast() {
    }

    /**
     * @param author
     * @param podcastId
     * @param imageUrl
     * @param description
     * @param name
     * @param currentItemCount
     * @param itunesUrl
     * @param lastItemDate
     * @param isPublic
     * @param otherRssUrl
     * @param publisher
     */
    public Podcast(Integer podcastId, String name, String itunesUrl, String otherRssUrl, String description, String currentItemCount, String publisher, String isPublic, String lastItemDate, String imageUrl, String author) {
        this.podcastId = podcastId;
        this.name = name;
        this.itunesUrl = itunesUrl;
        this.otherRssUrl = otherRssUrl;
        this.description = description;
        this.currentItemCount = currentItemCount;
        this.publisher = publisher;
        this.isPublic = isPublic;
        this.lastItemDate = lastItemDate;
        this.imageUrl = imageUrl;
        this.author = author;
    }

    /**
     * @return The podcastId
     */
    public Integer getPodcastId() {
        return podcastId;
    }

    /**
     * @param podcastId The podcastId
     */
    public void setPodcastId(Integer podcastId) {
        this.podcastId = podcastId;
    }

    /**
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return The itunesUrl
     */
    public String getItunesUrl() {
        return itunesUrl;
    }

    /**
     * @param itunesUrl The itunesUrl
     */
    public void setItunesUrl(String itunesUrl) {
        this.itunesUrl = itunesUrl;
    }

    /**
     * @return The otherRssUrl
     */
    public String getOtherRssUrl() {
        return otherRssUrl;
    }

    /**
     * @param otherRssUrl The otherRssUrl
     */
    public void setOtherRssUrl(String otherRssUrl) {
        this.otherRssUrl = otherRssUrl;
    }

    /**
     * @return The description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description The description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return The currentItemCount
     */
    public String getCurrentItemCount() {
        return currentItemCount;
    }

    /**
     * @param currentItemCount The currentItemCount
     */
    public void setCurrentItemCount(String currentItemCount) {
        this.currentItemCount = currentItemCount;
    }

    /**
     * @return The publisher
     */
    public String getPublisher() {
        return publisher;
    }

    /**
     * @param publisher The publisher
     */
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    /**
     * @return The isPublic
     */
    public String getIsPublic() {
        return isPublic;
    }

    /**
     * @param isPublic The isPublic
     */
    public void setIsPublic(String isPublic) {
        this.isPublic = isPublic;
    }

    /**
     * @return The lastItemDate
     */
    public String getLastItemDate() {
        return lastItemDate;
    }

    /**
     * @param lastItemDate The lastItemDate
     */
    public void setLastItemDate(String lastItemDate) {
        this.lastItemDate = lastItemDate;
    }

    /**
     * @return The imageUrl
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * @param imageUrl The imageUrl
     */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    /**
     * @return The author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * @param author The author
     */
    public void setAuthor(String author) {
        this.author = author;
    }


}

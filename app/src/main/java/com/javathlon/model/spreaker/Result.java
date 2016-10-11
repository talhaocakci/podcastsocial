
package com.javathlon.model.spreaker;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Result {

    @SerializedName("episode_id")
    @Expose
    private Long episodeId;
    @SerializedName("show_id")
    @Expose
    private Long showId;
    @SerializedName("user_id")
    @Expose
    private Long userId;
    @Expose
    private String permalink;
    @Expose
    private String type;
    @SerializedName("on_air")
    @Expose
    private Boolean onAir;
    @Expose
    private String title;
    @Expose
    private Long length;
    @SerializedName("published_at")
    @Expose
    private String publishedAt;
    @SerializedName("auto_published_at")
    @Expose
    private Object autoPublishedAt;
    @SerializedName("non_stop")
    @Expose
    private Boolean nonStop;
    @Expose
    private Boolean explicit;
    @SerializedName("download_enabled")
    @Expose
    private Boolean downloadEnabled;
    @Expose
    private Boolean hidden;
    @SerializedName("ihr_hidden")
    @Expose
    private Boolean ihrHidden;
    @SerializedName("stream_id")
    @Expose
    private Object streamId;
    @SerializedName("site_url")
    @Expose
    private String siteUrl;
    @SerializedName("api_url")
    @Expose
    private String apiUrl;
    @SerializedName("download_url")
    @Expose
    private String downloadUrl;
    @Expose
    private String description;
    @SerializedName("published_at_locale")
    @Expose
    private String publishedAtLocale;
    @Expose
    private Show show;
    @Expose
    private String status;
    @Expose
    private List<Object> styles = new ArrayList<Object>();
    @Expose
    private List<Tag> tags = new ArrayList<Tag>();
    @Expose
    private List<Category> categories = new ArrayList<Category>();
    @Expose
    private Category_ category;
    @Expose
    private Author author;
    @Expose
    private Stats stats;
    @Expose
    private Image__ image;

    /**
     * 
     * @return
     *     The episodeId
     */
    public Long getEpisodeId() {
        return episodeId;
    }

    /**
     * 
     * @param episodeId
     *     The episode_id
     */
    public void setEpisodeId(Long episodeId) {
        this.episodeId = episodeId;
    }

    /**
     * 
     * @return
     *     The showId
     */
    public Long getShowId() {
        return showId;
    }

    /**
     * 
     * @param showId
     *     The show_id
     */
    public void setShowId(Long showId) {
        this.showId = showId;
    }

    /**
     * 
     * @return
     *     The userId
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * 
     * @param userId
     *     The user_id
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * 
     * @return
     *     The permalink
     */
    public String getPermalink() {
        return permalink;
    }

    /**
     * 
     * @param permalink
     *     The permalink
     */
    public void setPermalink(String permalink) {
        this.permalink = permalink;
    }

    /**
     * 
     * @return
     *     The type
     */
    public String getType() {
        return type;
    }

    /**
     * 
     * @param type
     *     The type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 
     * @return
     *     The onAir
     */
    public Boolean getOnAir() {
        return onAir;
    }

    /**
     * 
     * @param onAir
     *     The on_air
     */
    public void setOnAir(Boolean onAir) {
        this.onAir = onAir;
    }

    /**
     * 
     * @return
     *     The title
     */
    public String getTitle() {
        return title;
    }

    /**
     * 
     * @param title
     *     The title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 
     * @return
     *     The length
     */
    public Long getLength() {
        return length;
    }

    /**
     * 
     * @param length
     *     The length
     */
    public void setLength(Long length) {
        this.length = length;
    }

    /**
     * 
     * @return
     *     The publishedAt
     */
    public String getPublishedAt() {
        return publishedAt;
    }

    /**
     * 
     * @param publishedAt
     *     The published_at
     */
    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    /**
     * 
     * @return
     *     The autoPublishedAt
     */
    public Object getAutoPublishedAt() {
        return autoPublishedAt;
    }

    /**
     * 
     * @param autoPublishedAt
     *     The auto_published_at
     */
    public void setAutoPublishedAt(Object autoPublishedAt) {
        this.autoPublishedAt = autoPublishedAt;
    }

    /**
     * 
     * @return
     *     The nonStop
     */
    public Boolean getNonStop() {
        return nonStop;
    }

    /**
     * 
     * @param nonStop
     *     The non_stop
     */
    public void setNonStop(Boolean nonStop) {
        this.nonStop = nonStop;
    }

    /**
     * 
     * @return
     *     The explicit
     */
    public Boolean getExplicit() {
        return explicit;
    }

    /**
     * 
     * @param explicit
     *     The explicit
     */
    public void setExplicit(Boolean explicit) {
        this.explicit = explicit;
    }

    /**
     * 
     * @return
     *     The downloadEnabled
     */
    public Boolean getDownloadEnabled() {
        return downloadEnabled;
    }

    /**
     * 
     * @param downloadEnabled
     *     The download_enabled
     */
    public void setDownloadEnabled(Boolean downloadEnabled) {
        this.downloadEnabled = downloadEnabled;
    }

    /**
     * 
     * @return
     *     The hidden
     */
    public Boolean getHidden() {
        return hidden;
    }

    /**
     * 
     * @param hidden
     *     The hidden
     */
    public void setHidden(Boolean hidden) {
        this.hidden = hidden;
    }

    /**
     * 
     * @return
     *     The ihrHidden
     */
    public Boolean getIhrHidden() {
        return ihrHidden;
    }

    /**
     * 
     * @param ihrHidden
     *     The ihr_hidden
     */
    public void setIhrHidden(Boolean ihrHidden) {
        this.ihrHidden = ihrHidden;
    }

    /**
     * 
     * @return
     *     The streamId
     */
    public Object getStreamId() {
        return streamId;
    }

    /**
     * 
     * @param streamId
     *     The stream_id
     */
    public void setStreamId(Object streamId) {
        this.streamId = streamId;
    }

    /**
     * 
     * @return
     *     The siteUrl
     */
    public String getSiteUrl() {
        return siteUrl;
    }

    /**
     * 
     * @param siteUrl
     *     The site_url
     */
    public void setSiteUrl(String siteUrl) {
        this.siteUrl = siteUrl;
    }

    /**
     * 
     * @return
     *     The apiUrl
     */
    public String getApiUrl() {
        return apiUrl;
    }

    /**
     * 
     * @param apiUrl
     *     The api_url
     */
    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    /**
     * 
     * @return
     *     The downloadUrl
     */
    public String getDownloadUrl() {
        return downloadUrl;
    }

    /**
     * 
     * @param downloadUrl
     *     The download_url
     */
    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    /**
     * 
     * @return
     *     The description
     */
    public String getDescription() {
        return description;
    }

    /**
     * 
     * @param description
     *     The description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 
     * @return
     *     The publishedAtLocale
     */
    public String getPublishedAtLocale() {
        return publishedAtLocale;
    }

    /**
     * 
     * @param publishedAtLocale
     *     The published_at_locale
     */
    public void setPublishedAtLocale(String publishedAtLocale) {
        this.publishedAtLocale = publishedAtLocale;
    }

    /**
     * 
     * @return
     *     The show
     */
    public Show getShow() {
        return show;
    }

    /**
     * 
     * @param show
     *     The show
     */
    public void setShow(Show show) {
        this.show = show;
    }

    /**
     * 
     * @return
     *     The status
     */
    public String getStatus() {
        return status;
    }

    /**
     * 
     * @param status
     *     The status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 
     * @return
     *     The styles
     */
    public List<Object> getStyles() {
        return styles;
    }

    /**
     * 
     * @param styles
     *     The styles
     */
    public void setStyles(List<Object> styles) {
        this.styles = styles;
    }

    /**
     * 
     * @return
     *     The tags
     */
    public List<Tag> getTags() {
        return tags;
    }

    /**
     * 
     * @param tags
     *     The tags
     */
    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    /**
     * 
     * @return
     *     The categories
     */
    public List<Category> getCategories() {
        return categories;
    }

    /**
     * 
     * @param categories
     *     The categories
     */
    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    /**
     * 
     * @return
     *     The category
     */
    public Category_ getCategory() {
        return category;
    }

    /**
     * 
     * @param category
     *     The category
     */
    public void setCategory(Category_ category) {
        this.category = category;
    }

    /**
     * 
     * @return
     *     The author
     */
    public Author getAuthor() {
        return author;
    }

    /**
     * 
     * @param author
     *     The author
     */
    public void setAuthor(Author author) {
        this.author = author;
    }

    /**
     * 
     * @return
     *     The stats
     */
    public Stats getStats() {
        return stats;
    }

    /**
     * 
     * @param stats
     *     The stats
     */
    public void setStats(Stats stats) {
        this.stats = stats;
    }

    /**
     * 
     * @return
     *     The image
     */
    public Image__ getImage() {
        return image;
    }

    /**
     * 
     * @param image
     *     The image
     */
    public void setImage(Image__ image) {
        this.image = image;
    }

}

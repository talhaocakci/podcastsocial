package com.javathlon.model.spreaker;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class Show {

    @SerializedName("show_id")
    @Expose
    private Long showId;
    @Expose
    private String title;
    @Expose
    private String permalink;
    @SerializedName("site_url")
    @Expose
    private String siteUrl;
    @SerializedName("api_url")
    @Expose
    private String apiUrl;
    @SerializedName("fb_page_id")
    @Expose
    private Object fbPageId;
    @SerializedName("fb_page_name")
    @Expose
    private Object fbPageName;
    @Expose
    private String language;
    @SerializedName("ihr_badge")
    @Expose
    private Boolean ihrBadge;
    @Expose
    private Image image;
    @Expose
    private Boolean fan;
    @Expose
    private Object email;
    @SerializedName("website_url")
    @Expose
    private Object websiteUrl;
    @SerializedName("facebook_url")
    @Expose
    private Object facebookUrl;
    @SerializedName("twitter_name")
    @Expose
    private Object twitterName;
    @SerializedName("skype_name")
    @Expose
    private Object skypeName;
    @SerializedName("tel_number")
    @Expose
    private Object telNumber;
    @SerializedName("sms_number")
    @Expose
    private Object smsNumber;
    @Expose
    private List<Author> authors = new ArrayList<Author>();
    @Expose
    private Stats stats;

    /**
     * @return The showId
     */
    public Long getShowId() {
        return showId;
    }

    /**
     * @param showId The show_id
     */
    public void setShowId(Long showId) {
        this.showId = showId;
    }

    /**
     * @return The title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title The title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return The permalink
     */
    public String getPermalink() {
        return permalink;
    }

    /**
     * @param permalink The permalink
     */
    public void setPermalink(String permalink) {
        this.permalink = permalink;
    }

    /**
     * @return The siteUrl
     */
    public String getSiteUrl() {
        return siteUrl;
    }

    /**
     * @param siteUrl The site_url
     */
    public void setSiteUrl(String siteUrl) {
        this.siteUrl = siteUrl;
    }

    /**
     * @return The apiUrl
     */
    public String getApiUrl() {
        return apiUrl;
    }

    /**
     * @param apiUrl The api_url
     */
    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    /**
     * @return The fbPageId
     */
    public Object getFbPageId() {
        return fbPageId;
    }

    /**
     * @param fbPageId The fb_page_id
     */
    public void setFbPageId(Object fbPageId) {
        this.fbPageId = fbPageId;
    }

    /**
     * @return The fbPageName
     */
    public Object getFbPageName() {
        return fbPageName;
    }

    /**
     * @param fbPageName The fb_page_name
     */
    public void setFbPageName(Object fbPageName) {
        this.fbPageName = fbPageName;
    }

    /**
     * @return The language
     */
    public String getLanguage() {
        return language;
    }

    /**
     * @param language The language
     */
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     * @return The ihrBadge
     */
    public Boolean getIhrBadge() {
        return ihrBadge;
    }

    /**
     * @param ihrBadge The ihr_badge
     */
    public void setIhrBadge(Boolean ihrBadge) {
        this.ihrBadge = ihrBadge;
    }

    /**
     * @return The image
     */
    public Image getImage() {
        return image;
    }

    /**
     * @param image The image
     */
    public void setImage(Image image) {
        this.image = image;
    }

    /**
     * @return The fan
     */
    public Boolean getFan() {
        return fan;
    }

    /**
     * @param fan The fan
     */
    public void setFan(Boolean fan) {
        this.fan = fan;
    }

    /**
     * @return The email
     */
    public Object getEmail() {
        return email;
    }

    /**
     * @param email The email
     */
    public void setEmail(Object email) {
        this.email = email;
    }

    /**
     * @return The websiteUrl
     */
    public Object getWebsiteUrl() {
        return websiteUrl;
    }

    /**
     * @param websiteUrl The website_url
     */
    public void setWebsiteUrl(Object websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    /**
     * @return The facebookUrl
     */
    public Object getFacebookUrl() {
        return facebookUrl;
    }

    /**
     * @param facebookUrl The facebook_url
     */
    public void setFacebookUrl(Object facebookUrl) {
        this.facebookUrl = facebookUrl;
    }

    /**
     * @return The twitterName
     */
    public Object getTwitterName() {
        return twitterName;
    }

    /**
     * @param twitterName The twitter_name
     */
    public void setTwitterName(Object twitterName) {
        this.twitterName = twitterName;
    }

    /**
     * @return The skypeName
     */
    public Object getSkypeName() {
        return skypeName;
    }

    /**
     * @param skypeName The skype_name
     */
    public void setSkypeName(Object skypeName) {
        this.skypeName = skypeName;
    }

    /**
     * @return The telNumber
     */
    public Object getTelNumber() {
        return telNumber;
    }

    /**
     * @param telNumber The tel_number
     */
    public void setTelNumber(Object telNumber) {
        this.telNumber = telNumber;
    }

    /**
     * @return The smsNumber
     */
    public Object getSmsNumber() {
        return smsNumber;
    }

    /**
     * @param smsNumber The sms_number
     */
    public void setSmsNumber(Object smsNumber) {
        this.smsNumber = smsNumber;
    }

    /**
     * @return The authors
     */
    public List<Author> getAuthors() {
        return authors;
    }

    /**
     * @param authors The authors
     */
    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }

    /**
     * @return The stats
     */
    public Stats getStats() {
        return stats;
    }

    /**
     * @param stats The stats
     */
    public void setStats(Stats stats) {
        this.stats = stats;
    }

}

package com.javathlon.model.spreaker;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Author {

    @SerializedName("user_id")
    @Expose
    private Long userId;
    @Expose
    private String fullname;
    @Expose
    private String type;
    @SerializedName("site_url")
    @Expose
    private String siteUrl;
    @SerializedName("shows_url")
    @Expose
    private String showsUrl;
    @Expose
    private String permalink;
    @SerializedName("profile_name")
    @Expose
    private String profileName;
    @Expose
    private Long occupation;
    @Expose
    private Image_ image;
    @Expose
    private String username;
    @SerializedName("fb_user_id")
    @Expose
    private String fbUserId;
    @Expose
    private String description;
    @Expose
    private String location;
    @SerializedName("location_latitude")
    @Expose
    private String locationLatitude;
    @SerializedName("location_longitude")
    @Expose
    private String locationLongitude;
    @Expose
    private Boolean fan;

    /**
     * @return The userId
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * @param userId The user_id
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * @return The fullname
     */
    public String getFullname() {
        return fullname;
    }

    /**
     * @param fullname The fullname
     */
    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    /**
     * @return The type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type The type
     */
    public void setType(String type) {
        this.type = type;
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
     * @return The showsUrl
     */
    public String getShowsUrl() {
        return showsUrl;
    }

    /**
     * @param showsUrl The shows_url
     */
    public void setShowsUrl(String showsUrl) {
        this.showsUrl = showsUrl;
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
     * @return The profileName
     */
    public String getProfileName() {
        return profileName;
    }

    /**
     * @param profileName The profile_name
     */
    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    /**
     * @return The occupation
     */
    public Long getOccupation() {
        return occupation;
    }

    /**
     * @param occupation The occupation
     */
    public void setOccupation(Long occupation) {
        this.occupation = occupation;
    }

    /**
     * @return The image
     */
    public Image_ getImage() {
        return image;
    }

    /**
     * @param image The image
     */
    public void setImage(Image_ image) {
        this.image = image;
    }

    /**
     * @return The username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username The username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return The fbUserId
     */
    public String getFbUserId() {
        return fbUserId;
    }

    /**
     * @param fbUserId The fb_user_id
     */
    public void setFbUserId(String fbUserId) {
        this.fbUserId = fbUserId;
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
     * @return The location
     */
    public String getLocation() {
        return location;
    }

    /**
     * @param location The location
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * @return The locationLatitude
     */
    public String getLocationLatitude() {
        return locationLatitude;
    }

    /**
     * @param locationLatitude The location_latitude
     */
    public void setLocationLatitude(String locationLatitude) {
        this.locationLatitude = locationLatitude;
    }

    /**
     * @return The locationLongitude
     */
    public String getLocationLongitude() {
        return locationLongitude;
    }

    /**
     * @param locationLongitude The location_longitude
     */
    public void setLocationLongitude(String locationLongitude) {
        this.locationLongitude = locationLongitude;
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

}

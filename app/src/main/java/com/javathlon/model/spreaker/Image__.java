package com.javathlon.model.spreaker;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Image__ {

    @SerializedName("image_id")
    @Expose
    private Long imageId;
    @Expose
    private Long type;
    @SerializedName("user_id")
    @Expose
    private Long userId;
    @SerializedName("small_url")
    @Expose
    private String smallUrl;
    @SerializedName("medium_url")
    @Expose
    private String mediumUrl;
    @SerializedName("large_url")
    @Expose
    private String largeUrl;
    @SerializedName("play_url")
    @Expose
    private String playUrl;

    /**
     * @return The imageId
     */
    public Long getImageId() {
        return imageId;
    }

    /**
     * @param imageId The image_id
     */
    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }

    /**
     * @return The type
     */
    public Long getType() {
        return type;
    }

    /**
     * @param type The type
     */
    public void setType(Long type) {
        this.type = type;
    }

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
     * @return The smallUrl
     */
    public String getSmallUrl() {
        return smallUrl;
    }

    /**
     * @param smallUrl The small_url
     */
    public void setSmallUrl(String smallUrl) {
        this.smallUrl = smallUrl;
    }

    /**
     * @return The mediumUrl
     */
    public String getMediumUrl() {
        return mediumUrl;
    }

    /**
     * @param mediumUrl The medium_url
     */
    public void setMediumUrl(String mediumUrl) {
        this.mediumUrl = mediumUrl;
    }

    /**
     * @return The largeUrl
     */
    public String getLargeUrl() {
        return largeUrl;
    }

    /**
     * @param largeUrl The large_url
     */
    public void setLargeUrl(String largeUrl) {
        this.largeUrl = largeUrl;
    }

    /**
     * @return The playUrl
     */
    public String getPlayUrl() {
        return playUrl;
    }

    /**
     * @param playUrl The play_url
     */
    public void setPlayUrl(String playUrl) {
        this.playUrl = playUrl;
    }

}

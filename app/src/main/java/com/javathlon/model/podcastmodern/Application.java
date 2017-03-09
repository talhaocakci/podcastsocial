package com.javathlon.model.podcastmodern;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class Application {

    @SerializedName("applicationId")
    @Expose
    private Integer applicationId;
    @SerializedName("applicationName")
    @Expose
    private String applicationName;
    @SerializedName("category")
    @Expose
    private Object category;
    @SerializedName("applicationUsers")
    @Expose
    private List<Object> applicationUsers = new ArrayList<Object>();
    @SerializedName("podcasts")
    @Expose
    private List<Podcast> podcasts = new ArrayList<Podcast>();

    /**
     * No args constructor for use in serialization
     */
    public Application() {
    }

    /**
     * @param podcasts
     * @param category
     * @param applicationUsers
     * @param applicationName
     * @param applicationId
     */
    public Application(Integer applicationId, String applicationName, Object category, List<Object> applicationUsers, List<Podcast> podcasts) {
        this.applicationId = applicationId;
        this.applicationName = applicationName;
        this.category = category;
        this.applicationUsers = applicationUsers;
        this.podcasts = podcasts;
    }

    /**
     * @return The applicationId
     */
    public Integer getApplicationId() {
        return applicationId;
    }

    /**
     * @param applicationId The applicationId
     */
    public void setApplicationId(Integer applicationId) {
        this.applicationId = applicationId;
    }

    /**
     * @return The applicationName
     */
    public String getApplicationName() {
        return applicationName;
    }

    /**
     * @param applicationName The applicationName
     */
    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    /**
     * @return The category
     */
    public Object getCategory() {
        return category;
    }

    /**
     * @param category The category
     */
    public void setCategory(Object category) {
        this.category = category;
    }

    /**
     * @return The applicationUsers
     */
    public List<Object> getApplicationUsers() {
        return applicationUsers;
    }

    /**
     * @param applicationUsers The applicationUsers
     */
    public void setApplicationUsers(List<Object> applicationUsers) {
        this.applicationUsers = applicationUsers;
    }

    /**
     * @return The podcasts
     */
    public List<Podcast> getPodcasts() {
        return podcasts;
    }

    /**
     * @param podcasts The podcasts
     */
    public void setPodcasts(List<Podcast> podcasts) {
        this.podcasts = podcasts;
    }


}

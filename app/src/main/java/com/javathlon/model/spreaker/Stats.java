package com.javathlon.model.spreaker;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Stats {

    @Expose
    private Long plays;
    @Expose
    private Long downloads;
    @Expose
    private Long fans;
    @SerializedName("listenable_episodes")
    @Expose
    private Long listenableEpisodes;
    @SerializedName("editable_episodes")
    @Expose
    private Long editableEpisodes;

    /**
     * @return The plays
     */
    public Long getPlays() {
        return plays;
    }

    /**
     * @param plays The plays
     */
    public void setPlays(Long plays) {
        this.plays = plays;
    }

    /**
     * @return The downloads
     */
    public Long getDownloads() {
        return downloads;
    }

    /**
     * @param downloads The downloads
     */
    public void setDownloads(Long downloads) {
        this.downloads = downloads;
    }

    /**
     * @return The fans
     */
    public Long getFans() {
        return fans;
    }

    /**
     * @param fans The fans
     */
    public void setFans(Long fans) {
        this.fans = fans;
    }

    /**
     * @return The listenableEpisodes
     */
    public Long getListenableEpisodes() {
        return listenableEpisodes;
    }

    /**
     * @param listenableEpisodes The listenable_episodes
     */
    public void setListenableEpisodes(Long listenableEpisodes) {
        this.listenableEpisodes = listenableEpisodes;
    }

    /**
     * @return The editableEpisodes
     */
    public Long getEditableEpisodes() {
        return editableEpisodes;
    }

    /**
     * @param editableEpisodes The editable_episodes
     */
    public void setEditableEpisodes(Long editableEpisodes) {
        this.editableEpisodes = editableEpisodes;
    }

}

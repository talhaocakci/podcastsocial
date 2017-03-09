package com.javathlon;

import java.util.Date;

/**
 * Created by ocakcit on 05/03/2017.
 */

public class PurchasedPodcastItem {

    private Long id;
    private Long podcastId;
    private Date validTill;

    public Long getPodcastId() {
        return podcastId;
    }

    public void setPodcastId(Long podcastId) {
        this.podcastId = podcastId;
    }

    public Date getValidTill() {
        return validTill;
    }

    public void setValidTill(Date validTill) {
        this.validTill = validTill;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}

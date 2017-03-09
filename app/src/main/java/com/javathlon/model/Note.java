package com.javathlon.model;

/**
 * Created by talha on 20.07.2015.
 */
public class Note {

    private Long id;
    private Long podcastId;
    private int beginSec;
    private int endSec;
    private String audioPath;
    private String text;
    private String author;
    private String createDate;
    private String uploadLink;
    private Long lastListenDateMil;
    private String lastListenDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPodcastId() {
        return podcastId;
    }

    public void setPodcastId(Long podcastId) {
        this.podcastId = podcastId;
    }

    public int getBeginSec() {
        return beginSec;
    }

    public void setBeginSec(int beginSec) {
        this.beginSec = beginSec;
    }

    public int getEndSec() {
        return endSec;
    }

    public void setEndSec(int endSec) {
        this.endSec = endSec;
    }

    public String getAudioPath() {
        return audioPath;
    }

    public void setAudioPath(String audioPath) {
        this.audioPath = audioPath;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getUploadLink() {
        return uploadLink;
    }

    public void setUploadLink(String uploadLink) {
        this.uploadLink = uploadLink;
    }

    public Long getLastListenDateMil() {
        return lastListenDateMil;
    }

    public void setLastListenDateMil(Long lastListenDateMil) {
        this.lastListenDateMil = lastListenDateMil;
    }

    public String getLastListenDate() {
        return lastListenDate;
    }

    public void setLastListenDate(String lastListenDate) {
        this.lastListenDate = lastListenDate;
    }
}

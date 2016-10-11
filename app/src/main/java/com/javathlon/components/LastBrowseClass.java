package com.javathlon.components;

import java.util.ArrayList;

public class LastBrowseClass {
    ArrayList<String> podcast_id = new ArrayList<String>();

    ArrayList<String> full_device_path = new ArrayList<String>();
    ArrayList<String> file_name = new ArrayList<String>();
    ArrayList<String> download_link = new ArrayList<String>();

    ArrayList<String> create_date = new ArrayList<String>();
    ArrayList<String> last_listen_date_mil = new ArrayList<String>();
    ArrayList<String> last_listen_date = new ArrayList<String>();

    public ArrayList<String> getPodcast_id() {
        return podcast_id;
    }

    public ArrayList<String> getFull_device_path() {
        return full_device_path;
    }

    public ArrayList<String> getFile_name() {
        return file_name;
    }

    public ArrayList<String> getDownload_link() {
        return download_link;
    }

    public ArrayList<String> getCreate_date() {
        return create_date;
    }

    public ArrayList<String> getLast_listen_date_mil() {
        return last_listen_date_mil;
    }

    public ArrayList<String> getLast_listen_date() {
        return last_listen_date;
    }

    public void setPodcast_id(String podcastId) {
        this.podcast_id.add(podcastId);
    }

    public void setFull_device_path(String fullDevicePath) {
        this.full_device_path.add(fullDevicePath);
    }

    public void setFile_name(String fileName) {
        this.file_name.add(fileName);
    }

    public void setDownload_link(String downloadLink) {
        this.download_link.add(downloadLink);
    }

    public void setCreate_date(String createDate) {
        this.create_date.add(createDate);
    }

    public void setLast_listen_date_mil(String lastListenDateMil) {
        this.last_listen_date_mil.add(lastListenDateMil);
    }

    public void setLast_listen_date(String lastListenDate) {
        this.last_listen_date.add(lastListenDate);
    }
}

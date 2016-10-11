package com.javathlon.components;

import java.util.ArrayList;

public class LastPlayClass {
    ArrayList<String> podcast_id = new ArrayList<String>();
    ArrayList<String> song_sp_id = new ArrayList<String>();
    ArrayList<String> begin_sec = new ArrayList<String>();
    ArrayList<String> end_sec = new ArrayList<String>();
    ArrayList<String> beginend = new ArrayList<String>();
    ArrayList<String> songpath = new ArrayList<String>();
    ArrayList<String> author = new ArrayList<String>();
    ArrayList<String> create_date = new ArrayList<String>();
    ArrayList<String> last_listen_date_mil = new ArrayList<String>();
    ArrayList<String> last_listen_date = new ArrayList<String>();
    ArrayList<String> note_text = new ArrayList<String>();
    public ArrayList<String> podcastName = new ArrayList<String>();

    public ArrayList<String> getPodcast_id() {
        return podcast_id;
    }

    public ArrayList<String> getSong_sp_id() {
        return song_sp_id;
    }

    public ArrayList<String> getBegin_sec() {
        return begin_sec;
    }

    public ArrayList<String> getEnd_sec() {
        return end_sec;
    }

    public ArrayList<String> getBeginend() {
        return beginend;
    }

    public ArrayList<String> getSongpath() {
        return songpath;
    }

    public ArrayList<String> getAuthor() {
        return author;
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

    public ArrayList<String> getNote_text() {
        return note_text;
    }

    public void setPodcast_id(String podcastId) {
        this.podcast_id.add(podcastId);
    }

    public void setSong_sp_id(String songSpId) {
        this.song_sp_id.add(songSpId);
    }

    public void setBegin_sec(String beginSec) {
        this.begin_sec.add(beginSec);
    }

    public void setEnd_sec(String endSec) {
        this.end_sec.add(endSec);
    }

    public void setBeginend(String beginend) {
        this.beginend.add(beginend);
    }

    public void setSongpath(String songpath) {
        this.songpath.add(songpath);
    }

    public void setAuthor(String author) {
        this.author.add(author);
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

    public void setNote_text(String noteText) {
        this.note_text.add(noteText);
    }

}

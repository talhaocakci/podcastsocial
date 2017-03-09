package com.javathlon.apiclient.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ocakcit on 05/02/2017.
 */

public class UsernamePassword {

    @SerializedName("j_username")
    private String username;

    @SerializedName("j_password")
    private String password;

    @SerializedName("remember-me")
    private boolean rememberMe;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(boolean rememberMe) {
        this.rememberMe = rememberMe;
    }
}

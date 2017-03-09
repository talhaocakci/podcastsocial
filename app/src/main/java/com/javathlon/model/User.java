package com.javathlon.model;

/**
 * Created by talha on 28.07.2015.
 */
public class User {

    private long userId;
    private String name;
    private String surname;
    private String displayName;
    private String email;
    private String gender;
    private int minAge;
    private String fbToken;
    private String googleToken;
    private String linkedin_token;
    private String swarmId;
    private String instagramId;
    private String registerDate;
    private String location;
    private String language;
    private String gcmToken;
    private String gooogleIdTokenForServer;

    public enum SOCIAL_TYPES {
        FACEBOOK, GOOGLE;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getMinAge() {
        return minAge;
    }

    public void setMinAge(int minAge) {
        this.minAge = minAge;
    }

    public String getFbToken() {
        return fbToken;
    }

    public void setFbToken(String fbToken) {
        this.fbToken = fbToken;
    }

    public String getGoogleToken() {
        return googleToken;
    }

    public void setGoogleToken(String googleToken) {
        this.googleToken = googleToken;
    }

    public String getLinkedin_token() {
        return linkedin_token;
    }

    public void setLinkedin_token(String linkedin_token) {
        this.linkedin_token = linkedin_token;
    }

    public String getSwarmId() {
        return swarmId;
    }

    public void setSwarmId(String swarmId) {
        this.swarmId = swarmId;
    }

    public String getInstagramId() {
        return instagramId;
    }

    public void setInstagramId(String instagramId) {
        this.instagramId = instagramId;
    }

    public String getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(String registerDate) {
        this.registerDate = registerDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getGcmToken() {
        return gcmToken;
    }

    public void setGcmToken(String gcmToken) {
        this.gcmToken = gcmToken;
    }

    public String getGooogleIdTokenForServer() {
        return gooogleIdTokenForServer;
    }

    public void setGooogleIdTokenForServer(String gooogleIdTokenForServer) {
        this.gooogleIdTokenForServer = gooogleIdTokenForServer;
    }
}


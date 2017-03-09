package com.javathlon.model.podcastmodern;

import com.javathlon.model.User;

import java.util.Date;

public class Subscription {

    private Long subscriptionId;
    private String subscriptionItem;
    private Application application;
    private User user;
    private String subscriptionDate;
    private Date trialEndDate;
    private String refreshPeriod;
    private Integer refreshCount;
    private String memberType;
    private String price;
    private String currency;
    // purchase flow identifier
    private String payload;

    public Subscription() {
    }

    public Subscription(Long subscriptionId, String subscriptionItem, Application application, User user, String
            subscriptionDate, Date trialEndDate, String refreshPeriod, Integer refreshCount, String memberType, String
                                price, String currency, String payload) {
        this.subscriptionId = subscriptionId;
        this.subscriptionItem = subscriptionItem;
        this.application = application;
        this.user = user;
        this.subscriptionDate = subscriptionDate;
        this.trialEndDate = trialEndDate;
        this.refreshPeriod = refreshPeriod;
        this.refreshCount = refreshCount;
        this.memberType = memberType;
        this.price = price;
        this.currency = currency;
        this.payload = payload;
    }

    public Long getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(Long subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public String getSubscriptionItem() {
        return subscriptionItem;
    }

    public void setSubscriptionItem(String subscriptionItem) {
        this.subscriptionItem = subscriptionItem;
    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getSubscriptionDate() {
        return subscriptionDate;
    }

    public void setSubscriptionDate(String subscriptionDate) {
        this.subscriptionDate = subscriptionDate;
    }

    public Date getTrialEndDate() {
        return trialEndDate;
    }

    public void setTrialEndDate(Date trialEndDate) {
        this.trialEndDate = trialEndDate;
    }

    public String getRefreshPeriod() {
        return refreshPeriod;
    }

    public void setRefreshPeriod(String refreshPeriod) {
        this.refreshPeriod = refreshPeriod;
    }

    public Integer getRefreshCount() {
        return refreshCount;
    }

    public void setRefreshCount(Integer refreshCount) {
        this.refreshCount = refreshCount;
    }

    public String getMemberType() {
        return memberType;
    }

    public void setMemberType(String memberType) {
        this.memberType = memberType;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }
}

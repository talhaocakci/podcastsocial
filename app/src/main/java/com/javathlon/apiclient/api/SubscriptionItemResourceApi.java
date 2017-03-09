package com.javathlon.apiclient.api;

import com.javathlon.apiclient.model.SubscriptionItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface SubscriptionitemresourceApi {


    @GET("api/subscription-items-by-app/{appId}")
    Call<List<SubscriptionItem>> getSubscriptionItems(@Path("appId") Long appId);

    @GET("api/subscription-items/by-podcast-id/{podcastId}")
    Call<List<SubscriptionItem>> getSubscriptionItemsByPodcast(@Path("podcastId") Long podcastId);
}

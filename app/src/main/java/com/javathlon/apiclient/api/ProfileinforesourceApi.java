package com.javathlon.apiclient.api;

import com.javathlon.apiclient.model.ProfileInfoResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ProfileinforesourceApi {
    /**
     * getActiveProfiles
     *
     * @return Call&lt;ProfileInfoResponse&gt;
     */

    @GET("api/profile-info")
    Call<ProfileInfoResponse> getActiveProfilesUsingGET();


}

package com.javathlon.apiclient.api;

import com.javathlon.apiclient.model.SecureUrlVM;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UrlResourceApi {
    /**
     * generateURL
     *
     * @param podcastId podcastId (required)
     * @param file      file (required)
     * @return Call&lt;SecureUrlVM&gt;
     */

    @GET("api/url/generateurl/{podcastId}")
    Call<SecureUrlVM> generateURLUsingGET(
            @Path("podcastId") String podcastId, @Query("file") String file
    );

}

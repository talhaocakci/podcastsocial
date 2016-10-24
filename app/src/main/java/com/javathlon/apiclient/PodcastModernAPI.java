package com.javathlon.apiclient;

import com.javathlon.memsoft.SecureUrl;
import com.javathlon.model.User;
import com.javathlon.model.podcastmodern.AccessToken;
import com.javathlon.model.podcastmodern.Application;
import com.javathlon.model.podcastmodern.Podcast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by ocakcit on 16/10/2016.
 */

public interface PodcastModernAPI {

    String REFRESH_GRANT_TYPE = "refresh_token";

    @GET("application/{appid}")
    public Call<Application> getPodcastsOfApp(@Path("appid") String appid);

    @GET("url/generateurl/{file}/{folder}")
    public Call<SecureUrl> getSecureURL(@Path("file") String file, @Path("folder") String folder);

    @POST("application/{appid}")
    Call<Long> saveUser( @Path("appid") Long appId, @Body User user);

    @POST("/oauth/token")
    @FormUrlEncoded
    void getUserCode(@Field("client_id") String clientId,
                         @Field("scope") String scope);

    @POST("/oauth/token")
    @FormUrlEncoded
    AccessToken getAccessToken(@Field("client_id") String clientId,
                               @Field("client_secret") String clientSecret,
                               @Field("code") String code,
                               @Field("grant_type") String grantType);

    @POST("/oauth/token")
    @FormUrlEncoded
    AccessToken refreshAccessToken(@Field("client_id") String clientId,
                                   @Field("client_secret") String clientSecret,
                                   @Field("refresh_token") String refreshToken,
                                   @Field("grant_type") String grantType);

}

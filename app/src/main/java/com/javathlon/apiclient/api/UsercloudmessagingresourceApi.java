package com.javathlon.apiclient.api;

import com.javathlon.apiclient.model.UserCloudMessagingDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UsercloudmessagingresourceApi {
    /**
     * createUserCloudMessaging
     *
     * @param userCloudMessagingDTO userCloudMessagingDTO (required)
     * @return Call&lt;UserCloudMessagingDTO&gt;
     */

    @POST("api/user-cloud-messagings")
    Call<UserCloudMessagingDTO> createUserCloudMessagingUsingPOST(
            @Body UserCloudMessagingDTO userCloudMessagingDTO
    );

    /**
     * deleteUserCloudMessaging
     *
     * @param id id (required)
     * @return Call&lt;Void&gt;
     */

    @DELETE("api/user-cloud-messagings/{id}")
    Call<Void> deleteUserCloudMessagingUsingDELETE(
            @Path("id") Long id
    );

    /**
     * getAllUserCloudMessagings
     *
     * @return Call&lt;List<UserCloudMessagingDTO>&gt;
     */

    @GET("api/user-cloud-messagings")
    Call<List<UserCloudMessagingDTO>> getAllUserCloudMessagingsUsingGET();


    /**
     * getUserCloudMessaging
     *
     * @param id id (required)
     * @return Call&lt;UserCloudMessagingDTO&gt;
     */

    @GET("api/user-cloud-messagings/{id}")
    Call<UserCloudMessagingDTO> getUserCloudMessagingUsingGET(
            @Path("id") Long id
    );

    /**
     * searchUserCloudMessagings
     *
     * @param query query (required)
     * @return Call&lt;List<UserCloudMessagingDTO>&gt;
     */

    @GET("api/_search/user-cloud-messagings")
    Call<List<UserCloudMessagingDTO>> searchUserCloudMessagingsUsingGET(
            @Query("query") String query
    );

    /**
     * updateUserCloudMessaging
     *
     * @param userCloudMessagingDTO userCloudMessagingDTO (required)
     * @return Call&lt;UserCloudMessagingDTO&gt;
     */

    @PUT("api/user-cloud-messagings")
    Call<UserCloudMessagingDTO> updateUserCloudMessagingUsingPUT(
            @Body UserCloudMessagingDTO userCloudMessagingDTO
    );

}

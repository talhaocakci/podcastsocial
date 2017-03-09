package com.javathlon.apiclient.api;

import com.javathlon.apiclient.model.UserSubscriptionDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UsersubscriptionresourceApi {
    /**
     * createUserSubscription
     *
     * @param userSubscriptionDTO userSubscriptionDTO (required)
     * @return Call&lt;UserSubscriptionDTO&gt;
     */

    @POST("api/user-subscriptions")
    Call<UserSubscriptionDTO> createUserSubscriptionUsingPOST(
            @Body UserSubscriptionDTO userSubscriptionDTO
    );

    /**
     * deleteUserSubscription
     *
     * @param id id (required)
     * @return Call&lt;Void&gt;
     */

    @DELETE("api/user-subscriptions/{id}")
    Call<Void> deleteUserSubscriptionUsingDELETE(
            @Path("id") Long id
    );

    /**
     * getAllUserSubscriptions
     *
     * @return Call&lt;List<UserSubscriptionDTO>&gt;
     */

    @GET("api/user-subscriptions")
    Call<List<UserSubscriptionDTO>> getAllUserSubscriptionsUsingGET();


    /**
     * getUserSubscription
     *
     * @param id id (required)
     * @return Call&lt;UserSubscriptionDTO&gt;
     */

    @GET("api/user-subscriptions/{id}")
    Call<UserSubscriptionDTO> getUserSubscriptionUsingGET(
            @Path("id") Long id
    );

    /**
     * searchUserSubscriptions
     *
     * @param query query (required)
     * @return Call&lt;List<UserSubscriptionDTO>&gt;
     */

    @GET("api/_search/user-subscriptions")
    Call<List<UserSubscriptionDTO>> searchUserSubscriptionsUsingGET(
            @Query("query") String query
    );

    /**
     * updateUserSubscription
     *
     * @param userSubscriptionDTO userSubscriptionDTO (required)
     * @return Call&lt;UserSubscriptionDTO&gt;
     */

    @PUT("api/user-subscriptions")
    Call<UserSubscriptionDTO> updateUserSubscriptionUsingPUT(
            @Body UserSubscriptionDTO userSubscriptionDTO
    );

}

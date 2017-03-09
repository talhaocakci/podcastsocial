package com.javathlon.apiclient.api;

import com.javathlon.apiclient.model.SubscriptionDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface SubscriptionresourceApi {
    /**
     * createSubscription
     *
     * @param subscriptionDTO subscriptionDTO (required)
     * @return Call&lt;SubscriptionDTO&gt;
     */

    @POST("api/subscriptions")
    Call<SubscriptionDTO> createSubscriptionUsingPOST(
            @Body SubscriptionDTO subscriptionDTO
    );

    /**
     * deleteSubscription
     *
     * @param id id (required)
     * @return Call&lt;Void&gt;
     */

    @DELETE("api/subscriptions/{id}")
    Call<Void> deleteSubscriptionUsingDELETE(
            @Path("id") Long id
    );

    /**
     * getAllSubscriptions
     *
     * @return Call&lt;List<SubscriptionDTO>&gt;
     */

    @GET("api/subscriptions")
    Call<List<SubscriptionDTO>> getAllSubscriptionsUsingGET();


    /**
     * getSubscription
     *
     * @param id id (required)
     * @return Call&lt;SubscriptionDTO&gt;
     */

    @GET("api/subscriptions/{id}")
    Call<SubscriptionDTO> getSubscriptionUsingGET(
            @Path("id") Long id
    );

    /**
     * searchSubscriptions
     *
     * @param query query (required)
     * @return Call&lt;List<SubscriptionDTO>&gt;
     */

    @GET("api/_search/subscriptions")
    Call<List<SubscriptionDTO>> searchSubscriptionsUsingGET(
            @Query("query") String query
    );

    /**
     * updateSubscription
     *
     * @param subscriptionDTO subscriptionDTO (required)
     * @return Call&lt;SubscriptionDTO&gt;
     */

    @PUT("api/subscriptions")
    Call<SubscriptionDTO> updateSubscriptionUsingPUT(
            @Body SubscriptionDTO subscriptionDTO
    );

}

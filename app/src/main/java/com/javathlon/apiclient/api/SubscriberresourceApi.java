package com.javathlon.apiclient.api;

import com.javathlon.apiclient.model.SubscriberDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface SubscriberresourceApi {
    /**
     * createSubscriber
     *
     * @param subscriberDTO subscriberDTO (required)
     * @return Call&lt;SubscriberDTO&gt;
     */

    @POST("api/subscribers")
    Call<SubscriberDTO> createSubscriberUsingPOST(
            @Body SubscriberDTO subscriberDTO
    );

    /**
     * deleteSubscriber
     *
     * @param id id (required)
     * @return Call&lt;Void&gt;
     */

    @DELETE("api/subscribers/{id}")
    Call<Void> deleteSubscriberUsingDELETE(
            @Path("id") Long id
    );

    /**
     * getAllSubscribers
     *
     * @return Call&lt;List<SubscriberDTO>&gt;
     */

    @GET("api/subscribers")
    Call<List<SubscriberDTO>> getAllSubscribersUsingGET();


    /**
     * getSubscriber
     *
     * @param id id (required)
     * @return Call&lt;SubscriberDTO&gt;
     */

    @GET("api/subscribers/{id}")
    Call<SubscriberDTO> getSubscriberUsingGET(
            @Path("id") Long id
    );

    /**
     * searchSubscribers
     *
     * @param query query (required)
     * @return Call&lt;List<SubscriberDTO>&gt;
     */

    @GET("api/_search/subscribers")
    Call<List<SubscriberDTO>> searchSubscribersUsingGET(
            @Query("query") String query
    );

    /**
     * updateSubscriber
     *
     * @param subscriberDTO subscriberDTO (required)
     * @return Call&lt;SubscriberDTO&gt;
     */

    @PUT("api/subscribers")
    Call<SubscriberDTO> updateSubscriberUsingPUT(
            @Body SubscriberDTO subscriberDTO
    );

}

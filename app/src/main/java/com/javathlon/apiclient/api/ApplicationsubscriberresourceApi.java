package com.javathlon.apiclient.api;

import com.javathlon.apiclient.model.ApplicationSubscriberDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApplicationsubscriberresourceApi {
    /**
     * createApplicationSubscriber
     *
     * @param applicationSubscriberDTO applicationSubscriberDTO (required)
     * @return Call&lt;ApplicationSubscriberDTO&gt;
     */

    @POST("api/application-subscribers")
    Call<ApplicationSubscriberDTO> createApplicationSubscriberUsingPOST(
            @Body ApplicationSubscriberDTO applicationSubscriberDTO
    );

    /**
     * deleteApplicationSubscriber
     *
     * @param id id (required)
     * @return Call&lt;Void&gt;
     */

    @DELETE("api/application-subscribers/{id}")
    Call<Void> deleteApplicationSubscriberUsingDELETE(
            @Path("id") Long id
    );

    /**
     * getAllApplicationSubscribers
     *
     * @return Call&lt;List<ApplicationSubscriberDTO>&gt;
     */

    @GET("api/application-subscribers")
    Call<List<ApplicationSubscriberDTO>> getAllApplicationSubscribersUsingGET();


    /**
     * getApplicationSubscriber
     *
     * @param id id (required)
     * @return Call&lt;ApplicationSubscriberDTO&gt;
     */

    @GET("api/application-subscribers/{id}")
    Call<ApplicationSubscriberDTO> getApplicationSubscriberUsingGET(
            @Path("id") Long id
    );

    /**
     * searchApplicationSubscribers
     *
     * @param query query (required)
     * @return Call&lt;List<ApplicationSubscriberDTO>&gt;
     */

    @GET("api/_search/application-subscribers")
    Call<List<ApplicationSubscriberDTO>> searchApplicationSubscribersUsingGET(
            @Query("query") String query
    );

    /**
     * updateApplicationSubscriber
     *
     * @param applicationSubscriberDTO applicationSubscriberDTO (required)
     * @return Call&lt;ApplicationSubscriberDTO&gt;
     */

    @PUT("api/application-subscribers")
    Call<ApplicationSubscriberDTO> updateApplicationSubscriberUsingPUT(
            @Body ApplicationSubscriberDTO applicationSubscriberDTO
    );

}

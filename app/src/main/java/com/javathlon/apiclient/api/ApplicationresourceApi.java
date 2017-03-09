package com.javathlon.apiclient.api;

import com.javathlon.apiclient.model.ApplicationDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApplicationresourceApi {
    /**
     * createApplication
     *
     * @param applicationDTO applicationDTO (required)
     * @return Call&lt;ApplicationDTO&gt;
     */

    @POST("api/applications")
    Call<ApplicationDTO> createApplicationUsingPOST(
            @Body ApplicationDTO applicationDTO
    );

    /**
     * deleteApplication
     *
     * @param id id (required)
     * @return Call&lt;Void&gt;
     */

    @DELETE("api/applications/{id}")
    Call<Void> deleteApplicationUsingDELETE(
            @Path("id") Long id
    );

    /**
     * getAllApplications
     *
     * @return Call&lt;List<ApplicationDTO>&gt;
     */

    @GET("api/applications")
    Call<List<ApplicationDTO>> getAllApplicationsUsingGET();


    /**
     * getApplication
     *
     * @param id id (required)
     * @return Call&lt;ApplicationDTO&gt;
     */

    @GET("api/applications/{id}")
    Call<ApplicationDTO> getApplicationUsingGET(
            @Path("id") Long id
    );

    /**
     * searchApplications
     *
     * @param query query (required)
     * @return Call&lt;List<ApplicationDTO>&gt;
     */

    @GET("api/_search/applications")
    Call<List<ApplicationDTO>> searchApplicationsUsingGET(
            @Query("query") String query
    );

    /**
     * updateApplication
     *
     * @param applicationDTO applicationDTO (required)
     * @return Call&lt;ApplicationDTO&gt;
     */

    @PUT("api/applications")
    Call<ApplicationDTO> updateApplicationUsingPUT(
            @Body ApplicationDTO applicationDTO
    );

}

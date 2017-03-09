package com.javathlon.apiclient.api;

import com.javathlon.apiclient.model.PodcastDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PodcastresourceApi {
    /**
     * createPodcast
     *
     * @param podcastDTO podcastDTO (required)
     * @return Call&lt;PodcastDTO&gt;
     */

    @POST("api/podcasts")
    Call<PodcastDTO> createPodcastUsingPOST(
            @Body PodcastDTO podcastDTO
    );

    /**
     * deletePodcast
     *
     * @param id id (required)
     * @return Call&lt;Void&gt;
     */

    @DELETE("api/podcasts/{id}")
    Call<Void> deletePodcastUsingDELETE(
            @Path("id") Long id
    );

    /**
     * getAllPodcasts
     *
     * @param page Page number of the requested page (optional)
     * @param size Size of a page (optional)
     * @param sort Sorting criteria in the format: property(,asc|desc). Default sort order is ascending. Multiple sort criteria are supported. (optional)
     * @return Call&lt;List<PodcastDTO>&gt;
     */

    @GET("api/podcasts")
    Call<List<PodcastDTO>> getAllPodcastsUsingGET(
            @Query("page") Integer page, @Query("size") Integer size, @Query("sort") List<String> sort
    );

    /**
     * getPodcast
     *
     * @param id id (required)
     * @return Call&lt;PodcastDTO&gt;
     */

    @GET("api/podcasts/{id}")
    Call<PodcastDTO> getPodcastUsingGET(
            @Path("id") Long id
    );

    /**
     * searchPodcasts
     *
     * @param query query (required)
     * @param page  Page number of the requested page (optional)
     * @param size  Size of a page (optional)
     * @param sort  Sorting criteria in the format: property(,asc|desc). Default sort order is ascending. Multiple sort criteria are supported. (optional)
     * @return Call&lt;List<PodcastDTO>&gt;
     */

    @GET("api/_search/podcasts")
    Call<List<PodcastDTO>> searchPodcastsUsingGET(
            @Query("query") String query, @Query("page") Integer page, @Query("size") Integer size, @Query("sort") List<String> sort
    );

    /**
     * updatePodcast
     *
     * @param podcastDTO podcastDTO (required)
     * @return Call&lt;PodcastDTO&gt;
     */

    @PUT("api/podcasts")
    Call<PodcastDTO> updatePodcastUsingPUT(
            @Body PodcastDTO podcastDTO
    );

}

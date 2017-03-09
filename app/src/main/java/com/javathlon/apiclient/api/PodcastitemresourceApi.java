package com.javathlon.apiclient.api;

import com.javathlon.apiclient.model.PodcastItemDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PodcastitemresourceApi {
    /**
     * createPodcastItem
     *
     * @param podcastItemDTO podcastItemDTO (required)
     * @return Call&lt;PodcastItemDTO&gt;
     */

    @POST("api/podcast-items")
    Call<PodcastItemDTO> createPodcastItemUsingPOST(
            @Body PodcastItemDTO podcastItemDTO
    );

    /**
     * deletePodcastItem
     *
     * @param id id (required)
     * @return Call&lt;Void&gt;
     */

    @DELETE("api/podcast-items/{id}")
    Call<Void> deletePodcastItemUsingDELETE(
            @Path("id") Long id
    );

    /**
     * getAllPodcastItems
     *
     * @param page Page number of the requested page (optional)
     * @param size Size of a page (optional)
     * @param sort Sorting criteria in the format: property(,asc|desc). Default sort order is ascending. Multiple sort criteria are supported. (optional)
     * @return Call&lt;List<PodcastItemDTO>&gt;
     */

    @GET("api/podcast-items")
    Call<List<PodcastItemDTO>> getAllPodcastItemsUsingGET(
            @Query("page") Integer page, @Query("size") Integer size, @Query("sort") List<String> sort
    );

    /**
     * getPodcastItem
     *
     * @param id id (required)
     * @return Call&lt;PodcastItemDTO&gt;
     */

    @GET("api/podcast-items/{id}")
    Call<PodcastItemDTO> getPodcastItemUsingGET(
            @Path("id") Long id
    );

    /**
     * searchPodcastItems
     *
     * @param query query (required)
     * @param page  Page number of the requested page (optional)
     * @param size  Size of a page (optional)
     * @param sort  Sorting criteria in the format: property(,asc|desc). Default sort order is ascending. Multiple sort criteria are supported. (optional)
     * @return Call&lt;List<PodcastItemDTO>&gt;
     */

    @GET("api/_search/podcast-items")
    Call<List<PodcastItemDTO>> searchPodcastItemsUsingGET(
            @Query("query") String query, @Query("page") Integer page, @Query("size") Integer size, @Query("sort") List<String> sort
    );

    /**
     * updatePodcastItem
     *
     * @param podcastItemDTO podcastItemDTO (required)
     * @return Call&lt;PodcastItemDTO&gt;
     */

    @PUT("api/podcast-items")
    Call<PodcastItemDTO> updatePodcastItemUsingPUT(
            @Body PodcastItemDTO podcastItemDTO
    );

}

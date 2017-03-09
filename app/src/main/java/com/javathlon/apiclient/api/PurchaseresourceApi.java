package com.javathlon.apiclient.api;

import com.javathlon.apiclient.model.PurchaseDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PurchaseresourceApi {
    /**
     * createPurchase
     *
     * @param purchaseDTO purchaseDTO (required)
     * @return Call&lt;PurchaseDTO&gt;
     */

    @POST("api/purchases")
    Call<PurchaseDTO> createPurchaseUsingPOST(
            @Body PurchaseDTO purchaseDTO
    );

    /**
     * deletePurchase
     *
     * @param id id (required)
     * @return Call&lt;Void&gt;
     */

    @DELETE("api/purchases/{id}")
    Call<Void> deletePurchaseUsingDELETE(
            @Path("id") Long id
    );

    /**
     * getAllPurchases
     *
     * @return Call&lt;List<PurchaseDTO>&gt;
     */

    @GET("api/purchases")
    Call<List<PurchaseDTO>> getAllPurchasesUsingGET();


    /**
     * getPurchase
     *
     * @param id id (required)
     * @return Call&lt;PurchaseDTO&gt;
     */

    @GET("api/purchases/{id}")
    Call<PurchaseDTO> getPurchaseUsingGET(
            @Path("id") Long id
    );

    /**
     * searchPurchases
     *
     * @param query query (required)
     * @return Call&lt;List<PurchaseDTO>&gt;
     */

    @GET("api/_search/purchases")
    Call<List<PurchaseDTO>> searchPurchasesUsingGET(
            @Query("query") String query
    );

    /**
     * updatePurchase
     *
     * @param purchaseDTO purchaseDTO (required)
     * @return Call&lt;PurchaseDTO&gt;
     */

    @PUT("api/purchases")
    Call<PurchaseDTO> updatePurchaseUsingPUT(
            @Body PurchaseDTO purchaseDTO
    );

}

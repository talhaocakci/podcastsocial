package com.javathlon.apiclient.api;


import com.javathlon.apiclient.model.KeyAndPasswordVM;
import com.javathlon.apiclient.model.ManagedUserVM;
import com.javathlon.apiclient.model.PersistentToken;
import com.javathlon.apiclient.model.UserDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface AccountresourceApi {

    @POST("api/authentication")
    @FormUrlEncoded
    Call<Void> authenticate(@Field("j_username") String username,
                            @Field("j_password") String password);


    @GET("api/activate")
    Call<String> activateAccountUsingGET(
            @Query("key") String key
    );

    @POST("api/register")
    Call<String> registerUser(@Body ManagedUserVM user);

    /**
     * changePassword
     *
     * @param password password (required)
     * @return Call&lt;Object&gt;
     */

    @POST("api/account/change_password")
    Call<Object> changePasswordUsingPOST(
            @Body String password
    );

    /**
     * finishPasswordReset
     *
     * @param keyAndPassword keyAndPassword (required)
     * @return Call&lt;String&gt;
     */

    @POST("api/account/reset_password/finish")
    Call<String> finishPasswordResetUsingPOST(
            @Body KeyAndPasswordVM keyAndPassword
    );

    /**
     * getAccount
     *
     * @return Call&lt;UserDTO&gt;
     */

    @GET("api/account")
    Call<UserDTO> getAccountUsingGET();


    /**
     * getCurrentSessions
     *
     * @return Call&lt;List<PersistentToken>&gt;
     */

    @GET("api/account/sessions")
    Call<List<PersistentToken>> getCurrentSessionsUsingGET();


    /**
     * invalidateSession
     *
     * @param series series (required)
     * @return Call&lt;Void&gt;
     */

    @DELETE("api/account/sessions/{series}")
    Call<Void> invalidateSessionUsingDELETE(
            @Path("series") String series
    );

    /**
     * isAuthenticated
     *
     * @return Call&lt;String&gt;
     */

    @GET("api/authenticate")
    Call<String> isAuthenticatedUsingGET();


    /**
     * registerAccount
     *
     * @param managedUserVM managedUserVM (required)
     * @return Call&lt;Object&gt;
     */

    @POST("api/register")
    Call<Object> registerAccountUsingPOST(
            @Body ManagedUserVM managedUserVM
    );

    /**
     * requestPasswordReset
     *
     * @param mail mail (required)
     * @return Call&lt;Object&gt;
     */

    @POST("api/account/reset_password/init")
    Call<Object> requestPasswordResetUsingPOST(
            @Body String mail
    );

    /**
     * saveAccount
     *
     * @param userDTO userDTO (required)
     * @return Call&lt;String&gt;
     */

    @POST("api/account")
    Call<String> saveAccountUsingPOST(
            @Body UserDTO userDTO
    );

}

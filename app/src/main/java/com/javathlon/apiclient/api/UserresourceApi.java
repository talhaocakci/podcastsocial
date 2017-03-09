package com.javathlon.apiclient.api;

import com.javathlon.apiclient.model.ManagedUserVM;
import com.javathlon.apiclient.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserresourceApi {
    /**
     * createUser
     *
     * @param managedUserVM managedUserVM (required)
     * @return Call&lt;Object&gt;
     */

    @POST("api/users")
    Call<Object> createUserUsingPOST(
            @Body ManagedUserVM managedUserVM
    );

    /**
     * deleteUser
     *
     * @param login login (required)
     * @return Call&lt;Void&gt;
     */

    @DELETE("api/users/{login}")
    Call<Void> deleteUserUsingDELETE(
            @Path("login") String login
    );

    /**
     * getAllUsers
     *
     * @param page Page number of the requested page (optional)
     * @param size Size of a page (optional)
     * @param sort Sorting criteria in the format: property(,asc|desc). Default sort order is ascending. Multiple sort criteria are supported. (optional)
     * @return Call&lt;List<ManagedUserVM>&gt;
     */

    @GET("api/users")
    Call<List<ManagedUserVM>> getAllUsersUsingGET(
            @Query("page") Integer page, @Query("size") Integer size, @Query("sort") List<String> sort
    );

    /**
     * getUser
     *
     * @param login login (required)
     * @return Call&lt;ManagedUserVM&gt;
     */

    @GET("api/users/{login}")
    Call<ManagedUserVM> getUserUsingGET(
            @Path("login") String login
    );

    /**
     * search
     *
     * @param query query (required)
     * @return Call&lt;List<User>&gt;
     */

    @GET("api/_search/users/{query}")
    Call<List<User>> searchUsingGET(
            @Path("query") String query
    );

    /**
     * updateUser
     *
     * @param managedUserVM managedUserVM (required)
     * @return Call&lt;ManagedUserVM&gt;
     */

    @PUT("api/users")
    Call<ManagedUserVM> updateUserUsingPUT(
            @Body ManagedUserVM managedUserVM
    );

}

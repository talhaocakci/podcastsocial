package com.javathlon.apiclient.api;

import com.javathlon.apiclient.model.EntityAuditEvent;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface EntityauditresourceApi {
    /**
     * getAuditedEntities
     *
     * @return Call&lt;List<String>&gt;
     */

    @GET("api/audits/entity/all")
    Call<List<String>> getAuditedEntitiesUsingGET();


    /**
     * getChanges
     *
     * @param entityType entityType (required)
     * @param limit      limit (required)
     * @return Call&lt;List<EntityAuditEvent>&gt;
     */

    @GET("api/audits/entity/changes")
    Call<List<EntityAuditEvent>> getChangesUsingGET(
            @Query("entityType") String entityType, @Query("limit") Integer limit
    );

    /**
     * getPrevVersion
     *
     * @param qualifiedName qualifiedName (required)
     * @param entityId      entityId (required)
     * @param commitVersion commitVersion (required)
     * @return Call&lt;EntityAuditEvent&gt;
     */

    @GET("api/audits/entity/changes/version/previous")
    Call<EntityAuditEvent> getPrevVersionUsingGET(
            @Query("qualifiedName") String qualifiedName, @Query("entityId") Long entityId, @Query("commitVersion") Integer commitVersion
    );

}

package com.studentcenter.weave.data.remote.api

import com.studentcenter.weave.data.remote.dto.team.CreateTeamReq
import com.studentcenter.weave.data.remote.dto.team.EditTeamReq
import com.studentcenter.weave.data.remote.dto.team.GetLocationsRes
import com.studentcenter.weave.data.remote.dto.team.GetMyTeamRes
import com.studentcenter.weave.data.remote.dto.team.GetTeamDetailRes
import com.studentcenter.weave.data.remote.dto.team.GetTeamListRes
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface TeamService {
    @GET("/api/meeting-teams")
    suspend fun getTeamList(
        @Header("Authorization") accessToken: String,
        @Query("memberCount") memberCount: Int?,
        @Query("youngestMemberBirthYear") youngestMemberBirthYear: Int,
        @Query("oldestMemberBirthYear") oldestMemberBirthYear: Int,
        @Query("preferredLocations") preferredLocations: List<String>?,
        @Query("next") next: String?,
        @Query("limit") limit: Int
    ): Response<GetTeamListRes>

    @POST("/api/meeting-teams")
    suspend fun createTeam(
        @Header("Authorization") accessToken: String,
        @Body body: CreateTeamReq
    ): Response<ResponseBody>

    @GET("/api/meeting-teams/my")
    suspend fun getMyTeam(
        @Header("Authorization") accessToken: String,
        @Query("next") next: String?,
        @Query("limit") limit: Int
    ): Response<GetMyTeamRes>

    @GET("/api/meeting-teams/{id}")
    suspend fun getTeamDetail(
        @Header("Authorization") accessToken: String,
        @Path("id") teamId: String
    ): Response<GetTeamDetailRes>

    @DELETE("/api/meeting-teams/{id}")
    suspend fun deleteTeam(
        @Header("Authorization") accessToken: String,
        @Path("id") teamId: String
    ): Response<ResponseBody>

    @PATCH("/api/meeting-teams/{id}")
    suspend fun editTeam(
        @Header("Authorization") accessToken: String,
        @Path("id") teamId: String,
        @Body body: EditTeamReq
    ): Response<ResponseBody>

    @GET("/api/meeting-teams/locations")
    suspend fun getLocations(): Response<GetLocationsRes>
}
package com.weave.weave.data.remote.api

import com.weave.weave.data.remote.dto.team.CreateTeamReq
import com.weave.weave.data.remote.dto.team.GetMyTeamRes
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface TeamService {
    @POST("/api/meeting-teams")
    suspend fun createTeam(
        @Header("Authorization") accessToken: String,
        @Body body: CreateTeamReq
    ): Response<ResponseBody>

    @GET("/api/meeting-teams/my")
    suspend fun getMyTeam(
        @Header("Authorization") accessToken: String,
        @Query("next") next: String,
        @Query("limit") limit: Int
    ): Response<GetMyTeamRes>
}
package com.studentcenter.weave.data.remote.api

import com.studentcenter.weave.data.remote.dto.meeting.FindMeetingRequestRes
import com.studentcenter.weave.data.remote.dto.meeting.GetAttendancesRes
import com.studentcenter.weave.data.remote.dto.meeting.GetMeetingListRes
import com.studentcenter.weave.data.remote.dto.meeting.RequestMeetingReq
import com.studentcenter.weave.domain.enums.TeamType
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface MeetingService {
    @POST("/api/meetings")
    suspend fun requestMeeting(
        @Header("Authorization") accessToken: String,
        @Body body: RequestMeetingReq
    ): Response<ResponseBody>

    @GET("/api/meetings/{id}/attendance")
    suspend fun getAttendances(
        @Header("Authorization") accessToken: String,
        @Path("id") meetingId: String
    ): Response<GetAttendancesRes>

    @POST("/api/meetings/{id}/attendance:pass")
    suspend fun passMeeting(
        @Header("Authorization") accessToken: String,
        @Path("id") meetingId: String,
    ): Response<ResponseBody>

    @POST("/api/meetings/{id}/attendance:attend")
    suspend fun attendMeeting(
        @Header("Authorization") accessToken: String,
        @Path("id") meetingId: String,
    ): Response<ResponseBody>

    @GET("/api/meetings/status/pending")
    suspend fun getMeetingList(
        @Header("Authorization") accessToken: String,
        @Query("teamType") teamType: TeamType,
        @Query("next") next: String?,
        @Query("limit") limit: Int
    ): Response<GetMeetingListRes>

    @GET("/api/meetings/requesting-team/my")
    suspend fun findMeetingRequest(
        @Header("Authorization") accessToken: String,
        @Query("receivingTeamId") receivingTeamId: String
    ): Response<FindMeetingRequestRes>
}
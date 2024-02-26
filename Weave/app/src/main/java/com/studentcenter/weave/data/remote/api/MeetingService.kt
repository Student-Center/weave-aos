package com.studentcenter.weave.data.remote.api

import com.studentcenter.weave.data.remote.dto.meeting.GetAttendancesRes
import com.studentcenter.weave.data.remote.dto.meeting.GetMeetingListRes
import com.studentcenter.weave.data.remote.dto.meeting.RequestMeetingReq
import com.studentcenter.weave.domain.enums.MeetingType
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface MeetingService {
    @POST("/api/meetings")
    suspend fun requestMeeting(
        @Body body: RequestMeetingReq
    ): Response<ResponseBody>

    @GET("/api/meetings/{id}/attendance")
    suspend fun getAttendances(
        @Path("id") meetingId: String
    ): Response<GetAttendancesRes>

    @POST("/api/meetings/{id}/attendance")
    suspend fun doAttendance(
        @Path("id") meetingId: String,
        @Query("attendance") isAttendance: Boolean
    ): Response<ResponseBody>

    @GET("/api/meetings/status/pending")
    suspend fun getMeetingList(
        @Query("teamType") teamType: MeetingType,
        @Query("next") next: String,
        @Query("limit") limit: Int
    ): Response<GetMeetingListRes>
}
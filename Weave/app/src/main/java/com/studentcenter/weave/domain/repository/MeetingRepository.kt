package com.studentcenter.weave.domain.repository

import com.studentcenter.weave.data.remote.dto.meeting.GetAttendancesRes
import com.studentcenter.weave.data.remote.dto.meeting.GetMeetingListRes
import com.studentcenter.weave.data.remote.dto.meeting.RequestMeetingReq
import com.studentcenter.weave.domain.enums.MeetingType
import okhttp3.ResponseBody
import retrofit2.Response

interface MeetingRepository {
    suspend fun requestMeeting(accessToken: String, body: RequestMeetingReq): Response<ResponseBody>

    suspend fun getAttendances(accessToken: String, meetingId: String): Response<GetAttendancesRes>

    suspend fun doAttendance(accessToken: String, meetingId: String, isAttendance: Boolean): Response<ResponseBody>

    suspend fun getMeetingList(accessToken: String, teamType: MeetingType, next: String, limit: Int): Response<GetMeetingListRes>
}
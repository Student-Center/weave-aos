package com.studentcenter.weave.domain.repository

import com.studentcenter.weave.data.remote.dto.meeting.FindMeetingRequestRes
import com.studentcenter.weave.data.remote.dto.meeting.GetAttendancesRes
import com.studentcenter.weave.data.remote.dto.meeting.GetMeetingListRes
import com.studentcenter.weave.data.remote.dto.meeting.GetOtherTeamKakaoIdRes
import com.studentcenter.weave.data.remote.dto.meeting.GetPreparedMeetingsRes
import com.studentcenter.weave.data.remote.dto.meeting.RequestMeetingReq
import com.studentcenter.weave.domain.enums.TeamType
import okhttp3.ResponseBody
import retrofit2.Response

interface MeetingRepository {
    suspend fun requestMeeting(accessToken: String, body: RequestMeetingReq): Response<ResponseBody>

    suspend fun getAttendances(accessToken: String, meetingId: String): Response<GetAttendancesRes>

    suspend fun passMeeting(accessToken: String, meetingId: String, ): Response<ResponseBody>

    suspend fun attendMeeting(accessToken: String, meetingId: String, ): Response<ResponseBody>

    suspend fun getMeetingList(accessToken: String, teamType: TeamType, next: String?, limit: Int): Response<GetMeetingListRes>

    suspend fun findMeetingRequest(accessToken: String, receivingTeamId: String): Response<FindMeetingRequestRes>

    suspend fun getPreparedMeetings(accessToken: String, next: String?, limit: Int): Response<GetPreparedMeetingsRes>

    suspend fun getOtherTeamKakaoId(accessToken: String, meetingId: String): Response<GetOtherTeamKakaoIdRes>
}
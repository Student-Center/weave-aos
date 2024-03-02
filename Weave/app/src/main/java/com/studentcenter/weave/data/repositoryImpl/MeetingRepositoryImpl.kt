package com.studentcenter.weave.data.repositoryImpl

import com.studentcenter.weave.data.remote.RetrofitClient
import com.studentcenter.weave.data.remote.api.MeetingService
import com.studentcenter.weave.data.remote.dto.meeting.FindMeetingRequestRes
import com.studentcenter.weave.data.remote.dto.meeting.GetAttendancesRes
import com.studentcenter.weave.data.remote.dto.meeting.GetMeetingListRes
import com.studentcenter.weave.data.remote.dto.meeting.RequestMeetingReq
import com.studentcenter.weave.domain.enums.TeamType
import com.studentcenter.weave.domain.repository.MeetingRepository
import okhttp3.ResponseBody
import retrofit2.Response

class MeetingRepositoryImpl: MeetingRepository {
    private val service = RetrofitClient.getInstance().create(MeetingService::class.java)

    override suspend fun requestMeeting(accessToken: String, body: RequestMeetingReq): Response<ResponseBody> {
        return service.requestMeeting(accessToken, body)
    }

    override suspend fun getAttendances(accessToken: String, meetingId: String): Response<GetAttendancesRes> {
        return service.getAttendances(accessToken, meetingId)
    }

    override suspend fun passMeeting(
        accessToken: String,
        meetingId: String
    ): Response<ResponseBody> {
        return service.passMeeting(accessToken, meetingId)
    }

    override suspend fun attendMeeting(
        accessToken: String,
        meetingId: String
    ): Response<ResponseBody> {
        return service.attendMeeting(accessToken, meetingId)
    }

    override suspend fun getMeetingList(
        accessToken: String,
        teamType: TeamType,
        next: String?,
        limit: Int
    ): Response<GetMeetingListRes> {
        return service.getMeetingList(accessToken, teamType, next, limit)
    }

    override suspend fun findMeetingRequest(
        accessToken: String,
        receivingTeamId: String
    ): Response<FindMeetingRequestRes> {
        return service.findMeetingRequest(accessToken, receivingTeamId)
    }
}
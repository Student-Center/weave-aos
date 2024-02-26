package com.studentcenter.weave.data.repositoryImpl

import com.studentcenter.weave.data.remote.RetrofitClient
import com.studentcenter.weave.data.remote.api.MeetingService
import com.studentcenter.weave.data.remote.dto.meeting.GetAttendancesRes
import com.studentcenter.weave.data.remote.dto.meeting.GetMeetingListRes
import com.studentcenter.weave.data.remote.dto.meeting.RequestMeetingReq
import com.studentcenter.weave.domain.enums.MeetingType
import com.studentcenter.weave.domain.repository.MeetingRepository
import okhttp3.ResponseBody
import retrofit2.Response

class MeetingRepositoryImpl: MeetingRepository {
    private val service = RetrofitClient.getInstance().create(MeetingService::class.java)

    override suspend fun requestMeeting(body: RequestMeetingReq): Response<ResponseBody> {
        return service.requestMeeting(body)
    }

    override suspend fun getAttendances(meetingId: String): Response<GetAttendancesRes> {
        return service.getAttendances(meetingId)
    }

    override suspend fun doAttendance(
        meetingId: String,
        isAttendance: Boolean
    ): Response<ResponseBody> {
        return service.doAttendance(meetingId, isAttendance)
    }

    override suspend fun getMeetingList(
        teamType: MeetingType,
        next: String,
        limit: Int
    ): Response<GetMeetingListRes> {
        return service.getMeetingList(teamType, next, limit)
    }
}
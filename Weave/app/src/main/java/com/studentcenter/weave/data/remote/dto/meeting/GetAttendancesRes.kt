package com.studentcenter.weave.data.remote.dto.meeting

import com.google.gson.annotations.SerializedName

data class GetAttendancesRes(
    @SerializedName("meetingAttendances")
    val meetingAttendances: List<MeetingAttendancesRes>
)

data class MeetingAttendancesRes(
    @SerializedName("memberId")
    val memberId: String,
    @SerializedName("attendance")
    val attendance: Boolean
)

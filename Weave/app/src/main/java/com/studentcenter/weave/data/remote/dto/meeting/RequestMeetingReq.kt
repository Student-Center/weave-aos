package com.studentcenter.weave.data.remote.dto.meeting

import com.google.gson.annotations.SerializedName

data class RequestMeetingReq(
    @SerializedName("receivingMeetingTeamId")
    val receivingMeetingTeamId: String
)

package com.studentcenter.weave.data.remote.dto.meeting

import com.google.gson.annotations.SerializedName

data class FindMeetingRequestRes(
    @SerializedName("meeting")
    val meeting: MeetingInfoRes?
)

data class MeetingInfoRes(
    @SerializedName("id")
    val id: String,
    @SerializedName("requestingTeamId")
    val requestingTeamId: String,
    @SerializedName("receivingTeamId")
    val receivingTeamId: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("pendingEndAt")
    val pendingEndAt: String
)

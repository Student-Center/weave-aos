package com.studentcenter.weave.data.remote.dto.team

import com.google.gson.annotations.SerializedName

data class CreateInvitationLinkRes(
    @SerializedName("meetingTeamInvitationLink")
    val meetingTeamInvitationLink: String,
    @SerializedName("meetingTeamInvitationCode")
    val meetingTeamInvitationCode: String
)

package com.studentcenter.weave.data.remote.dto.team

import com.google.gson.annotations.SerializedName

data class GetTeamByInvitationCodeRes(
    @SerializedName("teamId")
    val teamId: String,
    @SerializedName("teamIntroduce")
    val teamIntroduce: String,
    @SerializedName("status")
    val status: String
)

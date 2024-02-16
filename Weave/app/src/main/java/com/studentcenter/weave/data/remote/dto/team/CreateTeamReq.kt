package com.studentcenter.weave.data.remote.dto.team

import com.google.gson.annotations.SerializedName

data class CreateTeamReq (
    @SerializedName("teamIntroduce")
    val teamIntroduce: String,
    @SerializedName("memberCount")
    val memberCount: Int,
    @SerializedName("location")
    val location: String
)
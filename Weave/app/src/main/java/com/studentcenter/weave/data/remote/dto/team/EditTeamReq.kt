package com.studentcenter.weave.data.remote.dto.team

import com.google.gson.annotations.SerializedName

data class EditTeamReq(

    @SerializedName("location")
    val location: String,
    @SerializedName("memberCount")
    val memberCount: Int,
    @SerializedName("teamIntroduce")
    val teamIntroduce: String
)

package com.weave.weave.data.remote.dto.team

import com.google.gson.annotations.SerializedName

data class GetMyTeamReq(
    @SerializedName("next")
    val next: String,
    @SerializedName("limit")
    val limit: Int
)

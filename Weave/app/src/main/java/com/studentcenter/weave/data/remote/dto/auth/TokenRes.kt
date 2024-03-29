package com.studentcenter.weave.data.remote.dto.auth

import com.google.gson.annotations.SerializedName

data class TokenRes(
    @SerializedName("accessToken")
    val accessToken: String,
    @SerializedName("refreshToken")
    val refreshToken: String
)
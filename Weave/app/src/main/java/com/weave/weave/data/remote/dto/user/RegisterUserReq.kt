package com.weave.weave.data.remote.dto.user

import com.google.gson.annotations.SerializedName

data class RegisterUserReq(
    @SerializedName("gender")
    val gender: String,
    @SerializedName("birthYear")
    val birthYear: Int,
    @SerializedName("mbti")
    val mbti: String,
    @SerializedName("university")
    val university: String,
    @SerializedName("major")
    val major: String
)

package com.studentcenter.weave.data.remote.dto.user

import com.google.gson.annotations.SerializedName

data class RegisterUserReq(

    @SerializedName("gender")
    val gender: String,
    @SerializedName("birthYear")
    val birthYear: Int,
    @SerializedName("mbti")
    val mbti: String,
    @SerializedName("universityId")
    val universityId: String,
    @SerializedName("majorId")
    val majorId: String
)

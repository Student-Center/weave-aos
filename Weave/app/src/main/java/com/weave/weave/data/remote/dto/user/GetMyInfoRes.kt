package com.weave.weave.data.remote.dto.user

import com.google.gson.annotations.SerializedName

data class GetMyInfoRes(
    @SerializedName("id")
    val id: String,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("birthYear")
    val birthYear: Int,
    @SerializedName("majorName")
    val majorName: String,
    @SerializedName("avatar")
    val avatar: String,
    @SerializedName("mbti")
    val mbti: String,
    @SerializedName("animalType")
    val animalType: String,
    @SerializedName("height")
    val height: Int,
    @SerializedName("isUniversityEmailVerified")
    val isUniversityEmailVerified: Boolean,
    @SerializedName("sil")
    val sil: Int
)

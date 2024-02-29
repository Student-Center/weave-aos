package com.studentcenter.weave.data.remote.dto.team

import com.google.gson.annotations.SerializedName

data class GetTeamDetailRes(
    @SerializedName("id")
    val id: String,
    @SerializedName("teamIntroduce")
    val teamIntroduce: String,
    @SerializedName("memberCount")
    val memberCount: Int,
    @SerializedName("location")
    val location: String,
    @SerializedName("gender")
    val gender: String,
    @SerializedName("members")
    val members: List<GetTeamDetailMemberRes>,
    @SerializedName("status")
    val status: String,
    @SerializedName("affinityScore")
    val affinityScore: Int
)

data class GetTeamDetailMemberRes(
    @SerializedName("userId")
    val userId: String,
    @SerializedName("universityName")
    val universityName: String,
    @SerializedName("majorName")
    val majorName: String,
    @SerializedName("mbti")
    val mbti: String,
    @SerializedName("birthYear")
    val birthYear: Int,
    @SerializedName("role")
    val role: String,
    @SerializedName("animalType")
    val animalType: String,
    @SerializedName("height")
    val height: Int,
    @SerializedName("isUnivVerified")
    val isUnivVerified: Boolean
)

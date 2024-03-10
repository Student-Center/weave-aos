package com.studentcenter.weave.data.remote.dto.meeting

import com.google.gson.annotations.SerializedName

data class GetPreparedMeetingsRes(

    @SerializedName("items")
    val items: List<PreparedMeetingsItemRes>,
    @SerializedName("next")
    val next: String,
    @SerializedName("total")
    val total: Int
)

data class PreparedMeetingsItemRes(
    @SerializedName("id")
    val id: String,
    @SerializedName("memberCount")
    val memberCount: Int,
    @SerializedName("otherTeam")
    val otherTeam: PreparedMeetingsOtherTeamRes,
    @SerializedName("status")
    val status: String,
    @SerializedName("createdAt")
    val createdAt: String
)

data class PreparedMeetingsOtherTeamRes(
    @SerializedName("id")
    val id: String,
    @SerializedName("teamIntroduce")
    val teamIntroduce: String,
    @SerializedName("memberCount")
    val memberCount: Int,
    @SerializedName("gender")
    val gender: String,
    @SerializedName("location")
    val location: String,
    @SerializedName("memberInfos")
    val memberInfos: List<PreparedMeetingsMemberRes>
)

data class PreparedMeetingsMemberRes(
    @SerializedName("id")
    val id: String,
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
    @SerializedName("height")
    val height: Int,
    @SerializedName("animalType")
    val animalType: String,
    @SerializedName("isUnivVerified")
    val isUnivVerified: Boolean,
    @SerializedName("avatar")
    val avatar: String
)

package com.studentcenter.weave.data.remote.dto.team

import com.google.gson.annotations.SerializedName

data class GetMyTeamRes(
    @SerializedName("item")
    val item: List<GetMyTeamItem>,
    @SerializedName("next")
    val next: String,
    @SerializedName("total")
    val total: Int
)

data class GetMyTeamItem(
    @SerializedName("id")
    val id: String,
    @SerializedName("teamIntroduce")
    val teamIntroduce: String,
    @SerializedName("memberCount")
    val memberCount: Int,
    @SerializedName("location")
    val location: String,
    @SerializedName("memberInfos")
    val memberInfos: List<GetMyTeamMemberInfos>
)

data class GetMyTeamMemberInfos(
    @SerializedName("id")
    val id: String,
    @SerializedName("universityName")
    val universityName: String,
    @SerializedName("mbti")
    val mbti: String,
    @SerializedName("birthYear")
    val birthYear: Int,
    @SerializedName("isLeader")
    val isLeader: Boolean,
    @SerializedName("isMe")
    val isMe: Boolean
)

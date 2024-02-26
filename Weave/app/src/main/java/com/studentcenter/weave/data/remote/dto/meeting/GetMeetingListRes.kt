package com.studentcenter.weave.data.remote.dto.meeting

import com.google.gson.annotations.SerializedName

data class GetMeetingListRes(
    @SerializedName("items")
    val items: List<MeetingListItemRes>,
    @SerializedName("next")
    val next: String,
    @SerializedName("total")
    val total: Int
)

data class MeetingListItemRes(
    @SerializedName("id")
    val id: String,
    @SerializedName("requestingTeam")
    val requestingTeam: MeetingListTeamRes,
    @SerializedName("receivingTeam")
    val receivingTeam: MeetingListTeamRes,
    @SerializedName("teamType")
    val teamType: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("pendingEndAt")
    val pendingEndAt: String
)

data class MeetingListTeamRes(
    @SerializedName("id")
    val id: String,
    @SerializedName("teamIntroduce")
    val teamIntroduce: String,
    @SerializedName("memberCount")
    val memberCount: Int,
    @SerializedName("gender")
    val gender: String,
    @SerializedName("memberInfos")
    val memberInfos: List<MeetingListMemberInfoRes>
)

data class MeetingListMemberInfoRes(
    @SerializedName("id")
    val id: String,
    @SerializedName("userId")
    val userId: String,
    @SerializedName("universityName")
    val universityName: String,
    @SerializedName("mbti")
    val mbti: String,
    @SerializedName("birthYear")
    val birthYear: Int,
    @SerializedName("animalType")
    val animalType: String
)

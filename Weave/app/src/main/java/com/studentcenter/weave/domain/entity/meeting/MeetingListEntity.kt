package com.studentcenter.weave.domain.entity.meeting

import com.google.gson.annotations.SerializedName

data class MeetingListEntity(
    @SerializedName("items")
    val items: List<MeetingListItemEntity>,
    @SerializedName("next")
    val next: String,
    @SerializedName("total")
    val total: Int
)

data class MeetingListItemEntity(
    @SerializedName("id")
    val id: String,
    @SerializedName("requestingTeam")
    val requestingTeam: MeetingListTeamEntity,
    @SerializedName("receivingTeam")
    val receivingTeam: MeetingListTeamEntity,
    @SerializedName("teamType")
    val teamType: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("pendingEndAt")
    val pendingEndAt: String
)

data class MeetingListTeamEntity(
    @SerializedName("id")
    val id: String,
    @SerializedName("teamIntroduce")
    val teamIntroduce: String,
    @SerializedName("memberCount")
    val memberCount: Int,
    @SerializedName("gender")
    val gender: String,
    @SerializedName("memberInfos")
    val memberInfos: List<MeetingListMemberInfoEntity>
)

data class MeetingListMemberInfoEntity(
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

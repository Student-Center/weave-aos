package com.studentcenter.weave.domain.entity.meeting

data class MeetingListEntity(
    val items: List<MeetingListItemEntity>,
    val next: String?,
    val total: Int
)

data class MeetingListItemEntity(
    val id: String,
    val requestingTeam: MeetingListTeamEntity,
    val receivingTeam: MeetingListTeamEntity,
    val teamType: String,
    val status: String,
    val createdAt: String,
    val pendingEndAt: String
)

data class MeetingListTeamEntity(
    val id: String,
    val teamIntroduce: String,
    val memberCount: Int,
    val gender: String,
    val memberInfos: List<MeetingListMemberInfoEntity>
)

data class MeetingListMemberInfoEntity(
    val id: String,
    val userId: String,
    val universityName: String,
    val mbti: String,
    val birthYear: Int,
    val animalType: String?,
    var checked: Boolean
)

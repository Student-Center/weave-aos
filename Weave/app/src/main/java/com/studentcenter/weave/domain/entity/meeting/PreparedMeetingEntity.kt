package com.studentcenter.weave.domain.entity.meeting

data class PreparedMeetingEntity(
    val items: List<PreparedMeetingItemEntity>,
    val next: String?,
    val total: Int
)

data class PreparedMeetingItemEntity(
    val id: String,
    val memberCount: Int,
    val otherTeam: PreparedMeetingOtherTeamEntity,
    val status: String,
    val createdAt: String
)

data class PreparedMeetingOtherTeamEntity(
    val id: String,
    val teamIntroduce: String,
    val memberCount: Int,
    val gender: String,
    val location: String,
    val memberInfos: List<PreparedMeetingMemberEntity>
)

data class PreparedMeetingMemberEntity(
    val id: String,
    val userId: String,
    val universityName: String,
    val majorName: String,
    val mbti: String,
    val birthYear: Int,
    val animalType: String,
    val height: Int,
    val isUnivVerified: Boolean,
    val avatar: String?,
    var kakaoId: String
)
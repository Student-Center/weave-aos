package com.weave.weave.domain.entity.team

data class GetMyTeamEntity(
    val item: List<GetMyTeamItemEntity>,
    val next: String?,
    val limit: Int
)

data class GetMyTeamItemEntity(
    val id: String,
    val teamIntroduce: String,
    val memberCount: Int,
    val location: String,
    val memberInfos: List<GetMyTeamMemberInfoEntity>
)

data class GetMyTeamMemberInfoEntity(
    val id: String,
    val universityName: String,
    val mbti: String,
    val birthYear: Int,
    val isLeader: Boolean,
    val isMe: Boolean
)
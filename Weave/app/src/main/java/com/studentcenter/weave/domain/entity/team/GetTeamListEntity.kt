package com.studentcenter.weave.domain.entity.team

data class GetTeamListEntity(
    val items: List<GetTeamListItemEntity>,
    val next: String,
    val total: Int
)


data class GetTeamListItemEntity(
    val id: String,
    val teamIntroduce: String,
    val memberCount: Int,
    val location: String,
    val memberInfos: List<GetTeamListMemberEntity>
)

data class GetTeamListMemberEntity(
    val id: String,
    val universityName: String,
    val mbti: String,
    val birthYear: Int,
    val role: String
)
package com.weave.weave.domain.entity.team

data class RequestMatchTeamEntity(
    val id: String,
    val team: List<TeamInfo>
)

data class TeamInfo(
    val checked: Int,
    val title: String,
    val members: List<MemberInfo>
)

data class MemberInfo(
    val id: String,
    val url: String,
    val univ: String,
    val mbti: String,
    val checked: Boolean
)

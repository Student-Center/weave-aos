package com.weave.weave.domain.entity.team

data class RequestTeamTestEntity(
    val title: String,
    val time: String,
    val members: List<TeamMember>
)

data class TeamMember(
    val url: String,
    val univ: String,
    val mbti: String
)

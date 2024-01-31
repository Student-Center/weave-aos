package com.weave.weave.domain.entity.home

data class TeamTestEntity(
    val type: String,
    val title: String,
    val location: String,
    val members: List<TeamMember>
)

data class TeamMember(
    val url: String,
    val univ: String,
    val mbti: String
)

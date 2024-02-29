package com.studentcenter.weave.domain.entity.team

data class TeamDetailEntity(
    val id: String,
    val teamIntroduce: String,
    val memberCount: Int,
    val location: String,
    val gender: String,
    val members: List<TeamDetailMemberEntity>,
    val status: String,
    val affinityScore: Int
)
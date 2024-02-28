package com.studentcenter.weave.domain.entity.team

data class TeamDetailMemberEntity(
    val userId: String,
    val universityName: String,
    val majorName: String,
    val mbti: String,
    val birthYear: Int,
    val role: String,
    val animalType: String?,
    val height: Int?
)
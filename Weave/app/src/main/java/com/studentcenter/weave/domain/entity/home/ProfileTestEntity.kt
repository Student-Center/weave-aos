package com.studentcenter.weave.domain.entity.home

data class ProflieTestEntity(
    val teamName: String,
    val location: String,
    val score: Int,
    val members: List<Member>
)

data class Member(
    val mbti: String,
    val animal: String,
    val height: String,
    val univ: String,
    val major: String,
    val age: String,
    val url: String
)

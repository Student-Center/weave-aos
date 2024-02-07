package com.weave.weave.domain.entity.profile

data class MyInfoEntity(
    val id: String,
    val nickname: String,
    val birthYear: Int,
    val universityName: String,
    val majorName: String,
    val avatar: String?,
    val mbti: String,
    val animalType: String?,
    val height: Int?,
    val isUniversityEmailVerified: Boolean,
    val sil: Int
)

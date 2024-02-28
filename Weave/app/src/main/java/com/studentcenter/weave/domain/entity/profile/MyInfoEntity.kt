package com.studentcenter.weave.domain.entity.profile

data class MyInfoEntity(
    val id: String,
    val nickname: String,
    val birthYear: Int,
    val universityName: String,
    val majorName: String,
    var avatar: String?,
    var mbti: String,
    var animalType: String?,
    var height: Int?,
    var kakaoId: String?,
    val isUniversityEmailVerified: Boolean,
    val sil: Int
)

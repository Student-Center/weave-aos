package com.studentcenter.weave.domain.entity.login

data class TokenEntity(
    val accessToken: String,
    val refreshToken: String,
)
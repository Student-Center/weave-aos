package com.weave.weave.domain.entity

data class TokenEntity(
    val accessToken: String?,
    val refreshToken: String?,
    val registerToken: String?
)
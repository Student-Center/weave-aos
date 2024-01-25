package com.weave.weave.domain.extension

import com.weave.weave.data.remote.dto.auth.TokenRes
import com.weave.weave.domain.entity.TokenEntity

// Example
//fun UserRes.asDomain() = UserEntity(
//    title = this.title.orEmpty(),
//    price = this.price ?: 0
//)


// Auth
fun TokenRes.asDomain() = TokenEntity(
    accessToken = this.accessToken,
    refreshToken = this.refreshToken
)


// User


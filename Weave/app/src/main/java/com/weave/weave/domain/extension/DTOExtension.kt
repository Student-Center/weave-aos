package com.weave.weave.domain.extension

import com.weave.weave.data.remote.dto.auth.TokenRes
import com.weave.weave.data.remote.dto.univ.UniversityRes
import com.weave.weave.domain.entity.login.TokenEntity
import com.weave.weave.domain.entity.login.UniversityEntity

// Example
//fun UserRes.asDomain() = UserEntity(
//    title = this.title.orEmpty(),
//    price = this.price ?: 0
//)


// Login
fun TokenRes.asDomain() = TokenEntity(
    accessToken = this.accessToken,
    refreshToken = this.refreshToken,
)

fun UniversityRes.asDomain() = UniversityEntity(
    universities = this.universities
)





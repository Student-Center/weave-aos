package com.weave.weave.domain.extension

import com.weave.weave.data.remote.dto.UserRes
import com.weave.weave.domain.entity.UserEntity


fun UserRes.asDomain() = UserEntity(
    title = this.title.orEmpty(),
    price = this.price ?: 0
)


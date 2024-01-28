package com.weave.weave.domain.repository

import com.weave.weave.core.Resource
import com.weave.weave.data.remote.dto.UserRes
import com.weave.weave.domain.entity.UserEntity
import retrofit2.Response

interface TestRepository {
    suspend fun getUsers(): Resource<UserEntity>
}
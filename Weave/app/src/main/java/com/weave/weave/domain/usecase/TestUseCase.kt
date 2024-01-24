package com.weave.weave.domain.usecase

import com.weave.weave.core.Resource
import com.weave.weave.data.remote.dto.UserRes
import com.weave.weave.data.repositoryImpl.TestRepositoryImpl
import com.weave.weave.domain.entity.UserEntity

class TestUseCase {
    private val repositoryImpl = TestRepositoryImpl()

    suspend fun getUsers(): Resource<UserEntity> {
        return repositoryImpl.getUsers()
    }
}
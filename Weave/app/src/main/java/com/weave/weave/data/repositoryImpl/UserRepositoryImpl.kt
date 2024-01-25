package com.weave.weave.data.repositoryImpl

import com.weave.weave.data.remote.RetrofitClient
import com.weave.weave.data.remote.api.UserService
import com.weave.weave.data.remote.dto.auth.TokenRes
import com.weave.weave.data.remote.dto.user.RegisterUserReq
import com.weave.weave.domain.repository.UserRepository
import retrofit2.Response

class UserRepositoryImpl: UserRepository {
    private val service = RetrofitClient.getInstance().create(UserService::class.java)

    override suspend fun registerUser(registerToken: String, userInfo: RegisterUserReq): Response<TokenRes> {
        return service.registerUser(registerToken, userInfo)
    }
}
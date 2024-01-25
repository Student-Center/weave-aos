package com.weave.weave.domain.repository

import com.weave.weave.data.remote.dto.auth.TokenRes
import com.weave.weave.data.remote.dto.user.RegisterUserReq
import retrofit2.Response

interface UserRepository {
    suspend fun registerUser(registerToken: String, userInfo: RegisterUserReq): Response<TokenRes>
}
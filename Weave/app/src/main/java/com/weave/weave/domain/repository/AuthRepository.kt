package com.weave.weave.domain.repository

import com.weave.weave.data.remote.dto.auth.LoginTokenReq
import com.weave.weave.data.remote.dto.auth.RefreshTokenReq
import com.weave.weave.data.remote.dto.auth.TokenRes
import retrofit2.Response

interface AuthRepository {
    suspend fun refreshLoginToken(
        refreshTokenReq: RefreshTokenReq
    ): Response<TokenRes>

    suspend fun login(provider: String, idToken: LoginTokenReq): Response<TokenRes>

}
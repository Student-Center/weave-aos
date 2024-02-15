package com.studentcenter.weave.domain.repository

import com.studentcenter.weave.data.remote.dto.auth.LoginTokenReq
import com.studentcenter.weave.data.remote.dto.auth.RefreshTokenReq
import com.studentcenter.weave.data.remote.dto.auth.TokenRes
import okhttp3.ResponseBody
import retrofit2.Response

interface AuthRepository {
    suspend fun refreshLoginToken(
        refreshTokenReq: RefreshTokenReq
    ): Response<TokenRes>

    suspend fun login(provider: String, idToken: LoginTokenReq): Response<TokenRes>

    suspend fun logOut(accessToken: String): Response<ResponseBody>
}
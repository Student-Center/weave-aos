package com.weave.weave.data.remote.api

import com.weave.weave.data.remote.dto.auth.LoginTokenReq
import com.weave.weave.data.remote.dto.auth.RefreshTokenReq
import com.weave.weave.data.remote.dto.auth.TokenRes
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface AuthService {
    @POST("/api/auth/refresh")
    suspend fun refreshLoginToken(
        @Body refreshTokenReq: RefreshTokenReq
    ): Response<TokenRes>

    @POST("/api/auth/login/{provider}")
    suspend fun login(
        @Path("provider") provider: String,
        @Body idToken: LoginTokenReq
    ): Response<TokenRes>
}
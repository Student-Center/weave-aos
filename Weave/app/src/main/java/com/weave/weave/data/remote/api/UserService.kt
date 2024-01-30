package com.weave.weave.data.remote.api

import com.weave.weave.data.remote.dto.auth.TokenRes
import com.weave.weave.data.remote.dto.user.RegisterUserReq
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface UserService {

    @POST("/api/users")
    suspend fun registerUser(
        @Header("Register-Token") registerToken: String,
        @Body userInfo: RegisterUserReq
    ): Response<TokenRes>
}
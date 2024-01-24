package com.weave.weave.data.remote.api

import com.weave.weave.data.remote.dto.UserRes
import retrofit2.Response
import retrofit2.http.GET

interface TestService {
    @GET("/products/1")
    suspend fun getUsers(): Response<UserRes>
}
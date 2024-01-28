package com.weave.weave.data.remote.api

import com.weave.weave.data.remote.dto.univ.UniversityRes
import retrofit2.Response
import retrofit2.http.GET

interface UnivService {
    @GET("/api/univ")
    suspend fun findAllUniv(): Response<UniversityRes>
}
package com.weave.weave.data.remote.api

import com.weave.weave.data.remote.dto.univ.MajorRes
import com.weave.weave.data.remote.dto.univ.UniversityRes
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface UnivService {
    @GET("/api/univ")
    suspend fun findAllUniv(): Response<UniversityRes>

    @GET("/api/univ/{univName}/majors")
    suspend fun getAllMajor(
        @Path("univName") univName: String
    ): Response<MajorRes>
}
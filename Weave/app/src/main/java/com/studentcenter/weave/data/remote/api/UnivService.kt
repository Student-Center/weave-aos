package com.studentcenter.weave.data.remote.api

import com.studentcenter.weave.data.remote.dto.univ.MajorRes
import com.studentcenter.weave.data.remote.dto.univ.UniversityRes
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface UnivService {
    @GET("/api/univ")
    suspend fun findAllUniv(): Response<UniversityRes>

    @GET("/api/univ/{univId}/majors")
    suspend fun getAllMajor(
        @Path("univId") univId: String
    ): Response<MajorRes>
}
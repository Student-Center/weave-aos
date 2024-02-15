package com.studentcenter.weave.domain.repository

import com.studentcenter.weave.data.remote.dto.univ.MajorRes
import com.studentcenter.weave.data.remote.dto.univ.UniversityRes
import retrofit2.Response

interface UnivRepository {
    suspend fun findAllUniv(): Response<UniversityRes>

    suspend fun getAllMajor(univId: String): Response<MajorRes>
}
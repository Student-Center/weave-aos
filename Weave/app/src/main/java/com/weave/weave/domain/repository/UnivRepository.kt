package com.weave.weave.domain.repository

import com.weave.weave.data.remote.dto.univ.UniversityRes
import retrofit2.Response

interface UnivRepository {
    suspend fun findAllUniv(): Response<UniversityRes>
}
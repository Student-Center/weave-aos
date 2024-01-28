package com.weave.weave.data.repositoryImpl

import com.weave.weave.data.remote.RetrofitClient
import com.weave.weave.data.remote.api.UnivService
import com.weave.weave.data.remote.dto.univ.UniversityRes
import com.weave.weave.domain.repository.UnivRepository
import retrofit2.Response

class UnivRepositoryImpl: UnivRepository {
    private val service = RetrofitClient.getInstance().create(UnivService::class.java)

    override suspend fun findAllUniv(): Response<UniversityRes> {
        return service.findAllUniv()
    }
}
package com.studentcenter.weave.data.repositoryImpl

import com.studentcenter.weave.data.remote.RetrofitClient
import com.studentcenter.weave.data.remote.api.UnivService
import com.studentcenter.weave.data.remote.dto.univ.MajorRes
import com.studentcenter.weave.data.remote.dto.univ.UnivInfoRes
import com.studentcenter.weave.data.remote.dto.univ.UniversityRes
import com.studentcenter.weave.domain.repository.UnivRepository
import retrofit2.Response

class UnivRepositoryImpl: UnivRepository {
    private val service = RetrofitClient.getInstance().create(UnivService::class.java)

    override suspend fun findAllUniv(): Response<UniversityRes> {
        return service.findAllUniv()
    }

    override suspend fun getAllMajor(univId: String): Response<MajorRes> {
        return service.getAllMajor(univId)
    }

    override suspend fun getUnivByName(univName: String): Response<UnivInfoRes> {
        return service.getUnivByName(univName)
    }
}
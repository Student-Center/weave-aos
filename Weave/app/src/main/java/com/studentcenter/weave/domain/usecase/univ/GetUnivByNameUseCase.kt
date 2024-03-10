package com.studentcenter.weave.domain.usecase.univ

import com.studentcenter.weave.data.repositoryImpl.UnivRepositoryImpl
import com.studentcenter.weave.domain.entity.login.UniversityEntity
import com.studentcenter.weave.domain.extension.asDomain
import com.studentcenter.weave.domain.usecase.Resource
import org.json.JSONObject

class GetUnivByNameUseCase {
    private val univRepositoryImpl = UnivRepositoryImpl()

    suspend fun getUnivByName(univName: String): Resource<UniversityEntity> {
        return try {
            val res = univRepositoryImpl.getUnivByName(univName)

            if(res.isSuccessful){
                val data = res.body()
                if(data != null){
                    Resource.Success(data.asDomain())
                } else {
                    Resource.Error("Received null data")
                }
            } else {
                Resource.Error(JSONObject(res.errorBody()?.string()!!).getString("message"))
            }
        } catch (e: Exception){
            Resource.Error(e.message ?: "An error occurred")
        }
    }
}
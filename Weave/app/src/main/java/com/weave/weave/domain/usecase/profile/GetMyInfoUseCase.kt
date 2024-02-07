package com.weave.weave.domain.usecase.profile

import com.weave.weave.data.repositoryImpl.UserRepositoryImpl
import com.weave.weave.domain.entity.profile.MyInfoEntity
import com.weave.weave.domain.extension.asDomain
import com.weave.weave.domain.usecase.Resource

class GetMyInfoUseCase {
    private val userRepositoryImpl = UserRepositoryImpl()

    suspend fun getMyInfo(accessToken: String): Resource<MyInfoEntity> {
        return try {
            val res = userRepositoryImpl.getMyInfo("Bearer $accessToken")

            if(res.isSuccessful){
                val data = res.body()
                if(data != null){
                    Resource.Success(data.asDomain())
                } else {
                    Resource.Error("Received null data")
                }
            } else {
                Resource.Error(res.message())
            }
        } catch (e: Exception){
            Resource.Error(e.message ?: "An error occurred")
        }
    }
}
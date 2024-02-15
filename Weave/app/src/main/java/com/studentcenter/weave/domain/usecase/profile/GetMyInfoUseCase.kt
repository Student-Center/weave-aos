package com.studentcenter.weave.domain.usecase.profile

import com.studentcenter.weave.core.GlobalApplication.Companion.app
import com.studentcenter.weave.data.repositoryImpl.UserRepositoryImpl
import com.studentcenter.weave.domain.entity.profile.MyInfoEntity
import com.studentcenter.weave.domain.extension.asDomain
import com.studentcenter.weave.domain.usecase.Resource
import kotlinx.coroutines.flow.first

class GetMyInfoUseCase {
    private val userRepositoryImpl = UserRepositoryImpl()

    suspend fun getMyInfo(accessToken: String): Resource<MyInfoEntity> {
        return try {
            if(app.getUserDataStore().getLoginToken().first().refreshToken == ""){
                return Resource.Error("RefreshToken is Null")
            }

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
package com.studentcenter.weave.domain.usecase.profile

import com.studentcenter.weave.data.repositoryImpl.UserRepositoryImpl
import com.studentcenter.weave.domain.entity.profile.MyInfoEntity
import com.studentcenter.weave.domain.extension.asDomain
import com.studentcenter.weave.domain.usecase.Resource
import org.json.JSONObject

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
                Resource.Error(JSONObject(res.errorBody()?.string()!!).getString("message"))
            }
        } catch (e: Exception){
            Resource.Error(e.message ?: "An error occurred")
        }
    }
}
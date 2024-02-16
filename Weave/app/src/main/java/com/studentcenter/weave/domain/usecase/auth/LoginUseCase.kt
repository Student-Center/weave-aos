package com.studentcenter.weave.domain.usecase.auth

import com.google.gson.Gson
import com.studentcenter.weave.data.remote.dto.auth.LoginTokenReq
import com.studentcenter.weave.data.repositoryImpl.AuthRepositoryImpl
import com.studentcenter.weave.domain.entity.login.RegisterTokenEntity
import com.studentcenter.weave.domain.entity.login.TokenEntity
import com.studentcenter.weave.domain.extension.asDomain
import com.studentcenter.weave.domain.usecase.Resource

class LoginUseCase {
    private val authRepositoryImpl = AuthRepositoryImpl()

    suspend fun login(provider: String, idToken: LoginTokenReq): Resource<TokenEntity> {
        return try {
            val res = authRepositoryImpl.login(provider, idToken)

            if(res.isSuccessful){
                val data = res.body()
                if(data != null){
                    Resource.Success(data.asDomain())
                } else {
                    Resource.Error("Received null data")
                }
            } else {
                if(res.code() == 401){
                    val errorBody = res.errorBody()?.string()
                    val errorResponse = Gson().fromJson(errorBody, RegisterTokenEntity::class.java)
                    Resource.Error("registerToken ${errorResponse.registerToken}")
                } else {
                    Resource.Error(res.message())
                }
            }
        } catch (e: Exception){
            Resource.Error(e.message ?: "An error occurred")
        }
    }




}
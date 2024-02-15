package com.studentcenter.weave.domain.usecase.auth

import com.studentcenter.weave.core.GlobalApplication
import com.studentcenter.weave.data.repositoryImpl.AuthRepositoryImpl
import com.studentcenter.weave.domain.usecase.Resource
import kotlinx.coroutines.flow.first

class LogOutUseCase {
    private val authRepositoryImpl = AuthRepositoryImpl()

    suspend fun logOut(accessToken: String): Resource<Boolean> {
        if(GlobalApplication.app.getUserDataStore().getLoginToken().first().refreshToken == ""){
            return Resource.Error("RefreshToken is Null")
        }

        return try {
            val res = authRepositoryImpl.logOut("Bearer $accessToken")
            if(GlobalApplication.app.getUserDataStore().getLoginToken().first().refreshToken == ""){
                return Resource.Error("RefreshToken is Null")
            }

            if(res.isSuccessful){
                if(res.code() == 200){
                    Resource.Success(true)
                } else {
                    Resource.Error(res.message() ?: "isSuccessful but error occurred")
                }
            } else {
                Resource.Error(res.message())
            }
        } catch (e: Exception){
            Resource.Error(e.message ?: "An error occurred")
        }
    }




}
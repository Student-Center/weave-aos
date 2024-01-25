package com.weave.weave.domain.usecase

import com.weave.weave.data.remote.dto.auth.LoginTokenReq
import com.weave.weave.data.remote.dto.auth.RefreshTokenReq
import com.weave.weave.data.repositoryImpl.AuthRepositoryImpl
import com.weave.weave.domain.entity.TokenEntity
import com.weave.weave.domain.extension.asDomain

class LoginUseCase {
    private val repositoryImpl = AuthRepositoryImpl()

    suspend fun refreshLoginToken(refreshTokenReq: RefreshTokenReq): Resource<TokenEntity> {
        return try {
            val res = repositoryImpl.refreshLoginToken(refreshTokenReq)

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

    suspend fun login(provider: String, idToken: LoginTokenReq): Resource<TokenEntity> {
        return try {
            val res = repositoryImpl.login(provider, idToken)

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
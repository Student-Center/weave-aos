package com.weave.weave.domain.usecase.auth

import com.weave.weave.data.remote.dto.auth.RefreshTokenReq
import com.weave.weave.data.repositoryImpl.AuthRepositoryImpl
import com.weave.weave.domain.entity.login.TokenEntity
import com.weave.weave.domain.extension.asDomain
import com.weave.weave.domain.usecase.Resource

class RefreshLoginTokenUseCase {
    private val authRepositoryImpl = AuthRepositoryImpl()

    suspend fun refreshLoginToken(refreshTokenReq: RefreshTokenReq): Resource<TokenEntity> {
        return try {
            val res = authRepositoryImpl.refreshLoginToken(refreshTokenReq)

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
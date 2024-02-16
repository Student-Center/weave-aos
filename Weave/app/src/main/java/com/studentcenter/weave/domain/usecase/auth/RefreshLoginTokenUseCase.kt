package com.studentcenter.weave.domain.usecase.auth

import com.studentcenter.weave.data.remote.dto.auth.RefreshTokenReq
import com.studentcenter.weave.data.repositoryImpl.AuthRepositoryImpl
import com.studentcenter.weave.domain.entity.login.TokenEntity
import com.studentcenter.weave.domain.extension.asDomain
import com.studentcenter.weave.domain.usecase.Resource

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
package com.studentcenter.weave.domain.usecase.auth

import com.studentcenter.weave.core.GlobalApplication.Companion.app
import com.studentcenter.weave.data.remote.dto.auth.RefreshTokenReq
import com.studentcenter.weave.data.repositoryImpl.AuthRepositoryImpl
import com.studentcenter.weave.domain.entity.login.TokenEntity
import com.studentcenter.weave.domain.extension.asDomain
import com.studentcenter.weave.domain.usecase.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.json.JSONObject

class RefreshLoginTokenUseCase {
    private val authRepositoryImpl = AuthRepositoryImpl()

    suspend fun refreshLoginToken(refreshTokenReq: RefreshTokenReq): Resource<TokenEntity> {
        val refreshToken = runBlocking(Dispatchers.IO) {
            app.getUserDataStore().getLoginToken().first().refreshToken
        }

        if(refreshToken.isEmpty()) return Resource.Error("null")

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
                Resource.Error(JSONObject(res.errorBody()?.string()!!).getString("message"))
            }
        } catch (e: Exception){
            Resource.Error(e.message ?: "An error occurred")
        }
    }
}
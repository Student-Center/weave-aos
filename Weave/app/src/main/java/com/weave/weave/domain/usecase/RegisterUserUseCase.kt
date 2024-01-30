package com.weave.weave.domain.usecase

import com.weave.weave.data.remote.dto.user.RegisterUserReq
import com.weave.weave.data.repositoryImpl.UserRepositoryImpl
import com.weave.weave.domain.entity.login.TokenEntity
import com.weave.weave.domain.extension.asDomain

class RegisterUserUseCase {
    private val userRepositoryImpl = UserRepositoryImpl()

    suspend fun registerUser(registerToken: String, userInfo: RegisterUserReq): Resource<TokenEntity>{
        return try {
            val res = userRepositoryImpl.registerUser(registerToken, userInfo)

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
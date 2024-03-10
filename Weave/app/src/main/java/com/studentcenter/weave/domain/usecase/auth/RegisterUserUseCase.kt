package com.studentcenter.weave.domain.usecase.auth

import com.studentcenter.weave.data.remote.dto.user.RegisterUserReq
import com.studentcenter.weave.data.repositoryImpl.UserRepositoryImpl
import com.studentcenter.weave.domain.entity.login.TokenEntity
import com.studentcenter.weave.domain.extension.asDomain
import com.studentcenter.weave.domain.usecase.Resource
import org.json.JSONObject

class RegisterUserUseCase {
    private val userRepositoryImpl = UserRepositoryImpl()

    suspend fun registerUser(registerToken: String, userInfo: RegisterUserReq): Resource<TokenEntity> {
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
                Resource.Error(JSONObject(res.errorBody()?.string()!!).getString("message"))
            }
        } catch (e: Exception){
            Resource.Error(e.message ?: "An error occurred")
        }
    }
}
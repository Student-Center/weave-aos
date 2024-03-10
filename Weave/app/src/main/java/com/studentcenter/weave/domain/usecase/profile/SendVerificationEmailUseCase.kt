package com.studentcenter.weave.domain.usecase.profile

import com.studentcenter.weave.data.remote.dto.user.SendVerificationEmailReq
import com.studentcenter.weave.data.repositoryImpl.UserRepositoryImpl
import com.studentcenter.weave.domain.usecase.Resource
import org.json.JSONObject

class SendVerificationEmailUseCase {
    private val userRepositoryImpl = UserRepositoryImpl()

    suspend fun sendVerificationEmail(accessToken: String, body: SendVerificationEmailReq): Resource<Boolean> {
        return try {
            val res = userRepositoryImpl.sendVerificationEmail("Bearer $accessToken", body)

            if(res.isSuccessful){
                if(res.code() == 204){
                    Resource.Success(true)
                } else {
                    Resource.Error(res.message() ?: "isSuccessful but error occurred")
                }
            } else {
                Resource.Error(JSONObject(res.errorBody()?.string()!!).getString("message"))
            }
        } catch (e: Exception){
            Resource.Error(e.message ?: "An error occurred")
        }
    }
}
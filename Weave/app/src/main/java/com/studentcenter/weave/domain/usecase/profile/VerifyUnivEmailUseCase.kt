package com.studentcenter.weave.domain.usecase.profile

import com.studentcenter.weave.data.remote.dto.user.VerifyUnivEmailReq
import com.studentcenter.weave.data.repositoryImpl.UserRepositoryImpl
import com.studentcenter.weave.domain.usecase.Resource
import org.json.JSONObject

class VerifyUnivEmailUseCase {
    private val userRepositoryImpl = UserRepositoryImpl()

    suspend fun verifyUnivEmail(accessToken: String, body: VerifyUnivEmailReq): Resource<Boolean> {
        return try {
            val res = userRepositoryImpl.verifyUnivEmail("Bearer $accessToken", body)

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
package com.weave.weave.domain.usecase.profile

import com.weave.weave.data.remote.dto.user.SetMyHeightReq
import com.weave.weave.data.repositoryImpl.UserRepositoryImpl
import com.weave.weave.domain.usecase.Resource

class SetMyHeightUseCase {
    private val userRepositoryImpl = UserRepositoryImpl()

    suspend fun setMyHeight(accessToken: String, body: SetMyHeightReq): Resource<Boolean> {

        return try {
            val res = userRepositoryImpl.setMyHeight("Bearer $accessToken", body)

            if(res.isSuccessful){
                if(res.code() == 204){
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
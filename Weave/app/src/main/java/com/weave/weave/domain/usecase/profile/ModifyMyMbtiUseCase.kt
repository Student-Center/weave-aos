package com.weave.weave.domain.usecase.profile

import com.weave.weave.data.remote.dto.user.ModifyMyMbtiReq
import com.weave.weave.data.repositoryImpl.UserRepositoryImpl
import com.weave.weave.domain.usecase.Resource

class ModifyMyMbtiUseCase {
    private val userRepositoryImpl = UserRepositoryImpl()

    suspend fun modifyMyMbti(accessToken: String, body: ModifyMyMbtiReq): Resource<Boolean> {

        return try {
            val res = userRepositoryImpl.modifyMyMbti("Bearer $accessToken", body)

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
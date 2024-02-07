package com.weave.weave.domain.usecase.profile

import com.weave.weave.data.remote.dto.user.SetMyAnimalTypeReq
import com.weave.weave.data.repositoryImpl.UserRepositoryImpl
import com.weave.weave.domain.usecase.Resource

class SetMyAnimalTypeUseCase {
    private val userRepositoryImpl = UserRepositoryImpl()

    suspend fun setMyAnimalType(accessToken: String, body: SetMyAnimalTypeReq): Resource<Boolean> {

        return try {
            val res = userRepositoryImpl.setMyAnimalType("Bearer $accessToken", body)

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
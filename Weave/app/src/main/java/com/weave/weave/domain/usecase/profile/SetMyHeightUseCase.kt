package com.weave.weave.domain.usecase.profile

import com.weave.weave.core.GlobalApplication
import com.weave.weave.data.remote.dto.user.SetMyHeightReq
import com.weave.weave.data.repositoryImpl.UserRepositoryImpl
import com.weave.weave.domain.usecase.Resource
import kotlinx.coroutines.flow.first

class SetMyHeightUseCase {
    private val userRepositoryImpl = UserRepositoryImpl()

    suspend fun setMyHeight(accessToken: String, body: SetMyHeightReq): Resource<Boolean> {
        if(GlobalApplication.app.getUserDataStore().getLoginToken().first().refreshToken == ""){
            return Resource.Error("RefreshToken is Null")
        }

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
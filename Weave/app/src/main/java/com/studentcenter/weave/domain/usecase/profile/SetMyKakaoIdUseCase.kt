package com.studentcenter.weave.domain.usecase.profile

import com.studentcenter.weave.core.GlobalApplication
import com.studentcenter.weave.data.remote.dto.user.SetMyKakaoIdReq
import com.studentcenter.weave.data.repositoryImpl.UserRepositoryImpl
import com.studentcenter.weave.domain.usecase.Resource
import kotlinx.coroutines.flow.first

class SetMyKakaoIdUseCase {
    private val userRepositoryImpl = UserRepositoryImpl()

    suspend fun setMyKakaoId(accessToken: String, body: SetMyKakaoIdReq): Resource<Boolean> {
        if(GlobalApplication.app.getUserDataStore().getLoginToken().first().refreshToken == ""){
            return Resource.Error("RefreshToken is Null")
        }

        return try {
            val res = userRepositoryImpl.setMyKakaoId("Bearer $accessToken", body)

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
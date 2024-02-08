package com.weave.weave.data.repositoryImpl

import com.weave.weave.data.remote.RetrofitClient
import com.weave.weave.data.remote.api.UserService
import com.weave.weave.data.remote.dto.auth.TokenRes
import com.weave.weave.data.remote.dto.user.SetMyAnimalTypeReq
import com.weave.weave.data.remote.dto.user.SetMyHeightReq
import com.weave.weave.data.remote.dto.user.GetMyInfoRes
import com.weave.weave.data.remote.dto.user.ModifyMyMbtiReq
import com.weave.weave.data.remote.dto.user.RegisterUserReq
import com.weave.weave.domain.repository.UserRepository
import okhttp3.ResponseBody
import retrofit2.Response

class UserRepositoryImpl: UserRepository {
    private val service = RetrofitClient.getInstance().create(UserService::class.java)

    override suspend fun registerUser(registerToken: String, userInfo: RegisterUserReq): Response<TokenRes> {
        return service.registerUser(registerToken, userInfo)
    }

    override suspend fun unregisterUser(accessToken: String): Response<ResponseBody> {
        return service.unregisterUser(accessToken)
    }

    override suspend fun getMyInfo(accessToken: String): Response<GetMyInfoRes> {
        return service.getMyInfo(accessToken)
    }

    override suspend fun modifyMyMbti(accessToken: String, body: ModifyMyMbtiReq): Response<ResponseBody> {
        return service.modifyMyMbti(accessToken, body)
    }

    override suspend fun setMyHeight(accessToken: String, body: SetMyHeightReq): Response<ResponseBody> {
        return service.setMyHeight(accessToken, body)
    }

    override suspend fun setMyAnimalType(accessToken: String, body: SetMyAnimalTypeReq): Response<ResponseBody> {
        return service.setMyAnimalType(accessToken, body)
    }
}
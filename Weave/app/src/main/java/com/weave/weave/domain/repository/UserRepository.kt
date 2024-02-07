package com.weave.weave.domain.repository

import com.weave.weave.data.remote.dto.auth.TokenRes
import com.weave.weave.data.remote.dto.user.SetMyAnimalTypeReq
import com.weave.weave.data.remote.dto.user.SetMyHeightReq
import com.weave.weave.data.remote.dto.user.GetMyInfoRes
import com.weave.weave.data.remote.dto.user.ModifyMyMbtiReq
import com.weave.weave.data.remote.dto.user.RegisterUserReq
import okhttp3.ResponseBody
import retrofit2.Response

interface UserRepository {
    suspend fun registerUser(registerToken: String, userInfo: RegisterUserReq): Response<TokenRes>

    suspend fun unregisterUser(accessToken: String): Response<ResponseBody>

    suspend fun getMyInfo(accessToken: String): Response<GetMyInfoRes>

    suspend fun modifyMyMbti(accessToken: String, body: ModifyMyMbtiReq): Response<ResponseBody>

    suspend fun setMyHeight(accessToken: String, body: SetMyHeightReq): Response<ResponseBody>

    suspend fun setMyAnimalType(accessToken: String, body: SetMyAnimalTypeReq): Response<ResponseBody>
}
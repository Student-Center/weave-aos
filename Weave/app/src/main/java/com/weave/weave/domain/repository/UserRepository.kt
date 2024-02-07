package com.weave.weave.domain.repository

import com.weave.weave.data.remote.dto.auth.TokenRes
import com.weave.weave.data.remote.dto.user.SetMyAnimalTypeReq
import com.weave.weave.data.remote.dto.user.SetMyHeightReq
import com.weave.weave.data.remote.dto.user.GetMyInfoRes
import com.weave.weave.data.remote.dto.user.ModifyMyMbtiReq
import com.weave.weave.data.remote.dto.user.RegisterUserReq
import retrofit2.Response

interface UserRepository {
    suspend fun registerUser(registerToken: String, userInfo: RegisterUserReq): Response<TokenRes>

    suspend fun unregisterUser(accessToken: String)

    suspend fun getMyInfo(accessToken: String): Response<GetMyInfoRes>

    suspend fun getMyMbit(accessToken: String, body: ModifyMyMbtiReq)

    suspend fun getMyHeight(accessToken: String, body: SetMyHeightReq)

    suspend fun getMyAnimalType(accessToken: String, body: SetMyAnimalTypeReq)
}
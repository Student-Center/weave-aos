package com.weave.weave.data.remote.api

import com.weave.weave.data.remote.dto.auth.TokenRes
import com.weave.weave.data.remote.dto.user.SetMyAnimalTypeReq
import com.weave.weave.data.remote.dto.user.SetMyHeightReq
import com.weave.weave.data.remote.dto.user.GetMyInfoRes
import com.weave.weave.data.remote.dto.user.ModifyMyMbtiReq
import com.weave.weave.data.remote.dto.user.RegisterUserReq
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST

interface UserService {

    @POST("/api/users")
    suspend fun registerUser(
        @Header("Register-Token") registerToken: String,
        @Body userInfo: RegisterUserReq
    ): Response<TokenRes>

    @DELETE("/api/users")
    suspend fun unregisterUser(
        @Header("Authorization") accessToken: String
    )

    @GET("/api/users/my")
    suspend fun getMyInfo(
        @Header("Authorization") accessToken: String
    ): Response<GetMyInfoRes>

    @PATCH("/api/users/my/mbti")
    suspend fun modifyMyMbti(
        @Header("Authorization") accessToken: String,
        @Body body: ModifyMyMbtiReq
    )

    @PATCH("/api/users/my/height")
    suspend fun setMyHeight(
        @Header("Authorization") accessToken: String,
        @Body body: SetMyHeightReq
    )

    @PATCH("/api/users/my/animal-type")
    suspend fun setMyAnimalType(
        @Header("Authorization") accessToken: String,
        @Body body: SetMyAnimalTypeReq
    )
}
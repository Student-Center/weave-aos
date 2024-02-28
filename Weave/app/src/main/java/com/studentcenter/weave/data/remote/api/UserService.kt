package com.studentcenter.weave.data.remote.api

import com.studentcenter.weave.data.remote.dto.auth.TokenRes
import com.studentcenter.weave.data.remote.dto.user.SetMyAnimalTypeReq
import com.studentcenter.weave.data.remote.dto.user.SetMyHeightReq
import com.studentcenter.weave.data.remote.dto.user.GetMyInfoRes
import com.studentcenter.weave.data.remote.dto.user.ModifyMyMbtiReq
import com.studentcenter.weave.data.remote.dto.user.RegisterUserReq
import com.studentcenter.weave.data.remote.dto.user.SendVerificationEmailReq
import com.studentcenter.weave.data.remote.dto.user.SetMyKakaoIdReq
import com.studentcenter.weave.data.remote.dto.user.VerifyUnivEmailReq
import okhttp3.ResponseBody
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
    ): Response<ResponseBody>

    @GET("/api/users/my")
    suspend fun getMyInfo(
        @Header("Authorization") accessToken: String
    ): Response<GetMyInfoRes>

    @PATCH("/api/users/my/mbti")
    suspend fun modifyMyMbti(
        @Header("Authorization") accessToken: String,
        @Body body: ModifyMyMbtiReq
    ): Response<ResponseBody>

    @PATCH("/api/users/my/height")
    suspend fun setMyHeight(
        @Header("Authorization") accessToken: String,
        @Body body: SetMyHeightReq
    ): Response<ResponseBody>

    @PATCH("/api/users/my/animal-type")
    suspend fun setMyAnimalType(
        @Header("Authorization") accessToken: String,
        @Body body: SetMyAnimalTypeReq
    ): Response<ResponseBody>

    @POST("/api/users/my/university-verification:verify")
    suspend fun verifyUnivEmail(
        @Header("Authorization") accessToken: String,
        @Body body: VerifyUnivEmailReq
    ): Response<ResponseBody>

    @POST("/api/users/my/university-verification:send")
    suspend fun sendVerificationEmail(
        @Header("Authorization") accessToken: String,
        @Body body: SendVerificationEmailReq
    ): Response<ResponseBody>

    @PATCH("/api/users/my/kakao-id")
    suspend fun setMyKakaoId(
        @Header("Authorization") accessToken: String,
        @Body body: SetMyKakaoIdReq
    ): Response<ResponseBody>
}
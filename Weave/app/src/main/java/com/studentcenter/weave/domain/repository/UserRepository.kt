package com.studentcenter.weave.domain.repository

import com.studentcenter.weave.data.remote.dto.auth.TokenRes
import com.studentcenter.weave.data.remote.dto.user.SetMyAnimalTypeReq
import com.studentcenter.weave.data.remote.dto.user.SetMyHeightReq
import com.studentcenter.weave.data.remote.dto.user.GetMyInfoRes
import com.studentcenter.weave.data.remote.dto.user.GetUploadUrlRes
import com.studentcenter.weave.data.remote.dto.user.ModifyMyMbtiReq
import com.studentcenter.weave.data.remote.dto.user.RegisterUserReq
import com.studentcenter.weave.data.remote.dto.user.SendVerificationEmailReq
import com.studentcenter.weave.data.remote.dto.user.SetMyKakaoIdReq
import com.studentcenter.weave.data.remote.dto.user.UploadCallbackReq
import com.studentcenter.weave.data.remote.dto.user.VerifyUnivEmailReq
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response

interface UserRepository {
    suspend fun registerUser(registerToken: String, userInfo: RegisterUserReq): Response<TokenRes>

    suspend fun unregisterUser(accessToken: String): Response<ResponseBody>

    suspend fun getMyInfo(accessToken: String): Response<GetMyInfoRes>

    suspend fun modifyMyMbti(accessToken: String, body: ModifyMyMbtiReq): Response<ResponseBody>

    suspend fun setMyHeight(accessToken: String, body: SetMyHeightReq): Response<ResponseBody>

    suspend fun setMyAnimalType(accessToken: String, body: SetMyAnimalTypeReq): Response<ResponseBody>

    suspend fun verifyUnivEmail(accessToken: String, body: VerifyUnivEmailReq): Response<ResponseBody>

    suspend fun sendVerificationEmail(accessToken: String, body: SendVerificationEmailReq): Response<ResponseBody>

    suspend fun setMyKakaoId(accessToken: String, body: SetMyKakaoIdReq): Response<ResponseBody>

    suspend fun getUploadUrl(accessToken: String, extension: String): Response<GetUploadUrlRes>

    suspend fun uploadProfileImage(uploadUrl: String, file: RequestBody): Response<ResponseBody>

    suspend fun uploadCallback(accessToken: String, body: UploadCallbackReq): Response<ResponseBody>
}
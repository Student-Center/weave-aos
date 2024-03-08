package com.studentcenter.weave.data.repositoryImpl

import com.studentcenter.weave.data.remote.RetrofitClient
import com.studentcenter.weave.data.remote.api.UserService
import com.studentcenter.weave.data.remote.dto.auth.TokenRes
import com.studentcenter.weave.data.remote.dto.user.SetMyAnimalTypeReq
import com.studentcenter.weave.data.remote.dto.user.SetMyHeightReq
import com.studentcenter.weave.data.remote.dto.user.GetMyInfoRes
import com.studentcenter.weave.data.remote.dto.user.GetUploadUrlRes
import com.studentcenter.weave.data.remote.dto.user.ModifyMyMbtiReq
import com.studentcenter.weave.data.remote.dto.user.RegisterUserReq
import com.studentcenter.weave.data.remote.dto.user.SendVerificationEmailReq
import com.studentcenter.weave.data.remote.dto.user.SetMyKakaoIdReq
import com.studentcenter.weave.data.remote.dto.user.VerifyUnivEmailReq
import com.studentcenter.weave.domain.repository.UserRepository
import okhttp3.MultipartBody
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

    override suspend fun verifyUnivEmail(accessToken: String, body: VerifyUnivEmailReq): Response<ResponseBody> {
        return service.verifyUnivEmail(accessToken, body)
    }

    override suspend fun sendVerificationEmail(accessToken: String, body: SendVerificationEmailReq): Response<ResponseBody> {
        return service.sendVerificationEmail(accessToken, body)
    }

    override suspend fun setMyKakaoId(accessToken: String, body: SetMyKakaoIdReq): Response<ResponseBody> {
        return service.setMyKakaoId(accessToken, body)
    }

    override suspend fun getUploadUrl(accessToken: String, extension: String): Response<GetUploadUrlRes> {
        return service.getUploadUrl(accessToken, extension)
    }

    override suspend fun uploadProfileImage(uploadUrl: String, file: MultipartBody.Part): Response<ResponseBody> {
        return service.uploadProfileImage(uploadUrl, file)
    }

    override suspend fun uploadCallback(accessToken: String): Response<ResponseBody> {
        return service.uploadCallback(accessToken)
    }
}
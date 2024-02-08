package com.weave.weave.data.repositoryImpl

import com.weave.weave.data.remote.RetrofitClient
import com.weave.weave.data.remote.api.AuthService
import com.weave.weave.data.remote.dto.auth.LoginTokenReq
import com.weave.weave.data.remote.dto.auth.RefreshTokenReq
import com.weave.weave.data.remote.dto.auth.TokenRes
import com.weave.weave.domain.repository.AuthRepository
import okhttp3.ResponseBody
import retrofit2.Response

class AuthRepositoryImpl: AuthRepository {
    private val service = RetrofitClient.getInstance().create(AuthService::class.java)

    override suspend fun refreshLoginToken(refreshTokenReq: RefreshTokenReq): Response<TokenRes> {
        return service.refreshLoginToken(refreshTokenReq)
    }

    override suspend fun login(provider: String, idToken: LoginTokenReq): Response<TokenRes> {
        return service.login(provider, idToken)
    }

    override suspend fun logOut(accessToken: String): Response<ResponseBody> {
        return service.logOut(accessToken)
    }

}
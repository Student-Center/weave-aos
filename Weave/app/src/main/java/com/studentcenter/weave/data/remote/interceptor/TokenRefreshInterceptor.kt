package com.studentcenter.weave.data.remote.interceptor

import android.util.Log
import com.google.gson.Gson
import com.studentcenter.weave.BuildConfig
import com.studentcenter.weave.core.GlobalApplication.Companion.app
import com.studentcenter.weave.core.GlobalApplication.Companion.isFinish
import com.studentcenter.weave.core.GlobalApplication.Companion.loginState
import com.studentcenter.weave.data.remote.dto.auth.TokenRes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject

class TokenRefreshInterceptor : Interceptor {
    private val tag = this.javaClass.simpleName

    override fun intercept(chain: Interceptor.Chain): Response {
        val originRequest = chain.request()
        var response = chain.proceed(originRequest)

        if (!response.isSuccessful && response.code == 400) {
            val responseBody = response.peekBody(Long.MAX_VALUE)
            val exceptionCode = JSONObject(responseBody.string()).getString("exceptionCode")

            if (exceptionCode.contains("JWT") || exceptionCode == "API-003") {
                if (loginState) {
                    val refreshToken = runBlocking(Dispatchers.IO) {
                        app.getUserDataStore().getLoginToken().first().refreshToken
                    }

                    if(refreshToken.isEmpty()) {
                        handleTokenRefreshFailure()
                        return response
                    }

                    Log.i("REFRESH_INTERCEPTOR", "토큰 갱신 진행")
                    val jsonObject = JSONObject().apply {
                        put("refreshToken", refreshToken)
                    }

                    val mediaType = "application/json; charset=utf-8".toMediaType()

                    val refreshRequest = Request.Builder()
                        .url("${BuildConfig.BASE_URL}/api/auth/refresh")
                        .post(jsonObject.toString().toRequestBody(mediaType))
                        .build()

                    val refreshResponse = chain.proceed(refreshRequest)

                    if (refreshResponse.isSuccessful) {
                        val refreshResponseJson = Gson().fromJson(refreshResponse.body?.string(), TokenRes::class.java)
                        Log.d(tag, "Refresh Token 재발급 성공")

                        runBlocking(Dispatchers.IO) {
                            app.getUserDataStore().updatePreferencesRefreshToken(refreshResponseJson.refreshToken)
                            app.getUserDataStore().updatePreferencesAccessToken(refreshResponseJson.accessToken)
                        }

                        val newRequest = originRequest.newBuilder()
                            .removeHeader("Authorization")
                            .addHeader("Authorization", "Bearer ${refreshResponseJson.accessToken}")
                            .build()

                        response.close()
                        response = chain.proceed(newRequest)
                    } else {
                        Log.d(tag, "Refresh Token 재발급 실패 - 만료됨")
                        handleTokenRefreshFailure()
                    }
                }
            }
        }

        return response
    }

    private fun handleTokenRefreshFailure() {
        CoroutineScope(Dispatchers.Main).launch {
            app.getUserDataStore().clearData()
            isFinish.value = true
        }
    }
}

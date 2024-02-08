package com.weave.weave.data.remote

import android.util.Log
import com.google.gson.Gson
import com.weave.weave.BuildConfig
import com.weave.weave.core.GlobalApplication.Companion.app
import com.weave.weave.core.GlobalApplication.Companion.isFinish
import com.weave.weave.core.GlobalApplication.Companion.loginState
import com.weave.weave.data.remote.dto.auth.TokenRes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.Route
import org.json.JSONObject

class TokenRefreshInterceptor : Authenticator {
    private val tag = this.javaClass.simpleName

    override fun authenticate(route: Route?, response: Response): Request? {
        Log.d(tag, "authenticate 진입")

        var refreshToken: String?
        var accessToken: String?


        val originRequest = response.request
        if (originRequest.header("Authorization").isNullOrEmpty()) {
            Log.d(tag, "요청 헤더에 토큰이 없음")
            return null
        }

        if (!loginState) {
            Log.d(tag, "로그인 상태가 아님")
            return null
        }

        runBlocking(Dispatchers.IO){
            refreshToken = app.getUserDataStore().getLoginToken().first().refreshToken
        }

        val jsonObject = JSONObject().apply {
            put("refreshToken", refreshToken)
        }

        val mediaType = "application/json; charset=utf-8".toMediaType()

        val refreshRequest = Request.Builder()
            .url("${BuildConfig.BASE_URL}/api/auth/refresh")
            .post(jsonObject.toString().toRequestBody(mediaType))
            .build()

        try {
            val refreshResponse = OkHttpClient().newCall(refreshRequest).execute()
            val refreshResponseJson =
                Gson().fromJson(refreshResponse.body?.string(), TokenRes::class.java)

            if (refreshResponse.isSuccessful) {
                // 재발급 성공
                Log.d(tag, "Refresh Token 재발급 성공")
                runBlocking(Dispatchers.IO){
                    app.getUserDataStore().updatePreferencesRefreshToken(refreshResponseJson.refreshToken)
                    app.getUserDataStore().updatePreferencesAccessToken(refreshResponseJson.accessToken)
                    accessToken = refreshResponseJson.accessToken
                }


                return originRequest.newBuilder()
                    .removeHeader("Authorization")
                    .addHeader("Authorization", "Bearer $accessToken")
                    .build()

            } else {
                // 재발급 실패 - refreshToken 만료
                Log.d(tag, "Refresh Token 재발급 실패 - 만료됨")

                CoroutineScope(Dispatchers.Main).launch {
                    app.getUserDataStore().clearData()
                    isFinish.value = true
                }
            }
        } catch (e: Exception) {
            // 네트워크 예외 처리
//            Log.e(tag, "Network error: ${e.message}")
//
//            CoroutineScope(Dispatchers.Main).launch {
//                encryptedPrefs.clearAll()
//                prefs.setIsLoginState(false)
//                isFinish.value = true
//            }
        }

        return null
    }
}
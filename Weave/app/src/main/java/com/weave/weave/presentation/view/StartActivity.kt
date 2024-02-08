package com.weave.weave.presentation.view

import android.content.Intent
import android.util.Log
import com.weave.weave.R
import com.weave.weave.core.GlobalApplication.Companion.app
import com.weave.weave.data.remote.dto.auth.RefreshTokenReq
import com.weave.weave.databinding.ActivityStartBinding
import com.weave.weave.domain.usecase.Resource
import com.weave.weave.presentation.base.BaseActivity
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.common.model.KakaoSdkError
import com.kakao.sdk.user.UserApiClient
import com.weave.weave.domain.usecase.auth.RefreshLoginTokenUseCase
import com.weave.weave.domain.usecase.profile.GetMyInfoUseCase
import com.weave.weave.presentation.view.signIn.SignInActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class StartActivity: BaseActivity<ActivityStartBinding>(R.layout.activity_start) {

    override fun init() {
        kakaoLogin()
    }
    
    private fun kakaoLogin(){
        if(AuthApiClient.instance.hasToken()){
            UserApiClient.instance.accessTokenInfo { _, error ->
                if(error != null){
                    if(error is KakaoSdkError && error.isInvalidTokenError()){
                        Log.e("Auth", "토큰이 유효하지 않아 카카오 로그인 필요")
                    } else {
                        Log.e("Auth", "카카오 기타 에러", error)
                    }
                    moveActivity(SignInActivity())
                } else {
                    // 카카오 토큰 유효성 체크 성공 (필요 시 토크 갱신 됨)
                    CoroutineScope(Dispatchers.IO).launch {
                        Log.i("Auth", "자동 로그인 진행")

                        val accessToken = app.getUserDataStore().getLoginToken().first().accessToken

                        when(val res = GetMyInfoUseCase().getMyInfo(accessToken)){
                            is Resource.Success -> {
                                Log.i("Auth", "유효성 검사 성공")

                                launch(Dispatchers.Main) {
                                    moveActivity(MainActivity())
                                }
                            }
                            is Resource.Error -> {
                                Log.e("Auth", "유효성 검사 실패: ${res.message}")
                                refresh()
                            }
                            else -> {}
                        }
                    }
                }
            }
        } else {
            Log.i("Auth", "카카오 토큰 없음 -> SignInActivity 이동")
            moveActivity(SignInActivity())
        }
    }

    private suspend fun refresh(){
        Log.i("Auth", "서버 토큰 갱신 진행")

        val refreshToken = app.getUserDataStore().getLoginToken().first().refreshToken

        when(val res = RefreshLoginTokenUseCase().refreshLoginToken(RefreshTokenReq(refreshToken))){
            is Resource.Success -> {
                Log.i("Auth", "서버 토큰 갱신 성공")

                runBlocking {
                    app.getUserDataStore().updatePreferencesAccessToken(res.data.accessToken)
                    app.getUserDataStore().updatePreferencesRefreshToken(res.data.refreshToken)
                }

                CoroutineScope(Dispatchers.Main).launch {
                    moveActivity(MainActivity())
                }
            }
            is Resource.Error -> {
                Log.e("Auth", "서버 토큰 갱신 실패: ${res.message}")
                CoroutineScope(Dispatchers.Main).launch {
                    moveActivity(SignInActivity())
                }
            }
            else -> {}
        }
    }

    private fun moveActivity(p: Any){
        val intent = Intent(this@StartActivity, p::class.java)
        startActivity(intent)
        finish()
    }
}
package com.weave.weave.presentation.view.signIn

import android.content.Intent
import android.util.Log
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.weave.weave.R
import com.weave.weave.core.GlobalApplication.Companion.app
import com.weave.weave.core.GlobalApplication.Companion.loginState
import com.weave.weave.core.GlobalApplication.Companion.registerToken
import com.weave.weave.data.remote.dto.auth.LoginTokenReq
import com.weave.weave.databinding.ActivitySignInBinding
import com.weave.weave.domain.usecase.auth.LoginUseCase
import com.weave.weave.domain.usecase.Resource
import com.weave.weave.presentation.base.BaseActivity
import com.weave.weave.presentation.view.StartActivity
import com.weave.weave.presentation.view.signUp.SignUpActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class SignInActivity: BaseActivity<ActivitySignInBinding>(R.layout.activity_sign_in) {

    override fun init() {
        binding.btnKakaoLogin.setOnClickListener {
            kakaoLogin()
        }


        binding.btnAppleLogin.setOnClickListener {
            registerToken = "testToken"
            moveActivity(SignUpActivity()) // test 용
        }
    }

    private fun moveActivity(p: Any){
        val intent = Intent(this, p::class.java)
        startActivity(intent)
        finish()
    }

    private fun kakaoLogin(){
        // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
            UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                if (error != null) {
                    Log.e(TAG, "카카오톡으로 로그인 실패", error)

                    // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                    // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }

                    // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                    UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
                } else if (token != null) {
                    Log.i(TAG, "카카오톡으로 로그인 성공\nID TOKEN: ${token.idToken}")
                    serverLogin("KAKAO", token.idToken.toString())
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
        }
    }

    // 카카오계정으로 로그인 공통 callback 구성
    // 카카오톡으로 로그인 할 수 없어 카카오계정으로 로그인할 경우 사용됨
    val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            Log.e(TAG, "카카오계정으로 로그인 실패", error)
        } else if (token != null) {
            Log.i(TAG, "카카오계정으로 로그인 성공\nID TOKEN: ${token.idToken}")
            serverLogin("KAKAO", token.idToken.toString())
        }
    }



    // 회원가입 O -> accessToken, refreshToken 발급됨 (200)
    // 회원가입 X -> registerToken 발급됨 (401)
    private fun serverLogin(provider: String, idToken: String){
        CoroutineScope(Dispatchers.IO).launch {
            when (val res = LoginUseCase().login(provider, LoginTokenReq(idToken))) {
                is Resource.Success -> {
                    runBlocking {
                        app.getUserDataStore().updatePreferencesAccessToken(res.data.accessToken)
                        app.getUserDataStore().updatePreferencesRefreshToken(res.data.refreshToken)
                    }

                    launch(Dispatchers.Main) {
                        moveActivity(StartActivity())
                    }

                    loginState = true
                }


                is Resource.Error -> {
                    val msg = res.message
                    if(msg.contains("registerToken")){
                        Log.i(TAG, "회원가입 여부: False")
                        registerToken = msg.replace("registerToken ", "")

                        launch(Dispatchers.Main) {
                            moveActivity(SignUpActivity())
                        }
                    } else {
                        Log.e(TAG, msg)
                    }
                }

                else -> {}
            }
        }
    }
}
package com.weave.weave.presentation.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.weave.weave.R
import com.weave.weave.core.GlobalApplication.Companion.app
import com.weave.weave.data.remote.dto.auth.RefreshTokenReq
import com.weave.weave.domain.usecase.auth.RefreshLoginTokenUseCase
import com.weave.weave.domain.usecase.Resource
import com.weave.weave.domain.usecase.profile.GetMyInfoUseCase
import com.weave.weave.presentation.view.signIn.SignInActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class StartActivity: AppCompatActivity() {

    private lateinit var splashScreen: SplashScreen

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        splashScreen = installSplashScreen()
        startSplash()
        setContentView(R.layout.activity_start)
    }

    private fun startSplash() {
        splashScreen.setOnExitAnimationListener { splashScreenView ->
            Thread {
                runOnUiThread {
                    serverTokenValid()
//                    moveActivity(MainActivity())
                    splashScreenView.remove()
                }
            }.start()
        }
    }

    // 저장된 토큰이 있는지 확인하고 유효성 검사 진행
    private fun serverTokenValid(){
        CoroutineScope(Dispatchers.IO).launch {
            app.getUserDataStore().getLoginToken().collect {
                val accessToken = it.accessToken
                val refreshToken = it.refreshToken

                if(accessToken == "" || refreshToken == ""){
                    launch(Dispatchers.Main){
                        moveActivity(SignInActivity())
                    }
                } else {
                    tokenValidation()
                }

            }
        }
    }

    private fun moveActivity(p: Any){
        val intent = Intent(this, p::class.java)
        startActivity(intent)
        finish()
    }

    // 토큰 유효성 검사를 위해 GetMyInfo API를 호출
    // 여기서는 accessToken의 유효성만 검사
    private suspend fun tokenValidation(){
        app.getUserDataStore().getLoginToken().collect {
            when(val res = GetMyInfoUseCase().getMyInfo(it.accessToken)){
                is Resource.Success -> {
                    Log.i("START", "토큰 유효 -> 메인 액티비티 이동")
                    CoroutineScope(Dispatchers.Main).launch{
                        moveActivity(MainActivity())
                    }
                }
                is Resource.Error -> {
                    Log.e("START", "토큰 유효하지 않음: ${res.message}")
                    refreshLoginToken()
                }
                else -> {}
            }
        }
    }

    // accessToken이 유효하지 않다면 Refresh 요청
    //
    private fun refreshLoginToken(){
        runBlocking(Dispatchers.IO) {
            app.getUserDataStore().getLoginToken().collect {
                // 재발급 Interceptor에서 loginState가 true일 때만 수행하도록 해야 함
                Log.i("START", "토큰 재발급 진행")
                when(val res = RefreshLoginTokenUseCase().refreshLoginToken(RefreshTokenReq(it.refreshToken))){
                    // AccessToken 만료, RefreshToken 유효
                    // 토큰 재발급 성공
                    is Resource.Success -> {
                        Log.i("START", "토큰 재발급 성공")
                        app.getUserDataStore().updatePreferencesRefreshToken(res.data.refreshToken)
                        app.getUserDataStore().updatePreferencesAccessToken(res.data.accessToken)

//                      재발급한 AccessToken으로 유효성 검사 다시 진행 후 MainActivity로 이동
                        tokenValidation()
                    }
                    // AccessToken 만료, RefreshToken 만료 (재발급 실패 경우)
                    // 로컬에 저장된 유저 데이터 지우고 SignInActivity로 이동
                    is Resource.Error -> {
                        Log.e("START", "토큰 재발급 실패: ${res.message}")
                        app.getUserDataStore().clearData()
                        app.getSettingDataStore().clearData()
                        launch(Dispatchers.Main){
                            moveActivity(SignInActivity())
                        }
                    }
                    else -> {}
                }
            }
        }
    }
}
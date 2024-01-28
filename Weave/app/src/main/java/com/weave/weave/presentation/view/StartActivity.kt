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
import com.weave.weave.domain.usecase.LoginUseCase
import com.weave.weave.domain.usecase.Resource
import com.weave.weave.presentation.view.signIn.SignInActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
//                    serverTokenValid()
                    moveActivity(SignInActivity())
                    splashScreenView.remove()
                }
            }.start()
        }
    }

    private fun serverTokenValid(){
        CoroutineScope(Dispatchers.IO).launch {
            app.getUserDataStore().getLoginToken().collect {
                val accessToken = it.accessToken
                val refreshToken = it.refreshToken

                if(accessToken == "" || refreshToken == ""){
                    launch(Dispatchers.Main){
                        moveActivity(SignInActivity())
                    }
                }

                // AccessToken 유효
                // MainActivity로 이동
//                if(){
//                  moveActivity(MainActivity())
//                }

                // 재발급 Interceptor에서 loginState가 true일 때만 수행하도록 해야 함
                Log.i("START", "토큰 재발급 진행")
                when(val res = LoginUseCase().refreshLoginToken(RefreshTokenReq(refreshToken))){
                    // AccessToken 만료, RefreshToken 유효
                    // 토큰 재발급 성공
                    is Resource.Success -> {
                        Log.i("START", "토큰 재발급 성공")
                        app.getUserDataStore().updatePreferencesRefreshToken(res.data.refreshToken)
                        app.getUserDataStore().updatePreferencesAccessToken(res.data.accessToken)

                        // 재발급한 AccessToken으로 유효성 검사 다시 진행 후 MainActivity로 이동
//                             if(){
//                                 launch(Dispatchers.Main){
//                                     moveActivity(MainActivity())
//                                 }
//                             }

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


    private fun moveActivity(p: Any){
        val intent = Intent(this, p::class.java)
        startActivity(intent)
    }
}
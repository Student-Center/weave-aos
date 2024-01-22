package com.weave.presentation.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.model.KakaoSdkError
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApiClient
import com.weave.presentation.BuildConfig
import com.weave.presentation.R
import com.weave.presentation.view.signIn.SignInActivity

class StartActivity: AppCompatActivity() {

    private lateinit var splashScreen: SplashScreen

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_KEY)

        val keyHash = Utility.getKeyHash(this)
        Log.i("Hash", keyHash)

        splashScreen = installSplashScreen()
        startSplash()
        setContentView(R.layout.activity_start)
    }

    private fun startSplash() {
        splashScreen.setOnExitAnimationListener { splashScreenView ->
            Thread {
                runOnUiThread {
                    splashScreenView.remove()
//                    moveNextActivity()
                    LoginTest()
                }
            }.start()
        }
    }

    private fun moveNextActivity(){
        if(AuthApiClient.instance.hasToken()){
            Log.d("test", "hasToken: true")
            UserApiClient.instance.accessTokenInfo { _, error ->
                if (error != null) {
                    if (error is KakaoSdkError && error.isInvalidTokenError()) {
                        Log.e("test", "토큰이 유효하지 않아 사용자 로그인 필요", error)
                    } else {
                        Log.e("test", "기타 에러", error)
                    }
                    val intent = Intent(this, SignInActivity::class.java)
                    startActivity(intent)
                } else {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
            }
        } else {
            Log.d("test", "hasToken: false")
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }
    }

    private fun LoginTest(){
        val intent = Intent(this, SignInActivity::class.java)
        startActivity(intent)
    }
}
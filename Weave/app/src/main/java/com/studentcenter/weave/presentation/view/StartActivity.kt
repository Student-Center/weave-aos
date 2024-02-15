package com.studentcenter.weave.presentation.view

import android.content.Intent
import android.util.Log
import com.studentcenter.weave.R
import com.studentcenter.weave.core.GlobalApplication.Companion.app
import com.studentcenter.weave.data.remote.dto.auth.RefreshTokenReq
import com.studentcenter.weave.databinding.ActivityStartBinding
import com.studentcenter.weave.domain.usecase.Resource
import com.studentcenter.weave.presentation.base.BaseActivity
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.common.model.KakaoSdkError
import com.kakao.sdk.user.UserApiClient
import com.studentcenter.weave.core.GlobalApplication.Companion.myInfo
import com.studentcenter.weave.domain.entity.profile.MyInfoEntity
import com.studentcenter.weave.domain.usecase.auth.RefreshLoginTokenUseCase
import com.studentcenter.weave.domain.usecase.profile.GetMyInfoUseCase
import com.studentcenter.weave.presentation.view.signIn.SignInActivity
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
                    serverTokenValidation()
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

                serverTokenValidation()
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

    private fun serverTokenValidation(){
        CoroutineScope(Dispatchers.IO).launch {
            Log.i("Auth", "자동 로그인 진행")

            val accessToken = app.getUserDataStore().getLoginToken().first().accessToken

            when(val res = GetMyInfoUseCase().getMyInfo(accessToken)){
                is Resource.Success -> {
                    Log.i("Auth", "유효성 검사 성공")

                    setMyInfo(res.data)

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

    private fun moveActivity(p: Any){
        val intent = Intent(this@StartActivity, p::class.java)
        startActivity(intent)
        finish()
    }

    private fun setMyInfo(data: MyInfoEntity){
        myInfo = MyInfoEntity(
            id = data.id,
            nickname = data.nickname,
            birthYear = data.birthYear,
            universityName = data.universityName,
            majorName = data.majorName,
            avatar = data.avatar,
            mbti = data.mbti,
            animalType = data.animalType,
            height = data.height,
            isUniversityEmailVerified = data.isUniversityEmailVerified,
            sil = data.sil
        )
    }
}
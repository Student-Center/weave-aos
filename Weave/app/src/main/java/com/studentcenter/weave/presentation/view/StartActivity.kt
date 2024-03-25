package com.studentcenter.weave.presentation.view

import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.common.model.KakaoSdkError
import com.kakao.sdk.user.UserApiClient
import com.studentcenter.weave.R
import com.studentcenter.weave.core.GlobalApplication.Companion.app
import com.studentcenter.weave.core.GlobalApplication.Companion.density
import com.studentcenter.weave.core.GlobalApplication.Companion.invitationCode
import com.studentcenter.weave.core.GlobalApplication.Companion.locations
import com.studentcenter.weave.core.GlobalApplication.Companion.loginState
import com.studentcenter.weave.core.GlobalApplication.Companion.myInfo
import com.studentcenter.weave.data.remote.dto.auth.RefreshTokenReq
import com.studentcenter.weave.databinding.ActivityStartBinding
import com.studentcenter.weave.domain.entity.profile.MyInfoEntity
import com.studentcenter.weave.domain.usecase.Resource
import com.studentcenter.weave.domain.usecase.auth.RefreshLoginTokenUseCase
import com.studentcenter.weave.domain.usecase.profile.GetMyInfoUseCase
import com.studentcenter.weave.domain.usecase.team.GetLocationsUseCase
import com.studentcenter.weave.presentation.base.BaseActivity
import com.studentcenter.weave.presentation.view.signIn.SignInActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class StartActivity: BaseActivity<ActivityStartBinding>(R.layout.activity_start) {
    private val REQUEST_UPDATE = 1001

    override fun init() {
        val appUpdateManager = AppUpdateManagerFactory.create(this)
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            Log.i(TAG, appUpdateInfo.updateAvailability().toString())
            Log.i(TAG, appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE).toString())

            if(appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)){
                Log.i(TAG, "업데이트 진행")
                requestImmediateUpdate(appUpdateManager)
            } else {
                Log.i(TAG, "업데이트 필요 없음")

                setting()
                kakaoLogin()
            }
        }
    }

    private fun setting(){
        loginState = false
        setLocations()
        density = this.resources.displayMetrics.density
    }
    
    private fun kakaoLogin(){
        if(AuthApiClient.instance.hasToken()){
            UserApiClient.instance.accessTokenInfo { _, error ->
                if(error != null){
                    if(error is KakaoSdkError && error.isInvalidTokenError()){
                        Log.e("Start_Auth", "토큰이 유효하지 않아 카카오 로그인 필요")
                    } else {
                        Log.e("Start_Auth", "카카오 기타 에러", error)
                    }
                    moveActivity(SignInActivity())
                } else {
                    // 카카오 토큰 유효성 체크 성공 (필요 시 토크 갱신 됨)
                    serverTokenValidation()
                }
            }
        } else {
            Log.i("Start_Auth", "카카오 토큰 없음 -> SignInActivity 이동")
            moveActivity(SignInActivity())
        }
    }

    private suspend fun refresh(){
        Log.i("Start_Auth", "서버 토큰 갱신 진행")

        val refreshToken = app.getUserDataStore().getLoginToken().first().refreshToken
        if(refreshToken.isEmpty()) CoroutineScope(Dispatchers.Main).launch { moveActivity(SignInActivity()) }

        when(val res = RefreshLoginTokenUseCase().refreshLoginToken(RefreshTokenReq(refreshToken))){
            is Resource.Success -> {
                Log.i("Start_Auth", "서버 토큰 갱신 성공")

                runBlocking {
                    app.getUserDataStore().updatePreferencesAccessToken(res.data.accessToken)
                    app.getUserDataStore().updatePreferencesRefreshToken(res.data.refreshToken)
                }

                serverTokenValidation()
            }
            is Resource.Error -> {
                Log.e("Start_Auth", "서버 토큰 갱신 실패: ${res.message}")
                CoroutineScope(Dispatchers.Main).launch {
                    moveActivity(SignInActivity())
                }
            }
            else -> {}
        }
    }

    private fun serverTokenValidation(){
        CoroutineScope(Dispatchers.IO).launch {
            Log.i("Start_Auth", "자동 로그인 진행")

            val accessToken = app.getUserDataStore().getLoginToken().first().accessToken

            when(val res = GetMyInfoUseCase().getMyInfo(accessToken)){
                is Resource.Success -> {
                    Log.i("Start_Auth", "유효성 검사 성공")

                    setMyInfo(res.data)

                    launch(Dispatchers.Main) {
                        moveActivity(MainActivity())
                    }
                }
                is Resource.Error -> {
                    Log.e("Start_Auth", "유효성 검사 실패: ${res.message}")
                    refresh()
                }
                else -> {}
            }
        }
    }

    private fun moveActivity(p: Any){
        val moveIntent = Intent(this@StartActivity, p::class.java)
        moveIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)

        if(Intent.ACTION_VIEW == intent.action){
            val uri = intent.data
            if(uri != null){
                when(uri.getQueryParameter("type")){
                    "team" -> {
                        val teamId = uri.getQueryParameter("teamId")
                        if(teamId != null){
                            moveIntent.putExtra("teamId", teamId)
                        }
                    }
                    "invitation" -> {
                        invitationCode = uri.getQueryParameter("code")
                    }
                }
            }
        }

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(moveIntent)
            finish()
        },1000)
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
            kakaoId = data.kakaoId,
            isUniversityEmailVerified = data.isUniversityEmailVerified,
            sil = data.sil
        )
    }

    private fun setLocations(){
        CoroutineScope(Dispatchers.IO).launch {
            when(val res = GetLocationsUseCase().getLocations()){
                is Resource.Success -> { locations = res.data }
                is Resource.Error -> { Log.e(TAG, "지역 정보 로드 실패: ${res.message}") }
                else -> {}
            }
        }
    }

    private fun requestImmediateUpdate(appUpdateManager: AppUpdateManager) {
        val appUpdateInfo = appUpdateManager.appUpdateInfo

        appUpdateInfo.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    AppUpdateType.IMMEDIATE,
                    this,
                    REQUEST_UPDATE
                )
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_UPDATE) {
            if (resultCode != RESULT_OK) {
                Log.e(TAG, "업데이트 실패 또는 취소됨")
                Toast.makeText(this, "업데이트 실패 또는 취소됨", Toast.LENGTH_SHORT).show()

                finish()
            }
        }
    }
}
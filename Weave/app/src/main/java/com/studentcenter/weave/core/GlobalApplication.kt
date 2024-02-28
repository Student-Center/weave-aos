package com.studentcenter.weave.core

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.util.Utility
import com.studentcenter.weave.BuildConfig
import com.studentcenter.weave.data.local.SettingDataStoreModule
import com.studentcenter.weave.data.local.UserDataStoreModule
import com.studentcenter.weave.domain.entity.profile.MyInfoEntity
import com.studentcenter.weave.domain.entity.team.LocationEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GlobalApplication: Application() {
    private lateinit var userDataStore: UserDataStoreModule
    private lateinit var settingDataStore: SettingDataStoreModule

    companion object {
        lateinit var app: GlobalApplication
        var registerToken: String? = null
        var loginState: Boolean = false  // 로그인 상태
        var myInfo: MyInfoEntity? = null // 내 정보
        var locations: List<LocationEntity>? = null // 지역 리소스
        var isFinish = MutableLiveData(false) // 앱 종료 플래그
        var networkState = MutableLiveData(true) // 네트워크 연결 상태
        var isRefresh = MutableLiveData(false) // 네트워크 다이얼로그 다시 시도

        var invitationCode: String? = null // 초대장
    }

    override fun onCreate() {
        super.onCreate()

        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_KEY)

        val keyHash = Utility.getKeyHash(this)
        Log.i("Hash", keyHash)

        app = this
        userDataStore = UserDataStoreModule(this)
        settingDataStore = SettingDataStoreModule(this)
        CoroutineScope(Dispatchers.IO).launch {
            settingDataStore.getSettings().collect {
                loginState = it.loginState
            }
        }
    }

    fun getUserDataStore(): UserDataStoreModule = userDataStore
    fun getSettingDataStore(): SettingDataStoreModule = settingDataStore
}
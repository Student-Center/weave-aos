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
        var loginState: Boolean = false
        var myInfo: MyInfoEntity? = null
        var locations: List<LocationEntity>? = null
        var isFinish = MutableLiveData(false)
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
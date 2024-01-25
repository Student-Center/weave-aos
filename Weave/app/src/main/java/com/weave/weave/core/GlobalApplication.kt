package com.weave.weave.core

import android.app.Application
import android.util.Log
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.util.Utility
import com.weave.weave.BuildConfig
import com.weave.weave.data.local.SettingDataStoreModule
import com.weave.weave.data.local.UserDataStoreModule

class GlobalApplication: Application() {
    private lateinit var userDataStore: UserDataStoreModule
    private lateinit var settingDataStore: SettingDataStoreModule

    companion object {
        lateinit var app: GlobalApplication
    }

    override fun onCreate() {
        super.onCreate()

        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_KEY)

        val keyHash = Utility.getKeyHash(this)
        Log.i("Hash", keyHash)

        app = this
        userDataStore = UserDataStoreModule(this)
        settingDataStore = SettingDataStoreModule(this)
    }

    fun getUserDataStore(): UserDataStoreModule = userDataStore
    fun getSettingDataStore(): SettingDataStoreModule = settingDataStore
}
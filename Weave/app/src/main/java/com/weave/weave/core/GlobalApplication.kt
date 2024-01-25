package com.weave.weave.core

import android.app.Application
import android.util.Log
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.util.Utility
import com.weave.weave.BuildConfig
import com.weave.weave.data.local.DataStoreModule

class GlobalApplication: Application() {
    private lateinit var dataStore: DataStoreModule
    companion object {
        lateinit var app: GlobalApplication
    }

    override fun onCreate() {
        super.onCreate()

        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_KEY)

        val keyHash = Utility.getKeyHash(this)
        Log.i("Hash", keyHash)

        app = this
        dataStore = DataStoreModule(this)
    }

    fun getDataStore(): DataStoreModule = dataStore
}
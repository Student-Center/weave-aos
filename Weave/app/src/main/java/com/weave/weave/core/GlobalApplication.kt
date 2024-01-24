package com.weave.weave.core

import android.app.Application
import android.util.Log
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.util.Utility
import com.weave.weave.BuildConfig

class GlobalApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_KEY)

        val keyHash = Utility.getKeyHash(this)
        Log.i("Hash", keyHash)
    }
}
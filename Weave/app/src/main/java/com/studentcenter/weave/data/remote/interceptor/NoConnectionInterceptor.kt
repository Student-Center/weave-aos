package com.studentcenter.weave.data.remote.interceptor

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.studentcenter.weave.core.GlobalApplication.Companion.networkState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class NoConnectionInterceptor(
    private val context: Context
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        return if (!isConnectionOn()) {
            CoroutineScope(Dispatchers.Main).launch { networkState.value = false }
            throw NoConnectivityException()
        } else {
            CoroutineScope(Dispatchers.Main).launch { networkState.value = true }
            chain.proceed(chain.request())
        }
    }

    private fun postAndroidMInternetCheck(
        connectivityManager: ConnectivityManager
    ): Boolean {
        val network = connectivityManager.activeNetwork
        val connection = connectivityManager.getNetworkCapabilities(network)

        return connection != null && (
                connection.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        connection.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
    }

    private fun isConnectionOn(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager

        return postAndroidMInternetCheck(connectivityManager)
    }

    class NoConnectivityException : IOException() {

        override val message: String
            get() = "인터넷 연결이 끊겼습니다.\nWIFI나 데이터 연결을 확인해주세요"
    }

}
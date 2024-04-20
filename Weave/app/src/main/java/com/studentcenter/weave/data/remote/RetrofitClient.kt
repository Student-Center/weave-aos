package com.studentcenter.weave.data.remote

import com.google.gson.GsonBuilder
import com.studentcenter.weave.BuildConfig
import com.studentcenter.weave.core.GlobalApplication.Companion.app
import com.studentcenter.weave.data.remote.interceptor.NoConnectionInterceptor
import com.studentcenter.weave.data.remote.interceptor.TokenRefreshInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private var instance: Retrofit? = null
    private val gson = GsonBuilder().setLenient().create()

    private val loggingInterceptor = if(BuildConfig.DEBUG){
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }else {
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor(NoConnectionInterceptor(app.applicationContext))
        .addInterceptor(TokenRefreshInterceptor())
        .retryOnConnectionFailure(true)
        .build()


    fun getInstance(): Retrofit {
        if (instance == null) {
            instance = Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build()
        }

        return instance!!
    }
}
package com.jmvincenti.marvelcharacters.data.api

import com.jmvincenti.marvelcharacters.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

abstract class BaseApiClient {

    private val client: OkHttpClient by lazy {
        OkHttpClient.Builder()
                .addInterceptor(AuthInterceptor())
                .build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BuildConfig.API_BASE_URL)
                .build()
    }

    fun <T> getApi(api: Class<T>): T {
        return retrofit.create(api)
    }
}
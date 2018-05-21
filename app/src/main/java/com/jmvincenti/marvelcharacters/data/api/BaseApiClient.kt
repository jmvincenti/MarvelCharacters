package com.jmvincenti.marvelcharacters.data.api

import retrofit2.Retrofit

abstract class BaseApiClient(private val retrofit: Retrofit) {

    fun <T> getApi(api: Class<T>): T {
        return retrofit.create(api)
    }
}
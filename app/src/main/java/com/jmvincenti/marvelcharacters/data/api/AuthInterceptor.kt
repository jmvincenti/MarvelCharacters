package com.jmvincenti.marvelcharacters.data.api

import com.jmvincenti.marvelcharacters.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Auth interceptor, add required parameters to access api
 */
class AuthInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val ts = System.currentTimeMillis().toString()
        val request = chain.request()
        val url = request.url().newBuilder()
                .addQueryParameter(PARAM_APIKEY, BuildConfig.API_PUBLIC_KEY)
                .addQueryParameter(PARAM_TS, ts)
                .addQueryParameter(PARAM_HASH, HashGenerator.getHash(ts, BuildConfig.API_PUBLIC_KEY, BuildConfig.API_PRIVATE_KEY))
                .build()
        return chain.proceed(request.newBuilder().url(url).build())
    }

    private companion object {
        const val PARAM_APIKEY = "apikey"
        const val PARAM_HASH = "hash"
        const val PARAM_TS = "ts"
    }
}
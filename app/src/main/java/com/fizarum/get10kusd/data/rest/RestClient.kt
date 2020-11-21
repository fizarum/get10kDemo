package com.fizarum.get10kusd.data.rest

import com.fizarum.get10kusd.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object RestClient {

    private val baseUrl = "http://www.mocky.io/v2/"

    private val loggingInterceptor = with(HttpLoggingInterceptor()) {
        level = HttpLoggingInterceptor.Level.BODY
        this
    }

    private val client = with(OkHttpClient.Builder()) {
        if (BuildConfig.DEBUG) {
            addInterceptor(loggingInterceptor)
        }
        build()
    }

    val retrofit: Retrofit = Retrofit.Builder().baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(client).build()
}
package com.ulyssesp.activitysync

import io.reactivex.Single
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

class RestAPI {
    private val fitbitApi: FitbitApi

    init {
        val httpInterceptor = HttpLoggingInterceptor()
        httpInterceptor.level = HttpLoggingInterceptor.Level.HEADERS
        val client = OkHttpClient.Builder().addInterceptor(httpInterceptor).build()
        val retrofit = Retrofit.Builder()
                .baseUrl("https://api.fitbit.com/")
                .client(client)
                .addConverterFactory(MoshiConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        fitbitApi = retrofit.create(FitbitApi::class.java)
    }

    fun getFrequentActivities(accessToken: String): Single<List<FitbitActivityType>> {
        return fitbitApi.getFrequentActivities(accessToken)
    }
}
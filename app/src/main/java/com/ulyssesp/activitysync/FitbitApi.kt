package com.ulyssesp.activitysync

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Header

interface FitbitApi {
    @GET("/1/user/-/activities/frequent.json")
    fun getFrequentActivities(@Header("authorization") accessToken: String): Single<List<FitbitActivityType>>
}
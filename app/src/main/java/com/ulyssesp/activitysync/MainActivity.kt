package com.ulyssesp.activitysync

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.customtabs.CustomTabsIntent
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatButton
import android.util.Log
import com.jakewharton.rxbinding2.view.clicks
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MainActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "MainActivity"
    }
    private val restAPI: RestAPI by lazy { RestAPI() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val loginButton: AppCompatButton by lazy { findViewById(R.id.button_fitbit_login) as AppCompatButton }

        loginButton
                .clicks()
                .subscribe(
                        { _ -> openLogin() }
                )
    }

    fun openLogin() {
        val url = "https://www.fitbit.com/oauth2/authorize?response_type=token&client_id=22CVMB&redirect_uri=fitbitsync%3A%2F%2Fcom.ulyssesp.androidactivitysync&scope=activity%20heartrate%20location%20nutrition%20profile%20settings%20sleep%20social%20weight&expires_in=604800"
        val intent = CustomTabsIntent.Builder().build();
        intent.launchUrl(this, Uri.parse(url))
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val accesstoken =
                Uri.parse(intent?.toUri(0))
                        .fragment.split("&")
                        .map { l -> l.split("=") }
                        .filter { l -> l.size > 1 }
                        .map { l -> l[0] to l[1] }
                        .filter { v -> v.first == "access_token" }
                        .map { p -> p.second }
                        .first()

        restAPI.getFrequentActivities("Bearer $accesstoken")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { es -> Log.d(TAG, es.size.toString())},
                        { e -> Log.e(TAG, e.message) }
                )
    }
}

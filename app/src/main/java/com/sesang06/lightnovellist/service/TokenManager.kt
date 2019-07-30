package com.sesang06.lightnovellist.service

import com.google.firebase.messaging.FirebaseMessaging
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class TokenManager {

    private val api = provideLightNovelListApi()

    fun sendToken(token: String): Disposable {
        val compositeDisposable = CompositeDisposable()
        compositeDisposable.add (
            api
                .tokenInfo(token)
                .subscribeOn(Schedulers.io())
                .subscribe({

                }, {

                })



        )
        return compositeDisposable
    }


    fun subscribeTopic() {

        FirebaseMessaging.getInstance().subscribeToTopic("dailyReport")
            .addOnCompleteListener { task ->
                 if (!task.isSuccessful) {
                }
            }
    }
}
package com.sesang06.lightnovellist.service

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

class TokenManager {

    private val api = provideLightNovelListApi()

    fun sendToken(token: String): Disposable {
        val compositeDisposable = CompositeDisposable()
        compositeDisposable.add (
            api
                .tokenInfo(token)
                .subscribe { response ->
                    print(response)
                }


        )
        return compositeDisposable
    }


}
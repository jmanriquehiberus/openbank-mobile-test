package com.javiermp.openbankmobiletest.common.viewmodel

import androidx.lifecycle.ViewModel
import com.javiermp.model.exception.HTTPException
import io.reactivex.observers.DisposableCompletableObserver
import io.reactivex.observers.DisposableObserver
import io.reactivex.observers.DisposableSingleObserver

abstract class CommonEventsViewModel : ViewModel() {

    lateinit var commonLiveEvent: SingleLiveEvent<CommonEvent>

    abstract class ObservableRemoteInterceptor<R>(private val commonLiveEvent: SingleLiveEvent<CommonEvent>) : DisposableObserver<R>() {

        abstract override fun onComplete()

        abstract override fun onNext(t: R)

        override fun onError(e: Throwable) {
            when (e) {
                is HTTPException -> {
                    val errorCode = e.statusCode
                    if (errorCode == 401) {
                        commonLiveEvent.value = CommonEvent.Unauthorized
                    } else {
                        onRegularError(e)
                    }
                }
                else -> onRegularError(e)
            }
        }

        abstract fun onRegularError(e: Throwable)
    }

    abstract class CompletableRemoteInterceptor(private val commonLiveEvent: SingleLiveEvent<CommonEvent>) : DisposableCompletableObserver() {

        abstract override fun onComplete()

        override fun onError(e: Throwable) {
            when (e) {
                is HTTPException -> {
                    val errorCode = e.statusCode
                    if (errorCode == 401) {
                        commonLiveEvent.value = CommonEvent.Unauthorized
                    } else {
                        onRegularError(e)
                    }
                }
                else -> onRegularError(e)
            }
        }

        abstract fun onRegularError(e: Throwable)
    }
}
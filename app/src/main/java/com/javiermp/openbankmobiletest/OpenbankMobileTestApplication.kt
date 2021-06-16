package com.javiermp.openbankmobiletest

import android.app.Application
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.javiermp.openbankmobiletest.common.di.appModule
import com.javiermp.openbankmobiletest.common.di.charactersModule
import com.javiermp.openbankmobiletest.common.di.webviewModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

open class OpenbankMobileTestApplication : Application() {

    var isForegrounded = false

    override fun onCreate() {
        super.onCreate()

        ProcessLifecycleOwner.get().lifecycle.addObserver(lifecycleObserver)

        // start Koin!
        startKoin {
            androidContext(this@OpenbankMobileTestApplication)
            modules(
                listOf(
                    appModule,
                    charactersModule,
                    webviewModule
                )
            )
        }
    }

    //region Lifecycle observer
    private val lifecycleObserver = object : LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
        fun onAppBackgrounded() {
            isForegrounded = false
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_START)
        fun onAppForegrounded() {
            isForegrounded = true
        }
    }
    //endregion
}
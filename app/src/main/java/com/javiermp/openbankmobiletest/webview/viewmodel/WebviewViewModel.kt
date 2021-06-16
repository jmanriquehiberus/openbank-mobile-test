package com.javiermp.openbankmobiletest.webview.viewmodel

import com.javiermp.openbankmobiletest.common.viewmodel.CommonEventsViewModel
import io.reactivex.disposables.CompositeDisposable

class WebviewViewModel() : CommonEventsViewModel() {

    lateinit var url: String
    private var compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }
}
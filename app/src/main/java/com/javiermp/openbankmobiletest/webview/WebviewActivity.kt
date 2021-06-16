package com.javiermp.openbankmobiletest.webview

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.javiermp.openbankmobiletest.R
import com.javiermp.openbankmobiletest.webview.viewmodel.WebviewViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class WebviewActivity : AppCompatActivity() {

    private val webviewViewModel: WebviewViewModel by viewModel()

    companion object {

        fun getCallingIntent(context: Context, url: String): Intent {
            return Intent(context, WebviewActivity::class.java).putExtra("url", url)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.webview_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.webview_activity_fragment_container, WebviewFragment.newInstance())
                .commitNow()
        }

        intent.extras?.let {
            webviewViewModel.url = it.getString("url") ?: "https://www.marvel.com/"
        }
    }
}
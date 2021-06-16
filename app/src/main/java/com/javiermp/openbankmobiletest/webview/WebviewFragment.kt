package com.javiermp.openbankmobiletest.webview

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import com.javiermp.openbankmobiletest.R
import com.javiermp.openbankmobiletest.common.widget.BaseFragment
import com.javiermp.openbankmobiletest.webview.viewmodel.WebviewViewModel
import kotlinx.android.synthetic.main.webview_fragment.*
import kotlinx.android.synthetic.main.webview_fragment.siv_pound
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class WebviewFragment : BaseFragment(R.layout.webview_fragment) {

    private val webviewViewModel: WebviewViewModel by sharedViewModel()
    private lateinit var loadingAnimatorSet: AnimatorSet

    companion object {
        fun newInstance() = WebviewFragment()
    }

    override fun initializeViews(savedInstanceState: Bundle?) {
        super.initializeViews(savedInstanceState)
        initializeViews()
    }

    private fun initializeViews() {
        setupViewForLoading()
        wv_webview_fragment.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                loadingAnimatorSet.pause()
                siv_pound?.let { siv_pound.visibility = View.INVISIBLE }
            }
        }
        wv_webview_fragment.settings.domStorageEnabled = true
        wv_webview_fragment.settings.javaScriptEnabled = true
        wv_webview_fragment.loadUrl(webviewViewModel.url)
    }

    private fun setupViewForLoading() {
        setRotatingPoundAnimation()
        siv_pound.visibility = View.VISIBLE
    }

    private fun setRotatingPoundAnimation() {
        val rotationAnimation = ObjectAnimator.ofFloat(siv_pound, "rotation", 0f, 360f)
        val fadeAnimation = ObjectAnimator.ofFloat(siv_pound, "alpha", 0f, 0.3f)

        loadingAnimatorSet = AnimatorSet()
        rotationAnimation.repeatMode = ValueAnimator.RESTART
        rotationAnimation.repeatCount = ValueAnimator.INFINITE
        rotationAnimation.duration = 1500
        fadeAnimation.repeatMode = ValueAnimator.REVERSE
        fadeAnimation.repeatCount = ValueAnimator.INFINITE
        fadeAnimation.duration = 750

        loadingAnimatorSet.play(rotationAnimation).with(fadeAnimation)
        loadingAnimatorSet.start()
    }
}
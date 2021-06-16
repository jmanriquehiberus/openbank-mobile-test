package com.javiermp.openbankmobiletest.common.widget

import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.javiermp.openbankmobiletest.R

/**
 * Base [AppCompatActivity] class for every Activity in this application.
 */
abstract class BaseActivity(@LayoutRes layoutId: Int = 0) : AppCompatActivity(layoutId) {

    open fun setTranslucentStatusBar() {
        window.statusBarColor = ContextCompat.getColor(this, android.R.color.transparent)
    }

    open fun clearTranslucentStatusBar() {
        window.statusBarColor = ContextCompat.getColor(this, android.R.color.white)
    }
}